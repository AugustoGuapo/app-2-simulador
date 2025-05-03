package org.example.ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.model.Process;
import org.example.model.ProcessState;

public class SimulatorController {
    @FXML
    private TableView<Process> tablaProcesos;
    @FXML private TableColumn<Process, Integer> colPid;
    @FXML private TableColumn<Process, String> colNombre;
    @FXML private TableColumn<Process, String> colUsuario;
    @FXML private TableColumn<Process, Boolean> colPrioridad;
    @FXML private TableColumn<Process, String> colEstado;
    @FXML private Label stateLabel;
    @FXML private Button btnIniciar;
    ObservableList<Process> processes = FXCollections.observableArrayList();
    Process process = new Process(1, "Catalogo 1", 1, "Proceso 1", "Usuario 1", true, ProcessState.READY);

    @FXML
    private void startSimulation() {
        btnIniciar.setDisable(true);
        setStartedState();
        processes.add(process);
        process.setState(ProcessState.RUNNING);
        tablaProcesos.setItems(processes);
    }

    @FXML
    private void pauseSimulation() {
        stateLabel.setText("Estado: Pausado");
        process.setState(ProcessState.READY);
    }

    @FXML
    private void restartSimulation() {
        setStartedState();
        process.setState(ProcessState.RUNNING);
        // Lógica para reanudar
    }

    private void setStartedState() {
        stateLabel.setText("Estado: Ejecutando");
    }
}
