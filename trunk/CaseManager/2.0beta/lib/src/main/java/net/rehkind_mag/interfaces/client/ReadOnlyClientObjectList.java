/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.interfaces.client;

import java.util.Collection;
import java.util.HashMap;
import javafx.collections.ObservableListBase;

/**
 *
 * @author rehkind
 */
public class ReadOnlyClientObjectList<T extends IClientObject> extends ObservableListBase {
    HashMap<Integer, IClientObject> cachedObjects = new HashMap<>();
    
    @Override
    public T get(int index) {
        return (T)cachedObjects.get(index);
    }

    public Collection<T> getAll(int index) {
        return (Collection<T>)cachedObjects.values();
    }
    
    @Override
    public int size() {
        return cachedObjects.keySet().size();
    }
    
}
