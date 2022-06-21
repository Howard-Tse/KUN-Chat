package com.practice.chat.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {
    private String url = "jdbc:sqlserver://localhost:1433;user=Howard;password=5836475;trustServerCertificate=true;";
    private Connection connection;
    private Statement statement;

    private static final DBHelper dbHelper = new DBHelper();

    private DBHelper() {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Connection getConnection() throws SQLException {
        if(connection == null)
            connection = DriverManager.getConnection(url);
        return connection;
    }

    public Statement getStatement() throws SQLException {
        if(statement == null) {
            if(connection == null)
                connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        }
        return statement;
    }

    public static DBHelper getInstance() {
        return dbHelper;
    }

    public void closeAll() throws SQLException {
        statement.close();
        connection.close();

        statement = null;
        connection = null;
    }
}
