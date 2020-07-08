/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.views;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.patho234.controls.RegistrationController;
import net.patho234.interfaces.DTMSView;
import net.patho234.interfaces.client.ISubmitterReceiver;
import net.patho234.webapp_client.FxmlManager;

/**
 *
 * @author patri
 */
public class RegistrationWindow extends Stage{
    
    RegistrationController controller;
    ISubmitterReceiver receiver;
    
    public RegistrationWindow( ISubmitterReceiver receiver){
        this.receiver = receiver;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/fx_register_pane.fxml"));
        Parent root=null;
        try {
            root = fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(RegistrationWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for registration window...exiting.", ex);
            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
        }
        controller = fxmlLoader.getController();
        
        EventHandler<WindowEvent> onOkClickedHandler = new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent t) {
                System.out.println("CLOSING REGISTRATION WND");
                if( receiver!=null ){ receiver.setSubmitter( controller.getSubmitter() ); }
            }
        
        };
        
        Scene scene = new Scene(root);
        setTitle("Register a new user account");
        setScene(scene);
        
        this.setOnCloseRequest(onOkClickedHandler);
    }
    
}
