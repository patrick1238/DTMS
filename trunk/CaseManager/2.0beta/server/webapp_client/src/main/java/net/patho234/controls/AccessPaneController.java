/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import net.patho234.utils.HTTP_REQUEST_TYPE;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

/**
 *
 * @author rehkind
 */
public abstract class AccessPaneController {
    HashMap<Integer, String> pendingHttpRequests=new HashMap<>();
    String selectedMethod=HTTP_REQUEST_TYPE.GET_ALL;
    ArrayList<ClientObjectController> viewControls = new ArrayList<>();

    //FXML attributes:
    @FXML ComboBox<String> cbMethod;
    @FXML TextField txtCaseJson;
    @FXML TextField txtUser;
    @FXML PasswordField pfPassword;
    //   -- HttpResponseStatus
    @FXML HBox hbHTTPResponseStatus;
    @FXML Label lblResponseStatus;
    @FXML Label lblResponseTime;
    
    public void onMethodSelected(){
        String newSelectedMethod=cbMethod.getSelectionModel().getSelectedItem();
        if( !newSelectedMethod.equals( this.selectedMethod ) ){
            selectedMethod = newSelectedMethod;
            Logger.getLogger(getClass().getName()).log(Level.INFO, "New http method selected: {0}", new String[]{ newSelectedMethod });
        }
        hbHTTPResponseStatus.setVisible(false);
    }
    
    @FXML
    abstract public void fireRequest();
    
    abstract protected void buildView();
    
    protected void detachView(){
        if( viewControls==null ){ return; }
        
        for( ClientObjectController coc : viewControls ){
            coc.dispose();
        }
    }
}
