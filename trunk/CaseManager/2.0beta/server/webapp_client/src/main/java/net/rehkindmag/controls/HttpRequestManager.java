/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkindmag.controls;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.interfaces.IHttpResponseReceiver;
import net.rehkind_mag.utils.HttpAccessRequest;
import net.rehkind_mag.utils.JsonObjectHttpResponse;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class HttpRequestManager implements IHttpResponseReceiver{
    private static int UNIQUE_ID=0;
    private static HashMap<HttpRequestManagerName, HttpRequestManager> singletonManagerMap = new HashMap<>();
    
    private Set<Integer> pendingHttpRequests=new HashSet<>();
    
    String serverAddress;
    String contentType;
    Integer connectionTimeOut = 5000;
    Integer readTimeOut = 15000;
    
    static public final String CONTANT_TYPE_JSON = "application/json";
    static public final String CONTANT_TYPE_XML = "application/xml";
    
    static public HttpRequestManager createHttpRequestManager(String serverAddress, String contentType){
        HttpRequestManagerName key = new HttpRequestManagerName(serverAddress, contentType);
        if( singletonManagerMap.get( key )== null ){
            singletonManagerMap.put(key, new HttpRequestManager(serverAddress, contentType));
        }
        return singletonManagerMap.get( key );
    }

    private HttpRequestManager( String serverAddress, String contentType ){
        this.serverAddress = serverAddress;
        this.contentType = contentType;
    }
    
    public void setServerAddress(String address){
        serverAddress=address;
    }
    
        public void setConnectionTimeOut(Integer timeOut){
        connectionTimeOut=timeOut;
    }
        
    public void setReadTimeOut(Integer timeOut){
        readTimeOut=timeOut;
    }
    
    public Integer fireJsonHttpGETRequest(HttpAccessRequest request, IHttpResponseReceiver receiver){
        ArrayList<IHttpResponseReceiver> receivers=new ArrayList<>();
        receivers.add(receiver);
        receivers.add(this);
        HttpRequestWorker worker = new HttpRequestWorker(request, receivers, UNIQUE_ID, "GET");
        int assignedID = UNIQUE_ID;
        UNIQUE_ID++;
        worker.start();
        return assignedID;
    }

    @Override
    public void receiveHttpResponse(Integer requestID, IHttpResponse response) {
        System.out.println("http response for request "+requestID+" received.");
        this.pendingHttpRequests.remove(requestID);
        System.out.println(this.pendingHttpRequests.size()+" pending requests remaining");
    }
    
    
    private class HttpRequestWorker extends Thread{
        HttpAccessRequest request;
        List<IHttpResponseReceiver> receivers;
        final int ID;
        final String httpMethod;
        private HttpRequestWorker(HttpAccessRequest request, List<IHttpResponseReceiver> receivers, Integer ID, String httpMethod){
            this.ID = ID;
            this.request = request;
            this.receivers = receivers;
            this.httpMethod=httpMethod;
        }
        
        @Override
        public void run(){
            Logger.getLogger("global").info("New HttpRequest thread spawned.");
            URL url;
            HttpURLConnection con = null;
            try{
                url = new URL(serverAddress+request.getEndpoint());
                
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod(httpMethod);
                con.setRequestProperty("Content-Type", contentType);
                con.setConnectTimeout(connectionTimeOut);
                con.setReadTimeout(readTimeOut);
                con.setRequestMethod(httpMethod);
                con.setRequestProperty("Accept", contentType);
                
                // send http request body:
                if( "POST".equals( httpMethod ) || "PUT".equals( httpMethod ) ){
                    Logger.getLogger("global").info( "Writing HTTP-body in Json-format to HttpURLConnection output stream.");
                    
                    con.setDoOutput(true);
                    try(OutputStream os = con.getOutputStream()) {
                        JsonWriter writer = Json.createWriter(os);
                        JsonStructure struct = request.toJsonStructure();
                        switch( struct.getValueType() ){
                            case OBJECT:
                                writer.writeObject((JsonObject)struct);
                                break;
                            case ARRAY:
                                writer.writeArray((JsonArray)struct);
                                break;
                            default:
                                throw new UnsupportedOperationException("Only JsonObject and JsonArray are supported for http-request body type...skipping request.");
                        }
                    }
                }
                
                JsonObjectHttpResponse response = new JsonObjectHttpResponse( con );
                for(IHttpResponseReceiver receiver : receivers){
                    receiver.receiveHttpResponse(ID, response);
                }
            }catch(IOException ex){
                Logger.getLogger("global").warn("URL malformed, skipping request...");
                ex.printStackTrace();
            }finally{
                try{
                    if( con != null ){ con.disconnect(); }
                }catch(Exception ex){
                    
                }
            }
        }
    }
    
    static private class HttpRequestManagerName {
        final private String address;
        final private String contantType;
        
        private HttpRequestManagerName(String address, String contentType){
            this.address=address;
            this.contantType=contentType;
        }
        
        @Override
        public boolean equals(Object o){
            HttpRequestManagerName o2;
            if( o.getClass() == this.getClass() ){ return false; }
            return Objects.equals(this.address, ((HttpRequestManagerName)o).address) && Objects.equals(this.contantType, ((HttpRequestManagerName)o).contantType);
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 29 * hash + Objects.hashCode(this.address);
            return hash;
        }

    }
}
