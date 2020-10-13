/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.views;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.patho234.controls.ServiceController;
import net.patho234.entities.ClientService;
import net.patho234.interfaces.ICase;
import net.patho234.interfaces.IServiceDefinition;

/**
 *
 * @author rehkind
 */
public class ServicePane extends AnchorPane {
    FXMLLoader loader;
    ServiceController controller;
    ClientService myService;
    
    public ServicePane (ICase theCase, IServiceDefinition definition) throws IOException {
        this( ClientService.getServiceTemplate(theCase.getId(), definition.getId()) );
    }
    
    public ServicePane (ClientService theService) throws IOException {
        myService = theService;
        initView();
    }
    
    
    private void initView() throws IOException {
        loader = new FXMLLoader( getClass().getResource( "/fxml/fx_service_pane.fxml") );
        
        Pane caseView =  loader.load();
        controller = loader.getController();
        
        this.getChildren().add(caseView);
        
        controller.loadService(myService);
    }
}
