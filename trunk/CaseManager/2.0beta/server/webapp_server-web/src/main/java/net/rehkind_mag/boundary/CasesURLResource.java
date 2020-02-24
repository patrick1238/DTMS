/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.boundary;

import java.net.URI;
import javax.ws.rs.core.UriInfo;
import net.rehkind_mag.interfaces.ICase;

/**
 *
 * @author HS
 */
public class CasesURLResource {
    
    static public String getURL(ICase aCase, UriInfo uriInfo) {
        return getURL(aCase.getId(), uriInfo);
    }
    
    static public String getURL(int caseId, UriInfo uriInfo) {
        URI uri = uriInfo.getBaseUriBuilder()
            .path(CasesResource.class)
            .path(CasesResource.class, "getCase")
            .build(caseId);
        return uri.getRawPath();
    }
    
        static public String getUpdateURL(UriInfo uriInfo) {
        URI uri = uriInfo.getBaseUriBuilder()
            .path(CasesResource.class)
            .path(CasesResource.class, "updateCase")
            .build();
        return uri.getRawPath();
    }
}
