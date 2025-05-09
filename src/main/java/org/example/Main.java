package org.example;

import javafx.collections.ObservableList;
import org.example.core.Simulator;
import org.example.model.Catalog;
import org.example.service.ProcessCatalogService;

public class Main {
    public static void main(String[] args) {
        ObservableList<Catalog> catalogs = null;
        try (ProcessCatalogService processCatalogService = new ProcessCatalogService()){
            System.out.println("Process Catalogs:");
            catalogs = processCatalogService.getCatalogs();
            //catalogs.forEach(catalog -> System.out.println("ID: " + catalog.getId() + ", Name: " + catalog.getName()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Simulator simulator = new Simulator(1000, catalogs.get(2).getProcesses(), 100);
        simulator.startSimulation();
    }
}
