package com.repairshop.controller;

import com.repairshop.dao.*;
import com.repairshop.model.*;
import com.repairshop.view.RepairRequestPanelView;
import com.repairshop.containers.MachineModels;
import com.repairshop.containers.RepairTypes;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Toggle;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepairRequestPanelController {

    private final RepairRequestPanelView view = new RepairRequestPanelView();
    private final User currentUser;
    private Machine preselectedMachine;

    private final MachineDAO machineDAO = new MachineDAO();
    private final RepairDAO repairDAO = new RepairDAO();

    private Map<Integer, MachineModel> machineModelMap = new HashMap<>();

    public RepairRequestPanelController(User user) {
        this.currentUser = user;
        initialize();
    }

    public RepairRequestPanelController(User user, Machine machine) {
        this.currentUser = user;
        this.preselectedMachine = machine;
        initialize();
    }

    private void initialize() {
        loadData();
        setupListeners();
        updatePanels();

        if (preselectedMachine != null) {
            view.existingMachineRadio.setSelected(true);
            view.machineComboBox.setValue(preselectedMachine);
        }
    }

    private void loadData() {
        try {
            // Загрузка станков клиента
            if(currentUser.getClientId() != null){
                List<Machine> clientMachines = machineDAO.readByClientId(currentUser.getClientId());
                view.machineComboBox.setItems(FXCollections.observableArrayList(clientMachines));
            } else {
                view.machineComboBox.setItems(FXCollections.observableArrayList(new ArrayList<Machine>()));
            }


            // Загрузка видов ремонта
            List<RepairType> repairTypes = RepairTypes.getInstance().getAllRepairTypes();
            view.repairTypeComboBox.setItems(FXCollections.observableArrayList(repairTypes));

            // Фабрика для кастомного отображения станков в ComboBox
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
                                MachineModel model = MachineModels.getInstance().getModelById(item.getMachineModelId());
                                setText(model != null ? model.toString() + " (ID станка: " + item.getId() + ")" : "Станок ID: " + item.getId());
                            }
                        }
                    };
                }
            };
            view.machineComboBox.setCellFactory(factory);
            view.machineComboBox.setButtonCell(factory.call(null));

        } catch (Exception e) {
            view.infoLabel.setText("Ошибка загрузки данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupListeners() {
        view.machineChoiceGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                updatePanels();
            }
        });

        view.submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleSubmit();
            }
        });
    }

    private void updatePanels() {
        boolean isNewMachine = view.newMachineRadio.isSelected();
        view.newMachinePane.setVisible(isNewMachine);
        view.newMachinePane.setManaged(isNewMachine);
        view.existingMachinePane.setVisible(!isNewMachine);
        view.existingMachinePane.setManaged(!isNewMachine);
    }

    private void handleSubmit() {
        try {
            Machine machineToRepair;
            if (view.newMachineRadio.isSelected()) {

                if (currentUser.getClientId() == null) {
                    view.infoLabel.setText("Сначала укажите название компании в Профиле!");
                    return;
                }

                String brand = view.brandField.getText();
                String yearText = view.yearField.getText();
                String country = view.countryField.getText();

                if (brand.isEmpty() || yearText.isEmpty() || country.isEmpty()) {
                    view.infoLabel.setText("Заполните все поля для нового станка!");
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

            } else {
                machineToRepair = view.machineComboBox.getValue();
                if (machineToRepair == null) {
                    view.infoLabel.setText("Выберите станок для ремонта!");
                    return;
                }
            }

            RepairType selectedRepairType = view.repairTypeComboBox.getValue();
            LocalDate selectedDate = view.startDatePicker.getValue();

            if (selectedRepairType == null || selectedDate == null) {
                view.infoLabel.setText("Выберите вид ремонта и дату!");
                return;
            }

            Repair newRepair = new Repair();
            newRepair.setMachineId(machineToRepair.getId());
            newRepair.setRepairTypeId(selectedRepairType.getId());
            newRepair.setStartDate(java.sql.Date.valueOf(selectedDate));
            repairDAO.create(newRepair);

            view.infoLabel.setText("Заявка на ремонт успешно создана!");

            // Очищаем поля и перезагружаем данные, чтобы новый станок появился в списке
            if (view.newMachineRadio.isSelected()) {
                view.brandField.clear();
                view.yearField.clear();
                view.countryField.clear();
            }
            view.startDatePicker.setValue(null);
            loadData();

        } catch (NumberFormatException e) {
            view.infoLabel.setText("Год выпуска должен быть числом!");
        } catch (Exception e) {
            view.infoLabel.setText("Ошибка создания заявки: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Parent getView() {
        return view.getView();
    }
} 