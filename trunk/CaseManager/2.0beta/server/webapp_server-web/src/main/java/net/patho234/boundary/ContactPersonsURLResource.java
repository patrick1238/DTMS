/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.boundary;

import java.net.URI;
import javax.ws.rs.core.UriInfo;
import net.patho234.interfaces.ICase;
import net.patho234.interfaces.IContactPerson;

/**
 *
 * @author HS
 */
public class ContactPersonsURLResource {
    
    static public String getURL(IContactPerson contact, UriInfo uriInfo) {
        return getURL(contact.getId(), uriInfo);
    }
    
    static public String getURL(int contactId, UriInfo uriInfo) {
        URI uri = uriInfo.getBaseUriBuilder()
            .path(ContactPersonResource.class)
            .path(ContactPersonResource.class, "getContactPerson")
            .build(contactId);
        return uri.getRawPath();
    }
    
    static public String getUpdateURL(UriInfo uriInfo) {
        URI uri = uriInfo.getBaseUriBuilder()
            .path(ContactPersonResource.class)
            .path(ContactPersonResource.class, "updateContactPerson")
            .build();
        return uri.getRawPath();
    }
    
    static public String getCreateURL(UriInfo uriInfo) {
        URI uri = uriInfo.getBaseUriBuilder()
            .path(ContactPersonResource.class)
            .path(ContactPersonResource.class, "createContactPerson")
            .build();
        return uri.getRawPath();
    }
}
