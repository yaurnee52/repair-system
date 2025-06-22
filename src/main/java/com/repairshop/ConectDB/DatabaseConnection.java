package com.repairshop.ConectDB;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            
            if (input == null) {
                throw new RuntimeException("Критическая ошибка: Файл db.properties не найден в classpath.");
            }

            Properties props = new Properties();
            props.load(input);

            String dbUrl = props.getProperty("db.url");
            String dbUser = props.getProperty("db.user");
            String dbPassword = props.getProperty("db.password");

            this.connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        } catch (IOException | SQLException e) {
            // Если произошла любая ошибка (файл не прочитался или не удалось подключиться),
            // приложение аварийно завершится с понятным сообщением.
            throw new RuntimeException("Критическая ошибка: Не удалось подключиться к базе данных.", e);
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
}