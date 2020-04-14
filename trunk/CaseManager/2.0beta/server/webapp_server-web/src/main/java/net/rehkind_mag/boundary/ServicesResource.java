/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.boundary;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import net.rehkind_mag.boundary.utils.DefaultResponse;
import net.rehkind_mag.control.ErrorRepository;
import net.rehkind_mag.control.LocalCaseRepository;
import net.rehkind_mag.control.LocalMetadataRepository;
import net.rehkind_mag.control.LocalServiceDefinitionRepository;
import net.rehkind_mag.control.LocalServiceRepository;
import net.rehkind_mag.interfaces.ICase;
import net.rehkind_mag.interfaces.IMetadata;
import net.rehkind_mag.interfaces.IMetadataValue;
import net.rehkind_mag.interfaces.IService;
import net.rehkind_mag.interfaces.IServiceDefinition;
import net.rehkind_mag.control.LocalSubmitterRepository;
import net.rehkind_mag.entity.ServiceEntity;
import net.rehkind_mag.entity.validation.DefaultValidationException;
import net.rehkind_mag.utils.HttpAccessRequest;
import net.rehkind_mag.utils.LocalUUIDManager;
import org.jboss.logging.Logger;

/**
 *
 * @author HS
 */
@Path("/servicepool")
@Produces(MediaType.APPLICATION_JSON)
public class ServicesResource {
    static final String SERVICE_URL="/service/{SERVICEID}";
    static final String SERVICE_UPDATE_URL="/service/update";
    static final String SERVICE_DELETE_URL="/service/delete";
    static final String SERVICE_CREATE_URL="/service/create";
    static final String SERVICES_FOR_CASE_URL="/services/forcase/{CASEID}";
    static final String SERVICES_FOR_DEFINITION_URL="/services/fordef/{DEFINITIONID}";
    static final String SERVICES_URL="services";
    static final String SERVICE_UPDATE_METADATA_URL="/service/{SERVICEID}/metadata/update";
    
    @Context
    UriInfo uriInfo;
    
    @EJB
    LocalServiceRepository serviceRepo;
    @EJB
    LocalCaseRepository caseRepo;
    @EJB
    LocalSubmitterRepository submitterRepo;
    @EJB
    LocalServiceDefinitionRepository serviceDefRepo;
    @EJB
    LocalMetadataRepository metadataRepo;
    @EJB
    LocalUUIDManager uuidManager;
    
    public ServicesResource(){
    }
    
    public ServicesResource(UriInfo uriInfo){
        this.uriInfo=uriInfo;
    }
    
    @GET
    @Path(SERVICES_FOR_DEFINITION_URL)
    public Response getServicesForServiceDefinition(@PathParam("DEFINITIONID") Integer id) {
        JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
        
        for(IService s : serviceDefRepo.getServiceDefinition(id).getServices()){
            arrayBuilder.add( getServiceBuilderJson(s) );
        }
        return DefaultResponse.createOKResponse( arrayBuilder.build() );
    }
    
    @GET
    @Path(SERVICES_FOR_CASE_URL)
    public Response getServicesForCase(@PathParam("CASEID") Integer id) {
        JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
        
        for(IService s : serviceRepo.getServicesForCase(id)){
            arrayBuilder.add( getServiceBuilderJson(s) );
        }
        return DefaultResponse.createOKResponse( arrayBuilder.build() );
    }
  
    @GET
    @Path(SERVICES_URL)
    public Response getServices() {
        JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
        
        for(IService s : serviceRepo.getServices()){
            arrayBuilder.add( getServiceBuilderJson(s) );
        }
        return DefaultResponse.createOKResponse( arrayBuilder.build() );
    }
    
    @GET
    @Path(SERVICE_URL)
    public Response getService(@PathParam("SERVICEID") Integer id) {
        IService serviceToBuild = serviceRepo.getService(id);
        if(serviceToBuild==null){
            JsonObject response = ErrorRepository.createNotFoundError(ServicesURLResource.getURL(id, uriInfo), "@GET Service with id="+id);
            DefaultResponse.createNotFoundResponse(response);
        }
        
        return DefaultResponse.createOKResponse( buildServiceJson(serviceToBuild) );
    }
    
