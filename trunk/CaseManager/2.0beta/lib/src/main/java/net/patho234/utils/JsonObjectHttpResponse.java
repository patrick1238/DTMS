/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import net.patho234.interfaces.IHttpResponse;
import org.jboss.logging.Logger;

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
        headerFields = IHttpResponse.parseHeader(con);
        String header_msg=null;
        try{ status = con.getResponseCode(); }catch (IOException ex){
            for(String entry : headerFields){
                if(entry.startsWith("null: ")){
                    status=IHttpResponse.parseStatusFromHeaderLine(entry);
                    header_msg=entry.replace("null: ", "");
                    
                }
            }
        }
        try{ message = con.getResponseMessage(); }catch(IOException ioEx){
            message=header_msg;
        }
        try{ content = parseContent(con); }catch(IOException ioEx){
            content = Json.createObjectBuilder().build();
            Logger.getLogger(getClass()).warn("Could not parse HTTPResponse content...status was: "+status, ioEx);
        }
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
