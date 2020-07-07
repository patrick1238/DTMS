/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.views;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.patho234.controls.MainController;
import net.patho234.webapp_client.APPLICATION_DEFAULTS;
import net.patho234.webapp_client.FxmlManager;

/**
 *
 * @author rehkind
 */
public class MainWindow extends Stage {
    MainController controller;
    public MainWindow() {
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
    
}
