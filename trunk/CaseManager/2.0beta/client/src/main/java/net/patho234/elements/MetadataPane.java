/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements;

import java.util.HashSet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.patho234.elements.validator.DoubleTextFieldValidator;
import net.patho234.elements.validator.IntegerTextFieldValidator;
import net.patho234.elements.validator.MetadataStringConverter;
import net.patho234.entities.ClientMetadata;
import net.patho234.interfaces.IMetadata;

/**
 *
 * @author rehkind
 */
public class MetadataPane extends AnchorPane{
    MetadataStringConverter converter;
    
    IMetadata metadata;
    public MetadataPane(IMetadata data){
        this.metadata = data;
        this.converter = new MetadataStringConverter(data);
        initGui();
    }
    
    final public void initGui(){
        
        HBox row=new HBox();
        String key = metadata.getName();
        Object value = metadata.getName();
        
        AnchorPane.setBottomAnchor(this, 0.);
        AnchorPane.setLeftAnchor(this, 0.);
        AnchorPane.setTopAnchor(this, 0.);
        AnchorPane.setRightAnchor(this, 0.);
        
        Label keyLabel = new Label(key);
        keyLabel.setPrefWidth(150);
        row.getChildren().add(keyLabel);
        row.setSpacing(10);
        TextField valueField = new TextField("");
        
        switch( metadata.getType() ){
            case STRING:
            case TEXT:
                
                break;
            case INTEGER:
                new IntegerTextFieldValidator(valueField);
                break;
            case DOUBLE:
                new DoubleTextFieldValidator(valueField);
                break;
        }
        valueField.setPrefWidth(200);
        row.getChildren().add(valueField);
        this.getChildren().add(row);
        
        valueField.setText(String.valueOf( metadata.getData() ));
        valueField.textProperty().addListener(
            new ChangeListener<String>(){
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    System.out.println("OLD: "+oldValue+" NEW: "+newValue);
                    metadata.setData(converter.fromString(newValue));
                }
        });
    }
    
}
