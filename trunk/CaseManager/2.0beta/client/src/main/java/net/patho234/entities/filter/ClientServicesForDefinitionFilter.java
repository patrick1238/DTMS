/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.patho234.entities.ClientService;
import net.patho234.interfaces.IServiceDefinition;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;

/**
 *
 * @author rehkind
 */
public class ClientServicesForDefinitionFilter extends ClientObjectFilterBase<ClientService> {
    IServiceDefinition filterDef;

    public ClientServicesForDefinitionFilter(IServiceDefinition filterDefinition){
        this.filterDef = filterDefinition;
        
    }
    
    @Override
    public boolean isClientObjectInScope(ClientService clientObject) {
        //return getParentDefinitionIds().contains(clientObject.getServiceDefinition().getId());
        return(Objects.equals( clientObject.getServiceDefinition().getId(), filterDef.getId()));
    }
    
    private Set<Integer> getParentDefinitionIds(){
        IServiceDefinition currentDefinition = this.filterDef;
        Set<Integer> parentDefinitionIDs=new HashSet<>();
        while( currentDefinition != null ){
            parentDefinitionIDs.add(currentDefinition.getId());
            currentDefinition = currentDefinition.getParentDefinition();
        }
        return parentDefinitionIDs;
    }
}
