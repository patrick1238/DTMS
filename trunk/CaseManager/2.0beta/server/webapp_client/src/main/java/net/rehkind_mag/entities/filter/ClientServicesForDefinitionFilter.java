/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.filter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.rehkind_mag.entities.ClientService;
import net.rehkind_mag.interfaces.IServiceDefinition;
import net.rehkind_mag.interfaces.client.ReadOnlyClientObjectList;

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
    public ReadOnlyClientObjectList<ClientService> filterClientObjectList(ReadOnlyClientObjectList<ClientService> originalList) {
        return super.filterClientObjectList(originalList); //To change body of generated methods, choose Tools | Templates.
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
