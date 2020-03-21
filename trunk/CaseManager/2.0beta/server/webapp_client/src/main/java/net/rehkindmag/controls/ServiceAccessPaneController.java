/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkindmag.controls;

import com.sun.javafx.collections.ObservableListWrapper;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import net.rehkind_mag.interfaces.IHttpResponse;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class ServiceAccessPaneController extends AccessPaneController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        endpoints=new HashMap<>();
        
        endpoints.put("@GET service", "servicepool/service/{ID}");
        endpoints.put("@GET services", "servicepool/services");
        endpoints.put("@UPDATE service", "servicepool/service/update");
        endpoints.put("@CREATE service", "servicepool/service/create");
        endpoints.put("@DELETE service", "servicepool/service/delete");
        
        ArrayList<String> asList = new ArrayList<String>();
        asList.addAll(endpoints.keySet());
        ObservableList<String> endpointList = new ObservableListWrapper<String>( asList );
        
        cbEndpointTemplate.setItems(endpointList);
        cbEndpointTemplate.getSelectionModel().select("@GET services");
        onEndpointTemplateSelected();
    }    

    @Override
    public void receiveHttpResponse(Integer requestID, IHttpResponse response) {
        Logger.getLogger(getClass().getName()).info("HttpResponse received: id="+requestID+" status: "+response.getResponseStatus()+" message: "+response.getMessage());
    }
    
}
