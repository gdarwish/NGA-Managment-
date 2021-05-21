package Database;

import Const.Const;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {

    private static DatabaseConnection databaseConnection = null;
    private static Connection connection = null;
    private static PreparedStatement preparedStatement = null;

    private String error = "Unknown";

    public static DatabaseConnection getInstance() {
        if (databaseConnection == null) {
            databaseConnection = new DatabaseConnection();
        }
        return databaseConnection;
    }

    private DatabaseConnection() {
    }

    /**
     * Create Connection to Database
     *
     * @param host     of database
     * @param database name
     * @param username database username
     * @param password database password
     * @return if the connection was successfully created
     * @author Ali Dali
     */
    public boolean createConnection(String host, String database, String username, String password) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, username, password);

            // Create tables in Database if not exist
            createTable(Const.CREATE_TABLE_PROJECT);
            createTable(Const.CREATE_TABLE_TASKS);
            createTable(Const.CREATE_TABLE_CATEGORY);
            createTable(Const.CREATE_TABLE_STATUS);
            createTable(Const.CREATE_TABLE_PRIORITY);

            // Add foreign keys to tables
            alterTables(Const.ALTER_TABLES_FOREIGN_KEYS);

        } catch (Exception e) {
//            e.printStackTrace();
            error = e.getMessage();
        }
        return isConnected();
    }

    /**
     * @param sqlQuery create table query
     * @author Ali Dali
     */
    public void createTable(String sqlQuery) {
        if (connection != null) {
            try {
                preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                error = e.getMessage();
            } finally {
                try {
                    if (preparedStatement != null)
                        preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param sqlQueries
     * @author Ali Dali
     */
    public void alterTables(String[] sqlQueries) {
        if (connection != null) {
            try {
                for (String sqlQuery : sqlQueries) {
                    preparedStatement = connection.prepareStatement(sqlQuery);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                error = e.getMessage();
            } finally {
                try {
                    if (preparedStatement != null)
                        preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getError() {
        return error;
    }
}
