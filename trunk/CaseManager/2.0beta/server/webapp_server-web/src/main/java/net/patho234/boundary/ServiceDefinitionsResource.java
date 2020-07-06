/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.boundary;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import net.patho234.boundary.utils.DefaultResponse;
import net.patho234.control.ErrorRepository;
import net.patho234.control.LocalServiceDefinitionRepository;
import net.patho234.interfaces.IServiceDefinition;
import net.patho234.utils.LocalUUIDManager;
import org.jboss.logging.Logger;
/**
 *
 * @author rehkind
 */
@Path("/servicedefinitionpool")
@Produces(MediaType.APPLICATION_JSON)
public class ServiceDefinitionsResource {
    final private String ALL_SERVICE_DEFINITIONS_URL="/servicedefinitions";
    final private String SERVICE_DEFINITION_URL="/servicedefinition/{DEFINITION_ID}";
    @EJB
    LocalServiceDefinitionRepository definitionRepo;
    @EJB
    LocalUUIDManager uuidManager;
    
    @Context
    UriInfo uriInfo;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(ALL_SERVICE_DEFINITIONS_URL)
    public Response getServiceDefinitions() {
        JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
        System.out.println("definition_repo: "+definitionRepo);
        Logger.getLogger(getClass()).warn( "searching ALL ServiceDefinitions " );
        for(IServiceDefinition c : definitionRepo.getServiceDefinitions()){
            arrayBuilder.add( getServiceDefinitionBuilder(c) );
        }
        
        return DefaultResponse.createOKResponse( arrayBuilder.build() );
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(SERVICE_DEFINITION_URL)
    public Response getServiceDefinition(@PathParam("DEFINITION_ID") Integer id) {
        Logger.getLogger(getClass()).info( "[getServiceDefinition] searching ServiceDefinition: "+id );
        IServiceDefinition serviceDefToBuild = null;
        
        try{ serviceDefToBuild=definitionRepo.getServiceDefinition(id); }catch(NullPointerException ex){ /* handled in next if */ }

        if( serviceDefToBuild==null ){
            JsonObject response = ErrorRepository.createNotFoundError(ServiceDefinitionsURLResource.getURL(id, uriInfo), "@GET ServiceDefinition with id="+id);
            Logger.getLogger(getClass()).info( String.format( "ServiceDefinition[%d] does not exist...returning NOT_FOUND_ERROR (404)", new Object[]{id}) );
            return DefaultResponse.createNotFoundResponse(response);
        }
        return DefaultResponse.createOKResponse( getServiceDefinitionBuilder(serviceDefToBuild).build() );
    }
    
    static public JsonObjectBuilder getServiceDefinitionBuilder(IServiceDefinition serviceDef){
        IServiceDefinition parentServiceDef=serviceDef.getParentDefinition();
        JsonObjectBuilder parentBuilder;
        if( parentServiceDef==null ){
            parentBuilder=null;
        }else{
            parentBuilder=getServiceDefinitionBuilder(parentServiceDef);
        }
        
        JsonObjectBuilder serviceDefBuilder = Json.createObjectBuilder();
        serviceDefBuilder.add("id", serviceDef.getId())
                .add("name", serviceDef.getName())
                .add("description", serviceDef.getDescription());
        if(parentBuilder==null){
            serviceDefBuilder.add("parentDefinition", -1);
        }else{
            serviceDefBuilder.add("parentDefinition", parentBuilder);
        }
        
        return serviceDefBuilder;
    }
}
