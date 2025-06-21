package com.repairshop.controller;

import com.repairshop.MainApp;
import com.repairshop.dao.RoleDAO;
import com.repairshop.dao.UserDAO;
import com.repairshop.model.Role;
import com.repairshop.model.User;
import com.repairshop.view.LoginView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;

public class LoginController {
    private final LoginView view = new LoginView();
    private static final String ADMIN_SECRET_CODE = "admin123";

    public LoginController() {
        view.loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleLogin();
            }
        });
        view.registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showRegistration();
            }
        });
        view.regSubmitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleRegister();
            }
        });
        view.regBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showLogin();
            }
        });

        view.regRoleBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                boolean isAdmin = "Администратор".equals(newValue);
                view.adminCodeLabel.setVisible(isAdmin);
                view.adminCodeLabel.setManaged(isAdmin);
                view.adminCodeField.setVisible(isAdmin);
                view.adminCodeField.setManaged(isAdmin);
                if (isAdmin) {
                    view.adminCodeField.requestFocus();
                }
            }
        });
    }

    public Parent getView() {
        return view.getView();
    }

    // Показывает окно регистрации
    private void showRegistration() {
        view.regLoginField.clear();
        view.regPasswordField.clear();
        view.regConfirmPasswordField.clear();
        view.regRoleBox.setValue(null);
        view.regErrorLabel.setText("");
        // Меняем корневой элемент на форму регистрации
        view.loginButton.getScene().setRoot(view.getRegistrationView());
    }

    // Возвращает к окну авторизации
    private void showLogin() {
        view.loginField.clear();
        view.passwordField.clear();
        view.roleBox.getSelectionModel().clearSelection();
        view.regSubmitButton.getScene().setRoot(view.getView());
    }

    private void handleLogin() {
        String username = view.loginField.getText();
        String password = view.passwordField.getText();
        String roleName = view.roleBox.getValue();

        if (username.isEmpty() || password.isEmpty() || roleName == null) {
            view.errorLabel.setText("Заполните все поля и выберите роль!");
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.readByUsername(username);

            if (user == null) {
                view.errorLabel.setText("Неверный логин или пароль");
                return;
            }

            RoleDAO roleDAO = new RoleDAO();
            Role selectedRole = roleDAO.readByName(roleName);

            if (user.getPasswordHash().equals(password) && selectedRole != null && user.getRoleId() == selectedRole.getId()) {
                DashboardController dashboardController = new DashboardController(user);
                MainApp.setRoot(dashboardController.getView(), "Личный кабинет: " + user.getUsername());
            } else {
                view.errorLabel.setText("Неверный логин, пароль или роль");
            }
        } catch (Exception ex) {
            view.errorLabel.setText("Ошибка авторизации: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void handleRegister() {
        String username = view.regLoginField.getText();
        String password = view.regPasswordField.getText();
        String confirmPassword = view.regConfirmPasswordField.getText();
        String roleName = view.regRoleBox.getValue();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || roleName == null) {
            view.regErrorLabel.setText("Заполните все поля и выберите роль!");
            return;
        }
        if (!password.equals(confirmPassword)) {
            view.regErrorLabel.setText("Пароли не совпадают!");
            return;
        }

        if ("Администратор".equals(roleName)) {
            if (!ADMIN_SECRET_CODE.equals(view.adminCodeField.getText())) {
                view.regErrorLabel.setText("Неверный секретный код администратора!");
                return;
            }
        }

        try {
            UserDAO userDAO = new UserDAO();
            if (userDAO.readByUsername(username) != null) {
                view.regErrorLabel.setText("Пользователь с таким логином уже существует!");
                return;
            }
            // Получаем id роли по названию
            RoleDAO roleDAO = new RoleDAO();
            Role role = roleDAO.readByName(roleName);
            if (role == null) {
                view.regErrorLabel.setText("Ошибка: выбранная роль не найдена!");
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setPasswordHash(password); // Для теста, потом заменить на hash
            user.setRoleId(role.getId());
            userDAO.create(user);
            view.errorLabel.setText("Регистрация успешна! Теперь войдите.");
            showLogin();
        } catch (Exception ex) {
            view.regErrorLabel.setText("Ошибка регистрации: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}