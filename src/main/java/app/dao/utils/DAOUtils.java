package app.dao.utils;

import app.annotations.Column;
import app.annotations.Table;
import app.connection.DbConnection;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for extracting metadata from annotated classes used in the data access operations.
 */
public class DAOUtils {

    /**
     * Default constructor.
     */
    public DAOUtils() {}

    /**
     * Retrieves the name of the database table associated with the given class.
     *
     * @param clazz the class to inspect
     * @return the name of the database table
     * @throws RuntimeException if the class does not have a {@code @Table} annotation
     */
    public static String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            throw new RuntimeException("Class " + clazz.getName() + " has no @Table annotation");
        }
        return table.name();
    }

    /**
     * Returns a list of fields in the given class that are annotated with {@link Column}.
     * <p>
     * All returned fields have their accessibility set to true.
     *
     * @param clazz the class to search in
     * @return a list of fields annotated with {@code @Column}
     */
    public static List<Field> getFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the field annotated as the primary key in the given class.
     * <p>
     * The method searches for a field with {@link Column#primaryKey()} set to {@code true}.
     * Accessibility of the field is set to true.
     *
     * @param clazz the class to search in
     * @return the primary key field
     * @throws RuntimeException if no primary key field is found
     */
    public static Field getPrimaryKeyField(Class<?> clazz) {
        return getFields(clazz).stream()
                .filter(field -> {
                    Column column = field.getAnnotation(Column.class);
                    return column != null && column.primaryKey();
                })
                .peek(field -> field.setAccessible(true))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No field annotated as primary key in class: " + clazz.getSimpleName()));
    }

    /**
     * Retrieves the name of the primary key column from the entity's fields.
     *
     * @param clazz the entity class
     * @return the primary key column name
     * @throws RuntimeException if no primary key is found
     */
    public static String getPrimaryKeyName(Class<?> clazz) {
        return getFields(clazz).stream()
                .map(f -> f.getAnnotation(Column.class))
                .filter(Column::primaryKey)
                .map(Column::name)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No primary key defined for " + clazz.getName()));
    }

    /**
     * Builds the column definition string for a field based on its {@link Column} annotation.
     * Includes type, primary key and nullability constraints.
     *
     * @param field the field to process
     * @return the column definition SQL fragment
     */
    public static String buildColumnDefinition(Field field) {
        Column col = field.getAnnotation(Column.class);
        if (col.primaryKey()) {
            return col.name() + " INTEGER GENERATED ALWAYS AS IDENTITY";
        } else {
            String colDef = col.name() + " " + col.type();
            if (!col.nullable()) {
                colDef += " NOT NULL";
            }
            return colDef;
        }
    }

    /**
     * Builds foreign key constraint definitions for all fields that have foreign key annotations.
     *
     * @param fields the list of fields to process
     * @return list of foreign key constraint SQL fragments
     */
    public static List<String> buildForeignKeyConstraints(List<Field> fields) {
        return fields.stream()
                .map(field -> field.getAnnotation(Column.class))
                .filter(col -> !col.foreignKeyTable().isEmpty() && !col.foreignKeyColumn().isEmpty())
                .map(col -> "FOREIGN KEY (" + col.name() + ") REFERENCES " + col.foreignKeyTable() + "(" + col.foreignKeyColumn() + ")")
                .collect(Collectors.toList());
    }

    /**
     * Creates PostgreSQL triggers to prevent updates on immutable fields.
     * For each non-updatable column, a trigger is created that raises an exception if updated.
     *
     * @param clazz the entity class
     * @throws SQLException if an error occurs creating triggers or functions
     */
    public static void setImmutabilityTriggers(Class<?> clazz) throws SQLException {
        String tableName = getTableName(clazz);
        List<Field> fields = getFields(clazz);

        try (Connection con = DbConnection.getConnection();
             Statement stmt = con.createStatement()) {

            for (Field field : fields) {
                Column column = field.getAnnotation(Column.class);
                if (!column.updatable()) {
                    String colName = column.name();
                    String functionName = "prevent_update_" + tableName.toLowerCase() + "_" + colName.toLowerCase();
                    String triggerName = "no_update_" + tableName.toLowerCase() + "_" + colName.toLowerCase();

                    String checkTriggerSQL = "SELECT 1 FROM pg_trigger WHERE tgname = '" + triggerName + "'";
                    try (ResultSet rs = stmt.executeQuery(checkTriggerSQL)) {
                        if (!rs.next()) {
                            String functionSQL = String.format("""
                                CREATE OR REPLACE FUNCTION %s()
                                RETURNS TRIGGER AS $$
                                BEGIN
                                    IF NEW.%s IS DISTINCT FROM OLD.%s THEN
                                        RAISE EXCEPTION '%s is immutable';
                                    END IF;
                                    RETURN NEW;
                                END;
                                $$ LANGUAGE plpgsql;
                                """, functionName, colName, colName, colName);

                            String triggerSQL = String.format("""
                                CREATE TRIGGER %s
                                BEFORE UPDATE ON %s
                                FOR EACH ROW
                                EXECUTE FUNCTION %s();
                                """, triggerName, tableName, functionName);

                            stmt.execute(functionSQL);
                            stmt.execute(triggerSQL);
                        }
                    }
                }
            }
        }
    }
}
