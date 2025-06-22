package com.repairshop.controller;

import com.repairshop.dao.MachineDAO;
import com.repairshop.dao.MachineModelDAO;
import com.repairshop.dao.RepairDAO;
import com.repairshop.dao.RepairTypeDAO;
import com.repairshop.model.Machine;
import com.repairshop.model.MachineModel;
import com.repairshop.model.Repair;
import com.repairshop.model.RepairType;
import com.repairshop.model.User;
import com.repairshop.view.MyMachinesPanelView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMachinesPanelController {

    private final MyMachinesPanelView view;
    private final User currentUser;
    private final ClientDashboardController dashboardController;

    private final MachineDAO machineDAO = new MachineDAO();
    private final MachineModelDAO machineModelDAO = new MachineModelDAO();
    private final RepairDAO repairDAO = new RepairDAO();
    private final RepairTypeDAO repairTypeDAO = new RepairTypeDAO();

    private Map<Integer, MachineModel> machineModelMap;
    private Map<Integer, RepairType> repairTypeMap;

    public MyMachinesPanelController(User user, ClientDashboardController dashboardController) {
        this.currentUser = user;
        this.dashboardController = dashboardController;
        this.view = new MyMachinesPanelView();
        initialize();
    }

    private void initialize() {
        setupTables(); // Сначала настраиваем таблицы
        loadData(); // Потом загружаем в них данные
        setupListeners();
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

        view.machinesTable.getColumns().clear();
        view.machinesTable.getColumns().addAll(idCol, brandCol, yearCol, countryCol);


        // --- Таблица ремонтов ---
        TableColumn<Repair, String> dateCol = new TableColumn<>("Дата начала");
        dateCol.setCellValueFactory(new PropertyValueFactoryForRepair("date"));
        dateCol.setSortable(false);

        TableColumn<Repair, String> repairNameCol = new TableColumn<>("Вид ремонта");
        repairNameCol.setCellValueFactory(new PropertyValueFactoryForRepair("name"));
        repairNameCol.setSortable(false);

        TableColumn<Repair, String> costCol = new TableColumn<>("Стоимость");
        costCol.setCellValueFactory(new PropertyValueFactoryForRepair("cost"));
        costCol.setSortable(false);

        view.repairsTable.getColumns().clear();
        view.repairsTable.getColumns().addAll(dateCol, repairNameCol, costCol);
    }

    private void loadData() {
        try {
            machineModelMap = new HashMap<>();
            List<MachineModel> models = machineModelDAO.readAll();
            for (MachineModel model : models) {
                machineModelMap.put(model.getId(), model);
            }

            repairTypeMap = new HashMap<>();
            List<RepairType> types = repairTypeDAO.readAll();
            for (RepairType type : types) {
                repairTypeMap.put(type.getId(), type);
            }

            if(currentUser.getClientId() != null){
                List<Machine> machines = machineDAO.readByClientId(currentUser.getClientId());
                view.machinesTable.setItems(FXCollections.observableArrayList(machines));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupListeners() {
        view.machinesTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Machine>() {
            @Override
            public void changed(ObservableValue<? extends Machine> observable, Machine oldValue, Machine newValue) {
                if (newValue != null) {
                    loadRepairsForMachine(newValue);
                } else {
                    view.repairsTable.getItems().clear();
                }
            }
        });
    }

    private void loadRepairsForMachine(Machine machine) {
        try {
            List<Repair> repairs = repairDAO.readByMachineId(machine.getId());
            view.repairsTable.setItems(FXCollections.observableArrayList(repairs));
        } catch (Exception e) {
            e.printStackTrace();
            view.repairsTable.getItems().clear();
        }
    }

    public Parent getView() {
        return view.getView();
    }

    // Вспомогательный внутренний класс для таблицы станков
    private class PropertyValueFactoryForMachine implements javafx.util.Callback<TableColumn.CellDataFeatures<Machine, String>, ObservableValue<String>> {
        private final String property;
        public PropertyValueFactoryForMachine(String property) { this.property = property; }
        @Override
        public ObservableValue<String> call(TableColumn.CellDataFeatures<Machine, String> data) {
            Machine machine = data.getValue();
            MachineModel model = machineModelMap.get(machine.getMachineModelId());
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
    private class PropertyValueFactoryForRepair implements javafx.util.Callback<TableColumn.CellDataFeatures<Repair, String>, ObservableValue<String>> {
        private final String property;
        public PropertyValueFactoryForRepair(String property) { this.property = property; }
        @Override
        public ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> data) {
            Repair repair = data.getValue();
            RepairType type = repairTypeMap.get(repair.getRepairTypeId());
            String value = "";
            if ("date".equals(property)) {
                Date date = repair.getStartDate();
                if (date != null) {
                    value = new SimpleDateFormat("dd.MM.yyyy").format(date);
                }
            } else if (type != null) {
                if ("name".equals(property)) value = type.getName();
                if ("cost".equals(property)) value = String.valueOf(type.getCost());
            }
            return new SimpleStringProperty(value);
        }
    }
} 