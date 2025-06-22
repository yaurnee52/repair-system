package com.repairshop.view;

import com.repairshop.model.Machine;
import com.repairshop.model.Repair;
import com.repairshop.model.RepairType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ClientDashboardView {
    private final BorderPane root = new BorderPane();
    public final StackPane contentPane = new StackPane();

    // --- Панели ---
    private Parent profilePane;
    private Parent myMachinesPane;
    private Parent repairRequestPane;

    // --- Навигация ---
    public final Button mainPageButton = new Button("Главная страница");
    public final Button profileButton = new Button("Профиль");
    public final Button myMachinesButton = new Button("Мои станки");
    public final Button logoutButton = new Button("Выход");

    // --- Компоненты панели "Профиль" ---
    public TextField profile_companyNameField;
    public TextField profile_usernameField;
    public PasswordField profile_passwordField;
    public Button profile_saveButton;
    public Label profile_infoLabel;

    // --- Компоненты панели "Мои станки" ---
    public TableView<Machine> machines_table;
    public TableView<Repair> machines_repairsTable;

    // --- Компоненты панели "Заказ на ремонт" ---
    public ToggleGroup repair_machineChoiceGroup;
    public RadioButton repair_newMachineRadio;
    public RadioButton repair_existingMachineRadio;
    public GridPane repair_newMachinePane;
    public TextField repair_brandField;
    public TextField repair_yearField;
    public TextField repair_countryField;
    public GridPane repair_existingMachinePane;
    public ComboBox<Machine> repair_machineComboBox;
    public ComboBox<RepairType> repair_repairTypeComboBox;
    public DatePicker repair_startDatePicker;
    public Button repair_submitButton;
    public Label repair_infoLabel;

    public ClientDashboardView() {
        // Создаем панели
        this.profilePane = createProfilePane();
        this.myMachinesPane = createMyMachinesPane();
        this.repairRequestPane = createRepairRequestPane();

        // Навигация
        VBox navigationBox = new VBox(10);
        navigationBox.setPadding(new Insets(20));
        mainPageButton.setMaxWidth(Double.MAX_VALUE);
        profileButton.setMaxWidth(Double.MAX_VALUE);
        myMachinesButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setMaxWidth(Double.MAX_VALUE);
        navigationBox.getChildren().addAll(mainPageButton, profileButton, myMachinesButton, logoutButton);

        root.setRight(navigationBox);
        root.setCenter(contentPane);
    }

    private Parent createProfilePane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(30));
        pane.setAlignment(Pos.CENTER_LEFT);

        profile_companyNameField = new TextField();
        profile_usernameField = new TextField();
        profile_passwordField = new PasswordField();
        profile_saveButton = new Button("Сохранить изменения");
        profile_infoLabel = new Label();

        pane.getChildren().addAll(
                new Label("Название компании:"), profile_companyNameField,
                new Label("Логин:"), profile_usernameField,
                new Label("Новый пароль (оставьте пустым, если не меняете):"), profile_passwordField,
                profile_saveButton, profile_infoLabel
        );
        return pane;
    }

    private Parent createMyMachinesPane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(30));
        machines_table = new TableView<>();
        machines_repairsTable = new TableView<>();
        pane.getChildren().addAll(new Label("Мои станки"), machines_table, new Label("Общая история ремонтов"), machines_repairsTable);
        return pane;
    }

    private Parent createRepairRequestPane() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(30));

        repair_machineChoiceGroup = new ToggleGroup();
        repair_newMachineRadio = new RadioButton("Новый станок");
        repair_newMachineRadio.setToggleGroup(repair_machineChoiceGroup);
        repair_existingMachineRadio = new RadioButton("Существующий станок");
        repair_existingMachineRadio.setToggleGroup(repair_machineChoiceGroup);
        repair_existingMachineRadio.setSelected(true);

        // Форма для нового станка
        repair_newMachinePane = new GridPane();
        repair_newMachinePane.setHgap(10);
        repair_newMachinePane.setVgap(10);
        repair_brandField = new TextField();
        repair_yearField = new TextField();
        repair_countryField = new TextField();
        repair_newMachinePane.add(new Label("Марка:"), 0, 0);
        repair_newMachinePane.add(repair_brandField, 1, 0);
        repair_newMachinePane.add(new Label("Год выпуска:"), 0, 1);
        repair_newMachinePane.add(repair_yearField, 1, 1);
        repair_newMachinePane.add(new Label("Страна производитель:"), 0, 2);
        repair_newMachinePane.add(repair_countryField, 1, 2);

        // Форма для существующего станка
        repair_existingMachinePane = new GridPane();
        repair_existingMachinePane.setHgap(10);
        repair_existingMachinePane.setVgap(10);
        repair_machineComboBox = new ComboBox<>();
        repair_existingMachinePane.add(new Label("Выберите станок:"), 0, 0);
        repair_existingMachinePane.add(repair_machineComboBox, 1, 0);

        repair_repairTypeComboBox = new ComboBox<>();
        repair_startDatePicker = new DatePicker();
        repair_submitButton = new Button("Отправить заявку на ремонт");
        repair_infoLabel = new Label();

        pane.getChildren().addAll(
                new Label("Заказ на ремонт"), new VBox(10, repair_newMachineRadio, repair_existingMachineRadio),
                repair_existingMachinePane, repair_newMachinePane,
                new Label("Вид ремонта:"), repair_repairTypeComboBox,
                new Label("Дата начала ремонта:"), repair_startDatePicker,
                repair_submitButton, repair_infoLabel
        );
        return pane;
    }

    public Parent getView() { return root; }
    public Parent getProfilePane() { return profilePane; }
    public Parent getMyMachinesPane() { return myMachinesPane; }
    public Parent getRepairRequestPane() { return repairRequestPane; }
} 