/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkindmag.controls;

import com.sun.javafx.collections.ObservableListWrapper;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import net.rehkind_mag.interfaces.IHttpResponse;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class CaseAccessPaneController extends AccessPaneController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        endpoints=new HashMap<>();
        
        endpoints.put("@GET case", "casepool/case/{ID}");
        endpoints.put("@GET cases", "casepool/cases");
        endpoints.put("@UPDATE case", "casepool/case/update");
        endpoints.put("@CREATE case", "casepool/case/create");
        endpoints.put("@DELETE case", "casepool/case/delete");
        
        ArrayList<String> asList = new ArrayList<String>();
        asList.addAll(endpoints.keySet());
        ObservableList<String> endpointList = new ObservableListWrapper<String>( asList );
        
        cbEndpointTemplate.setItems(endpointList);
        cbEndpointTemplate.getSelectionModel().select("@GET cases");
        onEndpointTemplateSelected();
        
    }

    @Override
    public void receiveHttpResponse(Integer requestID, IHttpResponse response) {
        Logger.getLogger(getClass().getName()).info("HttpResponse received: id="+requestID+" status: "+response.getResponseStatus()+" message: "+response.getMessage());
    }
    
    
}
