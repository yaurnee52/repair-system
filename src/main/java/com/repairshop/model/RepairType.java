package com.repairshop.model;

public class RepairType {
    private int id;
    private String name;
    private double cost;
    private int durationDays;

    public RepairType() {}

    public RepairType(int id, String name, double cost, int durationDays) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.durationDays = durationDays;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    @Override
    public String toString() {
        return name + " (" + cost + " руб., " + durationDays + " дн.)";
    }
}