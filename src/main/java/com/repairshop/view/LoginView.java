package com.repairshop.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginView {
    private final VBox root = new VBox(10);
    public final TextField loginField = new TextField();
    public final PasswordField passwordField = new PasswordField();
    public final ComboBox<String> roleBox = new ComboBox<>();
    public final Button loginButton = new Button("Войти");
    public final Button registerButton = new Button("Регистрация");
    public final Label errorLabel = new Label();

    // --- Элементы для регистрации ---
    public final TextField regLoginField = new TextField();
    public final PasswordField regPasswordField = new PasswordField();
    public final PasswordField regConfirmPasswordField = new PasswordField();
    public final ComboBox<String> regRoleBox = new ComboBox<>();
    public final Label adminCodeLabel = new Label("Секретный код администратора:");
    public final PasswordField adminCodeField = new PasswordField();
    public final Button regSubmitButton = new Button("Зарегистрироваться");
    public final Button regBackButton = new Button("Назад");
    public final Label regErrorLabel = new Label();

    private final VBox regRoot = new VBox(10);

    public LoginView() {
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Авторизация пользователя");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        loginField.setPromptText("Логин");
        passwordField.setPromptText("Пароль");

        roleBox.getItems().addAll("Администратор", "Клиент");
        roleBox.setPromptText("Выберите роль");

        root.getChildren().addAll(
                title,
                loginField,
                passwordField,
                roleBox,
                loginButton,
                registerButton,
                errorLabel
        );

        // --- Регистрация ---
        regRoot.setPadding(new Insets(40));
        regRoot.setAlignment(Pos.CENTER);

        Label regTitle = new Label("Регистрация пользователя");
        regTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        regLoginField.setPromptText("Логин");
        regPasswordField.setPromptText("Пароль");
        regConfirmPasswordField.setPromptText("Повторите пароль");

        regRoleBox.getItems().addAll("Администратор", "Клиент");
        regRoleBox.setPromptText("Выберите роль");

        adminCodeLabel.setVisible(false);
        adminCodeLabel.setManaged(false);
        adminCodeField.setVisible(false);
        adminCodeField.setManaged(false);
        adminCodeField.setPromptText("Секретный код");

        regRoot.getChildren().addAll(
                regTitle,
                regLoginField,
                regPasswordField,
                regConfirmPasswordField,
                regRoleBox,
                adminCodeLabel,
                adminCodeField,
                regSubmitButton,
                regBackButton,
                regErrorLabel
        );
    }

    public Parent getView() {
        return root;
    }

    public Parent getRegistrationView() {
        return regRoot;
    }
}