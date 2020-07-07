/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.views;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import net.patho234.controls.MainController;
import net.patho234.controls.StatusWindowController;
import net.patho234.entities.pool.CasePool;
import net.patho234.entities.pool.ClinicPool;
import net.patho234.entities.pool.ServiceDefinitionPool;
import net.patho234.entities.pool.ServicePool;
import net.patho234.webapp_client.APPLICATION_DEFAULTS;
import net.patho234.webapp_client.FxmlManager;
import net.patho234.webapp_client.WebappClientFXML;

/**
 *
 * @author rehkind
 */
public class MainWindow extends Stage {
    MainController controller;
    public MainWindow() throws IOException {
        FXMLLoader statusLoader = new FXMLLoader(getClass().getResource("/fxml/fx_status_window.fxml"));
        Parent rootStatus = statusLoader.load();
        StatusWindowController statusControl = statusLoader.getController();
        
        Thread preloadThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try{
                            preloadClientObjectPools(statusControl);
                        }catch(IOException ioEx){}
                    }
                }
        );
        

        Stage rootStage = new Stage();
        Scene statusScene = new Scene(rootStatus);
        rootStage.setScene(statusScene);
        rootStage.setAlwaysOnTop(true);
        rootStage.show();
        
        preloadThread.start();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fx_main_pane.fxml"));
        Parent root=null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for main pane.", ex);
            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
        }
        
        controller = loader.getController();
        
        FxmlManager.applyDefaultStyle( root );
        Scene scene = new Scene(root);
        setScene(scene);
        setTitle(APPLICATION_DEFAULTS.APPLICATION_NAME + " - <todo: proper title>");
        setWidth(APPLICATION_DEFAULTS.MAIN_WINDOW_WIDTH);
        setHeight(APPLICATION_DEFAULTS.MAIN_WINDOW_HEIGHT);
        setScene(scene);
    }
    
    private void preloadClientObjectPools(StatusWindowController wndControl) throws IOException{
        // calls @GET_ALL for all entity_pools for intitial caching
        Platform.runLater( new MainWindow.StatusUpdate(wndControl, "Loading clinics...", 5));
        ClinicPool.createPool().getAllEntities(true);
        try{
            ClinicPool.createPool().waitFor(30000);
        }
        catch(TimeoutException ex){
            Logger.getLogger(getClass().getName()).severe( String.format( "ERROR during start-up: %s", new Object[]{ex.getMessage() } ) );
            ex.printStackTrace();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Timeout during start-up");
            alert.setHeaderText("Connection to wildfly server could not be established.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            
            System.exit(1);
        }
        //Logger.getGlobal().info( "loaded clinic: "+ClinicPool.createPool().getEntity(1).toString() );
        //Logger.getGlobal().info( "loaded clinic: "+ClinicPool.createPool().getEntity(2).toString() );
        
        Platform.runLater( new MainWindow.StatusUpdate(wndControl, "Loading service definitions...", 24));
        ServiceDefinitionPool.createPool().getAllEntities(true);
        try{
            ServiceDefinitionPool.createPool().waitFor(30000);
        }
        catch(TimeoutException ex){
            Logger.getLogger(getClass().getName()).severe( String.format( "ERROR during start-up: %s", new Object[]{ex.getMessage() } ) );
            ex.printStackTrace();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Timeout during start-up");
            alert.setHeaderText("Connection to wildfly server could not be established.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();

            System.exit(1);
        }
        Platform.runLater( new MainWindow.StatusUpdate(wndControl, "Loading services...", 56));
        ServicePool.createPool().getAllEntities(true);
        try{
            ServicePool.createPool().waitFor(30000);
        }
        catch(TimeoutException ex){
            Logger.getLogger(getClass().getName()).severe( String.format( "ERROR during start-up: %s", new Object[]{ex.getMessage() } ) );
            ex.printStackTrace();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Timeout during start-up");
            alert.setHeaderText("Connection to wildfly server could not be established.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();

            System.exit(1);
        }
        Platform.runLater( new MainWindow.StatusUpdate(wndControl, "Loading cases...", 69));
        CasePool.createPool().getAllEntities(true);
        try{
            CasePool.createPool().waitFor(30000);
        }
        catch(TimeoutException ex){
            Logger.getLogger(getClass().getName()).severe( String.format( "ERROR during start-up: %s", new Object[]{ex.getMessage() } ) );
            ex.printStackTrace();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Timeout during start-up");
            alert.setHeaderText("Connection to wildfly server could not be established.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();

            System.exit(1);
        }
        
        Platform.runLater( new MainWindow.StatusUpdate(wndControl, "Launching...", 100));
        
        Platform.runLater( new MainWindow.StatusUpdate(wndControl, "Terminate status", -1));
    }
        
        private class StatusUpdate implements Runnable{
        String job;
        Integer status;
        StatusWindowController control;
        private StatusUpdate(StatusWindowController wndControl,String job, Integer status){
            control = wndControl;
            this.job=job;
            this.status=status;
        }
        @Override
        public void run() {
            if(status>=0){
                control.setStatus(job, status);
            }else{
                control.terminate();
            }
        }
    
    }
}
