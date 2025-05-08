package org.example.ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.model.Catalog;
import org.example.model.Process;
import org.example.service.ProcessCatalogueService;

import java.util.stream.Collectors;

public class CatalogueSelectorController {
    @FXML
    private TableView<Process> processesTable;
    @FXML private TableColumn<Process, Integer> pidCol;
    @FXML private TableColumn<Process, String> nameCol;
    @FXML private TableColumn<Process, String> userCol;
    @FXML private TableColumn<Process, Boolean> priorityCol;
    @FXML private TableColumn<Process, String> stateCol;
    @FXML private Label stateLabel;
    @FXML private ComboBox<String> catalogueComboBox;
    @FXML private Button btnIniciar;
    private ProcessCatalogueService processCatalogueService;
    private ObservableList<Catalog> catalogs;
    public CatalogueSelectorController() {
        processCatalogueService = new ProcessCatalogueService();
    }
    @FXML
    private void startSimulation() {
        btnIniciar.setDisable(true);
    }

    private void initialize() {
        catalogs = processCatalogueService.getCatalogs();
        catalogueComboBox.setItems(catalogs.stream().map(Catalog::getName).collect(Collectors.toCollection(FXCollections::observableArrayList)));
        catalogueComboBox.setOnAction(event -> {
            String selectedCatalogue = catalogueComboBox.getSelectionModel().getSelectedItem();
            processesTable.setItems(catalogs.stream().filter(c -> selectedCatalogue.equalsIgnoreCase(c.getName()))
                    .findFirst()
                    .map(Catalog::getProcesses)
                    .orElse(FXCollections.observableArrayList()));
        });
    }

    private void setStartedState() {
        stateLabel.setText("Estado: Ejecutando");
    }
}
