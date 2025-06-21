package com.repairshop;

import com.repairshop.controller.LoginController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 600;
    private static final String INITIAL_WINDOW_TITLE = "Ремонт станков";
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        LoginController loginController = new LoginController();
        Parent root = loginController.getView();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle(INITIAL_WINDOW_TITLE);
        primaryStage.setResizable(false);
        primaryStage.setWidth(WINDOW_WIDTH);
        primaryStage.setHeight(WINDOW_HEIGHT);
        primaryStage.show();
    }

    public static void setRoot(Parent root, String title) {
        primaryStage.getScene().setRoot(root);
        primaryStage.setTitle(title);
    }

    public static void main(String[] args) {
        launch(args);
    }
}