package com.repairshop.dao;

import com.repairshop.ConectDB.DatabaseConnection;
import com.repairshop.model.RepairType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepairTypeDAO {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    // Получить тип ремонта по id
    public RepairType read(int id) throws SQLException {
        String sql = "SELECT * FROM RepairType WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new RepairType(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("cost"),
                    rs.getInt("durationDays")
                );
            }
        }
        return null;
    }

    // Получить список всех типов ремонта
    public List<RepairType> readAll() throws SQLException {
        List<RepairType> types = new ArrayList<>();
        String sql = "SELECT * FROM RepairType";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                types.add(new RepairType(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("cost"),
                    rs.getInt("durationDays")
                ));
            }
        }
        return types;
    }
}