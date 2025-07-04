package com.repairshop.controller;

import com.repairshop.containers.Machines;
import com.repairshop.containers.RepairTypes;
import com.repairshop.dao.RepairDAO;
import com.repairshop.model.Machine;
import com.repairshop.model.Repair;
import com.repairshop.model.RepairType;
import com.repairshop.view.AdminDashboardView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.text.SimpleDateFormat;
import java.util.List;


public class AdminReportsPanelController {
    private final AdminDashboardView view;
    private final RepairDAO repairDAO = new RepairDAO();

    public AdminReportsPanelController(AdminDashboardView view) {
        this.view = view;
        initialize();
    }

    private void initialize() {
        setupTables();
        loadData();
        view.reports_searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleSearch();
            }
        });
    }

    private void loadData() {
        try {
            List<Repair> allRepairs = repairDAO.readAll();
            view.reports_allRepairsTable.setItems(FXCollections.observableArrayList(allRepairs));
        } catch (Exception e) {
            view.reports_searchInfoLabel.setText("Ошибка загрузки отчетов: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupTables() {
        setupRepairTable(view.reports_allRepairsTable);
        setupRepairTable(view.reports_clientRepairsTable);
    }

    private void setupRepairTable(javafx.scene.control.TableView<Repair> table) {
        TableColumn<Repair, Integer> clientIdCol = new TableColumn<>("ID Клиента");
        clientIdCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Repair, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Repair, Integer> param) {
                Repair repair = param.getValue();
                Machine machine = Machines.getInstance().readById(repair.getMachineId());
                return new SimpleObjectProperty<>(machine != null ? machine.getClientId() : null);
            }
        });

        TableColumn<Repair, Integer> machineIdCol = new TableColumn<>("ID Станка");
        machineIdCol.setCellValueFactory(new PropertyValueFactory<>("machineId"));

        TableColumn<Repair, String> repairNameCol = new TableColumn<>("Название ремонта");
        repairNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Repair, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> param) {
                RepairType type = RepairTypes.getInstance().readById(param.getValue().getRepairTypeId());
                return new SimpleStringProperty(type != null ? type.getName() : "N/A");
            }
        });

        TableColumn<Repair, Double> costCol = new TableColumn<>("Стоимость");
        costCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Repair, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Repair, Double> param) {
                RepairType type = RepairTypes.getInstance().readById(param.getValue().getRepairTypeId());
                return new SimpleObjectProperty<>(type != null ? type.getCost() : 0.0);
            }
        });
        
        TableColumn<Repair, Integer> durationCol = new TableColumn<>("Длительность");
        durationCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Repair, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Repair, Integer> param) {
                RepairType type = RepairTypes.getInstance().readById(param.getValue().getRepairTypeId());
                return new SimpleObjectProperty<>(type != null ? type.getDurationDays() : 0);
            }
        });

        TableColumn<Repair, String> dateCol = new TableColumn<>("Дата начала");
        dateCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Repair, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> param) {
                return new SimpleStringProperty(new SimpleDateFormat("dd.MM.yyyy").format(param.getValue().getStartDate()));
            }
        });

        table.getColumns().setAll(clientIdCol, machineIdCol, repairNameCol, costCol, durationCol, dateCol);
    }

    private void handleSearch() {
        String clientIdText = view.reports_searchField.getText();
        if (clientIdText.isEmpty()) {
            view.reports_searchInfoLabel.setText("Введите ID клиента для поиска.");
            view.reports_clientRepairsTable.getItems().clear();
            return;
        }

        try {
            int clientId = Integer.parseInt(clientIdText);
            List<Repair> clientRepairs = repairDAO.findByClientId(clientId);
            if (clientRepairs.isEmpty()) {
                view.reports_searchInfoLabel.setText("Ремонты для клиента с ID " + clientId + " не найдены.");
            } else {
                view.reports_searchInfoLabel.setText("Найдены ремонты для клиента ID " + clientId);
            }
            view.reports_clientRepairsTable.setItems(FXCollections.observableArrayList(clientRepairs));
        } catch (NumberFormatException e) {
            view.reports_searchInfoLabel.setText("ID клиента должен быть числом.");
        } catch (Exception e) {
            view.reports_searchInfoLabel.setText("Ошибка поиска: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 