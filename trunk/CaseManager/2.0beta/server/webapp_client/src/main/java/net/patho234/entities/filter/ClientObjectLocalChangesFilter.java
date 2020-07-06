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
public class ClientObjectLocalChangesFilter<T extends ClientObjectBase> implements IClientObjectFilter<T>{
    @Override
    public ReadOnlyClientObjectList<T> filterClientObjectList(ReadOnlyClientObjectList<T> originalList) {
        ClientObjectList<T> filteredList= new ClientObjectList<>();
        for(T clientObject : originalList.getAll()){
            if(isClientObjectInScope(clientObject)){
                filteredList.add(clientObject);
            }
        }
        return filteredList;
    } 

    @Override
    public boolean isClientObjectInScope(T clientObject) {
        return clientObject.hasLocalChanges();
    }
}
