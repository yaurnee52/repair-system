package com.repairshop.dao;

import com.repairshop.ConectDB.DatabaseConnection;
import com.repairshop.model.Client;
import com.repairshop.model.Machine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    // Создать нового клиента
    public int create(Client client) throws SQLException {
        String sql = "INSERT INTO Client (companyName) VALUES (?)";
        int newId = 0;
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, client.getCompanyName());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newId = generatedKeys.getInt(1);
                }
            }
        }
        return newId;
    }

    // Получить клиента по id
    public Client read(int id) throws SQLException {
        String sql = "SELECT * FROM Client WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Client(
                        rs.getInt("id"),
                        rs.getString("companyName")
                );
            }
        }
        return null;
    }

    // Получить список всех клиентов
    public List<Client> readAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id"),
                        rs.getString("companyName")
                ));
            }
        }
        return clients;
    }

    // Обновить данные клиента
    public void update(Client client) throws SQLException {
        String sql = "UPDATE Client SET companyName = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getCompanyName());
            pstmt.setInt(2, client.getId());
            pstmt.executeUpdate();
        }
    }

    // Удалить клиента по id
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Client WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public void performCascadeDelete(int clientId, int userId) throws SQLException {
        MachineDAO machineDAO = new MachineDAO();
        RepairDAO repairDAO = new RepairDAO();
        UserDAO userDAO = new UserDAO();

        List<Machine> machinesToDelete = machineDAO.readByClientId(clientId);
        try {
            conn.setAutoCommit(false);

            for (Machine machine : machinesToDelete) {
                repairDAO.deleteByMachineId(machine.getId());
            }

            if (!machinesToDelete.isEmpty()) {
                machineDAO.deleteByClientId(clientId);
            }

            userDAO.delete(userId);
            this.delete(clientId);

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new SQLException("Ошибка каскадного удаления. Транзакция отменена.", e);
        } finally {
            conn.setAutoCommit(true);
        }
    }
}