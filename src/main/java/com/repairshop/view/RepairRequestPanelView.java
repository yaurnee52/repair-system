package com.repairshop.view;

import com.repairshop.model.Machine;
import com.repairshop.model.RepairType;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class RepairRequestPanelView {
    private final VBox root = new VBox(20);

    // Панель выбора
    public final ToggleGroup machineChoiceGroup = new ToggleGroup();
    public final RadioButton newMachineRadio = new RadioButton("Новый станок");
    public final RadioButton existingMachineRadio = new RadioButton("Существующий станок");

    // Панель для нового станка
    public final GridPane newMachinePane = new GridPane();
    public final TextField brandField = new TextField();
    public final TextField yearField = new TextField();
    public final TextField countryField = new TextField();

    // Панель для существующего станка
    public final GridPane existingMachinePane = new GridPane();
    public final ComboBox<Machine> machineComboBox = new ComboBox<>();

    // Общие поля
    public final ComboBox<RepairType> repairTypeComboBox = new ComboBox<>();
    public final DatePicker startDatePicker = new DatePicker();
    public final Button submitButton = new Button("Отправить заявку на ремонт");
    public final Label infoLabel = new Label();

    public RepairRequestPanelView() {
        root.setPadding(new Insets(30));

        newMachineRadio.setToggleGroup(machineChoiceGroup);
        existingMachineRadio.setToggleGroup(machineChoiceGroup);
        existingMachineRadio.setSelected(true);

        // Форма для нового станка
        newMachinePane.setHgap(10);
        newMachinePane.setVgap(10);
        newMachinePane.add(new Label("Марка:"), 0, 0);
        newMachinePane.add(brandField, 1, 0);
        newMachinePane.add(new Label("Год выпуска:"), 0, 1);
        newMachinePane.add(yearField, 1, 1);
        newMachinePane.add(new Label("Страна производитель:"), 0, 2);
        newMachinePane.add(countryField, 1, 2);

        // Форма для существующего станка
        existingMachinePane.setHgap(10);
        existingMachinePane.setVgap(10);
        existingMachinePane.add(new Label("Выберите станок:"), 0, 0);
        existingMachinePane.add(machineComboBox, 1, 0);

        VBox choiceBox = new VBox(10, newMachineRadio, existingMachineRadio);

        root.getChildren().addAll(
                new Label("Заказ на ремонт"),
                choiceBox,
                existingMachinePane,
                newMachinePane,
                new Label("Вид ремонта:"),
                repairTypeComboBox,
                new Label("Дата начала ремонта:"),
                startDatePicker,
                submitButton,
                infoLabel
        );
    }

    public Parent getView() {
        return root;
    }
} 