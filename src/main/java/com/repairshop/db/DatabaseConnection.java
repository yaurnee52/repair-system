package com.repairshop.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties props = new Properties();
            if (input == null) {
                throw new RuntimeException("Файл db.properties не найден в classpath");
            }
            props.load(input);
            DB_URL = props.getProperty("db.url");
            DB_USER = props.getProperty("db.user");
            DB_PASSWORD = props.getProperty("db.password");
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка загрузки настроек БД");
            e.printStackTrace();
        }
    }

    private DatabaseConnection() {
        try {
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к БД");
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Ошибка закрытия подключения к БД");
                e.printStackTrace();
            }
        }
    }
}