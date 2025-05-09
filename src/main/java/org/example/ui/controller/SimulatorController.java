package org.example.ui.controller;

import lombok.Data;
import org.example.model.Catalog;

public class SimulatorController {
    Catalog catalog;

    public SimulatorController(Catalog catalog) {
        this.catalog = catalog;
    }

    public SimulatorController() {

    }

    public void initData(Catalog catalog) {
        this.catalog = catalog;
    }
}
