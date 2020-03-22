/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkindmag.controls;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 *
 * @author rehkind
 */
abstract class ClientObjectController {
    boolean editable;
    @FXML Pane editablePane;
    @FXML Pane viewPane;
    
    public void setEditable(Boolean editable){
        this.editable=editable;
        if( editable ){
            editablePane.toFront();
            editablePane.setVisible(true);
            viewPane.setVisible(false);
        }else{
            viewPane.toFront();
            viewPane.setVisible(true);
            editablePane.setVisible(false);
        }
    }
    
    abstract void bindGUIElements();
}
