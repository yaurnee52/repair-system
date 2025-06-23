package com.repairshop.controller;

import com.repairshop.containers.Roles;
import com.repairshop.dao.ClientDAO;
import com.repairshop.dao.UserDAO;
import com.repairshop.model.Client;
import com.repairshop.model.Role;
import com.repairshop.model.User;
import com.repairshop.view.AdminDashboardView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        view.clients_deleteButton.setOnAction(event -> handleDelete());
    }

    private void loadClientData() {
        try {
            this.clientCache = clientDAO.readAll().stream()
                    .collect(Collectors.toMap(Client::getId, client -> client));

            Role clientRole = Roles.getInstance().readByName("Клиент");
            if (clientRole == null) {
                view.clients_infoLabel.setText("Ошибка: Роль 'Клиент' не найдена.");
                return;
            }

            List<User> clientUsers = userDAO.readAll().stream()
                    .filter(user -> user.getRoleId() == clientRole.getId())
                    .collect(Collectors.toList());

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
        companyCol.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            Client client = clientCache.get(user.getClientId());
            return new SimpleStringProperty(client != null ? client.getCompanyName() : "N/A");
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