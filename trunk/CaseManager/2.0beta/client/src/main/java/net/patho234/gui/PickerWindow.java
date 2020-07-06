/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.gui;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.patho234.controls.elements.PickerController;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class PickerWindow {
    List items;
    String itemClassName;
    public PickerWindow(List items, String className){
        this.items = items;
        this.itemClassName = className;
    }
    
    public Object showAndWait(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource( "/fxml/elements/fx_picker.fxml" ) );


            Stage stage = new Stage();
            stage.setAlwaysOnTop(true);
            stage.setMaxWidth(250.);
            stage.setMinWidth(250.);
            stage.setMaxHeight(600.);
            stage.setMinHeight(200.);

            Scene scene = new Scene(loader.load());
            PickerController pickerControl = loader.getController();
            pickerControl.setItems(items, itemClassName);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            Logger.getLogger(getClass()).info("Selected item: "+pickerControl.getSelectedItem());
            return pickerControl.getSelectedItem();
        }catch(IOException ioEx){
            Logger.getLogger(getClass()).error("Could not load the ClinicPicker window...check for correct path.");
            ioEx.printStackTrace();
        }
        return null;
    }
}
