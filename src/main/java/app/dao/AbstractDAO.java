package app.dao;

import app.annotations.Column;
import app.annotations.Table;
import app.connection.DbConnection;
import app.dao.utils.DAOUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Abstract generic DAO class providing CRUD operations and schema generation capabilities
 * for objects annotated with custom {@link Column} and {@link Table} annotations.
 *
 * @param <T> the type of the entity this DAO manages
 */
public class AbstractDAO<T> {

    /**
     * Logger constant to log errors or messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    /**
     * Type of entity DAO manages.
     */
    private final Class<T> type;

    /**
     * Constructor to initialize DAO with the type of entity it manages.
     *
     * @param type the class of the entity
     */
    protected AbstractDAO(Class<T> type) {
        this.type = type;
    }

    /**
     * Creates the database table for the entity if it does not already exist.
     * Uses {@link Column} annotations to determine the schema.
     *
     * @throws SQLException if an SQL error occurs during table creation
     */
    public void createTableIfNotExists() throws SQLException {
        String tableName = DAOUtils.getTableName(type);
        List<Field> fields = DAOUtils.getFields(type);

        List<String> columns = fields.stream()
                .map(DAOUtils::buildColumnDefinition)
                .collect(Collectors.toList());

        String primaryKey = DAOUtils.getPrimaryKeyName(type);
        columns.add("PRIMARY KEY (" + primaryKey + ")");

        List<String> foreignKeys = DAOUtils.buildForeignKeyConstraints(fields);
        columns.addAll(foreignKeys);

        String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + String.join(",", columns) + ")";

        try (Connection con = DbConnection.getConnection();
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate(query);
            DAOUtils.setImmutabilityTriggers(type);

        } catch (SQLException e) {
            LOGGER.severe(String.format("Error creating table %s: %s", tableName, e.getMessage()));
            throw e;
        }
    }

    /**
     * Constructs the SQL INSERT query and fills parameter list based on entity fields.
     *
     * @param tableName the name of the database table
     * @param objs list of objects to insert
     * @param params output list to collect query parameters
     * @return a complete SQL INSERT query string
     * @throws IllegalAccessException if access to fields is not allowed
     */
    private String prepareInsertQueryAndParams(String tableName, List<T> objs, List<Object> params) throws IllegalAccessException {
        List<Field> fields = DAOUtils.getFields(type);
        StringBuilder query = new StringBuilder("INSERT INTO ").append(tableName).append(" (");

        String colNames = fields.stream()
                .filter(field -> !field.getAnnotation(Column.class).primaryKey()) // Exclude the primary key
                .map(field -> field.getAnnotation(Column.class).name())
                .collect(Collectors.joining(","));
        query.append(colNames).append(") VALUES ");

        String placeholders = objs.stream()
                .map(obj -> {
                    String values = fields.stream()
                            .filter(field -> !field.getAnnotation(Column.class).primaryKey()) // Skip the primary key
                            .map(field -> {
                                try {
                                    field.setAccessible(true);
                                    params.add(field.get(obj));
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                                return "?";
                            }).collect(Collectors.joining(","));
                    return "(" + values + ")";
                })
                .collect(Collectors.joining(","));

        query.append(placeholders);
        return query.toString();
    }

    /**
     * Inserts a single entity into the database.
     *
     * @param obj the object to insert
     * @throws SQLException if an SQL error occurs
     * @throws IllegalAccessException if field access fails
     */
    public void insert(T obj) throws SQLException, IllegalAccessException {
        insertList(List.of(obj));
    }

    /**
     * Inserts a list of entities into the database all together.
     *
     * @param objs the list of objects to insert
     * @throws SQLException if an SQL error occurs
     * @throws IllegalAccessException if field access fails
     */
    public void insertList(List<T> objs) throws SQLException, IllegalAccessException {
        String tableName = DAOUtils.getTableName(type);
        List<Object> params = new ArrayList<>();
        String query = prepareInsertQueryAndParams(tableName, objs, params);

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            con.setAutoCommit(false);
            int index = 1;
            for (Object param : params) {
                ps.setObject(index++, param);
            }
            ps.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOGGER.severe(String.format("Error inserting into table %s: %s", tableName, e.getMessage()));
            throw e;
        }
    }

