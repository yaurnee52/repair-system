package com.repairshop.dao;

import com.repairshop.db.DatabaseConnection;
import com.repairshop.model.RepairType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepairTypeDAO {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    // Создать новый тип ремонта
    public void create(RepairType type) throws SQLException {
        String sql = "INSERT INTO repair_type (name, cost, duration_days) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type.getName());
            stmt.setDouble(2, type.getCost());
            stmt.setInt(3, type.getDurationDays());
            stmt.executeUpdate();
        }
    }

    // Получить тип ремонта по id
    public RepairType read(int id) throws SQLException {
        String sql = "SELECT * FROM repair_type WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new RepairType(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("cost"),
                    rs.getInt("duration_days")
                );
            }
        }
        return null;
    }

    // Получить список всех типов ремонта
    public List<RepairType> readAll() throws SQLException {
        List<RepairType> types = new ArrayList<>();
        String sql = "SELECT * FROM repair_type";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                types.add(new RepairType(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("cost"),
                    rs.getInt("duration_days")
                ));
            }
        }
        return types;
    }

    // Обновить данные типа ремонта
    public void update(RepairType type) throws SQLException {
        String sql = "UPDATE repair_type SET name = ?, cost = ?, duration_days = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type.getName());
            stmt.setDouble(2, type.getCost());
            stmt.setInt(3, type.getDurationDays());
            stmt.setInt(4, type.getId());
            stmt.executeUpdate();
        }
    }

    // Удалить тип ремонта по id
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM repair_type WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Найти типы ремонта по диапазону стоимости
    public List<RepairType> findByCostRange(double min, double max) throws SQLException {
        List<RepairType> types = new ArrayList<>();
        String sql = "SELECT * FROM repair_type WHERE cost BETWEEN ? AND ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, min);
            stmt.setDouble(2, max);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                types.add(new RepairType(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("cost"),
                    rs.getInt("duration_days")
                ));
            }
        }
        return types;
    }
}