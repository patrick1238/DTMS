/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.boundary;

import java.net.URI;
import javax.ws.rs.core.UriInfo;
import net.rehkind_mag.interfaces.IMetadataValue;

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
        URI serviceUri = uriInfo.getBaseUriBuilder()
            .path(MetadataValuesResource.class)
            .path(MetadataValuesResource.class, "getMetadataValue")
            .build(metaId);
        
        return serviceUri.getRawPath();
    }
    

}
