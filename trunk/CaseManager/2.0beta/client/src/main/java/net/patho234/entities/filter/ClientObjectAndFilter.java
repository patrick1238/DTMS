/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import net.patho234.entities.ClientObjectBase;

/**
 *
 * @author rehkind
 * @param <T> client object type; e.g. ClientCase, ClientClinic etc.
 */
public class ClientObjectAndFilter<T> extends ClientObjectFilterBase<ClientObjectBase>{
    Set<ClientObjectFilterBase<ClientObjectBase>> allFilter = new HashSet<>();
    
    public ClientObjectAndFilter(){}
    public ClientObjectAndFilter( Collection<ClientObjectFilterBase<ClientObjectBase>> filterToAdd ){
        allFilter.addAll(filterToAdd);
    }
    
    public void addFilter(ClientObjectFilterBase<ClientObjectBase> filter){ allFilter.add(filter); }
    public void removeFilter(ClientObjectFilterBase<ClientObjectBase> filter){ allFilter.remove(filter); }
    
    public void setSearch(String newSearchTerm){ Logger.getLogger(getClass().getName()).warning("setSearch(String searchString) was called on ClientObjectOrFilter - is ignored."); }

    @Override
    public boolean isClientObjectInScope(ClientObjectBase clientObject) {
        // if no filters are set or all of them accept the ClientObject return true
        if( allFilter.isEmpty() ){ return true; }
        return allFilter.stream().allMatch((filter) -> ( filter.isClientObjectInScope(clientObject) ));
    }
}
