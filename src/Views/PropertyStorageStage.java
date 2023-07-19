/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import BackgroundHandler.ErrorLog;
import BackgroundHandler.ViewController;
import Windowcontroller.TableWindowController;
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
public class PropertyStorageStage implements DTMSView {

    Stage stage;
    ViewController viewcontroller;
    TableWindowController controller;

    public PropertyStorageStage(String type) {
        this.stage = new Stage();
        this.viewcontroller = ViewController.getViewController();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/TableWindow.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
            this.controller = fxmlLoader.getController();
            controller.initView(type);
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            stage.setTitle("DTMS - " + type + " property viewer");
            stage.setScene(scene);
            stage.setOnCloseRequest((final WindowEvent event) -> {
                this.viewcontroller.closeView(this.getIdentifier() + type);
            });
            controller.removePropertiesButton();
            controller.removeExportButton();
            this.stage.show();
        } catch (IOException ex) {
            ErrorLog.getErrorLog().createLogEntry(2, getIdentifier() + ": Failed loading the fxml");
        }
    }

    public TableWindowController getWindowController() {
        return this.controller;
    }

    @Override
    public String getIdentifier() {
        return "PropertyStorageStage";
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
