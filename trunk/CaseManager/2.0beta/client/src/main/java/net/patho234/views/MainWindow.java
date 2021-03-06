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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import net.patho234.entities.ClientServiceDefinition;
import net.patho234.entities.filter.ClientObjectSearchManager;
import net.patho234.entities.pool.CasePool;
import net.patho234.entities.pool.ClinicPool;
import net.patho234.entities.pool.MetadataPool;
import net.patho234.entities.pool.ServiceDefinitionPool;
import net.patho234.entities.pool.ServicePool;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.interfaces.client.IDtmsSearchListener;
import net.patho234.webapp_client.APPLICATION_DEFAULTS;
import net.patho234.webapp_client.FxmlManager;

/**
 *
 * @author rehkind
 */
public class MainWindow extends Stage{

    MainPaneController controller;
    HomeController homeController;
    TableViewerWindow tableviewer;

    private SimpleBooleanProperty preloadingFinished=new SimpleBooleanProperty(false);
    
    public MainWindow() {
        preloadingFinished.addListener( new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if( newValue ){
                    loadTableViewerWindow();
                }
            }
        } );
        
        try {
            startClientObjectPoolPreloading();
        } catch (IOException ioEx) {
            Logger.getLogger(RegistrationWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for status preloader window...exiting.", ioEx);
            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
        }
//        
//        boolean timeout = false;
//        int counter=0;
//        while ( (!(CasePool.createPool().isInitialized() &&
//                ClinicPool.createPool().isInitialized() &&
//                ServicePool.createPool().isInitialized() &&
//                ServiceDefinitionPool.createPool().isInitialized() &&
//                MetadataPool.createPool().isInitialized())) && !timeout
//                ) {
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            counter++;
//            if(counter % 25 == 0){
//                System.out.println("<ClientPools>.init() "+(counter / 40.)+" seconds (counter='"+counter+"')" );
//                System.out.println("<ClientPools>: "+(CasePool.createPool().isInitialized())+" | "+(ClinicPool.createPool().isInitialized())+" | "
//                +(ServicePool.createPool().isInitialized())+" | "+(ServiceDefinitionPool.createPool().isInitialized())+" | "
//                +(MetadataPool.createPool().isInitialized())+" | ");
//            }
//            timeout = counter >= 200;
//        }
//        if( timeout ){ Logger.getLogger(getClass().getName()).warning( "Waiting for ClientObjectPools resulted in a timeout." ); }
//        
//        if (ServiceDefinitionPool.createPool().getAllEntities().size()  == 0){
//            Logger.getLogger(getClass().getName()).warning("Client object pools were not initialized correctly...performing one retry.");
//            
//            try {
//                startClientObjectPoolPreloading();
//            } catch (IOException ioEx) {
//                Logger.getLogger(RegistrationWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for status preloader window...exiting.", ioEx);
//                FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
//            }
//        }
//        
//        if (ServiceDefinitionPool.createPool().getAllEntities().size()  == 0){
//            Logger.getLogger(getClass().getName()).severe("Client object pools were not initialized correctly second time...exiting");
//            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
//        }

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
                        Logger.getLogger(getClass().getName()).info("Creating DtmsSearch objects for TabelViewer...");
                        long startTime = System.currentTimeMillis();
                        ClientObjectSearchManager searchManager = ClientObjectSearchManager.create();
                        searchManager.createSearch("global_cases", (ClientObjectList) CasePool.createPool().getAllEntities());
                        String timeStamp = String.format("%.3f", (System.currentTimeMillis()-startTime)/1000.d)+" seconds";
                        Logger.getLogger(getClass().getName()).info("DtmsSearch.init() runtime Cases: "+timeStamp);
                        ClientServiceDefinition def2D = ServiceDefinitionPool.createPool().getEntity( APPLICATION_DEFAULTS.SERVICE_DEFINITION_ID_2D );
                        searchManager.createSearch("global_2D", (ClientObjectList) ServicePool.createPool().getAllEntitiesForDefinition(def2D));
                        timeStamp = String.format("%.3f", (System.currentTimeMillis()-startTime)/1000.d)+" seconds";
                        Logger.getLogger(getClass().getName()).info("DtmsSearch.init() runtime 2D: "+timeStamp);
                        ClientServiceDefinition def3D = ServiceDefinitionPool.createPool().getEntity( APPLICATION_DEFAULTS.SERVICE_DEFINITION_ID_3D );
                        searchManager.createSearch("global_3D", (ClientObjectList) ServicePool.createPool().getAllEntitiesForDefinition(def3D));
                        timeStamp = String.format("%.3f", (System.currentTimeMillis()-startTime)/1000.d)+" seconds";
                        Logger.getLogger(getClass().getName()).info("DtmsSearch.init() runtime 3D: "+timeStamp);
                        ClientServiceDefinition def4D = ServiceDefinitionPool.createPool().getEntity( APPLICATION_DEFAULTS.SERVICE_DEFINITION_ID_4D );
                        searchManager.createSearch("global_4D", (ClientObjectList) ServicePool.createPool().getAllEntitiesForDefinition(def4D));
                        timeStamp = String.format("%.3f", (System.currentTimeMillis()-startTime)/1000.d)+" seconds";
                        Logger.getLogger(getClass().getName()).info("DtmsSearch.init() runtime 4D: "+timeStamp);
                        //loadTableViewerWindow();
                        //timeStamp = String.format("%.3f", (System.currentTimeMillis()-startTime)/1000.d)+" seconds";
                        //Logger.getLogger(getClass().getName()).info("TableViewerWindow.load() runtime: "+timeStamp);
                    }
                }
            }
        );

        preloadThread.start();
        
