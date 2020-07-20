/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import net.patho234.entities.ClientObjectBase;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.interfaces.client.IClientObjectFilter;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;

/**
 *
 * @author rehkind
 */
public class ClientObjectLocalChangesFilter<T extends ClientObjectBase> extends ClientObjectFilterBase<T>{
    
    @Override
    public boolean isClientObjectInScope(T clientObject) {
        return clientObject.hasLocalChanges();
    }
}
