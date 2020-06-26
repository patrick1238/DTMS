/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.filter;

import net.rehkind_mag.entities.ClientObjectBase;
import net.rehkind_mag.interfaces.client.ClientObjectList;
import net.rehkind_mag.interfaces.client.IClientObjectFilter;
import net.rehkind_mag.interfaces.client.ReadOnlyClientObjectList;

/**
 *
 * @author rehkind
 */
abstract public class ClientObjectFilterBase<T extends ClientObjectBase> implements IClientObjectFilter<T>{
    @Override
    synchronized public ReadOnlyClientObjectList<T> filterClientObjectList(ReadOnlyClientObjectList<T> originalList) {
        ClientObjectList<T> filteredList= new ClientObjectList<>();
        for(T clientObject : originalList.getAll()){
            if(isClientObjectInScope(clientObject)){
                filteredList.add(clientObject);
            }
        }
        return filteredList;
    } 
}
