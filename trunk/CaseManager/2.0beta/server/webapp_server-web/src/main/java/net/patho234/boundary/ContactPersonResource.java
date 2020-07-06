/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.boundary;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import net.patho234.boundary.utils.DefaultResponse;
import net.patho234.control.ErrorRepository;
import net.patho234.control.LocalContactPersonRepository;
import net.patho234.control.LocalSubmitterRepository;
import net.patho234.entity.ContactPersonEntity;
import net.patho234.entity.validation.DefaultValidationException;
import net.patho234.interfaces.IContactPerson;
import org.jboss.logging.Logger;

/**
 *
 * @author HS
 */
@Path("/contactpool")
@Produces(MediaType.APPLICATION_JSON)
public class ContactPersonResource {
    final private String CONTACTS_URL="/contacts";
    final private String CONTACTS_FOR_CLINIC_URL="/contacts/forclinic/{CLINICID}";
    final private String CONTACT_URL="/contact/{CONTACTID}";
    final private String CONTACT_UPDATE_URL="/contact/update";
    final private String CONTACT_CREATE_URL="/contact/create";
    final private String CONTACT_DELETE_URL="/contact/delete";
    
    @Context
    UriInfo uriInfo;
    
    @EJB
    LocalContactPersonRepository contactRepo;
    @EJB
    LocalSubmitterRepository submitterRepo;
    
    public ContactPersonResource(){  }
    
    public ContactPersonResource(UriInfo uri){
        uriInfo=uri;
    }
    
    @GET
    @Path(CONTACTS_URL)
    public Response getContactPersons() {
        List<IContactPerson> allContacts = contactRepo.getContactPersons();
        JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
        
        for( IContactPerson cp : allContacts ){
            arrayBuilder.add(getContactBuilderJson(cp));
        }
        
        return DefaultResponse.createOKResponse( arrayBuilder.build() );
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(CONTACT_URL)
    public Response getContactPerson(@PathParam("CONTACTID") Integer id) {
        IContactPerson getResult=contactRepo.getContactPerson(id);
        
        if(getResult==null){
            Logger.getLogger("global").warn("Could not find requested ContactPerson with id="+id);
            return DefaultResponse.createNotFoundResponse( ErrorRepository.createNotFoundError( ContactPersonsURLResource.getURL(id, uriInfo), "Get ContactPerson id="+id ) );
        }
        
        return DefaultResponse.createOKResponse( buildContactJson(getResult) );
    }
    
    @PUT
    @Path(CONTACT_UPDATE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateContactPerson(JsonArray input){
        JsonObject updatedContact = input.getJsonObject(0);
        JsonObject submitter = input.getJsonObject(1);
        
        Logger.getLogger("global").log(Logger.Level.INFO, "update for case requested by submitter: "+submitter.getString("login")+".");
        Logger.getLogger("global").log(Logger.Level.INFO, "updated object is "+toString(updatedContact));
        
        
        if (!submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"))){
            JsonObject err;
            err = ErrorRepository.createNoPermissionError(CasesURLResource.getUpdateURL(uriInfo), submitter.getString("login"), "update contact with id="+updatedContact.getInt("id"));
            return DefaultResponse.createNoPermissionResponse(err);
        }
        
        Integer contactId=null;
        try{ contactId=updatedContact.getInt("id"); }catch(NullPointerException ex){  }
        
        IContactPerson contactToUpdate = contactRepo.getContactPerson(contactId);
        if( contactToUpdate==null ){
            return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createTargetNotExisting(ContactPersonsURLResource.getURL(contactId, uriInfo), "update ContactPerson "+contactId));
        }
        
        Logger.getLogger("global").log(Logger.Level.INFO, "old object is "+toString(contactToUpdate));
        
        contactToUpdate.setTitle( updatedContact.getString("title") );
        contactToUpdate.setForename( updatedContact.getString("forename") );
        contactToUpdate.setSurname( updatedContact.getString("surname") );
        contactToUpdate.setPhone( updatedContact.getString("phone") );
        contactToUpdate.setEmail( updatedContact.getString("email") );
        
        try{
            Logger.getLogger("global").info("contactRepo.updateContact(contactToUpdate);");
            contactRepo.updateContactPerson(contactToUpdate);
        }catch(EJBException ejbEx){
            Exception cEx = ejbEx.getCausedByException();
            if (cEx.getClass().equals(DefaultValidationException.class)){
                return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createValidationContraintsViolatedError(ContactPersonsURLResource.getURL(contactId, uriInfo), "updating ContactPerson id="+contactId, ((DefaultValidationException)cEx).getViolations()));
            }
        }
        JsonObject returnValue=buildContactJson(contactToUpdate);
        return DefaultResponse.createOKResponse( returnValue );
    }
    
