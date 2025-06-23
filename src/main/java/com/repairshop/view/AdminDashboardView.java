package com.repairshop.view;

import com.repairshop.model.Repair;
import com.repairshop.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AdminDashboardView {
    private final BorderPane root = new BorderPane();
    public final StackPane contentPane = new StackPane();

    // Панели
    private Parent reportsPane;
    private Parent clientManagementPane;

    // Навигация
    public final Button reportsButton = new Button("Отчеты");
    public final Button clientManagementButton = new Button("Управление клиентами");
    public final Button logoutButton = new Button("Выход");

    // Компоненты "Отчеты"
    public TableView<Repair> reports_allRepairsTable;
    public TextField reports_searchField;
    public Button reports_searchButton;
    public TableView<Repair> reports_clientRepairsTable;
    public Label reports_searchInfoLabel;

    // Компоненты "Управление клиентами"
    public TableView<User> clients_table;
    public Button clients_deleteButton;
    public Label clients_infoLabel;

    public AdminDashboardView() {
        this.reportsPane = createReportsPane();
        this.clientManagementPane = createClientManagementPane();

        VBox navigationBox = new VBox(10, reportsButton, clientManagementButton, logoutButton);
        navigationBox.setPadding(new Insets(20));
        reportsButton.setMaxWidth(Double.MAX_VALUE);
        clientManagementButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setMaxWidth(Double.MAX_VALUE);

        root.setRight(navigationBox);
        root.setCenter(contentPane);
    }

    private Parent createReportsPane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(30));
        reports_allRepairsTable = new TableView<>();
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        reports_searchField = new TextField();
        reports_searchField.setPromptText("Введите ID клиента");
        reports_searchButton = new Button("Поиск");
        reports_searchInfoLabel = new Label();
        searchBox.getChildren().addAll(new Label("Поиск по клиенту:"), reports_searchField, reports_searchButton);
        reports_clientRepairsTable = new TableView<>();
        pane.getChildren().addAll(
                new Label("Все ремонты в системе"), reports_allRepairsTable,
                new Separator(),
                searchBox, reports_searchInfoLabel,
                new Label("История ремонтов для найденного клиента:"), reports_clientRepairsTable
        );
        return pane;
    }

    private Parent createClientManagementPane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(30));
        clients_table = new TableView<>();
        clients_deleteButton = new Button("Удалить выбранного клиента");
        clients_infoLabel = new Label();
        pane.getChildren().addAll(new Label("Все клиенты системы"), clients_table, clients_deleteButton, clients_infoLabel);
        return pane;
    }

    public Parent getView() { return root; }
    public Parent getReportsPane() { return reportsPane; }
    public Parent getClientManagementPane() { return clientManagementPane; }
} 