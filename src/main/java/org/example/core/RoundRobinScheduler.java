package org.example.core;

import org.example.model.Process;

import java.util.LinkedList;
import java.util.Queue;

public class RoundRobinScheduler {
    private final Queue<Process> readyQueue = new LinkedList<>();
    private final int quantumMs;

    public RoundRobinScheduler(int quantumMs) {
        this.quantumMs = quantumMs;
    }

    public void addProcess(Process process) {
        if (!process.isDone()) {
            readyQueue.offer(process);
        }
    }

    public Process nextProcess() {
        return readyQueue.poll();
    }

    public void reinsertProcess(Process process) {
        if (!process.isDone()) {
            readyQueue.offer(process);
        }
    }

    public boolean hasPendingProcesses() {
        return !readyQueue.isEmpty();
    }

    public int decideCharsToExecute(Process process, int charTimeMs) {
        if (process.isNonExpulsive()) {
            return process.getPendingCharacters();
        }

        return Math.min(quantumMs / charTimeMs, process.getPendingCharacters());
    }
}
