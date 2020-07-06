/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.boundary;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateful;
import javax.json.Json;
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
import static net.patho234.boundary.ServicesResource.SERVICE_DELETE_URL;
import net.patho234.boundary.utils.DefaultResponse;
import net.patho234.control.ErrorRepository;
import net.patho234.control.LocalMetadataValueRepository;
import net.patho234.entity.MetadataValueEntity;
import net.patho234.entity.validation.DefaultValidationException;
import net.patho234.interfaces.IMetadataValue;
import net.patho234.interfaces.IService;

import org.jboss.logging.Logger;


/**
 *
 * @author HS
 */
@Path("/metadatavaluepool")
@Produces(MediaType.APPLICATION_JSON)
@Stateful
public class MetadataValuesResource {
    @EJB
    LocalMetadataValueRepository metadataValueRepo;

    final private String METADATA_VALUES_URL="/metadatavalues";
    final private String METADATA_VALUE_URL="/metadatavalue/{MD_VALUE_ID}";
    final private String METADATA_VALUE_UPDATE_URL="/metadatavalue/update";
    final private String METADATA_VALUE_CREATE_URL="/metadatavalue/create";
    final String METADATA_VALUE_DELETE_URL="/metadatavalue/delete";
    
    @Context
    UriInfo uriInfo;
    
    public MetadataValuesResource(){
    }
    
    public MetadataValuesResource(UriInfo uriInfo){
        this.uriInfo = uriInfo;
    }
    
    
    @GET
    @Path(METADATA_VALUES_URL)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMetadataValues() {
        JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
        
        for(IMetadataValue mv : metadataValueRepo.getMetadataValues()){
            arrayBuilder.add( getMetadataValueBuilderJson(mv) );
        }
        return DefaultResponse.createOKResponse( arrayBuilder.build() );
    }
    
