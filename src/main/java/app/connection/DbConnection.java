package app.connection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the database connection to a PostgreSQL database.
 * It implements the Singleton pattern to ensure that the JDBC driver is loaded only once.
 * <p>
 * Provides utility methods to create a new connection and safely close JDBC resources.
 */
public class DbConnection {

    /**
     * The database connection URL for the PostgreSQL database.
     */
    private static final String URL = "jdbc:postgresql://localhost:5432/ordersdb";

    /**
     * The fully qualified class name of the PostgreSQL JDBC driver.
     */
    private static final String DRIVER = "org.postgresql.Driver";

    /**
     * The username for the database connection.
     * Default: root
     */
    private static final String USER = "root";

    /**
     * The password for the database connection.
     * Default: root
     */
    private static final String PASSWORD = "root";

    /**
     * Logger instance for logging database connection events and errors.
     */
    private static final Logger LOGGER = Logger.getLogger(DbConnection.class.getName());

    /**
     * Singleton instance of the database connection class.
     * Ensures a single connection instance is used throughout the application.
     */
    private static DbConnection singleConnection = new DbConnection();

    /**
     * Private constructor that loads the PostgreSQL JDBC driver.
     */
    private DbConnection() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new {@link java.sql.Connection} to the database.
     *
     * @return a new database connection, or {@code null} if the connection fails
     */
    private Connection createConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error: could not connect to the database");
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Provides a new {@link java.sql.Connection} instance.
     *
     * @return a new database connection
     */
    public static Connection getConnection() {
        return singleConnection.createConnection();
    }

    /**
     * Closes the given {@link java.sql.Connection} if it's not null.
     *
     * @param conn the connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error: trouble closing the Database Connection");
            }
        }
    }

    /**
     * Closes the given {@link java.sql.Statement} if it's not null.
     *
     * @param statement the statement to close
     */
    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error: trouble closing the Statement");
            }
        }
    }

    /**
     * Closes the given {@link java.sql.ResultSet} if it's not null.
     *
     * @param set the result set to close
     */
    public static void closeResultSet(ResultSet set) {
        if (set != null) {
            try {
                set.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error: trouble closing the ResultSet");
            }
        }
    }
}
