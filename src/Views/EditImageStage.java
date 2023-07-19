/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import BackgroundHandler.DataCache;
import BackgroundHandler.ErrorLog;
import BackgroundHandler.ViewController;
import Interfaces.ImageObject;
import Basicfunctions.Basicfunctions;
import Windowcontroller.ObjectWindowController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import Interfaces.DTMSView;

/**
 *
 * @author patri
 */
public class EditImageStage implements DTMSView {

    Stage stage;
    String type;

    public EditImageStage(TableView ImageTable, String type){
        this.type = type;
        DataCache cache = DataCache.getDataCache();
        for (Object o : ImageTable.getSelectionModel().getSelectedItems()) {
            if (o != null) {
                ImageObject t = (ImageObject) o;
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ObjectWindow.fxml"));
                    Parent node = (Parent) fxmlLoader.load();
                    this.stage = new Stage();
                    stage.setScene(new Scene(node));
                    ObjectWindowController controller = (ObjectWindowController) fxmlLoader.getController();
                    String oldValue = t.toString(cache.getDatabase(type).getEntryHeader());
                    stage.setTitle("Digital_tissue_management_suite");
                    stage.setOnCloseRequest((final WindowEvent event) -> {
                        ViewController.getViewController().closeView(this.getIdentifier()+type);
                    });
                    controller.addTitle("Edit " + t.getCaseNumber());
                    controller.initImageObject(type);
                    controller.setImageObject(t);
                    Basicfunctions.fillGridPane(controller, type);
                    controller.addUpdateButton(t);
                    controller.setWindow(t);
                    controller.setSize();
                    controller.initializeListenerForAddingAndEditing();
                    stage.show();
                } catch (IOException ex) {
                    ErrorLog.getErrorLog().createLogEntry(2, "Can't load fxml of" + this.getIdentifier());
                }
            }
        }
    }

    @Override
    public String getIdentifier() {
        return "EditImageStage";
    }

    @Override
    public String getProperty() {
        return "stage";
    }

    @Override
    public void close() {
        this.stage.close();
    }

}
