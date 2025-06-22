package com.repairshop.controller;

import com.repairshop.dao.ClientDAO;
import com.repairshop.dao.UserDAO;
import com.repairshop.model.Client;
import com.repairshop.model.User;
import com.repairshop.view.ClientDashboardView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


public class ClientProfilePanelController {
    private final ClientDashboardView view;
    private User currentUser;
    private Client currentClient;
    private final ClientDAO clientDAO = new ClientDAO();
    private final UserDAO userDAO = new UserDAO();

    public ClientProfilePanelController(User user, ClientDashboardView view) {
        this.currentUser = user;
        this.view = view;
        loadData();
        view.profile_saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleSave();
            }
        });
    }

    private void loadData() {
        view.profile_usernameField.setText(currentUser.getUsername());
        if (currentUser.getClientId() != null) {
            try {
                currentClient = clientDAO.read(currentUser.getClientId());
                if (currentClient != null) {
                    view.profile_companyNameField.setText(currentClient.getCompanyName());
                }
            } catch (Exception e) {
                view.profile_infoLabel.setText("Ошибка загрузки данных компании.");
                e.printStackTrace();
            }
        }
    }

    private void handleSave() {
        try {
            String companyName = view.profile_companyNameField.getText();
            if (currentClient == null) {
                if (!companyName.isEmpty()) {
                    Client newClient = new Client();
                    newClient.setCompanyName(companyName);
                    int newClientId = clientDAO.create(newClient);
                    currentUser.setClientId(newClientId);
                    currentClient = newClient;
                    currentClient.setId(newClientId);
                }
            } else {
                currentClient.setCompanyName(companyName);
                clientDAO.update(currentClient);
            }

            currentUser.setUsername(view.profile_usernameField.getText());
            String newPassword = view.profile_passwordField.getText();
            if (newPassword != null && !newPassword.isEmpty()) {
                currentUser.setPasswordHash(newPassword);
            }
            userDAO.update(currentUser);

            view.profile_infoLabel.setText("Данные успешно сохранены!");

        } catch (Exception e) {
            view.profile_infoLabel.setText("Ошибка сохранения: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 