/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.webapp_client;

import com.sun.scenario.Settings;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Popup;
import javafx.stage.Stage;
import net.patho234.controls.StatusWindowController;
import net.patho234.entities.UserLogin;
import net.patho234.entities.pool.CasePool;
import net.patho234.entities.pool.ClinicPool;
import net.patho234.entities.pool.ServiceDefinitionPool;
import net.patho234.entities.pool.ServicePool;

/**
 *
 * @author rehkind
 */
public class WebappClientFXML extends Application {
    public static void loadSettings() throws IOException{
        File f = new File(System.getProperty("user.home")+File.separator+".dtms"+File.separator, "case_manager_testclient.settings");
        // DEFAULT SETTINGS >>>
        String[] settingNames=new String[]{
            "server.address",
            "client.login",
            "client.password"
        };
        Settings.set("server.address", "http://192.168.31.1:8585/webapp/resources/");
        Settings.set("client.login", "guest");
        Settings.set("client.password", "123456");
        // DEFAULT SETTINGS <<<
        Logger.getLogger("global").info("Checking user settings file: "+f.getAbsolutePath());
        
        if( f.exists() ){
            Logger.getLogger("global").info("Loading user settings...");
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            try{
                String line=br.readLine();
                while ( line != null ){
                    if( line=="" ){
                        continue;
                    }
                    String[] splitStr=line.split(",");
                    String name=splitStr[0].trim();
                    String value=splitStr[1].trim();
                    Logger.getLogger("global").info("User setting loaded: "+name+"="+value);
                    Settings.set(name, value);
                    line=br.readLine();
                }
            }catch( IOException ioEx ){
                ioEx.printStackTrace();
            }finally{
                br.close();
                fr.close();
            }
            UserLogin.setLogin(Settings.get("client.login"), Settings.get("client.password"));
        }else{
            Logger.getLogger("global").info("No user settings file found at "+f.getAbsolutePath());
        }
        Logger.getGlobal().info("Application running with following settings:");
        String line;
        for( String name : settingNames ){
            line="   "+name+": "+Settings.get(name);
            Logger.getGlobal().info(line);
        }
    }
    
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        WebappClientFXML.loadSettings();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/fx_main_pane.fxml"));
        
        Scene scene = new Scene(root, 1200, 900);
        
        primaryStage.setTitle("TestClient for HTTPRequests - LINFO");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fx_status_window.fxml"));
        Parent rootStatus = loader.load();
        StatusWindowController statusControl = loader.getController();
        
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
        

    }
    
    private void preloadClientObjectPools(StatusWindowController wndControl) throws IOException{
        // calls @GET_ALL for all entity_pools for intitial caching
        Platform.runLater( new StatusUpdate(wndControl, "Loading clinics...", 5));
        ClinicPool.createPool().getAllEntities(true);
        try{
            ClinicPool.createPool().waitFor(30000);
        }
        catch(TimeoutException ex){
            Logger.getLogger(getClass().getName()).severe( String.format( "ERROR during start-up: %s", new Object[]{ex.getMessage() } ) );
            ex.printStackTrace();
            
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Timeout during start-up");
            alert.setHeaderText("Connection to wildfly server could not be established.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            
            System.exit(1);
        }
        //Logger.getGlobal().info( "loaded clinic: "+ClinicPool.createPool().getEntity(1).toString() );
        //Logger.getGlobal().info( "loaded clinic: "+ClinicPool.createPool().getEntity(2).toString() );
        
        Platform.runLater( new StatusUpdate(wndControl, "Loading service definitions...", 24));
        ServiceDefinitionPool.createPool().getAllEntities(true);
        try{
            ServiceDefinitionPool.createPool().waitFor(30000);
        }
        catch(TimeoutException ex){
            Logger.getLogger(getClass().getName()).severe( String.format( "ERROR during start-up: %s", new Object[]{ex.getMessage() } ) );
            ex.printStackTrace();
            
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Timeout during start-up");
            alert.setHeaderText("Connection to wildfly server could not be established.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();

            System.exit(1);
        }
        Platform.runLater( new StatusUpdate(wndControl, "Loading services...", 56));
        ServicePool.createPool().getAllEntities(true);
        try{
            ServicePool.createPool().waitFor(30000);
        }
        catch(TimeoutException ex){
            Logger.getLogger(getClass().getName()).severe( String.format( "ERROR during start-up: %s", new Object[]{ex.getMessage() } ) );
            ex.printStackTrace();
            
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Timeout during start-up");
            alert.setHeaderText("Connection to wildfly server could not be established.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();

            System.exit(1);
        }
        Platform.runLater( new StatusUpdate(wndControl, "Loading cases...", 69));
        CasePool.createPool().getAllEntities(true);
        try{
            CasePool.createPool().waitFor(30000);
        }
        catch(TimeoutException ex){
            Logger.getLogger(getClass().getName()).severe( String.format( "ERROR during start-up: %s", new Object[]{ex.getMessage() } ) );
            ex.printStackTrace();
            
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Timeout during start-up");
            alert.setHeaderText("Connection to wildfly server could not be established.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();

            System.exit(1);
        }
        
        Platform.runLater( new StatusUpdate(wndControl, "Launching...", 100));
        
        Platform.runLater( new StatusUpdate(wndControl, "Terminate status", -1));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
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
