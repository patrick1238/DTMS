/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.interfaces.client;

import java.util.Set;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javax.json.JsonObject;

/**
 *
 * @author rehkind
 */
public interface IClientObject<T extends IClientObject> extends ObservableValue<T>{
    public final static int STATE_PERSISTENT=0;
    public final static int STATE_CACHED=1;
    
    
    public JsonObject getOriginalJson();
    public void setOriginalJson( JsonObject obj );
    public boolean hasLocalChanges();
    public Integer getId();
    public T getLocalClone();
    public JsonObject toJson();
    public void merge(T toMergeWith);
    
    public static void notityAllListeners(Set<ChangeListener> listeners, ObservableValue obs, Object oldValue, Object newValue){
        listeners.forEach( (listener) -> {
            listener.changed(obs, oldValue, newValue);
        }
        );
    }
}
