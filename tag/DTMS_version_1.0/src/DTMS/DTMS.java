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
        if (config.get("open").equals("no")) {
            if (config.get("installed").equals("yes")) {
                startMainWindow(primaryStage);
            } else {
                installDTMS(primaryStage);
            }
        }else{
            JOptionPane.setDefaultLocale(Locale.ENGLISH);
            int result = JOptionPane.showConfirmDialog(null, "Digital_tissue_management_suite is still running, do your really want to open in?", "Open?", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                startMainWindow(primaryStage);
            }
        }
    }

    public void startMainWindow(Stage primaryStage) {
        Config config = Config.getConfig();
        config.replace("open", "yes");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(DTMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        final MainWindowController controller = fxmlLoader.getController();
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

    private void installDTMS(Stage primaryStage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/InstallationWindow.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(DTMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        final InstallationWindowController controller = fxmlLoader.getController();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        primaryStage.setTitle("DTMS Installation Window");
        primaryStage.setScene(scene);
        controller.setMain(primaryStage, this);
        primaryStage.showAndWait();
        startMainWindow(primaryStage);
    }

}
