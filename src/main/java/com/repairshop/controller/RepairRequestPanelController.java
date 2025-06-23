package com.repairshop.controller;

import com.repairshop.containers.MachineModels;
import com.repairshop.containers.Machines;
import com.repairshop.containers.RepairTypes;
import com.repairshop.dao.MachineDAO;
import com.repairshop.dao.RepairDAO;
import com.repairshop.model.*;
import com.repairshop.view.ClientDashboardView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Toggle;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepairRequestPanelController {

    private final ClientDashboardView view;
    private final User currentUser;
    private Machine preselectedMachine;

    private final MachineDAO machineDAO = new MachineDAO();
    private final RepairDAO repairDAO = new RepairDAO();

    public RepairRequestPanelController(User user, ClientDashboardView view) {
        this.currentUser = user;
        this.view = view;
        initialize();
    }

    public RepairRequestPanelController(User user, Machine machine, ClientDashboardView view) {
        this.currentUser = user;
        this.preselectedMachine = machine;
        this.view = view;
        initialize();
    }

    private void initialize() {
        loadData();
        setupListeners();
        updatePanels();

        if (preselectedMachine != null) {
            view.repair_existingMachineRadio.setSelected(true);
            view.repair_machineComboBox.setValue(preselectedMachine);
        }
    }

    private void loadData() {
        try {
            // Загрузка станков клиента из контейнера
            if(currentUser.getClientId() != null){
                List<Machine> clientMachines = Machines.getInstance().readByClientId(currentUser.getClientId());
                view.repair_machineComboBox.setItems(FXCollections.observableArrayList(clientMachines));
            } else {
                view.repair_machineComboBox.setItems(FXCollections.observableArrayList(new ArrayList<Machine>()));
            }

            List<RepairType> repairTypes = RepairTypes.getInstance().readAll();
            view.repair_repairTypeComboBox.setItems(FXCollections.observableArrayList(repairTypes));

            Callback<ListView<Machine>, ListCell<Machine>> factory = new Callback<ListView<Machine>, ListCell<Machine>>() {
                @Override
                public ListCell<Machine> call(ListView<Machine> param) {
                    return new ListCell<Machine>() {
                        @Override
                        protected void updateItem(Machine item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setText(null);
                            } else {
                                MachineModel model = MachineModels.getInstance().readById(item.getMachineModelId());
                                setText(model != null ? model.toString() + " (ID станка: " + item.getId() + ")" : "Станок ID: " + item.getId());
                            }
                        }
                    };
                }
            };
            view.repair_machineComboBox.setCellFactory(factory);
            view.repair_machineComboBox.setButtonCell(factory.call(null));

        } catch (Exception e) {
            view.repair_infoLabel.setText("Ошибка загрузки данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupListeners() {
        view.repair_machineChoiceGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                updatePanels();
            }
        });

        view.repair_submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleSubmit();
            }
        });
    }

    private void updatePanels() {
        boolean isNewMachine = view.repair_newMachineRadio.isSelected();
        view.repair_newMachinePane.setVisible(isNewMachine);
        view.repair_newMachinePane.setManaged(isNewMachine);
        view.repair_existingMachinePane.setVisible(!isNewMachine);
        view.repair_existingMachinePane.setManaged(!isNewMachine);
    }

    private void handleSubmit() {
        try {
            Machine machineToRepair;
            if (view.repair_newMachineRadio.isSelected()) {
                if (currentUser.getClientId() == null) {
                    view.repair_infoLabel.setText("Сначала укажите название компании в Профиле!");
                    return;
                }

                String brand = view.repair_brandField.getText();
                String yearText = view.repair_yearField.getText();
                String country = view.repair_countryField.getText();

                if (brand.isEmpty() || yearText.isEmpty() || country.isEmpty()) {
                    view.repair_infoLabel.setText("Заполните все поля для нового станка!");
                    return;
                }
                int year = Integer.parseInt(yearText);
                MachineModel model = MachineModels.getInstance().findOrCreate(brand, year, country);

                Machine newMachine = new Machine();
                newMachine.setClientId(currentUser.getClientId());
                newMachine.setMachineModelId(model.getId());
                int newMachineId = machineDAO.create(newMachine);
                newMachine.setId(newMachineId);
                machineToRepair = newMachine;
                // Обновляем кэш
                Machines.getInstance().refreshCache();
            } else {
                machineToRepair = view.repair_machineComboBox.getValue();
                if (machineToRepair == null) {
                    view.repair_infoLabel.setText("Выберите станок для ремонта!");
                    return;
                }
            }

            RepairType selectedRepairType = view.repair_repairTypeComboBox.getValue();
            LocalDate selectedDate = view.repair_startDatePicker.getValue();

            if (selectedRepairType == null || selectedDate == null) {
                view.repair_infoLabel.setText("Выберите вид ремонта и дату!");
                return;
            }

            Repair newRepair = new Repair();
            newRepair.setMachineId(machineToRepair.getId());
            newRepair.setRepairTypeId(selectedRepairType.getId());
            newRepair.setStartDate(java.sql.Date.valueOf(selectedDate));
            repairDAO.create(newRepair);

            view.repair_infoLabel.setText("Заявка на ремонт успешно создана!");

            if (view.repair_newMachineRadio.isSelected()) {
                view.repair_brandField.clear();
                view.repair_yearField.clear();
                view.repair_countryField.clear();
            }
            view.repair_startDatePicker.setValue(null);
            loadData();

        } catch (NumberFormatException e) {
            view.repair_infoLabel.setText("Год выпуска должен быть числом!");
        } catch (Exception e) {
            view.repair_infoLabel.setText("Ошибка создания заявки: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 