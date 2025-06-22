package com.repairshop.view;

import com.repairshop.model.Machine;
import com.repairshop.model.Repair;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class MyMachinesPanelView {
    private final VBox root = new VBox(15);
    public final TableView<Machine> machinesTable = new TableView<>();
    public final TableView<Repair> repairsTable = new TableView<>();

    public MyMachinesPanelView() {
        root.setPadding(new Insets(30));
        root.getChildren().addAll(
                new Label("Мои станки"),
                machinesTable,
                new Label("История ремонтов"),
                repairsTable
        );
    }

    public Parent getView() {
        return root;
    }
} 