/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.views;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.patho234.controls.CaseController;
import net.patho234.entities.ClientCase;
import net.patho234.webapp_client.FxmlManager;

/**
 *
 * @author rehkind
 */
public class CaseWindow extends Stage {
    FXMLLoader loader;
    CaseController controller;
    ClientCase myCase;
    
    public CaseWindow(ClientCase theCase) throws IOException{
        this.myCase = theCase;
        loader = new FXMLLoader( getClass().getResource( "/fxml/fx_case_pane.fxml") );
        Parent root=null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(RegistrationWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for registration window...exiting.", ex);
            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
        }
        controller = loader.getController();
        Scene scene = new Scene(root);
        setScene(scene);
    }
    
    public void initView(){
        controller.loadCase(myCase);
        this.setTitle(myCase.getCaseNumber());
    }   
}
