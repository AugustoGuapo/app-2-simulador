package org.example.ui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.utils.StageUtils;
import org.example.model.Catalog;

public class SimulatorController {
    @FXML public Button backButton;
    private Catalog catalog;
    private Stage previousStage;

    public SimulatorController(Catalog catalog) {
        this.catalog = catalog;
    }

    public SimulatorController() {

    }

    public void initData(Catalog catalog, Stage previousStage) {
        this.catalog = catalog;
        this.previousStage = previousStage;
    }

    public void handleBack(Event event) {
        Stage currentStage = StageUtils.getStageByEvent(event);
        previousStage.show();
        currentStage.close();
    }
}
