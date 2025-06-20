package com.repairshop.model;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private int roleId;
    private Integer clientId; // Может быть null

    public User() {}

    public User(int id, String username, String passwordHash, int roleId, Integer clientId) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roleId = roleId;
        this.clientId = clientId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", roleId=" + roleId +
                ", clientId=" + clientId +
                '}';
    }
}