package org.example.utils;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Stage;

public class StageUtils {

    private StageUtils(){}

    public static Stage getStageByEvent(Event event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}