    /**
     * Finds an entity by its primary key.
     *
     * @param id the primary key value
     * @return the found object or null if not found
     * @throws SQLException if a database error occurs
     * @throws IllegalAccessException if field access fails
     * @throws NoSuchMethodException if the default constructor is missing
     */
    public T findById(Object id) throws SQLException, IllegalAccessException, NoSuchMethodException {
        String tableName = DAOUtils.getTableName(type);
        Field pk = DAOUtils.getPrimaryKeyField(type);
        Column col = pk.getAnnotation(Column.class);

        String query = "SELECT * FROM " + tableName + " WHERE " + col.name() + " = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    T obj = type.getDeclaredConstructor().newInstance();
                    for (Field field : DAOUtils.getFields(type)) {
                        field.setAccessible(true);
                        Column c = field.getAnnotation(Column.class);
                        field.set(obj, rs.getObject(c.name()));
                    }
                    return obj;
                }
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException | IllegalAccessException | NoSuchMethodException e) {
            LOGGER.severe(String.format("Error finding entity by ID in table %s: %s", tableName, e.getMessage()));
            throw e;
        }
        return null;
    }

    /**
     * Fetches all records of the entity from the corresponding database table.
     *
     * @return a list of all entities
     * @throws Exception if any error occurs during retrieval
     */
    public List<T> findAll() throws Exception {
        String tableName = DAOUtils.getTableName(type);
        String query = "SELECT * FROM " + tableName + " ORDER BY id";

        List<T> list = new ArrayList<>();
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                T obj = type.getDeclaredConstructor().newInstance();
                for (Field field : DAOUtils.getFields(type)) {
                    field.setAccessible(true);
                    Column col = field.getAnnotation(Column.class);
                    field.set(obj, rs.getObject(col.name()));
                }
                list.add(obj);
            }
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.severe(String.format("Error fetching all entities from table %s: %s", tableName, e.getMessage()));
            throw e;
        }
        return list;
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param obj the updated object
     * @throws SQLException if a database error occurs
     * @throws IllegalAccessException if field access fails
     */
    public void update(T obj) throws SQLException, IllegalAccessException {
        String tableName = DAOUtils.getTableName(type);
        List<Field> fields = DAOUtils.getFields(type);
        Field pk = DAOUtils.getPrimaryKeyField(type);
        Column pkCol = pk.getAnnotation(Column.class);

        StringBuilder query = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        List<Object> params = new ArrayList<>();

        String setClause = fields.stream()
                .filter(field -> !field.equals(pk))
                .map(field -> {
                    Column col = field.getAnnotation(Column.class);
                    try {
                        field.setAccessible(true);
                        params.add(field.get(obj));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    return col.name() + " = ?";
                })
                .collect(Collectors.joining(","));

        query.append(setClause).append(" WHERE ").append(pkCol.name()).append(" = ?");
        pk.setAccessible(true);
        params.add(pk.get(obj));

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe(String.format("Error updating entity in table %s: %s", tableName, e.getMessage()));
            throw e;
        }
    }

    /**
     * Deletes an entity from the database based on the primary key.
     *
     * @param id the primary key value
     * @throws Exception if an error occurs during deletion
     */
    public void delete(Object id) throws Exception {
        String tableName = DAOUtils.getTableName(type);
        Field pk = DAOUtils.getPrimaryKeyField(type);
        Column pkCol = pk.getAnnotation(Column.class);

        String query = "DELETE FROM " + tableName + " WHERE " + pkCol.name() + " = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe(String.format("Error deleting entity from table %s: %s", tableName, e.getMessage()));
            throw e;
        }
    }
}
