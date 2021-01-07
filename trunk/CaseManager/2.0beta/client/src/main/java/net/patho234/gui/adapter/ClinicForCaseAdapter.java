/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.gui.adapter;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import net.patho234.entities.ClientCase;
import net.patho234.entities.ClientClinic;
import net.patho234.entities.pool.ClinicPool;

/**
 * adapter class to bind gui elements working on IClinic.getName with actual IClinic.ID property
 * @author rehkind
 */
public class ClinicForCaseAdapter {
    SimpleStringProperty clinicName = new SimpleStringProperty();
    SimpleIntegerProperty clinicId = new SimpleIntegerProperty();
    
    ClientCase myCase;
    
    public ClinicForCaseAdapter( ClientCase theCase ){
        myCase=theCase;
        clinicId.bindBidirectional(myCase.getClinicIDProperty());
        
        createAdapterListener();
    }
    
    public void bindName(Property<String> guiProperty){
        guiProperty.setValue(myCase.getClinic().getName());
        clinicName.bindBidirectional(guiProperty);
    }
    
    final public void createAdapterListener(){
        ChangeListener<String> nameListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ClientClinic c = ClinicPool.createPool().getClinicByName(newValue);
                if( c != null ){
                    clinicId.setValue(c.getId());
                }
            }
        };
        
        clinicName.addListener(nameListener);
        
        ChangeListener<Number> idListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                ClientClinic c = ClinicPool.createPool().getEntity((Integer)newValue);
                if( c != null ){
                    clinicName.setValue(c.getName());
                }
            }
        };
        
        clinicId.addListener(idListener);
    }
}
