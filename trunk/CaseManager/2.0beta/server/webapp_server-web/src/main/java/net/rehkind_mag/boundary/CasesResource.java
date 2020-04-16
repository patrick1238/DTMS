/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.boundary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
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
import net.rehkind_mag.boundary.utils.DefaultResponse;
import net.rehkind_mag.control.ErrorRepository;
import net.rehkind_mag.control.LocalCaseRepository;
import net.rehkind_mag.control.LocalClinicRepository;
import net.rehkind_mag.control.LocalServiceRepository;
import net.rehkind_mag.control.LocalSubmitterRepository;
import net.rehkind_mag.entity.CaseEntity;
import net.rehkind_mag.entity.validation.DefaultValidationException;
import net.rehkind_mag.interfaces.ICase;
import net.rehkind_mag.utils.HttpAccessRequest;
import net.rehkind_mag.utils.LocalUUIDManager;
import org.jboss.logging.Logger;

/**
 *
 * @author HS
 */
@Path("/casepool")
@Produces(MediaType.APPLICATION_JSON)
public class CasesResource {
    final private String CASES_URL="/cases";
    final private String CASES_FOR_CLINIC_URL="/cases/forclinic/{CLINICID}";
    final private String CASE_URL="/case/{CASEID}";
    final private String CASE_UPDATE_URL="/case/update";
    final private String CASE_CREATE_URL="/case/create";
    final private String CASE_DELETE_URL="/case/delete";
    final private String CASE_GET_CASENUMBER_URL="casepool/case/casenumber/{CaseNumber}";
    
    final private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
    
    @EJB
    LocalCaseRepository caseRepo;
    @EJB
    LocalServiceRepository serviceRepo;
    @EJB
    LocalClinicRepository clinicRepo;
    @EJB
    LocalSubmitterRepository submitterRepo;
    @EJB
    LocalUUIDManager uuidManager;
    
    CasesURLResource caseURLResource;
    ServicesURLResource serviceURLResource;
    
    @Context
    UriInfo uriInfo;
    
    public CasesResource(){
        df.setTimeZone(TimeZone.getTimeZone("MEZ"));
    }
    
