package com.practice.chat.dbconnector;

import java.sql.*;

public class DBHelper {
    private String url = "jdbc:sqlserver://localhost:1433;database=KUN_CHAT;user=Howard;password=5836475;trustServerCertificate=true;";
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
        if (connection == null)
            connection = DriverManager.getConnection(url);
        return connection;
    }

    public Statement getStatement() throws SQLException {
        if (statement == null) {
            if (connection == null)
                connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        }
        return statement;
    }

    public PreparedStatement preparedStatement(String sql) throws SQLException {
        if(connection == null || connection.isClosed())
            connection = DriverManager.getConnection(url);
        return connection.prepareStatement(sql);
    }

    public static DBHelper getInstance() {
        return dbHelper;
    }

    public void closeAll() throws SQLException {
        if (statement != null && !statement.isClosed())
            statement.close();
        if (connection != null && !connection.isClosed())
            connection.close();

        statement = null;
        connection = null;
    }
}
