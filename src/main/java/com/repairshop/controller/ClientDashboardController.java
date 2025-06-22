package com.repairshop.controller;

import com.repairshop.MainApp;
import com.repairshop.model.Machine;
import com.repairshop.model.User;
import com.repairshop.view.ClientDashboardView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;

public class ClientDashboardController {
    private final ClientDashboardView view = new ClientDashboardView();
    private final User currentUser;

    public ClientDashboardController(User user) {
        this.currentUser = user;
        setupActions();
        // По умолчанию показываем главную страницу
        showMainPanel();
    }

    private void setupActions() {
        view.mainPageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showMainPanel();
            }
        });
        view.profileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showProfilePanel();
            }
        });
        view.myMachinesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showMyMachinesPanel();
            }
        });
        view.logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleLogout();
            }
        });
    }

    private void showMainPanel() {
        RepairRequestPanelController repairController = new RepairRequestPanelController(currentUser, view);
        view.contentPane.getChildren().setAll(view.getRepairRequestPane());
    }

    private void showMyMachinesPanel() {
        MyMachinesPanelController machinesController = new MyMachinesPanelController(currentUser, view);
        view.contentPane.getChildren().setAll(view.getMyMachinesPane());
    }

    public void showRepairRequestPanel(Machine machine) {
        RepairRequestPanelController repairController = new RepairRequestPanelController(currentUser, machine, view);
        view.contentPane.getChildren().setAll(view.getRepairRequestPane());
    }

    private void showProfilePanel() {
        ClientProfilePanelController profileController = new ClientProfilePanelController(currentUser, view);
        view.contentPane.getChildren().setAll(view.getProfilePane());
    }

    private void handleLogout() {
        LoginController loginController = new LoginController();
        MainApp.setRoot(loginController.getView(), "Ремонт станков");
    }

    public Parent getView() {
        return view.getView();
    }
} 