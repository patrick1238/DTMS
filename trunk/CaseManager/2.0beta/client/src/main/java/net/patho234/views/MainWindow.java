/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.views;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.patho234.controls.MainPaneController;
import net.patho234.interfaces.IDataDisplay;
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
            
            Scene scene = new Scene(root);
            setTitle(APPLICATION_DEFAULTS.APPLICATION_NAME+" @"+APPLICATION_DEFAULTS.VERSION_NUMBER);
            setScene(scene);

            this.setOnCloseRequest(FxmlManager.EXIT_APPLICATION_HANDLER);
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
