package org.example.model;

public enum ProcessState {
    READY("Listo"),
    RUNNING("Ejecutando"),
    FINISHED("Terminado");

    private String state;

    ProcessState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return state;
    }
}
