/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.utils;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import javax.json.JsonValue;

/**
 * A HttpAccessRequest consists of three Object, provided as JsonObjects: uuid, submitter, body
 * The class can be used to build a request on client-side and to read it from a HttpRequest server-sides
 * @author rehkind
 */
public class HttpAccessRequest {
    String endpoint;
    String compiledEndpoint;
    String uuid;
    JsonObject submitter;
    JsonStructure body;
    ServerSettings settings;
    
    protected HttpAccessRequest(){}
    
    public HttpAccessRequest(String endpointTemplate, String compiledEndpoint, String uuid, JsonObject submitter, JsonStructure body){
        this.endpoint=endpointTemplate;
        this.compiledEndpoint=compiledEndpoint;
        this.uuid=uuid;
        this.submitter=submitter;
        this.body=body;
    }
    
    public HttpAccessRequest(JsonObject request){
        this.body=request.getJsonObject("body");
        this.submitter=request.getJsonObject("submitter");
        this.uuid=request.getString("uuid");
    }
    
    public JsonStructure toJsonStructure(){
        JsonObjectBuilder builder=Json.createObjectBuilder();
        builder.add("uuid", uuid);
        builder.add("submitter", submitter);
        builder.add("body", body);
        return builder.build();
    }
    
    public String getUUID(){
        return uuid;
    }
    public JsonObject getSubmitter(){
        return submitter;
    }
    public JsonObject getBodyObject(){
        return (JsonObject)body;
    }
    public JsonArray getBodyArray(){
        return (JsonArray)body;
    }
    public String getCompiledEndpoint() {
        return compiledEndpoint;
    }
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public ServerSettings getServerSettings() {
        return settings;
    }
    
    public void setServerSettings(ServerSettings settings) {
        this.settings=settings;
    }
        
}
