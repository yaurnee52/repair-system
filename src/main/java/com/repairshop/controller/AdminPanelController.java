package com.repairshop.controller;

import com.repairshop.containers.Roles;
import com.repairshop.dao.ClientDAO;
import com.repairshop.dao.UserDAO;
import com.repairshop.model.Client;
import com.repairshop.model.Role;
import com.repairshop.model.User;
import com.repairshop.view.AdminDashboardView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminPanelController {
    private final AdminDashboardView view;
    private final UserDAO userDAO = new UserDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private Map<Integer, Client> clientCache;

    public AdminPanelController(AdminDashboardView view) {
        this.view = view;
        initialize();
    }

    private void initialize() {
        setupTable();
        loadClientData();
        view.clients_deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleDelete();
            }
        });
    }

    private void loadClientData() {
        try {
            List<Client> allClients = clientDAO.readAll();
            this.clientCache = new HashMap<>();
            for (Client client : allClients) {
                this.clientCache.put(client.getId(), client);
            }

            Role clientRole = Roles.getInstance().readByName("Клиент");
            if (clientRole == null) {
                view.clients_infoLabel.setText("Ошибка: Роль 'Клиент' не найдена.");
                return;
            }

            List<User> allUsers = userDAO.readAll();
            List<User> clientUsers = new ArrayList<>();
            for (User user : allUsers) {
                if (user.getRoleId() == clientRole.getId()) {
                    clientUsers.add(user);
                }
            }

            view.clients_table.setItems(FXCollections.observableArrayList(clientUsers));
        } catch (Exception e) {
            view.clients_infoLabel.setText("Ошибка загрузки клиентов: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupTable() {
        TableColumn<User, Integer> clientIdCol = new TableColumn<>("ID Клиента");
        clientIdCol.setCellValueFactory(new PropertyValueFactory<>("clientId"));

        TableColumn<User, String> companyCol = new TableColumn<>("Название компании");
        companyCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                User user = param.getValue();
                Client client = clientCache.get(user.getClientId());
                return new SimpleStringProperty(client != null ? client.getCompanyName() : "N/A");
            }
        });

        TableColumn<User, String> loginCol = new TableColumn<>("Логин");
        loginCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> passwordCol = new TableColumn<>("Пароль");
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("passwordHash"));

        view.clients_table.getColumns().setAll(clientIdCol, companyCol, loginCol, passwordCol);
    }

    private void handleDelete() {
        User selected = (User) view.clients_table.getSelectionModel().getSelectedItem();
        if (selected == null || selected.getClientId() == null) {
            view.clients_infoLabel.setText("Выберите клиента для удаления.");
            return;
        }

        Client client = clientCache.get(selected.getClientId());
        String companyName = client != null ? client.getCompanyName() : "ID: " + selected.getClientId();

        try {
            clientDAO.performCascadeDelete(selected.getClientId(), selected.getId());
            view.clients_infoLabel.setText("Клиент '" + companyName + "' успешно удален.");
            loadClientData();
        } catch (Exception e) {
            view.clients_infoLabel.setText("Ошибка удаления: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 