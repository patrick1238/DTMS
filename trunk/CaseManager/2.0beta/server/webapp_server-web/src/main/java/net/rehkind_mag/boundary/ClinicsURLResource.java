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

    static String getCreateURL(UriInfo uriInfo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static String getUpdateURL(UriInfo uriInfo) {
        URI uri = uriInfo.getBaseUriBuilder()
            .path(ClinicsResource.class)
            .path(ClinicsResource.class, "updateClinic")
            .build();
        return uri.getRawPath();
    }

    static String getDeleteURL(UriInfo uriInfo) {
        URI uri = uriInfo.getBaseUriBuilder()
            .path(ClinicsResource.class)
            .path(ClinicsResource.class, "deleteClinic")
            .build();
        return uri.getRawPath();
    }

    static String getCasesURL(UriInfo uriInfo) {
        URI uri = uriInfo.getBaseUriBuilder()
            .path(ClinicsResource.class)
            .path(ClinicsResource.class, "getCases")
            .build();
        return uri.getRawPath();
    }
}
