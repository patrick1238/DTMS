/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import net.patho234.entities.ClientService;

/**
 *
 * @author rehkind
 */
public class ServiceTypeFilter  extends ClientObjectFilterBase<ClientService>{
    String searchTerm="";
    
    public ServiceTypeFilter(String serviceType){
        searchTerm=serviceType;
    }
    
    public void setSearch(String newSearchTerm){
        newSearchTerm = (newSearchTerm==null) ? "" : newSearchTerm;
        this.searchTerm = newSearchTerm.toLowerCase();
        notifyAllListeners();
    }
    
    @Override
    public boolean isClientObjectInScope(ClientService clientObject) {
        return clientObject.getServiceDefinition().getName().equals(this.searchTerm);
    }
    
}
