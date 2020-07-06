/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.boundary;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.UriInfo;
import net.patho234.interfaces.ISubmitter;

/**
 *
 * @author HS
 */
public class SubmittersURLResource {

    static public String getURL(ISubmitter aSupplier, UriInfo uriInfo) {
        return getURL(aSupplier.getId(), uriInfo);
    }
    
    static public String getURL(Integer supplierId, UriInfo uriInfo) {
        URI uri = uriInfo.getBaseUriBuilder()
            .path(SubmittersResource.class)
            .path(SubmittersResource.class, "getSubmitter")
            .build(supplierId);
        return uri.getRawPath();
    }
    
}
