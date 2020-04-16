/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.rehkind_mag.entities.ClientService;
import net.rehkind_mag.interfaces.ICase;
import net.rehkind_mag.interfaces.client.IClientObjectFilter;
import net.rehkind_mag.interfaces.client.ReadOnlyClientObjectList;

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
        return Objects.equals( clientObject.getCase().getId(), filterCase.getId());
    }
}
