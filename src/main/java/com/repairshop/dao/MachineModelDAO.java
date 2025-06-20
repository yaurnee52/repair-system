package com.repairshop.dao;

import com.repairshop.db.DatabaseConnection;
import com.repairshop.model.MachineModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MachineModelDAO {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    // Создать новую модель машины
    public void create(MachineModel model) throws SQLException {
        String sql = "INSERT INTO MachineModel (brand, yearOfRelease, countryOfManufacture) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, model.getBrand());
            stmt.setShort(2, model.getYearOfRelease());
            stmt.setString(3, model.getCountryOfManufacture());
            stmt.executeUpdate();
        }
    }

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
                    rs.getShort("yearOfRelease"),
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
                    rs.getShort("yearOfRelease"),
                    rs.getString("countryOfManufacture")
                ));
            }
        }
        return models;
    }

    // Обновить данные модели машины
    public void update(MachineModel model) throws SQLException {
        String sql = "UPDATE MachineModel SET brand = ?, yearOfRelease = ?, countryOfManufacture = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, model.getBrand());
            stmt.setShort(2, model.getYearOfRelease());
            stmt.setString(3, model.getCountryOfManufacture());
            stmt.setInt(4, model.getId());
            stmt.executeUpdate();
        }
    }

    // Удалить модель машины по id
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM MachineModel WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Найти модели по бренду
    public List<MachineModel> findByBrand(String brand) throws SQLException {
        List<MachineModel> models = new ArrayList<>();
        String sql = "SELECT * FROM MachineModel WHERE brand LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + brand + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                models.add(new MachineModel(
                    rs.getInt("id"),
                    rs.getString("brand"),
                    rs.getShort("yearOfRelease"),
                    rs.getString("countryOfManufacture")
                ));
            }
        }
        return models;
    }
}