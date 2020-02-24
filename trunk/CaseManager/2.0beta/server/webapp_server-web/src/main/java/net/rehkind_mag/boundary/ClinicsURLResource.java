/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.boundary;

import java.net.URI;
import javax.ws.rs.core.UriInfo;
import net.rehkind_mag.interfaces.IClinic;

/**
 *
 * @author HS
 */
public class ClinicsURLResource {
    static public String getURL(IClinic aClinic, UriInfo uriInfo) {
        return getURL(aClinic.getId(), uriInfo);
    }

    static String getURL(Integer clinicId, UriInfo uriInfo) {
        URI uri = uriInfo.getBaseUriBuilder()
            .path(ClinicsResource.class)
            .path(ClinicsResource.class, "getClinic")
            .build(clinicId);
        return uri.getRawPath();
    }
}
