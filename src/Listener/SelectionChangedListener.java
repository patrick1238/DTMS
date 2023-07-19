/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import BackgroundHandler.ViewController;
import Views.PropertyNode;
import ImageObjects.ImageObjectGeneral;
import Windowcontroller.TableWindowController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author patri
 */
public class SelectionChangedListener implements ChangeListener<ImageObjectGeneral> {
    ViewController viewcontroller;
    TableWindowController tablecontroller;
    
    
    public SelectionChangedListener(TableWindowController tablecontroller){
        this.viewcontroller = ViewController.getViewController();
        this.tablecontroller = tablecontroller;
    }

    @Override
    public void changed(ObservableValue<? extends ImageObjectGeneral> observable, ImageObjectGeneral oldValue, ImageObjectGeneral newValue) {
        if(newValue!=null){
            PropertyNode node = (PropertyNode)this.viewcontroller.getView("PropertyNode"+tablecontroller.getType());
            this.viewcontroller.setActiveTableWindowController(tablecontroller);
            this.viewcontroller.closeView("PropertyNode"+tablecontroller.getType());
            this.viewcontroller.createView("PropertyNode"+tablecontroller.getType());
        }
    }

}
