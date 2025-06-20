package com.repairshop.dao;

import com.repairshop.ConectDB.DatabaseConnection;
import com.repairshop.model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    // Создать новую роль
    public void create(Role role) throws SQLException {
        String sql = "INSERT INTO Role (roleName) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role.getRoleName());
            stmt.executeUpdate();
        }
    }

    // Получить роль по id
    public Role read(int id) throws SQLException {
        String sql = "SELECT * FROM Role WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Role(rs.getInt("id"), rs.getString("roleName"));
            }
        }
        return null;
    }

    // Получить роль по имени
    public Role readByName(String roleName) throws SQLException {
        String sql = "SELECT * FROM Role WHERE roleName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Role(rs.getInt("id"), rs.getString("roleName"));
            }
        }
        return null;
    }

    // Получить все роли
    public List<Role> readAll() throws SQLException {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM Role";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                roles.add(new Role(rs.getInt("id"), rs.getString("roleName")));
            }
        }
        return roles;
    }

    // Обновить роль
    public void update(Role role) throws SQLException {
        String sql = "UPDATE Role SET roleName = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role.getRoleName());
            stmt.setInt(2, role.getId());
            stmt.executeUpdate();
        }
    }

    // Удалить роль
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Role WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}