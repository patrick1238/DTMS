/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
    JsonStructure content;
    String message;
    
    public JsonObjectHttpResponse( HttpURLConnection con ) throws IOException{
        status = con.getResponseCode();
        message = con.getResponseMessage();
        
        headerFields = IHttpResponse.parseHeader(con);
        content = parseContent(con);
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
    
}
