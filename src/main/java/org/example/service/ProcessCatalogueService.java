package org.example.service;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import org.example.model.Catalog;
import org.example.model.Process;
import org.example.model.ProcessState;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProcessCatalogueService {
    Connection conn = null;
    ResultSet rs = null;
    Statement stmt = null;
    Logger logger = Logger.getLogger(ProcessCatalogueService.class.getName());

    public ProcessCatalogueService() {
        connect();
    }

    public ObservableList<Catalog> getCatalogs() {
        ObservableList<Catalog> catalogs = new ObservableListWrapper(new ArrayList());
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM catalogs");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                ObservableList<Process> processes = getProcessesByCatalogId(id, name);
                catalogs.add(new Catalog(id, name, processes));
            }
        } catch (Exception e) {
            logger.log(Level.WARNING,("Error fetching catalogs: " + e.getMessage()));
        }
        return catalogs;
    }

    public ObservableList<Process> getProcessesByCatalogId(int catalogId, String catalogName) {
        ObservableList<Process> processes = new ObservableListWrapper(new ArrayList());
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM processes WHERE catalog_id = " + catalogId);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String user = rs.getString("user");
                boolean priority = rs.getBoolean("priority");
                String description = rs.getString("description");
                processes.add(new Process(catalogId,  id, name, user, description, priority, ProcessState.READY));
            }
        } catch (Exception e) {
            logger.log(Level.WARNING,("Error fetching processes for catalogue " + catalogName + ": " + e.getMessage()));
        }
        return processes;
    }

    public void connect() {
        // Implement connection logic
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:process_catalogue.db");
            stmt = conn.createStatement();
            stmt.setQueryTimeout(30); // set timeout to 30 sec.
            stmt.executeQuery("CREATE TABLE IF NOT EXISTS catalogs (id INTEGER PRIMARY KEY, name TEXT);");
            stmt = conn.createStatement();
            stmt.setQueryTimeout(30);
            stmt.executeQuery("CREATE TABLE IF NOT EXISTS processes ( id INTEGER PRIMARY KEY, catalog_id INTEGER, name TEXT, pid INTEGER, user TEXT, description TEXT, priority BOOLEAN, FOREIGN KEY(catalog_id) REFERENCES catalogs(id) )");
        } catch(ClassNotFoundException e) {
            logger.log(Level.SEVERE, "SQLite JDBC driver not found: " + e.getMessage());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Connection error: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error: " + e.getMessage());
        }
    }
}
