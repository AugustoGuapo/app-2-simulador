package org.example.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Process {
    private int processCatalogueId;
    private int pid;
    private String processName;
    private String user;
    private String description;
    private boolean priority;
    private ObjectProperty<ProcessState> state = new SimpleObjectProperty<>();;

    public Process(int processCatalogueId, int pid, String processName, String user, String description, boolean priority, ProcessState state) {
        this.processCatalogueId = processCatalogueId;
        this.pid = pid;
        this.processName = processName;
        this.user = user;
        this.description = description;
        this.priority = priority;
        this.state.set(state);
    }

    public void setState(ProcessState state) {
        this.state.set(state);
    }

    public ObjectProperty<ProcessState> stateProperty() {
        return state;
    }

}