    public CasesResource(UriInfo uriInfo){
        this.uriInfo=uriInfo;
        df.setTimeZone(TimeZone.getTimeZone("MEZ"));
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path(CASES_FOR_CLINIC_URL)
    public Response getCasesForClinic(@PathParam("CLINICID") int id, JsonObject input) {
        HttpAccessRequest request = new HttpAccessRequest(input);
        JsonObject submitter = request.getSubmitter();
        
        boolean hasAccess=submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"));
        if( !hasAccess ){
            return submitterRepo.createNoPermissionResponse( CasesURLResource.getCasesForClinicURL(id, uriInfo), submitter.getString("login"), "get cases for clinic with id="+id);
        }
        
        JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
        
        List<ICase> filteredCases = caseRepo.getCasesForClinic(id);
        System.out.println("No. of found cases for clinic: "+filteredCases.size());
        for(ICase c : filteredCases ){
            arrayBuilder.add( getCaseBuilderJson(c) );
        }
        return DefaultResponse.createOKResponse(arrayBuilder.build());
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path(CASES_URL)
    public Response getCases() {
//        HttpAccessRequest request = new HttpAccessRequest(input);
//        JsonObject submitter = request.getSubmitter();
//        
//        boolean hasAccess=submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"));
//        if( !hasAccess ){
//            return submitterRepo.createNoPermissionResponse( ClinicsURLResource.getCasesURL(uriInfo), submitter.getString("login"), "get cases");
//        }
        
        JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
        for(ICase c : caseRepo.getCases()){
            arrayBuilder.add( getCaseBuilderJson(c) );
        }
        
        return DefaultResponse.createOKResponse( arrayBuilder.build() );
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(CASE_URL)
    public Response getCase(@PathParam("CASEID") Integer id) {
        Logger.getLogger(getClass()).info("Get case id="+id+" called.");
//        HttpAccessRequest request = new HttpAccessRequest(input);
//        JsonObject submitter = request.getSubmitter();
//        
//        boolean hasAccess=submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"));
//        if( !hasAccess ){
//            return submitterRepo.createNoPermissionResponse( CasesURLResource.getURL(id, uriInfo), submitter.getString("login"), "get cases");
//        }
        ICase caseToBuild = caseRepo.getCase(id);
        if(caseToBuild==null){
            JsonObject response = ErrorRepository.createNotFoundError(CasesURLResource.getURL(id, uriInfo), "@GET Case with id="+id);
            DefaultResponse.createNotFoundResponse(response);
        }
        
        return DefaultResponse.createOKResponse( buildCaseJson(caseToBuild) );
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(CASE_GET_CASENUMBER_URL)
    public Response getCaseByCaseNumber(@PathParam("CASE_NUMBER") String caseNumber) {
        Logger.getLogger(getClass()).info("Get case case_number="+caseNumber+" called.");
//        HttpAccessRequest request = new HttpAccessRequest(input);
//        JsonObject submitter = request.getSubmitter();
//        
//        boolean hasAccess=submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"));
//        if( !hasAccess ){
//            return submitterRepo.createNoPermissionResponse( CasesURLResource.getURL(id, uriInfo), submitter.getString("login"), "get cases");
//        }
        ICase caseToBuild = caseRepo.getCaseByCaseNumber(caseNumber);
        if(caseToBuild==null){
            JsonObject response = ErrorRepository.createNotFoundError(CasesURLResource.getCaseNumberURL(caseNumber, uriInfo), "@GET Case with caseNumber="+caseNumber);
            DefaultResponse.createNotFoundResponse(response);
        }
        
        return DefaultResponse.createOKResponse( buildCaseJson(caseToBuild) );
    }
    
    @PUT
    @Path(CASE_UPDATE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCase(JsonObject input){
        HttpAccessRequest request = new HttpAccessRequest(input);
        JsonObject updatedCase = request.getBodyObject();
        JsonObject submitter = request.getSubmitter();
        String uuid = request.getUUID();
        
        Logger.getLogger("global").log(Logger.Level.INFO, "update for case requested.");
        Logger.getLogger("global").log(Logger.Level.INFO, "updated object is "+toString(updatedCase));
        
        boolean hasAccess=submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"));
        if( !hasAccess ){
            return submitterRepo.createNoPermissionResponse(CasesURLResource.getUpdateURL(uriInfo), submitter.getString("login"), "update case with id="+updatedCase.getInt("id"));
        }
        
        boolean isAvailableUUID = uuidManager.isAvailableUUID(uuid);
        if( !isAvailableUUID ){
            JsonObject err;
            err = ErrorRepository.createDuplicatedRequestError(CasesURLResource.getUpdateURL(uriInfo), uuid, "update case with id="+updatedCase.getInt("id"), uuidManager);
            return DefaultResponse.createNoPermissionResponse(err);
        }
        
        try{ uuidManager.updateUUIDState(uuid, LocalUUIDManager.UUID_PROCESSING); }catch(Exception ex){ ex.printStackTrace(); }
        
        
        Integer caseId=Integer.MIN_VALUE;
        try{ caseId=updatedCase.getInt("id"); }catch(NullPointerException ex){  }
        Integer clinicId=null;
        try{ clinicId=updatedCase.getInt("clinicId"); }catch(NullPointerException ex){  }
        Integer submitterId=null;
        try{ submitterId=updatedCase.getInt("submitterId"); }catch(NullPointerException ex){  }
        
        ICase caseToUpdate = caseRepo.getCase(caseId);
        if(caseToUpdate==null){
            return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createTargetNotExisting(CasesURLResource.getURL(caseId, uriInfo), "update Case "+caseId));
        }
        Logger.getLogger("global").log(Logger.Level.INFO, "old object is "+toString(caseToUpdate));
        caseToUpdate.setCaseNumber(updatedCase.getString("caseNumber"));
        
        if(clinicId!=null){
            caseToUpdate.setClinic(clinicRepo.getClinic(clinicId));
        }else{
            caseToUpdate.setClinic(null);
        }
        if(submitterId!=null){
            caseToUpdate.setSubmitter(submitterRepo.getSubmitter(submitterId));
        }else{
            caseToUpdate.setSubmitter(null);
        }
        
        String diagnose = updatedCase.getString("diagnose");
        caseToUpdate.setDiagnose(diagnose);
        String dateAsString = updatedCase.getString("entryDate");
        Date date=null;
        try{
            //Date d=new Date(119, 5, 23, 10, 15, 11);
            // Logger.getLogger("global").info( "example date: "+df.format(d));
            date=df.parse(dateAsString);
            
        }catch(ParseException pEx){
            Logger.getLogger("global").info("Could not parse entryDate: '"+dateAsString+"'");
        }
        
        caseToUpdate.setEntryDate(date);
        
        try{
            System.out.println("caseRepo.updateCase(caseToUpdate);");
            caseRepo.updateCase(caseToUpdate);
        }catch(EJBException ejbEx){
            Exception cEx = ejbEx.getCausedByException();
            if (cEx.getClass().equals(DefaultValidationException.class)){
                return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createValidationContraintsViolatedError(CasesURLResource.getURL(caseId, uriInfo), "updating case id="+caseId, ((DefaultValidationException)cEx).getViolations()));
            }
        }
        JsonObject returnValue=buildCaseJson(caseToUpdate);
        try{ uuidManager.updateUUIDState(uuid, LocalUUIDManager.UUID_CLOSED); }catch(Exception ex){ ex.printStackTrace(); }
        return DefaultResponse.createOKResponse( returnValue );
    }

    @PUT
    @Path(CASE_CREATE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCase(JsonObject input) {
        HttpAccessRequest request = new HttpAccessRequest(input);
        JsonObject createCase = request.getBodyObject();
        JsonObject submitter = request.getSubmitter();
        String uuid = request.getUUID();
        
        Logger.getLogger("global").log(Logger.Level.INFO, "create for case requested.");
        Logger.getLogger("global").log(Logger.Level.INFO, "create object is "+toString(createCase));
        
        boolean hasAccess=submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"));
        if( !hasAccess ){
            return submitterRepo.createNoPermissionResponse(CasesURLResource.getUpdateURL(uriInfo), submitter.getString("login"), "create case with case number="+createCase.getString("caseNumber"));
        }
        
        boolean isAvailableUUID = uuidManager.isAvailableUUID(uuid);
        if( !isAvailableUUID ){
            JsonObject err;
            err = ErrorRepository.createDuplicatedRequestError(CasesURLResource.getCreateURL(uriInfo), uuid, "create case with case number="+createCase.getString("caseNumber"), uuidManager);
            return DefaultResponse.createNoPermissionResponse(err);
        }
        try{ uuidManager.updateUUIDState(uuid, LocalUUIDManager.UUID_PROCESSING); }catch(Exception ex){ ex.printStackTrace(); }
        
        Integer clinicId=null;
        try{ clinicId=createCase.getInt("clinicId"); }catch(NullPointerException ex){  }
        Integer submitterId=null;
        try{ submitterId=createCase.getInt("submitterId"); }catch(NullPointerException ex){  }
        
        ICase caseToCreate = (ICase) new CaseEntity();
        caseToCreate.setCaseNumber(createCase.getString("caseNumber"));
        
        if(clinicId!=null){
            caseToCreate.setClinic(clinicRepo.getClinic(clinicId));
        }else{
            caseToCreate.setClinic(null);
        }
        if(submitterId!=null){
            caseToCreate.setSubmitter(submitterRepo.getSubmitter(submitterId));
        }else{
            caseToCreate.setSubmitter(null);
        }
        String entryDate=createCase.getString("entryDate");
        Date date=null;
        if( entryDate != null ){
            try {
                date=df.parse(entryDate);
            } catch (ParseException ex) {
                java.util.logging.Logger.getGlobal().log(Level.WARNING, "Date format couldn't be parsed: '"+entryDate+"'", ex);
                return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createFormatException(CASE_CREATE_URL, "create new case", "Date"));
            }
        }
        caseToCreate.setEntryDate(date);
        
        String diagnose=createCase.getString("diagnose");
        caseToCreate.setDiagnose(diagnose);
        
        try{
            Logger.getLogger("global").log(Logger.Level.INFO, "caseRepo.createCase(caseToCreate);");
            caseRepo.createCase(caseToCreate);
        }catch(EJBException ejbEx){
            Exception cEx = ejbEx.getCausedByException();
            if (cEx.getClass().equals(DefaultValidationException.class)){
                Response r = DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createValidationContraintsViolatedError(CasesURLResource.getURL(caseToCreate.getId(), uriInfo), "updating case id="+caseToCreate.getId(), ((DefaultValidationException)cEx).getViolations()));
                DefaultValidationException castEx = (DefaultValidationException)cEx;
                castEx.getViolations().forEach( (violation) -> { System.out.println("[VIOLATION]: "+ violation.getMessage()); } );
                return r;
            }
        }
        JsonObject returnValue=buildCaseJson(caseToCreate);
        
        return DefaultResponse.createOKResponse( returnValue );
    }

    @DELETE
    @Path(CASE_DELETE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteCase(JsonObject input){
        HttpAccessRequest request = new HttpAccessRequest(input);
        JsonObject deleteCase = request.getBodyObject();
        JsonObject submitter = request.getSubmitter();
        String uuid = request.getUUID();
        
        Logger.getLogger(getClass()).log(Logger.Level.INFO, "delete for case requested.");
        Logger.getLogger(getClass()).log(Logger.Level.INFO, "delete object is "+toString(deleteCase));
        
        boolean hasAccess=submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"));
        if( !hasAccess ){
            return submitterRepo.createNoPermissionResponse(CasesURLResource.getDeleteURL(uriInfo), submitter.getString("login"), "delete case with id="+deleteCase.getInt("id"));
        }
        
        boolean isAvailableUUID = uuidManager.isAvailableUUID(uuid);
        if( !isAvailableUUID ){
            JsonObject err;
            err = ErrorRepository.createDuplicatedRequestError(CasesURLResource.getCreateURL(uriInfo), uuid, "delete case with case number="+deleteCase.getInt("id"), uuidManager);
            return DefaultResponse.createNoPermissionResponse(err);
        }
        try{ uuidManager.updateUUIDState(uuid, LocalUUIDManager.UUID_PROCESSING); }catch(Exception ex){ ex.printStackTrace(); }
        
        Integer id = deleteCase.getInt("id");
        
        ICase caseToDelete = caseRepo.getCase(id);
        
        try{
            Logger.getLogger("global").log(Logger.Level.INFO, "caseRepo.deleteCase(caseToDelete);");
            caseRepo.deleteCase(caseToDelete);
            Logger.getLogger("global").log(Logger.Level.INFO, "case deleted");
        }catch(Exception ex){
            Logger.getLogger("global").log(Logger.Level.INFO, "returning createNotDeletedError");
            return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createNotDeletedError(CasesURLResource.getURL(caseToDelete.getId(), uriInfo), "deleting case id="+caseToDelete.getId()));
        }
        Logger.getLogger("global").log(Logger.Level.INFO, "returning OK status");
        JsonObjectBuilder resultBuilder = Json.createObjectBuilder();
        resultBuilder.add("Status", "200: OK");
        return DefaultResponse.createOKResponse( resultBuilder.build() );
    }
    
    public JsonObject buildCaseJson(ICase caseToConvert){
        JsonObjectBuilder jsonCaseBuilder=getCaseBuilderJson(caseToConvert);
        return jsonCaseBuilder.build() ;
    }
    
    
    public JsonObjectBuilder getCaseBuilderJson(ICase caseToBuild){
        String url = CasesURLResource.getURL(caseToBuild, uriInfo);
        String urlClinic = ClinicsURLResource.getURL(caseToBuild.getClinic(), uriInfo);
        JsonObjectBuilder jsonCaseServicesBuilder = ServicesURLResource.getServiceURLsBuilderForCase(caseToBuild, uriInfo, serviceRepo);
        
        JsonObjectBuilder jsonUserBuilder = Json.createObjectBuilder();
        jsonUserBuilder.add("caseNumber", caseToBuild.getCaseNumber())
                .add("id", caseToBuild.getId())
                .add("caseNumber", caseToBuild.getCaseNumber())
                .add("entryDate", ((caseToBuild.getEntryDate()!=null)?df.format( caseToBuild.getEntryDate() ):""))
                .add("diagnose", (caseToBuild.getDiagnose()!=null)?caseToBuild.getDiagnose():"")
                .add("submitterId",caseToBuild.getSubmitter().getId() )
                .add("clinicId", caseToBuild.getClinic().getId() )
                .add("caseServices", jsonCaseServicesBuilder)
                .add("url", url)
                .add("urlClinic", urlClinic);
        return jsonUserBuilder;
    }
    
    
    public String toString(JsonObject aCase) {
        StringBuilder sb=new StringBuilder("Case[");
        System.out.println("toString(): "+aCase.toString());
        sb.append(aCase.getInt("id")).append("]=");
        //values
        sb.append("\tcase number: ").append(aCase.getString("caseNumber"));
        sb.append("\tclinic: ").append(aCase.getInt("clinicId"));
        sb.append("\tentry date: ").append(
                (aCase.getString("entryDate")!=null) ? aCase.getString("entryDate") : " - " );
        sb.append("\tdiagnosis: ").append(
                (aCase.getString("diagnose")!=null) ? aCase.getString("diagnose") : "-" );
        sb.append("\tsubmitter: ").append( aCase.getInt("submitterId"));
        
        return sb.toString();
    }
    
    public String toString(ICase aCase) {
        StringBuilder sb=new StringBuilder("Case[");
        sb.append(aCase.getId()).append("]=");
        //values
        sb.append("\tcase number: ").append(aCase.getCaseNumber());
        sb.append("\tclinic: ").append(aCase.getClinic());
        sb.append("\tentry date: ").append(
                (aCase.getEntryDate() != null) ? df.format( aCase.getEntryDate() ) : "-" );
        sb.append("\tdiagnosis: ").append(
                (aCase.getDiagnose() != null) ? aCase.getDiagnose() : "-" );
        sb.append("\tsubmitter: ").append(
                (aCase.getSubmitter()!=null) ? aCase.getSubmitter().getId() : "-" );
        return sb.toString();
    }
}
