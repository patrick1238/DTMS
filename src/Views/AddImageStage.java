/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import BackgroundHandler.ErrorLog;
import BackgroundHandler.ViewController;
import Basicfunctions.Basicfunctions;
import Windowcontroller.ObjectWindowController;
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
public class AddImageStage implements DTMSView {

    Stage stage;

    public AddImageStage(){
        try {
            this.stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/ObjectWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            stage.setScene(new Scene(root1));
            ObjectWindowController controller = (ObjectWindowController) fxmlLoader.getController();
            controller.initImageObject("General");
            Basicfunctions.fillGridPane(controller, "General");
            stage.setTitle("Digital_tissue_management_suite");
            stage.setOnCloseRequest((final WindowEvent event) -> {
                ViewController.getViewController().closeView(this.getIdentifier());
            });
            controller.addTitle("Add image");
            controller.addSubmitButton();
            controller.initializeListenerForAddingAndEditing();
            controller.setSize();
            stage.show();
        } catch (IOException ex) {
            ErrorLog.getErrorLog().createLogEntry(2, "Can't load fxml of" + this.getIdentifier());
        }
    }

    @Override
    public String getIdentifier() {
        return "AddImageStage";
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
