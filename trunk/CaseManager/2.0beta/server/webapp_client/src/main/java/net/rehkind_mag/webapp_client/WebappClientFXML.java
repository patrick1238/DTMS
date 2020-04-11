/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.webapp_client;

import com.sun.scenario.Settings;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import net.rehkind_mag.entities.pool.CasePool;
import net.rehkind_mag.entities.pool.ClinicPool;

/**
 *
 * @author rehkind
 */
public class WebappClientFXML extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
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
            
        }
        
        Logger.getGlobal().info("Application running with following settings:");
        String line;
        for( String name : settingNames ){
            line="   "+name+": "+Settings.get(name);
            Logger.getGlobal().info(line);
        }
        
        preloadClientObjectPools();
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/fx_main_pane.fxml"));
        
        Scene scene = new Scene(root, 1200, 900);
        
        primaryStage.setTitle("TestClient for HTTPRequests - LINFO");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void preloadClientObjectPools(){
        // calls @GET_ALL for all entity_pools for intitial caching
        ClinicPool.createPool().getAllEntities();
        try{
            ClinicPool.createPool().waitFor(6000);
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
        
        CasePool.createPool().getAllEntities();
        try{
            CasePool.createPool().waitFor(6000);
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
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
