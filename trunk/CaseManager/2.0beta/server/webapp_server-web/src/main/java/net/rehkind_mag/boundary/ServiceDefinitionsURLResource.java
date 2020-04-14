/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.boundary;

import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.UriInfo;
import net.rehkind_mag.control.LocalServiceDefinitionRepository;
import net.rehkind_mag.interfaces.ICase;
import net.rehkind_mag.interfaces.IService;
import net.rehkind_mag.interfaces.IServiceDefinition;

/**
 *
 * @author HS
 */
public class ServiceDefinitionsURLResource {   
    
   static public String getURL(IServiceDefinition serviceDef, UriInfo uriInfo){
        URI serviceUri = uriInfo.getBaseUriBuilder()
            .path(ServiceDefinitionsResource.class)
            .path(ServicesResource.class, "getServiceDefinition")
            .build(serviceDef.getId());
        
        return serviceUri.getRawPath();
    }

    static public String getURL(int serviceDefID, UriInfo uriInfo){
        URI serviceUri = uriInfo.getBaseUriBuilder()
            .path(ServiceDefinitionsResource.class)
            .path(ServicesResource.class, "getServiceDefinition")
            .build(serviceDefID);
        
        return serviceUri.getRawPath();
    }
}

