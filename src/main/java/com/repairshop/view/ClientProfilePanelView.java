package com.repairshop.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ClientProfilePanelView {
    private final VBox root = new VBox(15);
    public final TextField companyNameField = new TextField();
    public final TextField usernameField = new TextField();
    public final PasswordField passwordField = new PasswordField();
    public final Button saveButton = new Button("Сохранить изменения");
    public final Label infoLabel = new Label();

    public ClientProfilePanelView() {
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER_LEFT);

        root.getChildren().addAll(
                new Label("Название компании:"),
                companyNameField,
                new Label("Логин:"),
                usernameField,
                new Label("Новый пароль (оставьте пустым, если не меняете):"),
                passwordField,
                saveButton,
                infoLabel
        );
    }

    public Parent getView() {
        return root;
    }
} 