    @GET
    @Path(METADATA_VALUE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMetadataValue(@PathParam("MD_VALUE_ID") Integer id) {
        IMetadataValue metadataValueToBuild = metadataValueRepo.getMetadataValue(id);
        if(metadataValueToBuild==null){
            Logger.getLogger("global").info("metadataValue is NULL");
        }
        
        return buildMetadataValueJson(metadataValueToBuild);
    }

    @PUT
    @Path(METADATA_VALUE_CREATE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMetadataValue(JsonObject createMetadataValue){
        Logger.getLogger("global").log(Logger.Level.INFO, "Creating new metadata value requested.");
        Logger.getLogger("global").log(Logger.Level.INFO, "new object is "+toString(createMetadataValue));
        
        MetadataValueEntity metaValueToCreate = new MetadataValueEntity();

        Logger.getLogger("global").log(Logger.Level.INFO, toString(createMetadataValue));
        metaValueToCreate.setKey(createMetadataValue.getString("key")); 
        if( createMetadataValue.isNull("unit") ){
            metaValueToCreate.setUnit(null);
        }else{
            metaValueToCreate.setUnit(createMetadataValue.getString("unit"));
        }
        metaValueToCreate.setValueType(createMetadataValue.getString("valueType")); 
        metaValueToCreate.setDepricated(createMetadataValue.getBoolean("deprecated")); 
        
        try{
            metadataValueRepo.createMetadataValue(metaValueToCreate);
        }catch(EJBException ejbEx){
            Exception cEx = ejbEx.getCausedByException();
            if (cEx.getClass().equals(DefaultValidationException.class)){
                return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createValidationContraintsViolatedError(MetadataValuesURLResource.getCreateURL( uriInfo ), "Creating new MetadataValue", ((DefaultValidationException)cEx).getViolations()));
            }
        }
        return buildMetadataValueJson(metaValueToCreate);
    }
    
    @PUT
    @Path(METADATA_VALUE_UPDATE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMetadataValue(JsonObject updatedMetadataValue){
        Logger.getLogger("global").log(Logger.Level.INFO, "update for metadata value requested.");
        Logger.getLogger("global").log(Logger.Level.INFO, "updated object is "+toString(updatedMetadataValue));
        
        Integer metaId=updatedMetadataValue.getInt("id");
        IMetadataValue metaValueToUpdate = metadataValueRepo.getMetadataValue(metaId);
        if(metaValueToUpdate==null){
            Logger.getLogger("global").log(Logger.Level.WARN, "Update requested for non-existing metadata value.");
            JsonObject errObject = ErrorRepository.createTargetNotExisting(SubmittersURLResource.getURL(metaId, uriInfo), "update metadataValue[id="+metaId+"]");
            return DefaultResponse.createUnprocessableEntityResponse(errObject);
        }
        Logger.getLogger("global").log(Logger.Level.INFO, toString(metaValueToUpdate));
        if(updatedMetadataValue.keySet().contains("key")){ metaValueToUpdate.setKey(updatedMetadataValue.getString("key")); }
        if( updatedMetadataValue.isNull("unit") ){
            metaValueToUpdate.setUnit(null);
        }
        else{
            if(updatedMetadataValue.keySet().contains("unit")){ metaValueToUpdate.setUnit(updatedMetadataValue.getString("unit")); }
        }
        if(updatedMetadataValue.keySet().contains("valueType")){ metaValueToUpdate.setValueType(updatedMetadataValue.getString("valueType")); }
        if(updatedMetadataValue.keySet().contains("deprecated")){ metaValueToUpdate.setDepricated(updatedMetadataValue.getBoolean("deprecated")); }
        
        try{
            metadataValueRepo.updateMetadataValue(metaValueToUpdate);
        }catch(EJBException ejbEx){
            Exception cEx = ejbEx.getCausedByException();
            if (cEx.getClass().equals(DefaultValidationException.class)){
                return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createValidationContraintsViolatedError(SubmittersURLResource.getURL(metaId, uriInfo), "updating metadata value id="+metaId, ((DefaultValidationException)cEx).getViolations()));
            }
        }
        return buildMetadataValueJson(metaValueToUpdate);
    }
    
    @DELETE
    @Path(METADATA_VALUE_DELETE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteService(JsonObject deleteMetadataValue){
        Integer id = deleteMetadataValue.getInt("id");
        Logger.getLogger("global").log(Logger.Level.INFO, "Delete for metadataValue[id="+id+"] requested.");
        
        
        IMetadataValue metaToDelete = metadataValueRepo.getMetadataValue(id);
        
        try{
            Logger.getLogger("global").log(Logger.Level.INFO, "metadataValueRepo.deleteMetadataValuemetaToDelete);");
            metadataValueRepo.deleteMetadataValue(metaToDelete);
            Logger.getLogger("global").log(Logger.Level.INFO, "metadataValue deleted");
        }catch(Exception ex){
            Logger.getLogger("global").log(Logger.Level.INFO, "returning createNotDeletedError");
            return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createNotDeletedError(CasesURLResource.getURL(metaToDelete.getId(), uriInfo), "deleting metadata value id="+metaToDelete.getId()));
        }
        Logger.getLogger("global").log(Logger.Level.INFO, "returning OK status");
        JsonObjectBuilder resultBuilder = Json.createObjectBuilder();
        resultBuilder.add("Status", "200: OK");
        return DefaultResponse.createOKResponse( resultBuilder.build() );
    }
    
    public Response buildMetadataValueJson(IMetadataValue submitterToConvert){
        JsonObjectBuilder jsonSubmitterBuilder=getMetadataValueBuilderJson(submitterToConvert);
        return DefaultResponse.createOKResponse(jsonSubmitterBuilder.build() );
    }
    
    public JsonObjectBuilder getMetadataValueBuilderJson(IMetadataValue metadataValueToBuild){
        String url = MetadataValuesURLResource.getURL(metadataValueToBuild, uriInfo);
        
        JsonObjectBuilder jsonMetadataValueBuilder = Json.createObjectBuilder();
        jsonMetadataValueBuilder.add("id", metadataValueToBuild.getId())
                .add("key", metadataValueToBuild.getKey())
                .add("valueType", metadataValueToBuild.getValueType())
                .add("deprecated", metadataValueToBuild.isDepricated())
                .add("url", url);
        
        if( metadataValueToBuild.getUnit() != null ){
            jsonMetadataValueBuilder.add("unit", metadataValueToBuild.getUnit());
        }else{
            jsonMetadataValueBuilder.addNull("unit");
        }
        return jsonMetadataValueBuilder;
    }
    
    
    
    public String toString(JsonObject metaValue) {
        String theId = (metaValue.keySet().contains("id")) ? ""+metaValue.getInt("id") : "NOT_SET";
        String unit = ( metaValue.isNull("unit") ) ? "NULL" : ((metaValue.keySet().contains("unit")?metaValue.getString("unit"):"NOT_SET"));
        StringBuilder sb=new StringBuilder("MetadataValue[");
        sb.append(theId).append("]=");
        //values
        sb.append("\tkey: ").append(metaValue.getString("key"));
        sb.append("\tunit: ").append(unit);
        sb.append("\tvalueType: ").append(metaValue.getString("valueType"));
        sb.append("\tdeprecated: ").append(metaValue.getBoolean("deprecated"));
        return sb.toString();
    }
    
    public String toString(IMetadataValue metaValue) {
        StringBuilder sb=new StringBuilder("MetadataValue[");
        sb.append(metaValue.getId()).append("]=");
        //values
        sb.append("\tkey: ").append(metaValue.getKey());
        sb.append("\tunit: ").append( (metaValue.getUnit())!=null ? metaValue.getUnit() : "null" );
        sb.append("\tvaluetype: ").append(metaValue.getValueType());
        sb.append("\tdeprecated: ").append(metaValue.isDepricated());
        return sb.toString();
    }
}
