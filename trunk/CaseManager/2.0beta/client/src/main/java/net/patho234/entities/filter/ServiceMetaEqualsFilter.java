/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import java.util.Objects;
import net.patho234.entities.ClientService;
import net.patho234.interfaces.IMetadata;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class ServiceMetaEqualsFilter extends ClientObjectFilterBase<ClientService> {
    String metadataKey;
    Object filterObject;
    public ServiceMetaEqualsFilter(String metadataKey, Object filterObject){
        this.metadataKey = metadataKey;
        this.filterObject = filterObject;
    }
    
    public void setFilterObject( Object newFilterObject ){ this.filterObject = newFilterObject; }
    
    @Override
    public boolean isClientObjectInScope(ClientService clientObject) {
        if( this.filterObject == null){ return true; } // no required object set: accept all
        
        for (IMetadata md : clientObject.getMetadata() ){
            if ( md.getName().equals( metadataKey ) ){
                return (Objects.equals(filterObject, md.getData()));
            }
        }
        
        Logger.getLogger(getClass()).warn("Metadata key '"+metadataKey+"' wasn't found for ClientService. Filter potenitially applied to wrong service list?!");
        Logger.getLogger(getClass()).warn("Service was: "+clientObject.toString());
        return false;
    }
    
}
