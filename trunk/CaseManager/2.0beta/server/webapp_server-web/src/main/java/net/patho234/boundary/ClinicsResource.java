/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.boundary;

import java.util.List;
import org.jboss.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
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
import net.patho234.control.LocalClinicRepository;
import net.patho234.control.LocalContactPersonRepository;
import net.patho234.control.LocalSubmitterRepository;
import net.patho234.entity.ClinicEntity;
import net.patho234.entity.validation.DefaultValidationException;
import net.patho234.interfaces.IClinic;
import net.patho234.interfaces.IContactForClinic;
import net.patho234.interfaces.IContactPerson;
import net.patho234.utils.HttpAccessRequest;
import net.patho234.utils.LocalUUIDManager;

/**
 *
 * @author HS
 */
@Path("/clinicpool")
@Produces(MediaType.APPLICATION_JSON)
public class ClinicsResource {
    final private String CLINICS_URL="/clinics";
    final private String CLINIC_URL="/clinic/{CLINICID}";
    final private String CLINIC_UPDATE_URL="/clinic/update";
    final private String CLINIC_CREATE_URL="/clinic/create";
    final private String CLINIC_DELETE_URL="/clinic/delete";
    
    
    @EJB
    LocalClinicRepository clinicRepo;
    @EJB
    LocalSubmitterRepository submitterRepo;
    @EJB
    LocalContactPersonRepository contactRepo;
    @EJB
    LocalUUIDManager uuidManager;
    
    @Context
    UriInfo uriInfo;
    
