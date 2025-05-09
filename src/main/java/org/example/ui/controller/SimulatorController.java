package org.example.ui.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.core.Simulator;
import org.example.model.Catalog;
import org.example.model.Process;
import org.example.model.ProcessState;
import org.example.ui.components.ProcessView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SimulatorController {
    public HBox listosContainer;
    public HBox ejecucionContainer;
    public HBox terminadoContainer;
    Catalog catalog;
    int quantum;
    int th;
    Simulator simulator;
    @FXML private TextField quantumField;
    @FXML private TextField thField;
    private Map<Process, ProcessView> processLabels = new HashMap<>();
    private Stage stage;  // Añade esta variable


    public void setStage(Stage stage) {
        this.stage = stage;
        setupCloseHandler();
    }

    private void setupCloseHandler() {
        stage.setOnCloseRequest(event -> {
            onWindowClosing();
        });
    }

    private void onWindowClosing() {
        // Aquí tu lógica de limpieza
        System.out.println("Cerrando simulador...");

        if (simulator != null) {
            simulator.shutdown();  // Asegúrate de detener la simulación
        }

        // Puedes añadir más lógica de limpieza aquí
    }

    public SimulatorController(Catalog catalog) {
        this.catalog = catalog;
    }

    public SimulatorController() {

    }

    public void initData(Catalog catalog) throws IOException {
        this.catalog = catalog;
        loadProcesses(this.catalog.getProcesses());
    }
    public void loadProcesses(ObservableList<Process> catalogProcesses) throws IOException {
        ObservableList<Process> processes = catalogProcesses;
        for (Process p : processes) {
            ProcessView label = new ProcessView(p);
            processLabels.put(p, label);
            listosContainer.getChildren().add(label); // initial state is READY

            // Observe the process state
            p.stateProperty().addListener((obs, oldState, newState) -> {
                Platform.runLater(() -> {
                    moveToContainer(label, newState);
                });
            });
        }
    }

    private void moveToContainer(ProcessView view, ProcessState newState) {
        listosContainer.getChildren().remove(view);
        ejecucionContainer.getChildren().remove(view);
        terminadoContainer.getChildren().remove(view);

        switch (newState) {
            case READY -> listosContainer.getChildren().add(view);
            case RUNNING -> ejecucionContainer.getChildren().add(view);
            case FINISHED -> terminadoContainer.getChildren().add(view);
        }
    }


    public void startSimulation() {
        if (quantumField.getText().isEmpty() || thField.getText().isEmpty()) {
            System.out.println("Please fill in all fields.");
            return;
        }
        try {
            quantum = Integer.parseInt(quantumField.getText());
            th = Integer.parseInt(thField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numbers.");
            return;
        }
        System.out.println("Starting simulation with quantum: " + quantum + " and th: " + th);
        simulator = new Simulator(quantum, catalog.getProcesses(), th);
        simulator.startSimulation();
    }

    public void pauseSimulation() {
        simulator.pauseSimulation();
    }
}
