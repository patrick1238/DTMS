/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.views;

import com.sun.security.ntlm.Client;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.patho234.controls.MainPaneController;
import net.patho234.controls.StatusWindowController;
import net.patho234.entities.filter.ClientObjectSearchManager;
import net.patho234.entities.filter.DtmsSearch;
import net.patho234.entities.pool.CasePool;
import net.patho234.entities.pool.ClinicPool;
import net.patho234.entities.pool.ServiceDefinitionPool;
import net.patho234.entities.pool.ServicePool;
import net.patho234.interfaces.IDataDisplay;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.webapp_client.APPLICATION_DEFAULTS;
import net.patho234.webapp_client.FxmlManager;

/**
 *
 * @author rehkind
 */
public class MainWindow extends Stage{
    
    MainPaneController controller;
    FXMLLoader fxmlLoader;

    public MainWindow(){
        try{
            startClientObjectPoolPreloading();
        }catch(IOException ioEx){
            Logger.getLogger(RegistrationWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for status preloader window...exiting.", ioEx);
            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
        }

        System.out.println(this.getClass().getResource("/fxml/fx_main_pane.fxml"));
        fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/fx_main_pane.fxml"));
        Parent root=null;
        try {
            root = fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(RegistrationWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for main window...exiting.", ex);
            ex.printStackTrace();
            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
        }
        controller = fxmlLoader.getController();
        controller.setEnabled(false);

        Scene scene = new Scene(root);
        setTitle(APPLICATION_DEFAULTS.APPLICATION_NAME+" @"+APPLICATION_DEFAULTS.VERSION_NUMBER);
        setScene(scene);
        
        this.setOnCloseRequest(FxmlManager.EXIT_APPLICATION_HANDLER);
    }
        
    private void startClientObjectPoolPreloading() throws IOException{
        
        FXMLLoader statusLoader = new FXMLLoader(getClass().getResource("/fxml/fx_status_window.fxml"));
        Parent rootStatus = statusLoader.load();
        StatusWindowController statusControl = statusLoader.getController();
        
        Stage rootStage = new Stage();
        Scene statusScene = new Scene(rootStatus);
        rootStage.setScene(statusScene);
        rootStage.setAlwaysOnTop(true);
        rootStage.show();
        
        Thread preloadThread = new Thread(
        new Runnable() {
                    @Override
                    public void run() {
                        try{
                            preloadClientObjectPools(statusControl);
                        }catch(IOException ioEx){}
                        finally{
                            controller.setEnabled(true);
                            ClientObjectSearchManager searchManager = ClientObjectSearchManager.create();
                            searchManager.createSearch("global", (ClientObjectList)CasePool.createPool().getAllEntities());
                        }
                    }
                }
        );
        
        preloadThread.start();
    }
    
    private void preloadClientObjectPools(StatusWindowController wndControl) throws IOException{
        // calls @GET_ALL for all entity_pools for intitial caching
        Platform.runLater(new MainWindow.StatusUpdate(wndControl, "Loading clinics...", 5));
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
        
        Platform.runLater(new MainWindow.StatusUpdate(wndControl, "Loading service definitions...", 24));
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
        Platform.runLater(new MainWindow.StatusUpdate(wndControl, "Loading services...", 56));
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
        Platform.runLater(new MainWindow.StatusUpdate(wndControl, "Loading cases...", 69));
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
        
        Platform.runLater(new MainWindow.StatusUpdate(wndControl, "Launching...", 100));
        
        Platform.runLater(new MainWindow.StatusUpdate(wndControl, "Terminate status", -1));
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
        
    public boolean loadSubpanes(IDataDisplay display){
        HomePane pane = new HomePane(display);
        //Button pane = new Button();
        AnchorPane anchor = new AnchorPane(pane);
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
        controller.getDisplayStack().getChildren().add(anchor);
        return true;
    }
}
