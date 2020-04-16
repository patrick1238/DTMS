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

/**
 *
 * @author rehkind
 */
public class ReadOnlyClientObjectList<T extends IClientObject> extends ObservableListBase<T> {
    HashMap<Integer, IClientObject> cachedObjects = new HashMap<>();
    
    @Override
    public T get(int index) {
        return (T)cachedObjects.get(cachedObjects.keySet().toArray(new Integer[]{})[index]);
    }
    
    public T getByID(int id) {
        return (T)cachedObjects.get(id);
    }

    public Collection<T> getAll() {
        return (Collection<T>)cachedObjects.values();
    }
    
    public Set<Integer> getAllIDs() {
        return cachedObjects.keySet();
    }
    
    @Override
    public int size() {
        return cachedObjects.keySet().size();
    }
    
}
