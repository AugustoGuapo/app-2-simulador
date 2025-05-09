package org.example.ui.components;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.model.Process;
import org.example.model.ProcessState;

import java.io.IOException;

public class ProcessView extends VBox {
    @FXML private Label nameLabel;
    @FXML private Label pidLabel;
    @FXML private Label stateLabel;
    @FXML private Label priorityLabel;
    @FXML private Label charactersLeftLabel;

    private Process process;

    public ProcessView(Process process) {
        this.process = process;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/components/processview.fxml"));
        fxmlLoader.setController(this);
        try {
            VBox root = fxmlLoader.load();
            this.getChildren().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        process.setState(ProcessState.READY);

        Platform.runLater(() -> {
            nameLabel.setText("Nombre: " + process.getProcessName());
            pidLabel.setText("PID: " + process.getPid());
            stateLabel.setText("Estado: " + process.stateProperty().get().getState());
            priorityLabel.setText(process.isNonExpulsive() ? "No expulsivo" : "Expulsivo");
            charactersLeftLabel.setText("Caracteres restantes: " + process.getPendingCharacters());
        });

        process.stateProperty().addListener((obs, old, newState) -> {
            Platform.runLater(() -> {
                stateLabel.setText("Estado: " + newState.getState());
            });
        });
    }

    public Process getProcess() {
        return process;
    }
}