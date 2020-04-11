/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.controls;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import net.rehkind_mag.gui.ClientPopup;
import net.rehkind_mag.interfaces.client.IClientObject;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
abstract class ClientObjectController<T extends IClientObject> {
    boolean editable;
    
    T item;
    
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
    
    
    @FXML
    public void copyToClipboard(  ) throws InterruptedException{
        StringSelection toCopy = new StringSelection(item.toJson().toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(toCopy, null);
        
        try{
            ClientPopup popup = new ClientPopup(String.format("ClientObject copy to ClipBoard.", new Object[]{}), "The ClientObject can now be instered in any TextField using Strg+V...");
            popup.setAutoHide(true);
            popup.show(editPane.getScene().getWindow());
        }catch(IOException ioEx){
            Logger.getLogger(getClass()).warn("Could not create PopUp to inform user that ClientObject was copied to ClipBoard.");
            ioEx.printStackTrace();
        }
        
       
    }
}
