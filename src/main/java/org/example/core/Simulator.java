package org.example.core;

import javafx.collections.ObservableList;
import org.example.model.Process;
import org.example.model.ProcessState;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Simulator {
    private final RoundRobinScheduler scheduler;
    private final ExecutorService virtualThreadExecutor;
    private final ObservableList<Process> processes;
    private final int th;
    private boolean isRunning;

    public Simulator(int quantumMs, ObservableList<Process> processes, int th) {
        this.scheduler = new RoundRobinScheduler(quantumMs);
        this.processes = processes;
        this.th = th;
        this.virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();
    }

    public void startSimulation() {
            if (isRunning) return;
            isRunning = true;

            // Inicializar procesos
            for (Process process : processes) {
                process.setState(ProcessState.READY);
                process.setPendingCharacters(process.getDescription().length());
                scheduler.addProcess(process);
            }

            // Usar virtual threads para el bucle principal

            while (isRunning && scheduler.hasPendingProcesses()) {
                Process currentProcess = scheduler.nextProcess();
                if (currentProcess == null) continue;

                executeProcess(currentProcess);
            }

        }


    private void executeProcess(Process process) {
        process.setState(ProcessState.RUNNING);
        int charsThisBurst = scheduler.decideCharsToExecute(process, th);
        process.prepareExecution(charsThisBurst, th, "output_" + process.getPid() + ".txt");

        // Crear un virtual thread para el proceso
        Thread processThread = Thread.startVirtualThread(() -> {
            process.run(); // Ejecuta la escritura del archivo (con sus pausas TH)

            // Gestionar estado post-ejecución
            if (process.isDone()) {
                process.setState(ProcessState.FINISHED);
            } else if (!process.isNonExpulsive()) {
                process.setState(ProcessState.READY);
                scheduler.reinsertProcess(process);
            }
        });

        try {
            processThread.join(); // Espera a que el virtual thread termine su ráfaga
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Process " + process.getPid() + " finished execution. Remaining characters: " + process.getPendingCharacters() + " number of executions: " + process.getNumberOfExecutions() + " is expulsive " + !process.isNonExpulsive());

    }

    public void pauseSimulation() {
        isRunning = false;
    }
}
