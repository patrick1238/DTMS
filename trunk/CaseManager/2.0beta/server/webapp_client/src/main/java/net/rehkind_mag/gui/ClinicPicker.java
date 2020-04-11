/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import net.rehkind_mag.entities.pool.ClinicPool;
import net.rehkind_mag.interfaces.IClinic;
import net.rehkind_mag.interfaces.IClinicReceiver;
import org.jboss.logging.Logger;


/**
 *
 * @author rehkind
 */
public class ClinicPicker extends Label{
    IClinicReceiver receiver;

    
    public ClinicPicker(){
        initialize();
    }
    
    public void setReceiver(IClinicReceiver receiver){
        this.receiver=receiver;
        initialize();
    }
    
    private void initialize(){
        EventHandler handler = new EventHandler() {
            @Override
            public void handle(Event event) {
                if(receiver==null){
                    Logger.getLogger(getClass()).error("No receiver for clinic picker set...ignoring call!");
                    return;
                }
                
                PickerWindow wnd = new PickerWindow(ClinicPool.createPool().getAllEntities(), "net.rehkind_mag.entities.ClientClinic");
                IClinic pickedClinic = (IClinic)wnd.showAndWait();
                Logger.getLogger(getClass()).info("Selected item: "+pickedClinic);
                receiver.setClinic(pickedClinic);
            }
        };
        setOnMouseClicked(handler);
    }
    

}
