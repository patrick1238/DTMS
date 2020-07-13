/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.boundary;

import java.net.URI;
import javax.ws.rs.core.UriInfo;
import net.patho234.interfaces.IMetadataValue;

/**
 *
 * @author rehkind
 */
class MetadataValuesURLResource {
    static public String getUpdateURL(UriInfo uriInfo){
        URI serviceUri = uriInfo.getBaseUriBuilder()
            .path(MetadataValuesResource.class)
            .path(MetadataValuesResource.class, "updateMetadataValue")
            .build();
        return serviceUri.getRawPath();
    }
    static public String getCreateURL(UriInfo uriInfo){
        URI serviceUri = uriInfo.getBaseUriBuilder()
            .path(MetadataValuesResource.class)
            .path(MetadataValuesResource.class, "createMetadataValue")
            .build();
        return serviceUri.getRawPath();
    }
   
    static public String getURL(IMetadataValue metadataValue, UriInfo uriInfo){
        return MetadataValuesURLResource.getURL(metadataValue.getId(), uriInfo);
    }
    
    static public String getURL(int metaId, UriInfo uriInfo){
        if(uriInfo==null){ return ""; }
        
        URI serviceUri = uriInfo.getBaseUriBuilder()
            .path(MetadataValuesResource.class)
            .path(MetadataValuesResource.class, "getMetadataValue")
            .build(metaId);
        
        return serviceUri.getRawPath();
    }
    

}
