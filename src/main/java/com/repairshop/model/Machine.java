package com.repairshop.model;

public class Machine {
    private int id;
    private int clientId;
    private int machineModelId;

    public Machine() {}

    public Machine(int id, int clientId, int machineModelId) {
        this.id = id;
        this.clientId = clientId;
        this.machineModelId = machineModelId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getMachineModelId() {
        return machineModelId;
    }

    public void setMachineModelId(int machineModelId) {
        this.machineModelId = machineModelId;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", machineModelId=" + machineModelId +
                '}';
    }
}