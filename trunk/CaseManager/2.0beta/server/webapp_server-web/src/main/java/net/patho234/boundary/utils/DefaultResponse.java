/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.boundary.utils;

import java.net.URI;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 *
 * @author MLH
 */
public class DefaultResponse {
    public static Response createOKResponse(Object responseEntity){
        ResponseBuilder rb = Response.ok().type(MediaType.APPLICATION_JSON_TYPE).entity(responseEntity).header("Access-Control-Allow-Origin", "*");
        
        return rb.build();
    }
    
    public static Response createUnprocessableEntityResponse(Object responseEntity){
        ResponseBuilder rb;
 
        rb = Response.status(422).type(MediaType.APPLICATION_JSON_TYPE).entity(responseEntity).header("Access-Control-Allow-Origin", "*");
        //System.out.println("@RESPONSE: "+responseEntity.toString());
        Response r = rb.build();
        //System.out.println("@BUILD_RESPONSE: "+r.getEntity().toString());
        return r;
    }
    
    public static Response createCreatedResponse(Object responseEntity, String url){
        ResponseBuilder rb;
        rb = Response.created(URI.create(url)).type(MediaType.APPLICATION_JSON_TYPE).entity(responseEntity).header("Access-Control-Allow-Origin", "*");
        return rb.build();
    }
    
    
    public static Response createNoPermissionResponse(JsonObject responseEntity){
        ResponseBuilder rb;
 
        rb = Response.status(403).type(MediaType.APPLICATION_JSON_TYPE).entity(responseEntity).header("Access-Control-Allow-Origin", "*");
        Response r = rb.build();
        return r;
    }

    public static Response createNotFoundResponse(JsonObject responseEntity) {        
        ResponseBuilder rb = Response.status(404).type(MediaType.APPLICATION_JSON_TYPE).entity(responseEntity).header("Access-Control-Allow-Origin", "*");
        return rb.build();
    }
}
