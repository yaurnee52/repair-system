package com.repairshop.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ClientDashboardView {
    private final BorderPane root = new BorderPane();
    public final StackPane contentPane = new StackPane();

    public final Button mainPageButton = new Button("Главная страница");
    public final Button profileButton = new Button("Профиль");
    public final Button myMachinesButton = new Button("Мои станки");
    public final Button logoutButton = new Button("Выход");

    public ClientDashboardView() {
        VBox navigationBox = new VBox(10);
        navigationBox.setPadding(new Insets(20));
        navigationBox.getStyleClass().add("navigation-box");

        mainPageButton.setMaxWidth(Double.MAX_VALUE);
        profileButton.setMaxWidth(Double.MAX_VALUE);
        myMachinesButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setMaxWidth(Double.MAX_VALUE);

        navigationBox.getChildren().addAll(mainPageButton, profileButton, myMachinesButton, logoutButton);

        root.setRight(navigationBox);
        root.setCenter(contentPane);
    }

    public Parent getView() {
        return root;
    }
} 