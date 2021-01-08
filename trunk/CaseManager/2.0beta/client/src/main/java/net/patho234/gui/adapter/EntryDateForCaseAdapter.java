/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.gui.adapter;

import java.time.LocalDate;
import java.util.Date;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import net.patho234.entities.ClientCase;
import net.patho234.entities.ClientClinic;
import net.patho234.entities.pool.ClinicPool;
import net.patho234.webapp_client.APPLICATION_DEFAULTS;

/**
 * adapter class to bind gui elements working on IClinic.getName with actual IClinic.ID property
 * @author rehkind
 */
public class EntryDateForCaseAdapter {
    SimpleStringProperty strEntryDate = new SimpleStringProperty();
    SimpleObjectProperty<LocalDate> localDateProperty = new SimpleObjectProperty();
    
    ClientCase myCase;
    
    public EntryDateForCaseAdapter( ClientCase theCase ){
        myCase=theCase;
        strEntryDate.bindBidirectional(myCase.getEntryDateProperty());
        
        createAdapterListener();
    }
    
    public void bindDate(Property<LocalDate> guiProperty){
        String asStr = myCase.getEntryDateProperty().getValue();
        
        guiProperty.setValue(APPLICATION_DEFAULTS.DEFAULT_DATE_CONVERTER.fromString(asStr));
        localDateProperty.bindBidirectional(guiProperty);
    }
    
    final public void createAdapterListener(){
        ChangeListener<String> stringListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LocalDate ld = APPLICATION_DEFAULTS.DEFAULT_DATE_CONVERTER.fromString(newValue);
                if( ld != null ){
                    localDateProperty.setValue(ld);
                }
            }
        };
        
        strEntryDate.addListener(stringListener);
        
        ChangeListener<LocalDate> idListener = new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                String dateAsStr = APPLICATION_DEFAULTS.DEFAULT_DATE_CONVERTER.toString(newValue);
                if( dateAsStr != null ){
                    strEntryDate.setValue(dateAsStr);
                }
            }
        };
        
        localDateProperty.addListener(idListener);
    }
}
