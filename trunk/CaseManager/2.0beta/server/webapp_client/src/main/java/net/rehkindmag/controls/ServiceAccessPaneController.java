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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.utils.HTTP_REQUEST_TYPE;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class ServiceAccessPaneController extends AccessPaneController implements Initializable {
    ArrayList<String> methods;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        methods=new ArrayList<>();
        
        methods.add(HTTP_REQUEST_TYPE.GET);
        methods.add(HTTP_REQUEST_TYPE.GET_ALL);
        methods.add(HTTP_REQUEST_TYPE.CREATE);
        methods.add(HTTP_REQUEST_TYPE.UPDATE);
        methods.add(HTTP_REQUEST_TYPE.DELETE);
        
        ObservableList<String> methodList = new ObservableListWrapper<String>( methods );
        
        cbMethod.setItems(methodList);
        cbMethod.getSelectionModel().select(HTTP_REQUEST_TYPE.GET_ALL);
        onMethodSelected();
    }    

    @FXML
    @Override
    public void fireRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void buildView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
