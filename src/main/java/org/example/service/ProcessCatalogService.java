package org.example.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Catalog;
import org.example.model.Process;
import org.example.model.ProcessState;


import java.sql.*;

@Slf4j
public class ProcessCatalogService implements AutoCloseable {
    private Connection conn = null;

    public ProcessCatalogService() {
        connect();
    }

    public ObservableList<Catalog> getCatalogs() {
        ObservableList<Catalog> catalogs = FXCollections.observableArrayList();

        String query = "SELECT * FROM catalogs";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                ObservableList<Process> processes = getProcessesByCatalogId(id);
                catalogs.add(new Catalog(id, name, processes));
            }
        } catch (SQLException e) {
            log.error("Error fetching catalogs", e);
        }
        return catalogs;
    }

    public ObservableList<Process> getProcessesByCatalogId(int catalogId) {
        ObservableList<Process> processes = FXCollections.observableArrayList();

        String query = "SELECT * FROM processes WHERE catalog_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, catalogId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    processes.add(createProcessFromResultSet(rs, catalogId));
                }
            }
        } catch (SQLException e) {
            log.error("Error fetching processes for catalog ID: {}", catalogId, e);
        }
        return processes;
    }

    private Process createProcessFromResultSet(ResultSet rs, int catalogId) throws SQLException {
        return new Process(
                catalogId,
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("user"),
                rs.getString("description"),
                rs.getBoolean("priority"),
                ProcessState.READY
        );
    }

    public void connect() {
        if (conn != null) return;

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:process_catalogue.db");
            initializeDatabase();
        } catch (SQLException e) {
            log.error("Database connection error", e);
            closeResources();
        }
    }

    private void initializeDatabase() throws SQLException {
        String[] ddl = {
                """
            CREATE TABLE IF NOT EXISTS catalogs (
                id INTEGER PRIMARY KEY, 
                name TEXT
            )""",
                """
            CREATE TABLE IF NOT EXISTS processes (
                id INTEGER PRIMARY KEY,
                catalog_id INTEGER,
                name TEXT,
                pid INTEGER,
                user TEXT,
                description TEXT,
                priority BOOLEAN,
                FOREIGN KEY(catalog_id) REFERENCES catalogs(id)
            )"""
        };

        try (Statement stmt = conn.createStatement()) {
            for (String sql : ddl) {
                stmt.executeUpdate(sql);
            }
        }
    }

    @Override
    public void close() {
        closeResources();
    }

    private void closeResources() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            log.error("Error closing database connection", e);
        } finally {
            conn = null;
        }
    }
}
