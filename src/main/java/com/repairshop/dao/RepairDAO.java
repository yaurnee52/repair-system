package com.repairshop.dao;

import com.repairshop.db.DatabaseConnection;
import com.repairshop.model.Repair;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RepairDAO {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    // Создать новую запись о ремонте
    public void create(Repair repair) throws SQLException {
        String sql = "INSERT INTO repair (start_date, machine_id, repair_type_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(repair.getStartDate().getTime()));
            stmt.setInt(2, repair.getMachineId());
            stmt.setInt(3, repair.getRepairTypeId());
            stmt.executeUpdate();
        }
    }

    // Получить ремонт по id
    public Repair read(int id) throws SQLException {
        String sql = "SELECT * FROM repair WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Repair(
                    rs.getInt("id"),
                    rs.getDate("start_date"),
                    rs.getInt("machine_id"),
                    rs.getInt("repair_type_id")
                );
            }
        }
        return null;
    }

    // Получить список всех ремонтов
    public List<Repair> readAll() throws SQLException {
        List<Repair> repairs = new ArrayList<>();
        String sql = "SELECT * FROM repair";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                repairs.add(new Repair(
                    rs.getInt("id"),
                    rs.getDate("start_date"),
                    rs.getInt("machine_id"),
                    rs.getInt("repair_type_id")
                ));
            }
        }
        return repairs;
    }

    // Обновить данные ремонта
    public void update(Repair repair) throws SQLException {
        String sql = "UPDATE repair SET start_date = ?, machine_id = ?, repair_type_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(repair.getStartDate().getTime()));
            stmt.setInt(2, repair.getMachineId());
            stmt.setInt(3, repair.getRepairTypeId());
            stmt.setInt(4, repair.getId());
            stmt.executeUpdate();
        }
    }

    // Удалить ремонт по id
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM repair WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Получить все ремонты по id машины
    public List<Repair> findByMachineId(int machineId) throws SQLException {
        List<Repair> repairs = new ArrayList<>();
        String sql = "SELECT * FROM repair WHERE machine_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, machineId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                repairs.add(new Repair(
                    rs.getInt("id"),
                    rs.getDate("start_date"),
                    rs.getInt("machine_id"),
                    rs.getInt("repair_type_id")
                ));
            }
        }
        return repairs;
    }

    // Получить ремонты за определенный период
    public List<Repair> findByDateRange(Date from, Date to) throws SQLException {
        List<Repair> repairs = new ArrayList<>();
        String sql = "SELECT * FROM repair WHERE start_date BETWEEN ? AND ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(from.getTime()));
            stmt.setDate(2, new java.sql.Date(to.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                repairs.add(new Repair(
                    rs.getInt("id"),
                    rs.getDate("start_date"),
                    rs.getInt("machine_id"),
                    rs.getInt("repair_type_id")
                ));
            }
        }
        return repairs;
    }
}