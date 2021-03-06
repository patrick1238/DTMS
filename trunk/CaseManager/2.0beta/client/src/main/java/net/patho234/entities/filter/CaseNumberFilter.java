/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import net.patho234.entities.ClientCase;

/**
 *
 * @author rehkind
 */
public class CaseNumberFilter  extends ClientObjectFilterBase<ClientCase>{
    String searchTerm="";
    
    public void setSearch(String newSearchTerm){
        newSearchTerm = (newSearchTerm==null) ? "" : newSearchTerm;
        this.searchTerm = newSearchTerm.toLowerCase();
        notifyAllListeners();
    }
    
    @Override
    public boolean isClientObjectInScope(ClientCase clientObject) {
        return clientObject.getCaseNumber().toLowerCase().contains(this.searchTerm);
    }
    
}
