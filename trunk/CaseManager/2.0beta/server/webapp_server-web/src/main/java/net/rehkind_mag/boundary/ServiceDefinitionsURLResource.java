/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.boundary;

import java.net.URI;
import javax.ws.rs.core.UriInfo;
import net.rehkind_mag.interfaces.IServiceDefinition;

/**
 *
 * @author HS
 */
public class ServiceDefinitionsURLResource {   
    
   static public String getURL(IServiceDefinition serviceDef, UriInfo uriInfo){
        URI serviceUri = uriInfo.getBaseUriBuilder()
            .path(ServiceDefinitionsResource.class)
            .path(ServiceDefinitionsResource.class, "getServiceDefinition")
            .build(serviceDef.getId());
        
        return serviceUri.getRawPath();
    }

    static public String getURL(int serviceDefID, UriInfo uriInfo){
        URI serviceUri = uriInfo.getBaseUriBuilder()
            .path(ServiceDefinitionsResource.class)
            .path(ServiceDefinitionsResource.class, "getServiceDefinition")
            .build(serviceDefID);
        
        return serviceUri.getRawPath();
    }
}

