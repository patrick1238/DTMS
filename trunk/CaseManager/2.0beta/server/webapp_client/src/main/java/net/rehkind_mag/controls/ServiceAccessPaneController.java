/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.controls;

import com.sun.javafx.collections.ObservableListWrapper;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.rehkind_mag.entities.pool.CasePool;
import net.rehkind_mag.utils.HTTP_REQUEST_TYPE;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class ServiceAccessPaneController implements Initializable {
    ArrayList<String> methods;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CasePool.createPool().getAllEntities(false);
    }    
    
    protected void buildView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
