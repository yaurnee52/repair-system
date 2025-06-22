package com.repairshop.model;

public class MachineModel {
    private int id;
    private String brand;
    private int yearOfRelease;
    private String countryOfManufacture;

    public MachineModel() {}

    public MachineModel(int id, String brand, int yearOfRelease, String countryOfManufacture) {
        this.id = id;
        this.brand = brand;
        this.yearOfRelease = yearOfRelease;
        this.countryOfManufacture = countryOfManufacture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(int yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public String getCountryOfManufacture() {
        return countryOfManufacture;
    }

    public void setCountryOfManufacture(String countryOfManufacture) {
        this.countryOfManufacture = countryOfManufacture;
    }

    @Override
    public String toString() {
        return brand + " (" + countryOfManufacture + ", " + yearOfRelease + ")";
    }
}
