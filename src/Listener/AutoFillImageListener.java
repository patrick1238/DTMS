/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import Interfaces.ImageObject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 *
 * @author patri
 */
public class AutoFillImageListener implements ChangeListener<String> {
    ImageObject img;

    /**
     *
     * @param img
     */
    public AutoFillImageListener(ImageObject img) {
        this.img = img;
    }
    
    
    /**
     *
     * @param field
     */
    public void bindTextField(TextField field) {
        field.textProperty().addListener(this);
    }
    
    public void bindComboBox(ComboBox box){
        box.valueProperty().addListener(this);
    }
    
    

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        String key = null;
        if(observable.toString().contains("StringProperty")){
            StringProperty textProperty = (StringProperty) observable ;
            TextField textField = (TextField) textProperty.getBean();
            key = textField.getId();
        }else{
            ObjectProperty prop = (ObjectProperty) observable;
            ComboBox box = (ComboBox)prop.getBean();
            key = box.getId();
        }
        this.img.setValue(key, new SimpleStringProperty(newValue));
    }
    
}
