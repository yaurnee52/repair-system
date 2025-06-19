package com.repairshop.model;

import java.util.Date;

public class Repair {
    private int id;
    private Date startDate;
    private int machineId;
    private int repairTypeId;

    public Repair() {}

    public Repair(int id, Date startDate, int machineId, int repairTypeId) {
        this.id = id;
        this.startDate = startDate;
        this.machineId = machineId;
        this.repairTypeId = repairTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public int getRepairTypeId() {
        return repairTypeId;
    }

    public void setRepairTypeId(int repairTypeId) {
        this.repairTypeId = repairTypeId;
    }

    @Override
    public String toString() {
        return "Repair{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", machineId=" + machineId +
                ", repairTypeId=" + repairTypeId +
                '}';
    }
}
