package com.repairshop.model;

public class MachineModel {
    private int id;
    private String brand;
    private short yearOfRelease;
    private String countryOfManufacture;

    public MachineModel() {}

    public MachineModel(int id, String brand, short yearOfRelease, String countryOfManufacture) {
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

    public short getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(short yearOfRelease) {
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
        return "MachineModel{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", yearOfRelease=" + yearOfRelease +
                ", countryOfManufacture='" + countryOfManufacture + '\'' +
                '}';
    }
}
