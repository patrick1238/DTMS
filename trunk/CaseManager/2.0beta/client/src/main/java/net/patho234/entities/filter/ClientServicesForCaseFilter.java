/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import java.util.Objects;
import net.patho234.entities.ClientService;
import net.patho234.interfaces.ICase;

/**
 *
 * @author rehkind
 */
public class ClientServicesForCaseFilter extends ClientObjectFilterBase<ClientService> {
    ICase filterCase;
    public ClientServicesForCaseFilter(ICase filterCase){
        this.filterCase = filterCase;
    }
    
    @Override
    public boolean isClientObjectInScope(ClientService clientObject) {
        if( Objects.equals( clientObject.getCase().getId(), filterCase.getId())){ System.out.println("cases match: "+clientObject.getCase().getId()+" / "+filterCase.getId()); }
        return Objects.equals( clientObject.getCase().getId(), filterCase.getId());
    }
}
