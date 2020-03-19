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
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class CaseAccessPaneController implements Initializable {
    HashMap<String,String> endpoints;
    protected String epTemplateName="";
    protected String epTemplate;
    //FXML attributes:
    @FXML ComboBox<String> cbEndpointTemplate;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        endpoints=new HashMap<>();
        
        endpoints.put("@GET case", "/casepool/case/{ID}");
        endpoints.put("@GET cases", "/casepool/cases");
        endpoints.put("@UPDATE case", "/casepool/case/update");
        endpoints.put("@CREATE case", "/casepool/case/create");
        endpoints.put("@DELETE case", "/casepool/case/delete");
        
        ArrayList<String> asList = new ArrayList<String>();
        asList.addAll(endpoints.keySet());
        ObservableList<String> endpointList = new ObservableListWrapper<String>( asList );
        
        cbEndpointTemplate.setItems(endpointList);
        cbEndpointTemplate.getSelectionModel().select("@GET cases");
        
    }
    
    @FXML
    public void onEndpointTemplateSelected(){
        String newEndpointName=cbEndpointTemplate.getSelectionModel().getSelectedItem();
        if( newEndpointName != this.epTemplateName ){
            Logger.getLogger(getClass().getName()).log(Level.INFO, "New endpoint selected: {0}", newEndpointName);
            this.epTemplate=endpoints.get(newEndpointName);
            // TODO: update GUI / listener
            this.epTemplateName=newEndpointName;
        }
    }
    
}
