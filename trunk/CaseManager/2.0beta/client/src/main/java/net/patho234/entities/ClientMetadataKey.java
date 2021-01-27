/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities;

import java.util.Objects;

/**
 *
 * @author rehkind
 */
public class ClientMetadataKey {
    String name;
    Integer serviceId;
    
    public ClientMetadataKey(ClientMetadata meta){
        name = meta.getName();
        serviceId = meta.getService().getId();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.name);
        hash = 41 * hash + Objects.hashCode(this.serviceId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if ( !this.getClass().equals( obj.getClass() )) {
            return false;
        }
        
        final ClientMetadataKey other = (ClientMetadataKey) obj;
        Boolean isEqual = Objects.equals(name, other.name) && Objects.equals(serviceId, other.serviceId);
        return isEqual;
    }
    
    
}
