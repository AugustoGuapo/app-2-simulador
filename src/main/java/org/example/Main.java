package org.example;

import javafx.collections.ObservableList;
import org.example.model.Catalog;
import org.example.service.ProcessCatalogueService;

public class Main {
    public static void main(String[] args) {
        ProcessCatalogueService processCatalogueService = new ProcessCatalogueService();
        System.out.println("Process Catalogues:");
        ObservableList<Catalog> list = processCatalogueService.getCatalogs();
        list.forEach(catalog -> System.out.println("ID: " + catalog.getId() + ", Name: " + catalog.getName()));
    }
}
