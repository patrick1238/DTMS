/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.views;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.patho234.controls.MainPaneController;
import net.patho234.controls.StatusWindowController;
import net.patho234.controls.elements.ExportController;
import net.patho234.controls.elements.FilterController;
import net.patho234.controls.elements.HomeController;
import net.patho234.entities.filter.ClientObjectSearchManager;
import net.patho234.entities.pool.CasePool;
import net.patho234.entities.pool.ClinicPool;
import net.patho234.entities.pool.MetadataPool;
import net.patho234.entities.pool.ServiceDefinitionPool;
import net.patho234.entities.pool.ServicePool;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.webapp_client.APPLICATION_DEFAULTS;
import net.patho234.webapp_client.FxmlManager;

/**
 *
 * @author rehkind
 */
public class MainWindow extends Stage {

    MainPaneController controller;
    HomeController homeController;
    TableViewerWindow tableviewer;

    public MainWindow() {

        try {
            startClientObjectPoolPreloading();
        } catch (IOException ioEx) {
            Logger.getLogger(RegistrationWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for status preloader window...exiting.", ioEx);
            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/fx_main_pane.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(RegistrationWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for main window...exiting.", ex);
            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
        }
        controller = fxmlLoader.getController();

        Scene scene = new Scene(root);
        setTitle(APPLICATION_DEFAULTS.APPLICATION_NAME + " @" + APPLICATION_DEFAULTS.VERSION_NUMBER);
        setScene(scene);

        this.setOnCloseRequest(FxmlManager.EXIT_APPLICATION_HANDLER);
    }

    private void startClientObjectPoolPreloading() throws IOException {

        FXMLLoader statusLoader = new FXMLLoader(getClass().getResource("/fxml/fx_status_window.fxml"));
        Parent rootStatus = statusLoader.load();
        StatusWindowController statusControl = statusLoader.getController();

        Stage rootStage = new Stage();
        rootStage.initStyle(StageStyle.UNDECORATED);
        Scene statusScene = new Scene(rootStatus);
        rootStage.setScene(statusScene);
        rootStage.setAlwaysOnTop(true);
        rootStage.show();

        Thread preloadThread = new Thread(
                new Runnable() {
            @Override
            public void run() {
                try {
                    preloadClientObjectPools(statusControl);
                } catch (IOException ioEx) {
                } finally {
                    ClientObjectSearchManager searchManager = ClientObjectSearchManager.create();
                    searchManager.createSearch("global_cases", (ClientObjectList) CasePool.createPool().getAllEntities());
                    searchManager.createSearch("global_2D", (ClientObjectList) ServicePool.createPool().getAllEntities());
                    
                    loadTableViewerWindow();
                }
            }
        }
        );

        preloadThread.start();
    }

    private void preloadClientObjectPools(StatusWindowController wndControl) throws IOException {
        // calls @GET_ALL for all entity_pools for intitial caching
        Platform.runLater(new MainWindow.StatusUpdate(wndControl, "Loading clinics...", 5));
        ClinicPool.createPool().getAllEntities(true);
        try {
            ClinicPool.createPool().waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(getClass().getName()).severe(String.format("ERROR during start-up: %s", new Object[]{ex.getMessage()}));
            ex.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Timeout during start-up");
            alert.setHeaderText("Connection to wildfly server could not be established.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();

            System.exit(1);
        }

        Platform.runLater(new MainWindow.StatusUpdate(wndControl, "Loading service definitions...", 24));
        ServiceDefinitionPool.createPool().getAllEntities(true);
        try {
            ServiceDefinitionPool.createPool().waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(getClass().getName()).severe(String.format("ERROR during start-up: %s", new Object[]{ex.getMessage()}));
            ex.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Timeout during start-up");
            alert.setHeaderText("Connection to wildfly server could not be established.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();

            System.exit(1);
        }
        Platform.runLater(new MainWindow.StatusUpdate(wndControl, "Loading services...", 51));
        ServicePool.createPool().getAllEntities(true);
        try {
            ServicePool.createPool().waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(getClass().getName()).severe(String.format("ERROR during start-up: %s", new Object[]{ex.getMessage()}));
            ex.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Timeout during start-up");
            alert.setHeaderText("Connection to wildfly server could not be established.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();

            System.exit(1);
        }
        Platform.runLater(new MainWindow.StatusUpdate(wndControl, "Loading cases...", 61));
        CasePool.createPool().getAllEntities(true);
        try {
            CasePool.createPool().waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(getClass().getName()).severe(String.format("ERROR during start-up: %s", new Object[]{ex.getMessage()}));
            ex.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Timeout during start-up");
            alert.setHeaderText("Connection to wildfly server could not be established.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();

            System.exit(1);
        }
        Platform.runLater(new MainWindow.StatusUpdate(wndControl, "Loading metadata...", 90));
        MetadataPool.createPool().getAllEntities(true);
        try {
            MetadataPool.createPool().waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(getClass().getName()).severe(String.format("ERROR during start-up: %s", new Object[]{ex.getMessage()}));
            ex.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Timeout during start-up");
            alert.setHeaderText("Connection to wildfly server could not be established.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();

            System.exit(1);
        }
        Platform.runLater(new MainWindow.StatusUpdate(wndControl, "Launching...", 100));

        Platform.runLater(new MainWindow.StatusUpdate(wndControl, "Terminate status", -1));
    }

    private class StatusUpdate implements Runnable {

        String job;
        Integer status;
        StatusWindowController control;

        private StatusUpdate(StatusWindowController wndControl, String job, Integer status) {
            control = wndControl;
            this.job = job;
            this.status = status;
        }

        @Override
        public void run() {
            if (status >= 0) {
                control.setStatus(job, status);
            } else {
                control.terminate();
            }
        }
    }

    public boolean loadTableViewerWindow() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tableviewer = new TableViewerWindow();
                tableviewer.initializeTables();
                tableviewer.show();
                loadSubpanes();
            }
        });
        return true;
    }

    public boolean loadSubpanes() {
        this.show();
        HashMap<Integer,AnchorPane> mainViewHandler = new HashMap<>();
        FXMLLoader fxmlLoader2;
        fxmlLoader2 = new FXMLLoader(this.getClass().getResource("/fxml/elements/fx_home_pane.fxml"));
        Node node = null;
        try {
            node = fxmlLoader2.load();
        } catch (IOException ex) {
            Logger.getLogger(RegistrationWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for main window...exiting.", ex);
            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
        }
        mainViewHandler.put(0,anchor(node));
        HomeController homecontroller = fxmlLoader2.getController();
        homecontroller.setDisplay(this.tableviewer);
        fxmlLoader2 = new FXMLLoader(this.getClass().getResource("/fxml/elements/fx_filter_pane.fxml"));
        node = null;
        try {
            node = fxmlLoader2.load();
        } catch (IOException ex) {
            Logger.getLogger(RegistrationWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for main window...exiting.", ex);
            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
        }
        mainViewHandler.put(1,anchor(node));
        FilterController filtercontroller = fxmlLoader2.getController();
        fxmlLoader2 = new FXMLLoader(this.getClass().getResource("/fxml/elements/fx_export_pane.fxml"));
        node = null;
        try {
            node = fxmlLoader2.load();
        } catch (IOException ex) {
            Logger.getLogger(RegistrationWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for main window...exiting.", ex);
            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
        }
        mainViewHandler.put(2,anchor(node));
        ExportController exportcontroller = fxmlLoader2.getController();
        controller.setPossibleDisplays(mainViewHandler);
        return true;
    }

    private AnchorPane anchor(Node node) {
        AnchorPane anchor = new AnchorPane(node);
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        anchor.setVisible(false);
        return anchor;
    }
}
