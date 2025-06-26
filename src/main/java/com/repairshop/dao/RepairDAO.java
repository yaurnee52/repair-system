package com.repairshop.dao;

import com.repairshop.ConectDB.DatabaseConnection;
import com.repairshop.model.Repair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepairDAO {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    // Создать новую запись о ремонте
    public void create(Repair repair) throws SQLException {
        String sql = "INSERT INTO Repair (startDate, machineId, repairTypeId) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(repair.getStartDate().getTime()));
            stmt.setInt(2, repair.getMachineId());
            stmt.setInt(3, repair.getRepairTypeId());
            stmt.executeUpdate();
        }
    }

    // Получить список всех ремонтов
    public List<Repair> readAll() throws SQLException {
        List<Repair> repairs = new ArrayList<>();
        String sql = "SELECT * FROM Repair";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                repairs.add(new Repair(
                    rs.getInt("id"),
                    rs.getDate("startDate"),
                    rs.getInt("machineId"),
                    rs.getInt("repairTypeId")
                ));
            }
        }
        return repairs;
    }

    public void deleteByMachineId(int machineId) throws SQLException {
        String sql = "DELETE FROM Repair WHERE machineId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, machineId);
            pstmt.executeUpdate();
        }
    }

    public List<Repair> findByClientId(int clientId) throws SQLException {
        List<Repair> repairs = new ArrayList<>();
        String sql = "SELECT r.* FROM Repair r JOIN Machine m ON r.machineId = m.id WHERE m.clientId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                repairs.add(new Repair(
                    rs.getInt("id"),
                    rs.getDate("startDate"),
                    rs.getInt("machineId"),
                    rs.getInt("repairTypeId")
                ));
            }
        }
        return repairs;
    }
}