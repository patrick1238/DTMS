/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.controls.elements;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import net.rehkind_mag.entities.pool.ClinicPool;
import net.rehkind_mag.interfaces.client.IClientObject;
import net.rehkind_mag.utils.ListItemConverterFactory;

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
