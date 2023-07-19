/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import BackgroundHandler.ErrorLog;
import BackgroundHandler.ViewController;
import Windowcontroller.SettingsWindowController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import Interfaces.DTMSView;

/**
 *
 * @author patri
 */
public class SettingsStage implements DTMSView {

    Stage stage;

    public SettingsStage() {
        this.stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/SettingsWindow.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
            stage.setScene(new Scene(root1));
            SettingsWindowController controller = (SettingsWindowController) fxmlLoader.getController();
            stage.setTitle("Digital_tissue_management_suite Settings");
            controller.initView();
            stage.setOnCloseRequest((final WindowEvent event) -> {
                ViewController.getViewController().closeView(this.getIdentifier());
            });
            stage.show();
        } catch (IOException ex) {
            ErrorLog.getErrorLog().createLogEntry(2, getIdentifier() + ": Failed loading the fxml");
        }
    }

    @Override
    public String getIdentifier() {
        return "SettingsStage";
    }

    @Override
    public String getProperty() {
        return "Stage";
    }

    @Override
    public void close() {
        this.stage.close();
    }

}
