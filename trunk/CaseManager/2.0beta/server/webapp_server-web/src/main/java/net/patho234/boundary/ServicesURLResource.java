/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.boundary;

import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.UriInfo;
import net.patho234.control.LocalServiceRepository;
import net.patho234.interfaces.ICase;
import net.patho234.interfaces.IService;
import net.patho234.interfaces.IServiceDefinition;

/**
 *
 * @author HS
 */
public class ServicesURLResource {   
    
   static public String getURL(IService service, UriInfo uriInfo){
        URI serviceUri = uriInfo.getBaseUriBuilder()
            .path(ServicesResource.class)
            .path(ServicesResource.class, "getService")
            .build(service.getId());
        
        return serviceUri.getRawPath();
    }
   
    static public String getURL(IServiceDefinition serviceDef, UriInfo uriInfo){
        return ServicesURLResource.getURL(serviceDef.getId(), uriInfo);
    }
    
    static public String getURL(int serviceId, UriInfo uriInfo){
        URI serviceUri = uriInfo.getBaseUriBuilder()
            .path(ServicesResource.class)
            .path(ServicesResource.class, "getService")
            .build(serviceId);
        
        return serviceUri.getRawPath();
    }
    
    static public JsonObjectBuilder getServiceURLsBuilderForCase(ICase caseToBuildFor, UriInfo uriInfo, LocalServiceRepository serviceRepo){
        List<IService> services = serviceRepo.getServicesForCase(caseToBuildFor.getId());
        JsonObjectBuilder urlBuilder=Json.createObjectBuilder();
        for( IService service : services ){
            addServiceURL(service, urlBuilder, uriInfo);
            Logger.getGlobal().log(Level.SEVERE, "getServiceURLsBuilderForCase: doing nothing currently");
        }
        
        return urlBuilder;
    }
        
    static private void addServiceURL(IService serviceToAdd, JsonObjectBuilder builder, UriInfo uriInfo){
        builder.add("service:id="+serviceToAdd.getId(), getURL(serviceToAdd,uriInfo));
    }

    static String getUpdateURL(UriInfo uriInfo) {
        URI serviceUri = uriInfo.getBaseUriBuilder()
            .path(ServicesResource.class)
            .path(ServicesResource.class, "updateService")
            .build();
        
        return serviceUri.getRawPath();
    }

    static String getUpdateMetadataURL(Integer id, UriInfo uriInfo) {
        URI serviceUri = uriInfo.getBaseUriBuilder()
            .path(ServicesResource.class)
            .path(ServicesResource.class, "updateServiceMetadata")
            .build(id);
        
        return serviceUri.getRawPath();
    }

    static String getCreateURL(UriInfo uriInfo) {
        URI serviceUri = uriInfo.getBaseUriBuilder()
            .path(ServicesResource.class)
            .path(ServicesResource.class, "createService")
            .build();
        
        return serviceUri.getRawPath();
    }
}
