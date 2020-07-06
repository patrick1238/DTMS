/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls.elements;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import net.patho234.entities.pool.ClinicPool;
import net.patho234.interfaces.client.IClientObject;
import net.patho234.utils.ListItemConverterFactory;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class PickerController<T extends IClientObject> implements Initializable {
    @FXML
    TextField txtFilter;
    @FXML
    ListView lstList;
    
    List items;
    
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lstList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    
    public void setItems(List items, String className){
    
        items = ClinicPool.createPool().getAllEntities();
        Object item = items.toArray()[0];
        lstList.setCellFactory(param ->  { return ListItemConverterFactory.getConverter(className).convert();
        });
        
        
        for( int i =0; i<items.size(); i++) {
            lstList.getItems().add(items.get(i));
        }
    }
    
    @FXML
    public void filterUpdate(){}
    
    public Object getSelectedItem(){ return this.lstList.getSelectionModel().getSelectedItem(); }
}
