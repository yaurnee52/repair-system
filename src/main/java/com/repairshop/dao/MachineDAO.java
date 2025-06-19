package com.repairshop.dao;

import com.repairshop.db.DatabaseConnection;
import com.repairshop.model.Machine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MachineDAO {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    // Создать новую машину
    public void create(Machine machine) throws SQLException {
        String sql = "INSERT INTO machine (client_id, machine_model_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, machine.getClientId());
            stmt.setInt(2, machine.getMachineModelId());
            stmt.executeUpdate();
        }
    }

    // Получить машину по id
    public Machine read(int id) throws SQLException {
        String sql = "SELECT * FROM machine WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Machine(rs.getInt("id"), rs.getInt("client_id"), rs.getInt("machine_model_id"));
            }
        }
        return null;
    }

    // Получить список всех машин
    public List<Machine> readAll() throws SQLException {
        List<Machine> machines = new ArrayList<>();
        String sql = "SELECT * FROM machine";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                machines.add(new Machine(rs.getInt("id"), rs.getInt("client_id"), rs.getInt("machine_model_id")));
            }
        }
        return machines;
    }

    // Обновить данные машины
    public void update(Machine machine) throws SQLException {
        String sql = "UPDATE machine SET client_id = ?, machine_model_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, machine.getClientId());
            stmt.setInt(2, machine.getMachineModelId());
            stmt.setInt(3, machine.getId());
            stmt.executeUpdate();
        }
    }

    // Удалить машину по id
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM machine WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Получить все машины определенного клиента
    public List<Machine> findByClientId(int clientId) throws SQLException {
        List<Machine> machines = new ArrayList<>();
        String sql = "SELECT * FROM machine WHERE client_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                machines.add(new Machine(rs.getInt("id"), rs.getInt("client_id"), rs.getInt("machine_model_id")));
            }
        }
        return machines;
    }
}