package com.repairshop.controller;

import com.repairshop.dao.ClientDAO;
import com.repairshop.dao.UserDAO;
import com.repairshop.model.Client;
import com.repairshop.model.User;
import com.repairshop.view.ClientProfilePanelView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;

public class ClientProfilePanelController {
    private final ClientProfilePanelView view = new ClientProfilePanelView();
    private User currentUser;
    private Client currentClient;
    private final ClientDAO clientDAO = new ClientDAO();
    private final UserDAO userDAO = new UserDAO();

    public ClientProfilePanelController(User user) {
        this.currentUser = user;
        loadData();
        view.saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleSave();
            }
        });
    }

    private void loadData() {
        view.usernameField.setText(currentUser.getUsername());
        if (currentUser.getClientId() != null) {
            try {
                currentClient = clientDAO.read(currentUser.getClientId());
                if (currentClient != null) {
                    view.companyNameField.setText(currentClient.getCompanyName());
                }
            } catch (Exception e) {
                view.infoLabel.setText("Ошибка загрузки данных компании.");
                e.printStackTrace();
            }
        }
    }

    private void handleSave() {
        try {
            // Обновление данных клиента (компании)
            String companyName = view.companyNameField.getText();
            if (currentClient == null) { // Если клиент еще не создан
                if (!companyName.isEmpty()) {
                    Client newClient = new Client();
                    newClient.setCompanyName(companyName);
                    int newClientId = clientDAO.create(newClient);
                    currentUser.setClientId(newClientId);
                    currentClient = newClient;
                    currentClient.setId(newClientId);
                }
            } else { // Если клиент уже есть, обновляем
                currentClient.setCompanyName(companyName);
                clientDAO.update(currentClient);
            }

            // Обновление данных пользователя
            currentUser.setUsername(view.usernameField.getText());
            String newPassword = view.passwordField.getText();
            if (newPassword != null && !newPassword.isEmpty()) {
                currentUser.setPasswordHash(newPassword);
            }
            userDAO.update(currentUser);

            view.infoLabel.setText("Данные успешно сохранены!");

        } catch (Exception e) {
            view.infoLabel.setText("Ошибка сохранения: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Parent getView() {
        return view.getView();
    }
} 