    ContactPersonResource contactResource=new ContactPersonResource();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(CLINICS_URL)
    public Response getClinics() {
//        HttpAccessRequest request = new HttpAccessRequest(input);
//        JsonObject submitter = request.getSubmitter();
//        
//        boolean hasAccess=submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"));
//        if( !hasAccess ){
//            return submitterRepo.createNoPermissionResponse( ClinicsURLResource.getURL(id, uriInfo), submitter.getString("login"), "get clinics with id="+id);
//        }
        JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
        for(IClinic c : clinicRepo.getClinics()){
            arrayBuilder.add( getClinicBuilderJson(c) );
        }
        return DefaultResponse.createOKResponse( arrayBuilder.build() );
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(CLINIC_URL)
    public Response getClinic(@PathParam("CLINICID") Integer id) {
//        HttpAccessRequest request = new HttpAccessRequest(input);
//        JsonObject submitter = request.getSubmitter();
//        
//        boolean hasAccess=submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"));
//        if( !hasAccess ){
//            return submitterRepo.createNoPermissionResponse( ClinicsURLResource.getURL(id, uriInfo), submitter.getString("login"), "get clinic with id="+id);
//        }
        IClinic clinicToBuild = clinicRepo.getClinic(id);
        if(clinicToBuild==null){
            System.out.println("clinic is NULL");
        }
        
        return DefaultResponse.createOKResponse( buildClinicJson(clinicToBuild) );
    }
    
    @PUT
    @Path(CLINIC_UPDATE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateClinic(JsonObject input){
        HttpAccessRequest request = new HttpAccessRequest(input);
        JsonObject updateClinic = request.getBodyObject();
        JsonObject submitter = request.getSubmitter();
        String uuid = request.getUUID();
        
        Logger.getLogger(getClass()).log(Logger.Level.INFO, "update for clinic requested.");
        Logger.getLogger(getClass()).log(Logger.Level.INFO, "update object is "+toString(updateClinic));
        boolean hasAccess=submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"));
        if( !hasAccess ){
            return submitterRepo.createNoPermissionResponse( ClinicsURLResource.getUpdateURL(uriInfo), submitter.getString("login"), "update clinic with id="+updateClinic.getInt("id"));
        }
        boolean isAvailableUUID = uuidManager.isAvailableUUID(uuid);
        if( !isAvailableUUID ){
            JsonObject err;
            err = ErrorRepository.createDuplicatedRequestError(CasesURLResource.getUpdateURL(uriInfo), uuid, "update clinic with id="+updateClinic.getInt("id"), uuidManager);
            return DefaultResponse.createNoPermissionResponse(err);
        }
        
        try{ uuidManager.updateUUIDState(uuid, LocalUUIDManager.UUID_PROCESSING); }catch(Exception ex){ ex.printStackTrace(); }
        
        Integer clinicId=updateClinic.getInt("id");
        IClinic clinicToUpdate = clinicRepo.getClinic(clinicId);
        if(clinicToUpdate==null){
            Logger.getLogger("global").log(Logger.Level.WARN, "Update requested for non-existing clinic.");
            JsonObject errObject = ErrorRepository.createTargetNotExisting(ClinicsURLResource.getURL(clinicId, uriInfo), "update clinic[id="+clinicId+"]");
            return DefaultResponse.createUnprocessableEntityResponse(errObject);
        }
        
        Logger.getLogger("global").log(Logger.Level.INFO, "old object is "+toString(clinicToUpdate));
        
        clinicToUpdate.setName(updateClinic.getString("name"));
        clinicToUpdate.setStreet(updateClinic.getString("street"));
        clinicToUpdate.setZipCode(updateClinic.getString("zipCode"));
        clinicToUpdate.setCity(updateClinic.getString("city"));
        
        try{
            clinicRepo.updateClinic(clinicToUpdate);
        }catch(EJBException ejbEx){
            Exception cEx = ejbEx.getCausedByException();
            if (cEx.getClass().equals(DefaultValidationException.class)){
                return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createValidationContraintsViolatedError(ClinicsURLResource.getURL(clinicId, uriInfo), "updating clinic id="+clinicId, ((DefaultValidationException)cEx).getViolations()));
            }
        }
        
        if(updateClinic.getJsonArray("contacts")!=null){
            for(JsonValue contactValue : updateClinic.getJsonArray("contacts")){
                JsonObject contactObj = (JsonObject)contactValue;
                JsonObject contact = contactObj.getJsonObject("contactPerson");
                
                if( !contact.keySet().contains("contacId") ){
                    Logger.getLogger("global").info("Creating new contact for clinic "+updateClinic.getString("name"));
                    Response res=contactResource.createContactPerson(contact);
                    if( res.getStatus()!=202 ){ 
                        Logger.getLogger("global").warn("Could not create contact '"+contact.getString("forename")+" "+contact.getString("surname")+"'");
                    }else{
                        IContactPerson createdContact=contactRepo.getContactPerson(contact.getInt("contactId"));
                        contactRepo.addContactPersonToClinic(createdContact, clinicToUpdate, contact.getString("contactNotes"));
                    }
                }else{
                    IContactPerson createdContact=contactRepo.getContactPerson(contact.getInt("contactId"));
                    contactRepo.addContactPersonToClinic(createdContact, clinicToUpdate, contact.getString("contactNotes"));
                }
            }
        }
        
        return DefaultResponse.createOKResponse( buildClinicJson(clinicToUpdate) );
    }
    
    @PUT
    @Path(CLINIC_CREATE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createClinic(JsonObject input){
        HttpAccessRequest request = new HttpAccessRequest(input);
        JsonObject createClinic = request.getBodyObject();
        JsonObject submitter = request.getSubmitter();
        String uuid = request.getUUID();
        
        Logger.getLogger(getClass()).log(Logger.Level.INFO, "create for clinic requested.");
        Logger.getLogger(getClass()).log(Logger.Level.INFO, "create object is "+toString(createClinic));
        boolean hasAccess=submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"));
        if( !hasAccess ){
            return submitterRepo.createNoPermissionResponse( ClinicsURLResource.getCreateURL(uriInfo), submitter.getString("login"), "create clinic with name="+createClinic.getString("name"));
        }
        boolean isAvailableUUID = uuidManager.isAvailableUUID(uuid);
        if( !isAvailableUUID ){
            JsonObject err;
            err = ErrorRepository.createDuplicatedRequestError(CasesURLResource.getUpdateURL(uriInfo), uuid, "create clinic with name="+createClinic.getString("name"), uuidManager);
            return DefaultResponse.createNoPermissionResponse(err);
        }
        
        try{ uuidManager.updateUUIDState(uuid, LocalUUIDManager.UUID_PROCESSING); }catch(Exception ex){ ex.printStackTrace(); }
        
        Integer clinicId=null;
        try{ clinicId=createClinic.getInt("clinicId"); }catch(NullPointerException ex){  }
        
        IClinic clinicToCreate = (IClinic) new ClinicEntity();
        clinicToCreate.setName(createClinic.getString("name"));
        clinicToCreate.setStreet(createClinic.getString("street"));
        clinicToCreate.setZipCode(createClinic.getString("zipCode"));
        clinicToCreate.setCity(createClinic.getString("city"));
        
        try{
            Logger.getLogger("global").log(Logger.Level.INFO, "clinicRepo.createClinic(clinicToCreate);");
            clinicRepo.createClinic(clinicToCreate);
        }catch(EJBException ejbEx){
            Exception cEx = ejbEx.getCausedByException();
            if (cEx.getClass().equals(DefaultValidationException.class)){
                return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createValidationContraintsViolatedError(CasesURLResource.getURL(clinicToCreate.getId(), uriInfo), "updating clinic id="+clinicToCreate.getId(), ((DefaultValidationException)cEx).getViolations()));
            }
        }
        
        if(createClinic.getJsonArray("contacts")!=null){
            for(JsonValue contactValue : createClinic.getJsonArray("contacts")){
                JsonObject contactObj = (JsonObject)contactValue;
                JsonObject contact = contactObj.getJsonObject("contactPerson");
                if( !contact.keySet().contains("contactId") ){
                    Logger.getLogger("global").info("Creating new contact for clinic "+createClinic.getString("name"));
                    Response res=contactResource.createContactPerson(contact);
                    if( res.getStatus()!=202 ){ 
                        Logger.getLogger("global").warn("Could not create contact '"+contact.getString("forename")+" "+contact.getString("surname")+"'");
                    }else{
                        IContactPerson createdContact=contactRepo.getContactPerson(contact.getInt("contactId"));
                        contactRepo.addContactPersonToClinic(createdContact, clinicToCreate, contact.getString("contactNotes"));
                    }
                }else{
                    IContactPerson createdContact=contactRepo.getContactPerson(contact.getInt("contactId"));
                    contactRepo.addContactPersonToClinic(createdContact, clinicToCreate, contact.getString("contactNotes"));
                }
            }
        }
        
        JsonObject returnValue=buildClinicJson(clinicToCreate);
        return DefaultResponse.createOKResponse( returnValue );
    }

    @DELETE
    @Path(CLINIC_DELETE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteClinic(JsonObject input){
       HttpAccessRequest request = new HttpAccessRequest(input);
        JsonObject deleteClinic = request.getBodyObject();
        JsonObject submitter = request.getSubmitter();
        String uuid = request.getUUID();
        
        Logger.getLogger(getClass()).log(Logger.Level.INFO, "delete for clinic requested.");
        Logger.getLogger(getClass()).log(Logger.Level.INFO, "delete object is "+toString(deleteClinic));
        boolean hasAccess=submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"));
        if( !hasAccess ){
            return submitterRepo.createNoPermissionResponse( ClinicsURLResource.getDeleteURL(uriInfo), submitter.getString("login"), "create clinic with name="+deleteClinic.getInt("id"));
        }
        boolean isAvailableUUID = uuidManager.isAvailableUUID(uuid);
        if( !isAvailableUUID ){
            JsonObject err;
            err = ErrorRepository.createDuplicatedRequestError(CasesURLResource.getUpdateURL(uriInfo), uuid, "create clinic with name="+deleteClinic.getInt("id"), uuidManager);
            return DefaultResponse.createNoPermissionResponse(err);
        }
        
        try{ uuidManager.updateUUIDState(uuid, LocalUUIDManager.UUID_PROCESSING); }catch(Exception ex){ ex.printStackTrace(); }
        
        Integer id = deleteClinic.getInt("id");
        IClinic clinicToDelete = clinicRepo.getClinic(id);
        
        try{
            Logger.getLogger("global").log(Logger.Level.INFO, "clinicRepo.deleteClinic(clinicToDelete);");
            clinicRepo.deleteClinic(clinicToDelete);
            Logger.getLogger("global").log(Logger.Level.INFO, "clinic deleted");
        }catch(Exception ex){
            Logger.getLogger("global").log(Logger.Level.INFO, "returning createNotDeletedError");
            return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createNotDeletedError(CasesURLResource.getURL(clinicToDelete.getId(), uriInfo), "deleting clinic id="+clinicToDelete.getId()));
        }
        Logger.getLogger("global").log(Logger.Level.INFO, "returning OK status");
        JsonObjectBuilder resultBuilder = Json.createObjectBuilder();
        resultBuilder.add("Status", "200: OK");
        return DefaultResponse.createOKResponse( resultBuilder.build() );
    }
    
    public JsonObject buildClinicJson(IClinic clinicToConvert){
        JsonObjectBuilder jsonCaseBuilder=getClinicBuilderJson(clinicToConvert);
        return jsonCaseBuilder.build();
    }
    
    public JsonObjectBuilder getClinicBuilderJson(IClinic clinicToBuild){
        String url = ClinicsURLResource.getURL(clinicToBuild, uriInfo);
        
        JsonArrayBuilder jsonClinicContactsArrayBuilder = Json.createArrayBuilder();
        //List<IContactForClinic> contacts = clinicRepo.getContactsForClinic( clinicToBuild );
        List<IContactForClinic> contacts = clinicToBuild.getContactsForClinicList();
        if(contacts!=null){
            Logger.getLogger("global").log(Logger.Level.INFO, "checking for contacts");
            for(IContactForClinic iCFC : contacts){
                Logger.getLogger("global").log(Logger.Level.INFO, "adding contact: "+iCFC.getContact().getSurname());
                jsonClinicContactsArrayBuilder.add(getClinicContactBuilder(iCFC));
            }
        }else{
            Logger.getLogger("global").log(Logger.Level.INFO, "No contacts found for clinic id="+clinicToBuild.getId());
        }
        JsonObjectBuilder jsonClinicBuilder = Json.createObjectBuilder();
        jsonClinicBuilder.add("id", clinicToBuild.getId())
                .add("name", clinicToBuild.getName())
                .add("street", clinicToBuild.getStreet())
                .add("zipCode", clinicToBuild.getZipCode())
                .add("city", clinicToBuild.getCity())
                .add("contacts", jsonClinicContactsArrayBuilder)
                .add("url", url);
        return jsonClinicBuilder;
    }
    
    public JsonObjectBuilder getClinicContactBuilder(IContactForClinic contact){
        JsonObjectBuilder jsonClinicContactsBuilder = Json.createObjectBuilder();
        jsonClinicContactsBuilder.add("contactClinicId", contact.getClinic().getId());
        jsonClinicContactsBuilder.add("contactNotes", contact.getNotes());
        
        jsonClinicContactsBuilder.add("contactPerson", getClinicContactPersonBuilder(contact.getContact()));
        return jsonClinicContactsBuilder;
    }
    
    public JsonObjectBuilder getClinicContactPersonBuilder(IContactPerson contact){
        JsonObjectBuilder jsonClinicContactPersonBuilder = Json.createObjectBuilder();
        jsonClinicContactPersonBuilder.add("contactId", contact.getId())
                .add("contactForename", contact.getForename())
                .add("contactSurname", contact.getSurname())
                .add("contactTitle", contact.getTitle())
                .add("contactPhone", contact.getPhone())
                .add("contactEmail", contact.getEmail());
        return jsonClinicContactPersonBuilder;
    }
    
    public String toString(JsonObject clinic) {
        StringBuilder sb=new StringBuilder("Clinic[");
        sb.append(clinic.getInt("id")).append("]=");
        //values
        sb.append( "\tname: " ).append( clinic.getString( "name" ));
        sb.append( "\tstreet " ).append( clinic.getString( "street" ));
        sb.append( "\tzipcode: " ).append( clinic.getString( "zipCode" ));
        sb.append( "\tcity: " ).append( clinic.getString( "city" ));
        
        return sb.toString();
    }
    
    public String toString(IClinic clinic) {
        StringBuilder sb=new StringBuilder("Clinic[");
        sb.append(clinic.getId()).append("]=");
        //values
        sb.append( "\tname: " ).append( clinic.getName());
        sb.append( "\tstreet " ).append( clinic.getStreet());
        sb.append( "\tzipCode: " ).append( clinic.getZipCode());
        sb.append( "\tcity: " ).append( clinic.getCity());
        
        return sb.toString();
        
    }
}
