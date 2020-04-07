/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.controls;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 * @author rehkind
 */
abstract class ClientObjectController {
    boolean editable;
    
    @FXML Pane editPane;
    @FXML Pane viewPane;
    @FXML ImageView viewStatus;
    @FXML ImageView editStatus;
    @FXML ImageView editEdit;
    
    public void setEditable(Boolean editable){
        this.editable=editable;
        
        if( editable ){
            editEdit.setImage(new Image("/edit_icon_30.png"));
            editPane.toFront();
            editPane.setVisible(true);
            viewPane.toBack();
            viewPane.setVisible(false);
            
        }else{
            viewPane.toFront();
            viewPane.setVisible(true);
            editPane.toBack();
            editPane.setVisible(false);
        }
    }
    
    public void setCachedView(Boolean isCached){
        if(isCached){
            viewStatus.setImage( new Image("/wait_icon_30.png") );
            editStatus.setImage( new Image("/wait_icon_30.png") );
        }else{
            viewStatus.setImage(null);
            editStatus.setId(null);
        }
    }
    
    abstract void bindGUIElements();
    abstract void dispose();
}
