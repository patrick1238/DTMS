/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTMS;

import BackgroundHandler.Config;
import BackgroundHandler.ErrorLog;
import Windowcontroller.InstallationWindowController;
import javafx.application.Application;
import javafx.stage.Stage;
import Windowcontroller.MainWindowController;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author patri
 */
public class DTMS extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Config config = Config.getConfig();
        ErrorLog.startErrorlogCollection();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(DTMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        final MainWindowController controller = fxmlLoader.getController();
        controller.set_stage(primaryStage);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        primaryStage.setTitle("Digital_tissue_management_suite");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((final WindowEvent event) -> {
            controller.close();
            config.replace("open", "no");
        });
        primaryStage.show();
    }

}
