package com.repairshop.controller;

import com.repairshop.containers.MachineModels;
import com.repairshop.containers.Machines;
import com.repairshop.containers.RepairTypes;
import com.repairshop.dao.MachineDAO;
import com.repairshop.dao.RepairDAO;
import com.repairshop.model.*;
import com.repairshop.view.ClientDashboardView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyMachinesPanelController {

    private final ClientDashboardView view;
    private final User currentUser;

    private final MachineDAO machineDAO = new MachineDAO();
    private final RepairDAO repairDAO = new RepairDAO();

    public MyMachinesPanelController(User user, ClientDashboardView view) {
        this.currentUser = user;
        this.view = view;
        initialize();
    }

    private void initialize() {
        setupTables();
        loadData();
    }

    private void setupTables() {
        // --- Таблица станков ---
        TableColumn<Machine, Integer> idCol = new TableColumn<>("ID станка");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setSortable(false);

        TableColumn<Machine, String> brandCol = new TableColumn<>("Марка");
        brandCol.setCellValueFactory(new PropertyValueFactoryForMachine("brand"));
        brandCol.setSortable(false);

        TableColumn<Machine, String> yearCol = new TableColumn<>("Год выпуска");
        yearCol.setCellValueFactory(new PropertyValueFactoryForMachine("year"));
        yearCol.setSortable(false);

        TableColumn<Machine, String> countryCol = new TableColumn<>("Страна");
        countryCol.setCellValueFactory(new PropertyValueFactoryForMachine("country"));
        countryCol.setSortable(false);

        view.machines_table.getColumns().setAll(idCol, brandCol, yearCol, countryCol);

        // --- Таблица ремонтов (добавляем колонку "Станок") ---
        TableColumn<Repair, String> machineRepairCol = new TableColumn<>("Станок");
        machineRepairCol.setCellValueFactory(new PropertyValueFactoryForRepair("machine"));
        machineRepairCol.setSortable(false);

        TableColumn<Repair, String> dateCol = new TableColumn<>("Дата начала");
        dateCol.setCellValueFactory(new PropertyValueFactoryForRepair("date"));
        dateCol.setSortable(false);

        TableColumn<Repair, String> repairNameCol = new TableColumn<>("Вид ремонта");
        repairNameCol.setCellValueFactory(new PropertyValueFactoryForRepair("name"));
        repairNameCol.setSortable(false);

        TableColumn<Repair, String> costCol = new TableColumn<>("Стоимость");
        costCol.setCellValueFactory(new PropertyValueFactoryForRepair("cost"));
        costCol.setSortable(false);

        view.machines_repairsTable.getColumns().setAll(machineRepairCol, dateCol, repairNameCol, costCol);
    }

    private void loadData() {
        try {
            if (currentUser.getClientId() != null) {
                // 1. Загружаем станки клиента из нового контейнера
                List<Machine> machines = Machines.getInstance().readByClientId(currentUser.getClientId());
                view.machines_table.setItems(FXCollections.observableArrayList(machines));

                // 2. Загружаем ВСЮ историю ремонтов для клиента
                List<Repair> repairs = repairDAO.findByClientId(currentUser.getClientId());
                view.machines_repairsTable.setItems(FXCollections.observableArrayList(repairs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Вспомогательный внутренний класс для таблицы станков
    private class PropertyValueFactoryForMachine implements Callback<TableColumn.CellDataFeatures<Machine, String>, ObservableValue<String>> {
        private final String property;
        public PropertyValueFactoryForMachine(String property) { this.property = property; }
        @Override
        public ObservableValue<String> call(TableColumn.CellDataFeatures<Machine, String> data) {
            Machine machine = data.getValue();
            MachineModel model = MachineModels.getInstance().readById(machine.getMachineModelId());
            String value = "";
            if (model != null) {
                if ("brand".equals(property)) value = model.getBrand();
                if ("year".equals(property)) value = String.valueOf(model.getYearOfRelease());
                if ("country".equals(property)) value = model.getCountryOfManufacture();
            }
            return new SimpleStringProperty(value);
        }
    }

    // Вспомогательный внутренний класс для таблицы ремонтов
    private class PropertyValueFactoryForRepair implements Callback<TableColumn.CellDataFeatures<Repair, String>, ObservableValue<String>> {
        private final String property;
        public PropertyValueFactoryForRepair(String property) { this.property = property; }
        @Override
        public ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> data) {
            Repair repair = data.getValue();
            String value = "";

            if ("machine".equals(property)) {
                Machine machine = Machines.getInstance().readById(repair.getMachineId());
                if (machine != null) {
                    MachineModel model = MachineModels.getInstance().readById(machine.getMachineModelId());
                    if (model != null) {
                        value = model.toString();
                    }
                }
                 if(value.isEmpty()) {
                    value = "Станок ID: " + repair.getMachineId();
                }
            }
            else if ("date".equals(property)) {
                Date date = repair.getStartDate();
                if (date != null) {
                    value = new SimpleDateFormat("dd.MM.yyyy").format(date);
                }
            } else if (repair.getRepairTypeId() != 0) {
                RepairType type = RepairTypes.getInstance().readById(repair.getRepairTypeId());
                if (type != null) {
                    if ("name".equals(property)) value = type.getName();
                    if ("cost".equals(property)) value = String.valueOf(type.getCost());
                }
            }
            return new SimpleStringProperty(value);
        }
    }
} 
 