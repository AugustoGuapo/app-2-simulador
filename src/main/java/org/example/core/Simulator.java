package org.example.core;

import javafx.collections.ObservableList;
import org.example.model.Process;
import org.example.model.ProcessState;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Simulator {
    private final RoundRobinScheduler scheduler;
    private final ExecutorService virtualThreadExecutor;
    private final ObservableList<Process> processes;
    private final int th;
    private boolean isRunning;
    private boolean alreadyStarted;
    private Future<?> simulationTask;
    private ExecutorService simulationExecutor;

    public Simulator(int quantumMs, ObservableList<Process> processes, int th) {
        this.scheduler = new RoundRobinScheduler(quantumMs);
        this.processes = processes;
        this.th = th;
        this.virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();
        this.simulationExecutor = Executors.newSingleThreadExecutor();
    }

    public void startSimulation() {
            if (isRunning) return;
            isRunning = true;

        System.out.println(alreadyStarted);
        if (!alreadyStarted) {
            for (Process process : processes) {
                process.setState(ProcessState.READY);
                process.setPendingCharacters(process.getDescription().length());
                scheduler.addProcess(process);
            }
        }

        simulationTask = simulationExecutor.submit(() -> {
            while (isRunning && scheduler.hasPendingProcesses()) {
                Process currentProcess = scheduler.nextProcess();
                if (currentProcess == null) continue;
                executeProcess(currentProcess);
            }
        });
        alreadyStarted = true;
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
        if (simulationTask != null) {
            simulationTask.cancel(false);
        }
        System.out.println("Simulation paused.");
        System.out.println("Scheduler state: " + scheduler.toString());
    }

    public void shutdown() {
        pauseSimulation();
        simulationExecutor.shutdownNow();
        virtualThreadExecutor.shutdownNow();
    }
}
