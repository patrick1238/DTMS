/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Label;

/**
 *
 * @author patri
 */
public class TableListChangedListener implements ListChangeListener<Object>{
    
    Label tableLabel;
    String fileType;
    
    /**
     *
     * @param tableLabel
     * @param fileType
     */
    public TableListChangedListener(Label tableLabel, String fileType) {
        this.tableLabel = tableLabel;
        this.fileType = fileType;
    } 
    
    @Override
    public void onChanged(ListChangeListener.Change c) {
        this.tableLabel.setText(Integer.toString(c.getList().size()) + " "  + this.fileType +"-Images  found");
    }
}
