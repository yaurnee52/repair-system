package com.repairshop.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardView {
    private final VBox root = new VBox(20);
    public final Label welcomeLabel = new Label();
    public final Button logoutButton = new Button("Выйти");

    public DashboardView() {
        root.setPadding(new Insets(50));
        root.setAlignment(Pos.CENTER);
        welcomeLabel.setStyle("-fx-font-size: 24px;");

        root.getChildren().addAll(welcomeLabel, logoutButton);
    }

    public Parent getView() {
        return root;
    }
} 