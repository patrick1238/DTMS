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
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.patho234.entities.UserLogin;

/**
 *
 * @author rehkind
 */
public class WebappClientFXML extends Application {
    public static void loadSettings() throws IOException{
        File f = new File(System.getProperty("user.home")+File.separator+".dtms"+File.separator, "dtms_client.settings");
        // DEFAULT SETTINGS >>>
        String[] settingNames=new String[]{
            "server.address",
            "client.login",
            "client.password"
        };
        Settings.set("server.address", "http://192.168.31.1:8585/webapp/resources/");
        Settings.set("client.login", "guest");
        Settings.set("client.password", "123456");
        Settings.set("dtms.string_filters", "");
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
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/fx_login_pane.fxml"));
        FxmlManager.applyDefaultStyle( root );
        
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("Digital tissue management suite");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(FxmlManager.EXIT_APPLICATION_HANDLER);
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