    @PUT
    @Path(CONTACT_CREATE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createContactPerson(JsonArray input){
        JsonObject createContact = input.getJsonObject(0);
        JsonObject submitter = input.getJsonObject(1);
        
        Logger.getLogger("global").log(Logger.Level.INFO, "create new case for submitter: "+submitter.getString("login")+".");
        Logger.getLogger("global").log(Logger.Level.INFO, "new object is "+toString(createContact));
        
        
        if (!submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"))){
            JsonObject err;
            err = ErrorRepository.createNoPermissionError(ContactPersonsURLResource.getCreateURL(uriInfo), submitter.getString("login"), "create new contact");
            return DefaultResponse.createNoPermissionResponse(err);
        }
        
        return createContactPerson(createContact);
        
    }
    
    public Response createContactPerson(JsonObject createContact){
        IContactPerson contactToCreate = new ContactPersonEntity();
        
        contactToCreate.setTitle( createContact.getString("title") );
        contactToCreate.setForename( createContact.getString("forename") );
        contactToCreate.setSurname( createContact.getString("surname") );
        contactToCreate.setPhone( createContact.getString("phone") );
        contactToCreate.setEmail( createContact.getString("email") );
        
        try{
            Logger.getLogger("global").info("contactRepo.updateContact(contactToUpdate);");
            contactRepo.createContactPerson(contactToCreate);
        }catch(EJBException ejbEx){
            Exception cEx = ejbEx.getCausedByException();
            if (cEx.getClass().equals(DefaultValidationException.class)){
                return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createValidationContraintsViolatedError(ContactPersonsURLResource.getURL(-1, uriInfo), "create new ContactPerson", ((DefaultValidationException)cEx).getViolations()));
            }
        }
        JsonObject returnValue=buildContactJson(contactToCreate);
        return DefaultResponse.createOKResponse( returnValue );
    }
    
    @DELETE
    @Path(CONTACT_DELETE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteContactPerson(JsonArray input){
        JsonObject deleteContact = input.getJsonObject(0);
        JsonObject submitter = input.getJsonObject(1);
        
        Integer id = deleteContact.getInt("id");
        
        Logger.getLogger("global").log(Logger.Level.INFO, "delete contact requested by submitter: "+submitter.getString("login")+".");
        Logger.getLogger("global").log(Logger.Level.INFO, "delete requested for "+toString(deleteContact));
        
        if (!submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"))){
            JsonObject err;
            err = ErrorRepository.createNoPermissionError(CasesURLResource.getUpdateURL(uriInfo), submitter.getString("login"), "delete contact with id="+id);
            return DefaultResponse.createNoPermissionResponse(err);
        }
        
        Logger.getLogger("global").log(Logger.Level.INFO, "Delete for ContactPerson[id="+id+"] requested.");
        
        IContactPerson contactToDelete = contactRepo.getContactPerson(id);
        
        try{
            Logger.getLogger("global").log(Logger.Level.INFO, "contactRepo.deleteContact(contactToDelete);");
            contactRepo.deleteContactPerson(contactToDelete);
            Logger.getLogger("global").log(Logger.Level.INFO, "contact deleted");
        }catch(Exception ex){
            Logger.getLogger("global").log(Logger.Level.INFO, "returning createNotDeletedError");
            return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createNotDeletedError(CasesURLResource.getURL(contactToDelete.getId(), uriInfo), "deleting ContactPerson id="+contactToDelete.getId()));
        }
        
        Logger.getLogger("global").log(Logger.Level.INFO, "returning OK status");
        JsonObjectBuilder resultBuilder = Json.createObjectBuilder();
        resultBuilder.add("Status", "200: OK");
        return DefaultResponse.createOKResponse( resultBuilder.build() );
    }
    
    
    public JsonObject buildContactJson(IContactPerson contactToConvert){
        JsonObjectBuilder jsonCaseBuilder=getContactBuilderJson(contactToConvert);
        return jsonCaseBuilder.build() ;
    }
    
    
    public JsonObjectBuilder getContactBuilderJson(IContactPerson contactToBuild){
        String url = ContactPersonsURLResource.getURL(contactToBuild, uriInfo);
        
        JsonObjectBuilder jsonUserBuilder = Json.createObjectBuilder();
        jsonUserBuilder
                .add("id", contactToBuild.getId())
                .add("title", contactToBuild.getTitle())
                .add("forename", contactToBuild.getForename() )
                .add("surname", contactToBuild.getSurname() )
                .add("phone", contactToBuild.getPhone() )
                .add("email", contactToBuild.getEmail() )
                .add("url", url);
        return jsonUserBuilder;
    }
    
    
    public String toString(JsonObject contact) {

        StringBuilder sb=new StringBuilder("ContactPerson[");
        sb.append((contact.keySet().contains("id"))?contact.getInt("id"):"??").append("]=");
        //values
        sb.append( "\ttitle: " ).append( contact.getString("title") );
        sb.append( "\tforename: " ).append( contact.getString("forename") );
        sb.append( "\tsurname: " ).append( contact.getString("surname") );
        sb.append( "\tphone: " ).append( contact.getString("phone") );
        sb.append( "\temail: " ).append( contact.getString("email") );
        return sb.toString();
    }
    
    public String toString( IContactPerson contact ) {
        Integer id = contact.getId();
        StringBuilder sb=new StringBuilder("ContactPerson[");
        sb.append( contact.getId() ).append( "]=" );
        //values
        sb.append( "\ttitle: " ).append( contact.getTitle() );
        sb.append( "\tforename: " ).append( contact.getForename() );
        sb.append( "\tsurname: " ).append( contact.getSurname() );
        sb.append( "\tphone: " ).append( contact.getPhone() );
        sb.append( "\temail: " ).append( contact.getEmail());
        return sb.toString();
    }
}