//        while( preloadThread.isAlive() ){
//            try{
//                System.out.println("waiting for preloader thread to finish");
//                Thread.sleep(200);
//            }catch(InterruptedException ex){
//            
//            }
//        }
    }

    private void preloadClientObjectPools(StatusWindowController wndControl) throws IOException {
        // calls @GET_ALL for all entity_pools for intitial caching
        Platform.runLater(new MainWindow.StatusUpdate(wndControl, "Loading clinics...", 5));
        long startTime = System.currentTimeMillis();
        ClinicPool.createPool().getAllEntities(true);
        try {
            ClinicPool.createPool().waitFor(10000);
            Logger.getLogger(getClass().getName()).info("ClinicPool preloaded..."+String.format("%.3f seconds", ((System.currentTimeMillis()-startTime)/1000.d)));
        } catch (TimeoutException ex) {
            Logger.getLogger(getClass().getName()).severe(String.format("ERROR during start-up: %s", new Object[]{ex.getMessage()}));
            ex.printStackTrace();
        }
        
        boolean timeout=false; int counter=0;
        while ( !ClinicPool.createPool().isInitialized() && !timeout ){
            try { Thread.sleep(100); } catch (InterruptedException ex) { Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex); }
            counter++;
            timeout = counter > 100;
        }
        if( !ClinicPool.createPool().isInitialized() ){
            try {
                Logger.getLogger(getClass().getName()).info("ClinicPool preloading failed...trying a second time.");
                ClinicPool.createPool().waitFor(10000);
                Logger.getLogger(getClass().getName()).info("ClinicPool preloaded..."+String.format("%.3f seconds", ((System.currentTimeMillis()-startTime)/1000.d)));
            } catch (TimeoutException ex2) {
                Logger.getLogger(getClass().getName()).severe(String.format("ERROR during start-up: %s", new Object[]{ex2.getMessage()}));
                ex2.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Timeout during start-up");
                alert.setHeaderText("Connection to wildfly server could not be established.");
                alert.setContentText(ex2.getMessage());
                alert.showAndWait();

                System.exit(1);
            }
        }else{
            System.out.println("CLINIC_POOL all entities size: "+ClinicPool.createPool().getAllEntities(false).size());
        }
        Platform.runLater(new MainWindow.StatusUpdate(wndControl, "Loading service definitions...", 24));
        startTime = System.currentTimeMillis();
        ServiceDefinitionPool.createPool().getAllEntities(true);
        try {
            ServiceDefinitionPool.createPool().waitFor(10000);
            Logger.getLogger(getClass().getName()).info("ServiceDefinitionPool preloaded......"+String.format("%.3f seconds", ((System.currentTimeMillis()-startTime)/1000.d)));
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
        startTime = System.currentTimeMillis();
        ServicePool.createPool().getAllEntities(true);
        try {
            ServicePool.createPool().waitFor(10000);
            Logger.getLogger(getClass().getName()).info("ServicePool preloaded......"+String.format("%.3f seconds", ((System.currentTimeMillis()-startTime)/1000.d)));
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
        startTime = System.currentTimeMillis();
        CasePool.createPool().getAllEntities(true);
        try {
            Logger.getLogger(getClass().getName()).info("CasePool preloaded......"+String.format("%.3f seconds", ((System.currentTimeMillis()-startTime)/1000.d)));
            CasePool.createPool().waitFor(10000);
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
        startTime = System.currentTimeMillis();
        MetadataPool.createPool().getAllEntities(true);
        try {
            MetadataPool.createPool().waitFor(15000);
            Logger.getLogger(getClass().getName()).info("MetadataPool preloaded......"+String.format("%.3f seconds", ((System.currentTimeMillis()-startTime)/1000.d)));
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
        Platform.runLater(() -> { 
            System.out.println("NOW DONE PRELOADING");
            this.preloadingFinished.set(true);} );
        
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
        if( tableviewer != null ){
            tableviewer.close();
        }
        tableviewer = new TableViewerWindow();
        Logger.getLogger(getClass().getName()).info("Initializing TableViewer tables...");
        tableviewer.initializeTables();
        Logger.getLogger(getClass().getName()).info("Showing TableViewer GUI...");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Logger.getLogger(getClass().getName()).info("Showing TableViewer GUI...platform call");
                tableviewer.show();
            }
        });
        Logger.getLogger(getClass().getName()).info("Loading subpanes...");
        loadSubpanes();
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
        
        ClientObjectSearchManager.create().addResultListener(homecontroller);
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
        filtercontroller.setDisplay(this.tableviewer);
        fxmlLoader2 = new FXMLLoader(this.getClass().getResource("/fxml/elements/fx_export_pane.fxml"));
        node = null;
        try {
            node = fxmlLoader2.load();
        } catch (IOException ex) {
            Logger.getLogger(RegistrationWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for main window...exiting.", ex);
            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
        }
        ClientObjectSearchManager.create().addResultListener(filtercontroller);
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
