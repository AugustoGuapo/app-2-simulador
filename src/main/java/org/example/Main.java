package org.example;

import javafx.collections.ObservableList;
import org.example.model.Catalog;
import org.example.service.ProcessCatalogService;

public class Main {
    public static void main(String[] args) {
        try (ProcessCatalogService processCatalogService = new ProcessCatalogService()){
            System.out.println("Process Catalogs:");
            ObservableList<Catalog> list = processCatalogService.getCatalogs();
            list.forEach(catalog -> System.out.println("ID: " + catalog.getId() + ", Name: " + catalog.getName()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
