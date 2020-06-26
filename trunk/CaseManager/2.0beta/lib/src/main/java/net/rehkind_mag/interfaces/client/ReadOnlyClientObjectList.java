/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.interfaces.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import javafx.collections.ObservableListBase;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

/**
 *
 * @author rehkind
 */
public class ReadOnlyClientObjectList<T extends IClientObject> extends ObservableListBase<T> {
    HashMap<Integer, IClientObject> cachedObjects = new HashMap<>();
    
    @Override
    public synchronized T get(int index) {
        return (T)cachedObjects.get(cachedObjects.keySet().toArray(new Integer[]{})[index]);
    }
    
    public synchronized T getByID(int id) {
        return (T)cachedObjects.get(id);
    }

    public synchronized Collection<T> getAll() {
        return (Collection<T>)cachedObjects.values();
    }
    
    public synchronized Set<Integer> getAllIDs() {
        return cachedObjects.keySet();
    }
    
    @Override
    public synchronized int size() {
        return cachedObjects.keySet().size();
    }
    
    public synchronized JsonArray toJson(){
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for( IClientObject co : getAll() ){
            builder.add(co.toJson());
        }

        return builder.build();
    }
}
