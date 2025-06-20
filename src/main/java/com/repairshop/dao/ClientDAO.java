package com.repairshop.dao;

import com.repairshop.db.DatabaseConnection;
import com.repairshop.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    // Создать нового клиента
    public void create(Client client) throws SQLException {
        String sql = "INSERT INTO Client (companyName) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, client.getCompanyName());
            stmt.executeUpdate();
        }
    }

    // Получить клиента по id
    public Client read(int id) throws SQLException {
        String sql = "SELECT * FROM Client WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(rs.getInt("id"), rs.getString("companyName"));
            }
        }
        return null;
    }

    // Получить список всех клиентов
    public List<Client> readAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                clients.add(new Client(rs.getInt("id"), rs.getString("companyName")));
            }
        }
        return clients;
    }

    // Обновить данные клиента
    public void update(Client client) throws SQLException {
        String sql = "UPDATE Client SET companyName = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, client.getCompanyName());
            stmt.setInt(2, client.getId());
            stmt.executeUpdate();
        }
    }

    // Удалить клиента по id
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Client WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Найти клиентов по части названия компании
    public List<Client> findByCompanyName(String name) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client WHERE companyName LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                clients.add(new Client(rs.getInt("id"), rs.getString("companyName")));
            }
        }
        return clients;
    }
}