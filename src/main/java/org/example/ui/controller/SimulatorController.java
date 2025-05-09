package org.example.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.core.Simulator;
import org.example.model.Catalog;

public class SimulatorController {
    Catalog catalog;
    int quantum;
    int th;
    Simulator simulator;
    @FXML private TextField quantumField;
    @FXML private TextField thField;

    public SimulatorController(Catalog catalog) {
        this.catalog = catalog;
    }

    public SimulatorController() {

    }

    public void initData(Catalog catalog) {
        this.catalog = catalog;
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
