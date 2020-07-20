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
abstract public class ClientObjectFilterBase<T extends ClientObjectBase> implements IClientObjectFilter<T>{
    IClientObjectFilter preFilter;
    
    @Override
    public void setPrefilter(IClientObjectFilter preFilter){
        this.preFilter=preFilter;
    }

    
    @Override
    synchronized public ReadOnlyClientObjectList<T> filterClientObjectList(ReadOnlyClientObjectList<T> originalList) {
        ReadOnlyClientObjectList<T> prefilteredList;
        if( preFilter != null ){
            prefilteredList = (ReadOnlyClientObjectList<T>)preFilter.filterClientObjectList(originalList);
        }else{
            prefilteredList=originalList;
        }
        
        ClientObjectList<T> filteredList= new ClientObjectList<>();
        for(T clientObject : prefilteredList.getAll()){
            if(isClientObjectInScope(clientObject)){
                filteredList.add(clientObject);
            }
        }
        return filteredList;
    } 
}
