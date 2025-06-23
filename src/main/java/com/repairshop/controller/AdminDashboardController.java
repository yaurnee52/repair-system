package com.repairshop.controller;

import com.repairshop.MainApp;
import com.repairshop.model.User;
import com.repairshop.view.AdminDashboardView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;

public class AdminDashboardController {
    private final AdminDashboardView view = new AdminDashboardView();

    public AdminDashboardController(User user) {
        setupActions();
        showReportsPanel(); // Показываем отчеты по умолчанию
    }

    private void setupActions() {
        view.reportsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showReportsPanel();
            }
        });
        view.clientManagementButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showClientManagementPanel();
            }
        });
        view.logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleLogout();
            }
        });
    }

    private void showReportsPanel() {
        new AdminReportsPanelController(view);
        view.contentPane.getChildren().setAll(view.getReportsPane());
    }

    private void showClientManagementPanel() {
        new AdminPanelController(view);
        view.contentPane.getChildren().setAll(view.getClientManagementPane());
    }

    private void handleLogout() {
        LoginController loginController = new LoginController();
        MainApp.setRoot(loginController.getView(), "Ремонт станков");
    }

    public Parent getView() {
        return view.getView();
    }
} 