package org.example.ui.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Catalog;
import org.example.model.Process;
import org.example.service.ProcessCatalogService;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class CatalogueSelectorController {
    @FXML private TableView<Process> processesTable;
    @FXML private ComboBox<Catalog> catalogsComboBox;
    @FXML private Button selectCatalogBtn;
    private ProcessCatalogService processCatalogService;
    private ObservableList<Catalog> catalogs;
    private Catalog selectedCatalog;
    public CatalogueSelectorController() {
        processCatalogService = new ProcessCatalogService();
        initialize();
    }
    @FXML
    private void startSimulation(Event event) {
        try {
            // Usa getResourceAsStream para mejor control de errores
            InputStream fxmlStream = getClass().getResourceAsStream("/view/simulator.fxml");
            if (fxmlStream == null) {
                throw new IOException("No se encontró el archivo FXML en /view/simulator.fxml");
            }

            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(fxmlStream);

            SimulatorController controller = loader.getController();
            if (controller == null) {
                throw new IllegalStateException("El controlador no fue inicializado");
            }

            controller.initData(selectedCatalog);
            selectCatalogBtn.setDisable(true);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            controller.setStage(stage);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            log.error("Error crítico al cargar FXML: ", e);
        } catch (IllegalStateException e) {
            log.error("Error en el controlador: ", e);
        }
    }

    @FXML
    private void initialize() {
        try {
            //Cargar datos
            catalogs = FXCollections.observableArrayList(
                    processCatalogService.getCatalogs()
            );

            // Configurar ComboBox
            Platform.runLater(() -> {
                catalogsComboBox.setItems(catalogs);
                catalogsComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        processesTable.setItems(newVal.getProcesses());
                        selectedCatalog = newVal;
                    }
                });

                if (!catalogs.isEmpty()) {
                    catalogsComboBox.getSelectionModel().selectFirst();
                }
            });

        } catch (Exception e) {
            System.err.println("Error crítico en inicialización: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