    @PUT
    @Path(SERVICE_UPDATE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateService(JsonObject input){
        HttpAccessRequest request = new HttpAccessRequest(input);
        JsonObject updatedService = request.getBody();
        JsonObject submitter = request.getSubmitter();
        String uuid = request.getUUID();
        
        Logger.getLogger("global").log(Logger.Level.INFO, "update for service requested.");
        Logger.getLogger("global").log(Logger.Level.INFO, "updated object is "+toString(updatedService));
        
        boolean isAvailableUUID = uuidManager.isAvailableUUID(uuid);
        if( !isAvailableUUID ){
            JsonObject err;
            err = ErrorRepository.createDuplicatedRequestError(CasesURLResource.getUpdateURL(uriInfo), uuid, "update case with id="+updatedService.getInt("id"), uuidManager);
            return DefaultResponse.createNoPermissionResponse(err);
        }
        
        try{ uuidManager.updateUUIDState(uuid, LocalUUIDManager.UUID_PROCESSING); }catch(Exception ex){ ex.printStackTrace(); }
        boolean hasAccess=submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"));
        if( !hasAccess ){
            JsonObject err;
            err = ErrorRepository.createNoPermissionError(CasesURLResource.getUpdateURL(uriInfo), submitter.getString("login"), "update case with id="+updatedService.getInt("id"));
            return DefaultResponse.createNoPermissionResponse(err);
        }
        
        Integer serviceId=updatedService.getInt("id");
        Integer caseId=updatedService.getInt("case");
        Integer serviceDefId=updatedService.getJsonObject("serviceDefinition").getInt("id");
        
        Boolean validValues=true;
        IService serviceToUpdate = serviceRepo.getService(serviceId);
        if(serviceToUpdate==null){
            org.jboss.logging.Logger.getLogger("global").log(org.jboss.logging.Logger.Level.INFO, "service is NULL");
            validValues=false;
        }
        
        ICase newCase = caseRepo.getCase(caseId);
        if(newCase==null){
            org.jboss.logging.Logger.getLogger("global").log(org.jboss.logging.Logger.Level.INFO, "case is NULL");
            validValues=false;
        }
        
        IServiceDefinition serviceDef = serviceDefRepo.getServiceDefinition(serviceDefId);
        if(serviceDef==null){
            org.jboss.logging.Logger.getLogger("global").log(org.jboss.logging.Logger.Level.INFO, "serviceDef is NULL");
            validValues=false;
        }
        
        Logger.getLogger("global").log(Logger.Level.INFO, "old values="+serviceToUpdate.getId()+" / "+serviceToUpdate.getCase().getId());
        serviceToUpdate.setCase(newCase);
        serviceToUpdate.setServiceDefinition(serviceDef);
        
        serviceRepo.updateService(serviceToUpdate);
        return DefaultResponse.createOKResponse( buildServiceJson(serviceToUpdate) );
    }

    @PUT
    @Path(SERVICE_UPDATE_METADATA_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateServiceMetadata(@PathParam("SERVICEID") Integer id, JsonArray updateRequest){
        JsonArray updatedMetadata=updateRequest.getJsonArray(0);
        JsonObject submitter=updateRequest.getJsonObject(1);
        Logger.getLogger("global").log(Logger.Level.INFO, "Trying to update metadata for service with id="+id);
        if (!submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"))){
            JsonObject err;
            err = ErrorRepository.createNoPermissionError(CasesURLResource.getUpdateURL(uriInfo), submitter.getString("login"), "create new service");
            return DefaultResponse.createNoPermissionResponse(err);
        }
        IService service = serviceRepo.getService(id);
        if( service==null ){
            JsonObject response = ErrorRepository.createNotFoundError(ServicesURLResource.getURL(id, uriInfo), "@PUT update metadata for service with id="+id);
            return DefaultResponse.createNotFoundResponse(response);
        }
        
        Logger.getLogger("global").info( updatedMetadata.size()+" fields received for update" );
        HashMap<IServiceDefinition, List<IMetadataValue>> fields4service = service.getServiceDefinition().getMetadataValues();
        ArrayList<IMetadataValue> allFieldsMV = new ArrayList<>();
        for( IServiceDefinition def : fields4service.keySet() ){ allFieldsMV.addAll(fields4service.get(def)); }
        HashSet<String> allFieldsString = new HashSet<>();
        allFieldsMV.stream().forEach( (mv) -> {
            allFieldsString.add(mv.getKey());
        } );
        for( int i=0; i<updatedMetadata.size(); i++ ){
            JsonObject newData = updatedMetadata.getJsonObject(i);
            String key=newData.getString("key");
            if( !allFieldsString.contains(newData.getString("key")) ){
                Logger.getLogger("global").warn("Updating field '"+key+"' not possible: does not belong to ServiceDefinition");
            }else{
                Logger.getLogger("global").warn("Updating field '"+key+"' new value="+newData.getString("value"));
                switch( newData.getString("type") ){
                    case "int":
                        metadataRepo.setIntegerMetadata(service, key, Integer.valueOf( newData.getString("value") ));
                        break;
                    case "double":
                        metadataRepo.setDoubleMetadata(service, key, Double.valueOf( newData.getString("value") ));
                        break;
                    case "string":
                        metadataRepo.setStringMetadata(service, key, newData.getString("value") );
                        break;
                    case "text":
                        metadataRepo.setTextMetadata(service, key, newData.getString("value") );
                        break;
                    case "url":
                        metadataRepo.setUrlMetadata(service, key, newData.getString("value") );
                        break;
                    default:
                        Logger.getLogger("global").error("Metadata field type '"+newData.getValueType()+"' is unknown. Cannot add field for new service.");
                }
            }
        }
        
        return DefaultResponse.createOKResponse( buildServiceJson(service) );
    }
    
    @PUT
    @Path(SERVICE_CREATE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createService(JsonObject input){
        HttpAccessRequest request = new HttpAccessRequest(input);
        JsonObject createService = request.getBody();
        JsonObject submitter = request.getSubmitter();
        String uuid = request.getUUID();
        
        Logger.getLogger("global").log(Logger.Level.INFO, "create for service requested.");
        Logger.getLogger("global").log(Logger.Level.INFO, "create object is "+toString(createService));
        
        boolean isAvailableUUID = uuidManager.isAvailableUUID(uuid);
        if( !isAvailableUUID ){
            JsonObject err;
            err = ErrorRepository.createDuplicatedRequestError(CasesURLResource.getUpdateURL(uriInfo), uuid, "create service with id="+createService.getInt("id"), uuidManager);
            return DefaultResponse.createNoPermissionResponse(err);
        }
        
        try{ uuidManager.updateUUIDState(uuid, LocalUUIDManager.UUID_PROCESSING); }catch(Exception ex){ ex.printStackTrace(); }
        boolean hasAccess=submitterRepo.submitterHasAccess(submitter.getString("login"), submitter.getString("password"));
        if( !hasAccess ){
            JsonObject err;
            err = ErrorRepository.createNoPermissionError(CasesURLResource.getUpdateURL(uriInfo), submitter.getString("login"), "create service with id="+createService.getInt("id"));
            return DefaultResponse.createNoPermissionResponse(err);
        }
        Integer caseId=createService.getInt("case");
        Integer serviceDefId=createService.getJsonObject("serviceDefinition").getInt("id");
        
        IService serviceToCreate = new ServiceEntity();
        
        ICase newCase = caseRepo.getCase(caseId);
        
        IServiceDefinition serviceDef = serviceDefRepo.getServiceDefinition(serviceDefId);
        
        org.jboss.logging.Logger.getLogger("global").log(org.jboss.logging.Logger.Level.INFO, "creating new service");
        serviceToCreate.setCase(newCase);
        serviceToCreate.setServiceDefinition(serviceDef);
        
        try{
            org.jboss.logging.Logger.getLogger("global").log(org.jboss.logging.Logger.Level.INFO, "serviceRepo.createService(serviceToCreate);");
            Boolean isCreated = serviceRepo.createService(serviceToCreate);
        } catch(EJBException ejbEx){
            Exception cEx = ejbEx.getCausedByException();
            if (cEx.getClass().equals(DefaultValidationException.class)){
                return DefaultResponse.createUnprocessableEntityResponse(
                    ErrorRepository.createValidationContraintsViolatedError(SERVICE_CREATE_URL, "creating new service", ((DefaultValidationException)cEx).getViolations()
                    ));
            }
        }
        
        
        Logger.getLogger("DEBUG").warn( "whole service: "+createService.toString() );
        if(createService.containsKey("serviceMetadata")){
            Logger.getLogger("DEBUG").warn( "serviceMetadata is present in createService" );
        }else{
            Logger.getLogger("DEBUG").warn( "serviceMetadata MISSING in createService" );
        }
        //if(createService.isNull("serviceMetadata")){
            //Logger.getLogger("DEBUG").warn( "serviceMetadata is NULL");
        //}
        JsonArray meta=createService.getJsonArray("serviceMetadata");
        HashMap<String, Object> setValues=new HashMap<>();
        for( int i=0; i<meta.size(); i++){
            JsonObject obj = meta.getJsonObject(i);
            switch (obj.getString("type")) {
                case "int":
                    setValues.put(obj.getString("key"), Integer.valueOf(obj.getString("value")));
                    break;
                case "double":
                    setValues.put(obj.getString("key"), Double.valueOf(obj.getString("value")));
                    break;
                default:
                    setValues.put(obj.getString("key"), obj.getString("value"));
                    break;
            }
        }
        
        // create metadata fields:
        HashMap<IServiceDefinition, List<IMetadataValue>> metadataFields = serviceDef.getMetadataValues();
        StringBuilder sb = new StringBuilder();
        for(IServiceDefinition def : metadataFields.keySet()){
            sb.append(def.getName()).append(": ");
            for(IMetadataValue tmp : metadataFields.get(def)){
                sb.append(tmp.getKey()).append(":").append(tmp.getValueType()).append(" ");
            }
            sb.append("\n");
        }
        Logger.getLogger("global").info("\n------------------------------\n"+sb.toString()+"-----------------------------\n");
        
        for(IServiceDefinition def : metadataFields.keySet()){
            Logger.getLogger("global").info("Adding metadata fields for definition: "+def.getName());
            List<IMetadataValue> fields2Add=metadataFields.get(def);
            Logger.getLogger("global").info(fields2Add.size() + " fields found.");
            StringBuilder sb2 = new StringBuilder();
            
            for(IMetadataValue tmp : fields2Add){ sb2.append(tmp.getKey()).append(":").append(tmp.getValueType()).append(" "); }
            Logger.getLogger("global").info("Adding fields: "+sb2.toString());
            Object newVal;
            for(IMetadataValue mVal :fields2Add){
                Logger.getLogger("global").info("Adding new metadata field to service: "+mVal.getKey());
                IMetadata newData;
                switch( mVal.getValueType() ){
                    case "int":
                        newData = metadataRepo.setIntegerMetadata(serviceToCreate, mVal.getKey(), (Integer)setValues.get(mVal.getKey()));
                        break;
                    case "double":
                        newData = metadataRepo.setDoubleMetadata(serviceToCreate, mVal.getKey(), (Double)setValues.get(mVal.getKey()));
                        break;
                    case "string":
                        newData = metadataRepo.setStringMetadata(serviceToCreate, mVal.getKey(), (String)setValues.get(mVal.getKey()));
                        break;
                    case "text":
                        newData = metadataRepo.setTextMetadata(serviceToCreate, mVal.getKey(), (String)setValues.get(mVal.getKey()));
                        break;
                    case "url":
                        newData = metadataRepo.setUrlMetadata(serviceToCreate, mVal.getKey(), (String)setValues.get(mVal.getKey()));
                        break;
                    default:
                        Logger.getLogger("global").error("Metadata field type '"+mVal.getValueType()+"' is unknown. Cannot add field for new service.");
                }
            }
        }
        
        return DefaultResponse.createOKResponse( buildServiceJson(serviceToCreate) );
    }
    
    @DELETE
    @Path(SERVICE_DELETE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteService(JsonObject deleteService){
        Integer id = deleteService.getInt("id");
        Logger.getLogger("global").log(Logger.Level.INFO, "Delete for service[id="+id+"] requested.");
        
        
        IService serviceToDelete = serviceRepo.getService(id);
        
        try{
            Logger.getLogger("global").log(Logger.Level.INFO, "serviceRepo.deleteService(serviceToDelete);");
            serviceRepo.deleteService(serviceToDelete);
            Logger.getLogger("global").log(Logger.Level.INFO, "service deleted");
        }catch(Exception ex){
            Logger.getLogger("global").log(Logger.Level.INFO, "returning createNotDeletedError");
            return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createNotDeletedError(CasesURLResource.getURL(serviceToDelete.getId(), uriInfo), "deleting service id="+serviceToDelete.getId()));
        }
        Logger.getLogger("global").log(Logger.Level.INFO, "returning OK status");
        JsonObjectBuilder resultBuilder = Json.createObjectBuilder();
        resultBuilder.add("Status", "200: OK");
        return DefaultResponse.createOKResponse( resultBuilder.build() );
    }
    
    private JsonObject buildServiceJson(IService serviceToConvert){
        JsonObjectBuilder jsonServiceBuilder=getServiceBuilderJson(serviceToConvert);
        return jsonServiceBuilder.build();
    }
    
    private JsonObjectBuilder getServiceBuilderJson(IService serviceToBuild){
        URI selfUri = uriInfo.getBaseUriBuilder()
            .path(ServicesResource.class)
            .path(ServicesResource.class, "getService")
            .build(serviceToBuild.getId());
        
        JsonObjectBuilder jsonServiceDefinitionBuilder = getServiceDefinitionBuilderForService(serviceToBuild);
        String caseURL = CasesURLResource.getURL(serviceToBuild.getCase(), uriInfo);
        Integer caseId = serviceToBuild.getCase().getId();
        JsonArrayBuilder jsonServiceMetaBuilder = getMetadataBuilder(serviceToBuild);
        
        JsonObjectBuilder jsonServiceBuilder = Json.createObjectBuilder();
        jsonServiceBuilder.add("id", serviceToBuild.getId())
                .add("case", caseId)
                .add("serviceDefinition", jsonServiceDefinitionBuilder)
                .add("serviceMetadata", jsonServiceMetaBuilder)
                .add("url", selfUri.getRawPath())
                .add("caseURL", caseURL);
        return jsonServiceBuilder;
    }
    
    public JsonObjectBuilder getServiceDefinitionBuilderForService(IService service){
        IServiceDefinition def = service.getServiceDefinition();
        
        return ServiceDefinitionsResource.getServiceDefinitionBuilder(def);
    }
    
//    public JsonArrayBuilder getServiceMetadataBuilderForService(IService service){
//        IServiceDefinition def = service.getServiceDesciption();
//        List<IMetadataValue> metadataFields = def.getMetadataValues();
//        
//        JsonArrayBuilder metadataBuilder = Json.createArrayBuilder();
//        JsonArrayBuilder metavalueBuilder=getMetadataBuilder(service);
//        for( IMetadataValue mv : metadataFields ){
//            if(mv.isDepricated()){
//                Logger.getGlobal().log(Level.INFO, "Skipping metadata field with key ''{0}'': was marked as deprecated.", mv.getKey());
//                continue;
//            }
//            
//            metadataBuilder.add(metavalueBuilder);
//        }
//        return metadataBuilder;
//    }
    
    public JsonArrayBuilder getMetadataBuilder(IService service){
        JsonArrayBuilder metadataArrayBuilder = Json.createArrayBuilder();
        HashMap<IServiceDefinition, List<IMetadataValue>> fieldsPerDef = service.getServiceDefinition().getMetadataValues();
        
        Logger.getLogger("global").info("ServiceDefs: "+fieldsPerDef.size()+ " META: "+service.getMetadata().size());
            StringBuilder sb = new StringBuilder();
        for(IServiceDefinition def : fieldsPerDef.keySet()){
            sb.append(def.getName()).append(": ");
            for(IMetadataValue tmp : fieldsPerDef.get(def)){
                sb.append(tmp.getKey()).append(":").append(tmp.getValueType()).append(" ");
            }
            sb.append("\n");
        }
        Logger.getLogger("global").info("\n------------------------------\n"+sb.toString()+"-----------------------------\n");
        
        for( IMetadata meta : metadataRepo.getMetadataForService(service)){
            IServiceDefinition currentServiceDef=null;
            IMetadataValue field = null;
            keyloop:
            for(IServiceDefinition serviceDef : fieldsPerDef.keySet()){
                List<IMetadataValue> fields = fieldsPerDef.get(serviceDef);
                for( IMetadataValue tmpField : fields ){
                    if( tmpField.getKey().equals(meta.getName()) ){
                        field = tmpField; currentServiceDef=serviceDef; break keyloop;
                    }
                }
            }
            if(field==null){
                Logger.getLogger("global").log(Logger.Level.WARN, "{0} does not exist in service definition metadata value list.".replace("{0}", meta.getName()));
                continue;
            }else if(field.isDepricated()){
                Logger.getLogger("global").log(Logger.Level.WARN, "{0} key is marked as depricated for service and will be ignored.".replace("{0}", field.getKey()));
                continue;
            }
            JsonObjectBuilder metadataObjectBuilder = Json.createObjectBuilder();
            metadataObjectBuilder.add("key", field.getKey())
                .add("value", ""+meta.getData())
                .add("type", field.getValueType())
                .add("unit", (field.getUnit()==null)?"NO_UNIT":field.getUnit())
                .add("deprecated", field.isDepricated())
                .add("category", currentServiceDef.getName());
            
            metadataArrayBuilder.add(metadataObjectBuilder);
        }
        
        
        return metadataArrayBuilder;
    }
    
    public String toString(JsonValue serviceAsJson){
        return serviceAsJson.toString();
    }
}
