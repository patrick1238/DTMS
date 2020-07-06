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
import net.patho234.entity.validation.DefaultValidationException;

import org.jboss.logging.Logger;
import net.patho234.interfaces.ISubmitter;
import net.patho234.control.LocalSubmitterRepository;

/**
 *
 * @author HS
 */
@Path("/submitterpool")
@Produces(MediaType.APPLICATION_JSON)
@Stateful
public class SubmittersResource {
    @EJB
    LocalSubmitterRepository submitterRepository;

    final private String SUBMITTERS_URL="/submitters";
    final private String SUBMITTER_URL="/submitter/{SUPPL_ID}";
    final private String SUBMITTER_UPDATE_URL="/submitter/update";
    
    @Context
    UriInfo uriInfo;
    
    public SubmittersResource(){
    }
    
    public SubmittersResource(UriInfo uriInfo){
        this.uriInfo = uriInfo;
    }
    
    
    @GET
    @Path(SUBMITTERS_URL)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubmitters() {
        JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
        
        for(ISubmitter s : submitterRepository.getSubmitters()){
            arrayBuilder.add( getSubmitterBuilderJson(s) );
        }
        return DefaultResponse.createOKResponse( arrayBuilder.build() );
    }
    
    @GET
    @Path(SUBMITTER_URL)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubmitter(@PathParam("SUPPL_ID") Integer id) {
        ISubmitter supplierToBuild = submitterRepository.getSubmitter(id);
        if(supplierToBuild==null){
            System.out.println("supplier is NULL");
        }
        
        return buildSubmitterJson(supplierToBuild);
    }
    
    @PUT
    @Path(SUBMITTER_UPDATE_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateSubmitter(JsonObject updatedSubmitter){
        Logger.getLogger("global").log(Logger.Level.INFO, "update for submitter requested.");
        Logger.getLogger("global").log(Logger.Level.INFO, "updated object is "+toString(updatedSubmitter));
        
        Integer submitterId=updatedSubmitter.getInt("submitterId");
        ISubmitter submitterToUpdate = submitterRepository.getSubmitter(submitterId);
        if(submitterToUpdate==null){
            Logger.getLogger("global").log(Logger.Level.WARN, "Update requested for non-existing submitter.");
            JsonObject errObject = ErrorRepository.createTargetNotExisting(SubmittersURLResource.getURL(submitterId, uriInfo), "update submitter[id="+submitterId+"]");
            return DefaultResponse.createUnprocessableEntityResponse(errObject);
        }
        Logger.getLogger("global").log(Logger.Level.INFO, toString(submitterToUpdate));
        submitterToUpdate.setForename(updatedSubmitter.getString("forename"));
        submitterToUpdate.setSurname(updatedSubmitter.getString("surname"));
        submitterToUpdate.setTitle(updatedSubmitter.getString("title"));
        submitterToUpdate.setLogin(updatedSubmitter.getString("login"));
        submitterToUpdate.setPassword(updatedSubmitter.getString("password"));
        
        try{
            submitterRepository.updateSubmitter(submitterToUpdate);
        }catch(EJBException ejbEx){
            Exception cEx = ejbEx.getCausedByException();
            if (cEx.getClass().equals(DefaultValidationException.class)){
                return DefaultResponse.createUnprocessableEntityResponse(ErrorRepository.createValidationContraintsViolatedError(SubmittersURLResource.getURL(submitterId, uriInfo), "updating supplier id="+submitterId, ((DefaultValidationException)cEx).getViolations()));
            }
        }
        return buildSubmitterJson(submitterToUpdate);
    }
    
    public Response buildSubmitterJson(ISubmitter submitterToConvert){
        JsonObjectBuilder jsonSubmitterBuilder=getSubmitterBuilderJson(submitterToConvert);
        return DefaultResponse.createOKResponse(jsonSubmitterBuilder.build() );
    }
    
    public JsonObjectBuilder getSubmitterBuilderJson(ISubmitter submitterToBuild){
        String url = SubmittersURLResource.getURL(submitterToBuild, uriInfo);
        
        JsonObjectBuilder jsonSupplierBuilder = Json.createObjectBuilder();
        jsonSupplierBuilder.add("id", submitterToBuild.getId())
                .add("forename", submitterToBuild.getForename())
                .add("surname", submitterToBuild.getSurname())
                .add("title", submitterToBuild.getTitle())
                .add("login", submitterToBuild.getLogin())
                .add("password", submitterToBuild.getPassword())
                .add("url", url);
        return jsonSupplierBuilder;
    }
    
    public String toString(JsonObject submitter) {
        StringBuilder sb=new StringBuilder("Submitter[");
        sb.append(submitter.getInt("submitterId")).append("]=");
        //values
        sb.append("\tforename: ").append(submitter.getString("submitterForename"));
        sb.append("\tname: ").append(submitter.getString("submitterSurname"));
        sb.append("\ttitle: ").append(submitter.getString("submitterTitle"));
        sb.append("\tlogin: ").append(submitter.getString("submitterLogin"));
        sb.append("\tpassword: ").append(submitter.getString("submitterPassword"));
        return sb.toString();
    }
    
    public String toString(ISubmitter submitter) {
        StringBuilder sb=new StringBuilder("Submitter[");
        sb.append(submitter.getId()).append("]=");
        //values
        sb.append("\tforename: ").append(submitter.getForename());
        sb.append("\tsurname: ").append(submitter.getSurname());
        sb.append("\ttitle: ").append(submitter.getTitle());
        sb.append("\tlogin: ").append(submitter.getLogin());
        sb.append("\tpassword: ").append(submitter.getPassword());
        
        return sb.toString();
    }
}
