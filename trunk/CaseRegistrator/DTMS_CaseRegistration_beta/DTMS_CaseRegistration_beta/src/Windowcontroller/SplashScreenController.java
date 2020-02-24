/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windowcontroller;

import BackgroundHandler.Config;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class SplashScreenController implements Initializable {
    
     @FXML
     ImageView imageview;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new ShowSplashScreen().start();
    }  
    
    class ShowSplashScreen extends Thread{
        @Override
        public void run(){
            try {
                Thread.sleep(500);                
                Platform.runLater(() -> {
                    Stage stage = new Stage();
                    Parent root = null;
                    Config config = Config.getConfig();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/StartScreen.fxml"));
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException ex) {
                        Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    final StartScreenController controller = fxmlLoader.getController();
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add("/styles/Styles.css");
                    stage.setTitle("DTMS_CaseRegistration_beta");
                    stage.setScene(scene);
                    stage.show();
                    imageview.getScene().getWindow().hide();
                });                
            } catch (InterruptedException ex) {
                Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
