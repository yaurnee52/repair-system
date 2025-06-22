package com.repairshop.dao;

import com.repairshop.ConectDB.DatabaseConnection;
import com.repairshop.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    // Создать пользователя
    public void create(User user) throws SQLException {
        String sql = "INSERT INTO Users (username, passwordHash, roleId, clientId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setInt(3, user.getRoleId());
            if (user.getClientId() != null) {
                stmt.setInt(4, user.getClientId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.executeUpdate();
        }
    }

    // Получить пользователя по id
    public User read(int id) throws SQLException {
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("passwordHash"),
                    rs.getInt("roleId"),
                    rs.getObject("clientId") != null ? rs.getInt("clientId") : null
                );
            }
        }
        return null;
    }

    // Получить пользователя по username
    public User readByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("passwordHash"),
                    rs.getInt("roleId"),
                    rs.getObject("clientId") != null ? rs.getInt("clientId") : null
                );
            }
        }
        return null;
    }

    // Получить всех пользователей
    public List<User> readAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("passwordHash"),
                    rs.getInt("roleId"),
                    rs.getObject("clientId") != null ? rs.getInt("clientId") : null
                ));
            }
        }
        return users;
    }

    // Обновить пользователя
    public void update(User user) throws SQLException {
        String sql = "UPDATE Users SET username = ?, passwordHash = ?, roleId = ?, clientId = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setInt(3, user.getRoleId());
            if (user.getClientId() != null) {
                pstmt.setInt(4, user.getClientId());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
            pstmt.setInt(5, user.getId());
            pstmt.executeUpdate();
        }
    }

    // Удалить пользователя
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}