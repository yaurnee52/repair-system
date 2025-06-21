package com.repairshop.controller;

import com.repairshop.MainApp;
import com.repairshop.model.User;
import com.repairshop.view.DashboardView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;

public class DashboardController {
    private final DashboardView view = new DashboardView();
    private final User currentUser;

    public DashboardController(User user) {
        this.currentUser = user;
        setupView();
        setupActions();
    }

    private void setupView() {
        view.welcomeLabel.setText("Добро пожаловать, " + currentUser.getUsername() + "!");
    }

    private void setupActions() {
        view.logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleLogout();
            }
        });
    }

    private void handleLogout() {
        LoginController loginController = new LoginController();
        MainApp.setRoot(loginController.getView(), "Ремонт станков");
    }

    public Parent getView() {
        return view.getView();
    }
} 