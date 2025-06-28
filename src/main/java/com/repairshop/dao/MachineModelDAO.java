package com.repairshop.dao;

import com.repairshop.ConectDB.DatabaseConnection;
import com.repairshop.model.MachineModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MachineModelDAO {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    // Получить модель машины по id
    public MachineModel read(int id) throws SQLException {
        String sql = "SELECT * FROM MachineModel WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new MachineModel(
                    rs.getInt("id"),
                    rs.getString("brand"),
                    rs.getInt("yearOfRelease"),
                    rs.getString("countryOfManufacture")
                );
            }
        }
        return null;
    }

    // Получить список всех моделей машин
    public List<MachineModel> readAll() throws SQLException {
        List<MachineModel> models = new ArrayList<>();
        String sql = "SELECT * FROM MachineModel";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                models.add(new MachineModel(
                    rs.getInt("id"),
                    rs.getString("brand"),
                    rs.getInt("yearOfRelease"),
                    rs.getString("countryOfManufacture")
                ));
            }
        }
        return models;
    }

    // Получить все модели станков по ID клиента
    public List<MachineModel> readByClientId(int clientId) throws SQLException {
        List<MachineModel> models = new ArrayList<>();
        String sql = "SELECT DISTINCT mm.* FROM MachineModel mm JOIN Machine m ON mm.id = m.machineModelId WHERE m.clientId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                models.add(new MachineModel(
                    rs.getInt("id"),
                    rs.getString("brand"),
                    rs.getInt("yearOfRelease"),
                    rs.getString("countryOfManufacture")
                ));
            }
        }
        return models;
    }


    public MachineModel findOrCreate(String brand, int year, String country) throws SQLException {
        String selectSql = "SELECT * FROM MachineModel WHERE brand = ? AND yearOfRelease = ? AND countryOfManufacture = ?";
        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setString(1, brand);
            selectStmt.setInt(2, year);
            selectStmt.setString(3, country);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                return new MachineModel(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getInt("yearOfRelease"),
                        rs.getString("countryOfManufacture")
                );
            }
        }
        String insertSql = "INSERT INTO MachineModel (brand, yearOfRelease, countryOfManufacture) VALUES (?, ?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, brand);
            insertStmt.setInt(2, year);
            insertStmt.setString(3, country);
            insertStmt.executeUpdate();

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    return new MachineModel(newId, brand, year, country);
                }
            }
        }
        return null;
    }
}