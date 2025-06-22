package com.repairshop.dao;

import com.repairshop.ConectDB.DatabaseConnection;
import com.repairshop.model.Client;

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