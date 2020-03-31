/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import net.rehkind_mag.interfaces.IHttpResponse;

/**
 *
 * @author rehkind
 */
public class JsonObjectHttpResponse implements IHttpResponse<JsonStructure> {
    List<String> headerFields;
    int status;
    int requestId;
    JsonStructure content;
    String message;
    
    private JsonObjectHttpResponse( List<String> headerFields, int status, int requestId, JsonStructure content, String message ){
        this.headerFields=headerFields;
        this.status=status;
        this.requestId=requestId;
        this.content=content;
        this.message=message;
    }
    
    public JsonObjectHttpResponse( HttpURLConnection con, Integer requestId ) throws IOException{
        status = con.getResponseCode();
        message = con.getResponseMessage();
        
        headerFields = IHttpResponse.parseHeader(con);
        content = parseContent(con);
        this.requestId=requestId;
    }
    
    @Override
    public List<String> getHeaderFields() {
        return this.headerFields;
    }

    @Override
    public int getResponseStatus() {
        return this.status;
    }

    @Override
    public JsonStructure getContent() {
        return this.content;
    }
    
    static public JsonStructure parseContent( HttpURLConnection con) throws IOException{
        JsonReader jsonReader = Json.createReader((InputStream)con.getContent());
        JsonStructure asJson=null;
        
        asJson = jsonReader.read();
        
        return asJson;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getRequestId() {
        return this.requestId;
    }

    @Override
    public IHttpResponse clone(int cloneId, int statusOverride) {
        ArrayList<String> newHeader=new ArrayList<>();
        this.headerFields.forEach((field) -> { newHeader.add(field); });
        return new JsonObjectHttpResponse(newHeader, statusOverride, cloneId, content, ""+message);
    }

    @Override
    public boolean responseSucceeded() {
        return (status==HTTP_STATUS.OK || status==HTTP_STATUS.CACHED);
    }
    
}
