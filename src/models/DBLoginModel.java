package models;

import Database.DatabaseConnection;

public class DBLoginModel {

    private static  DBLoginModel instance = new DBLoginModel();

    public static DBLoginModel getInstance() {
        return instance;
    }

    private DBLoginModel() {

    }

    public boolean establishConnection(String host, String database, String username, String password) {
        return DatabaseConnection.getInstance().createConnection(host, database, username, password);
    }

    public String errorMessage() {
        return DatabaseConnection.getInstance().getError();
    }

}
