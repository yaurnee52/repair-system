package com.repairshop.dao;

import com.repairshop.ConectDB.DatabaseConnection;
import com.repairshop.model.Machine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MachineDAO {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    // Создать новую машину
    public void create(Machine machine) throws SQLException {
        String sql = "INSERT INTO Machine (clientId, machineModelId) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, machine.getClientId());
            stmt.setInt(2, machine.getMachineModelId());
            stmt.executeUpdate();
        }
    }

    // Получить машину по id
    public Machine read(int id) throws SQLException {
        String sql = "SELECT * FROM Machine WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Machine(rs.getInt("id"), rs.getInt("clientId"), rs.getInt("machineModelId"));
            }
        }
        return null;
    }

    // Получить список всех машин
    public List<Machine> readAll() throws SQLException {
        List<Machine> machines = new ArrayList<>();
        String sql = "SELECT * FROM Machine";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                machines.add(new Machine(rs.getInt("id"), rs.getInt("clientId"), rs.getInt("machineModelId")));
            }
        }
        return machines;
    }

    // Обновить данные машины
    public void update(Machine machine) throws SQLException {
        String sql = "UPDATE Machine SET clientId = ?, machineModelId = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, machine.getClientId());
            stmt.setInt(2, machine.getMachineModelId());
            stmt.setInt(3, machine.getId());
            stmt.executeUpdate();
        }
    }

    // Удалить машину по id
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Machine WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Получить все машины определенного клиента
    public List<Machine> findByClientId(int clientId) throws SQLException {
        List<Machine> machines = new ArrayList<>();
        String sql = "SELECT * FROM Machine WHERE clientId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                machines.add(new Machine(rs.getInt("id"), rs.getInt("clientId"), rs.getInt("machineModelId")));
            }
        }
        return machines;
    }
}