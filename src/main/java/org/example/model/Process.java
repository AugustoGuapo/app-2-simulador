package org.example.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.FileWriter;
import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Process implements Runnable {
    private int processCatalogueId;
    private int pid;
    private String processName;
    private String user;
    private String description;
    private boolean priority;
    private ObjectProperty<ProcessState> state = new SimpleObjectProperty<>();
    private int pendingCharacters;
    private int charactersForExecution;
    private int th;
    private String destinationFile;
    private int numberOfExecutions;

    public Process(int processCatalogueId, int pid, String processName, String user, String description, boolean priority) {
        this.processCatalogueId = processCatalogueId;
        this.pid = pid;
        this.processName = processName;
        this.user = user;
        this.description = description;
        this.priority = priority;
    }

    public void setState(ProcessState state) {
        this.state.set(state);
    }

    public ObjectProperty<ProcessState> stateProperty() {
        return state;
    }

    public void prepareExecution(int charactersForExecution, int th, String destinationFile) {
        this.charactersForExecution = charactersForExecution;
        this.th = th;
        this.destinationFile = destinationFile;
    }

    @Override
    public void run() {
        try (FileWriter writer = new FileWriter(destinationFile, true)) {
            int startIndex = description.length() - pendingCharacters;

            for (int i = 0; i < charactersForExecution && pendingCharacters > 0; i++) {
                char c = description.charAt(startIndex + i);
                writer.write(c);
                writer.flush();
                Thread.sleep(th);
                pendingCharacters--;
            }
            numberOfExecutions++;

        } catch (IOException | InterruptedException e) {
            System.err.println("Process PID " + pid + " failed: " + e.getMessage());
        }
    }


    public boolean isDone() {
        return pendingCharacters <= 0;
    }

    public boolean isNonExpulsive() {
        return priority; // true = no expulsivo
    }

    public String toString() {
        return "Process{" +
                "processCatalogueId=" + processCatalogueId +
                ", pid=" + pid +
                ", processName='" + processName + '\'' +
                ", user='" + user + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", state=" + state.get() +
                ", pendingCharacters=" + pendingCharacters +
                ", charactersForExecution=" + charactersForExecution +
                ", th=" + th +
                ", destinationFile='" + destinationFile + '\'' +
                '}';
    }
}
