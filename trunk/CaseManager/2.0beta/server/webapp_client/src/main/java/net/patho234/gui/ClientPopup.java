/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.gui;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Popup;
import javafx.stage.Window;
import net.patho234.controls.elements.ClientPopupController;

/**
 *
 * @author rehkind
 */
public class ClientPopup extends Popup{
    FXMLLoader loader;
    Parent root;
    ClientPopupController control;
    Window owner;
    
    public ClientPopup( String title, String msg ) throws IOException{
        loader = new FXMLLoader(getClass().getResource("/fxml/elements/fx_client_popup.fxml"));
        
        root = loader.load();
        
        control = loader.getController();
        control.setTitle(title);
        control.setMessage(msg);
        getScene().setRoot(root);
    }
    
    @Override
    public void show(){
        this.setHeight( control.getPrefHeight() );
        super.show();
    }
    
    
    @Override
    public void show(Window owner){
        this.owner=owner;
        this.setHeight( control.getPrefHeight() );
        control.setHeight(control.getPrefHeight());
        super.show(owner);
    }
    
}
