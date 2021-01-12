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
import net.patho234.controls.ServiceController;
import net.patho234.entities.ClientService;
import net.patho234.webapp_client.APPLICATION_DEFAULTS;
import net.patho234.webapp_client.FxmlManager;

/**
 *
 * @author rehkind
 */
public class ServiceWindow extends Stage {
    FXMLLoader loader;
    ServiceController controller;
    ClientService myService;
    
    public ServiceWindow(ClientService theService) throws IOException{
        this.myService = theService;
        loader = new FXMLLoader( getClass().getResource( "/fxml/fx_service_pane.fxml") );
        Parent root=null;
        
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(RegistrationWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for registration window...exiting.", ex);
            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
        }
        controller = loader.getController();
        controller.loadService( theService );
        Scene scene = new Scene(root);
        setScene(scene);
        
        initView();
    }
    
    final public void initView(){
        controller.loadService(myService);
        this.setTitle(myService.getCase().getCaseNumber() + " - " + myService.getServiceDefinition().getName());
        setMinWidth(400);
        setMaxWidth(400);
        setMinHeight(400);
    }
}
