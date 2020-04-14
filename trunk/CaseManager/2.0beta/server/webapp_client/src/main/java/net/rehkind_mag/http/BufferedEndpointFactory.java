/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.interfaces.IHttpResponseReceiver;
import net.rehkind_mag.utils.HttpAccessRequest;
import net.rehkind_mag.utils.JsonObjectHttpResponse;
import net.rehkind_mag.utils.RegisteredHttpAccessRequest;
import net.rehkind_mag.utils.ServerSettings;
import net.rehkind_mag.http.IBufferedEndpoint;

/**
 *
 * @author rehkind
 */
public class BufferedEndpointFactory {
    static HashMap<String,IBufferedEndpoint> endpoints = new HashMap<>();
    
    static IBufferedEndpoint getBufferedEndpoint(String endpoint, ServerSettings serverSettings){
        switch( endpoint ){
            case HTTP_ENDPOINT_TEMPLATES.GET_CASE:
            case HTTP_ENDPOINT_TEMPLATES.GET_CASES:
            case HTTP_ENDPOINT_TEMPLATES.CREATE_CASE:
            case HTTP_ENDPOINT_TEMPLATES.UPDATE_CASE:
            case HTTP_ENDPOINT_TEMPLATES.DELETE_CASE:
            if(endpoints.get("CASES:"+serverSettings.serverAddress)==null){
                endpoints.put("CASES:"+serverSettings.serverAddress, CREATE_ENDPOINT(serverSettings.serverAddress));
            }
            return endpoints.get("CASES:"+serverSettings.serverAddress);
            
            case HTTP_ENDPOINT_TEMPLATES.GET_CLINIC:;
            case HTTP_ENDPOINT_TEMPLATES.GET_CLINICS:
            case HTTP_ENDPOINT_TEMPLATES.CREATE_CLINIC:
            case HTTP_ENDPOINT_TEMPLATES.UPDATE_CLINIC:
            case HTTP_ENDPOINT_TEMPLATES.DELETE_CLINIC:
            if(endpoints.get("CLINICS:"+serverSettings.serverAddress)==null){
                endpoints.put("CLINICS:"+serverSettings.serverAddress, CREATE_ENDPOINT(serverSettings.serverAddress));
            }
            return endpoints.get("CLINICS:"+serverSettings.serverAddress);
            
            case HTTP_ENDPOINT_TEMPLATES.GET_SERVICE:
            case HTTP_ENDPOINT_TEMPLATES.GET_SERVICES:
            case HTTP_ENDPOINT_TEMPLATES.CREATE_SERVICE:
            case HTTP_ENDPOINT_TEMPLATES.UPDATE_SERVICE:
            case HTTP_ENDPOINT_TEMPLATES.DELETE_SERVICE:
            if(endpoints.get("SERVICES:"+serverSettings.serverAddress)==null){
                endpoints.put("SERVICES:"+serverSettings.serverAddress, CREATE_ENDPOINT(serverSettings.serverAddress));
            }
            return endpoints.get("SERVICES:"+serverSettings.serverAddress);
            
            case HTTP_ENDPOINT_TEMPLATES.GET_SERVICE_DEFINITION:
            case HTTP_ENDPOINT_TEMPLATES.GET_SERVICE_DEFINITIONS:
            if(endpoints.get("SERVICE_DEFINITIONS:"+serverSettings.serverAddress)==null){
                endpoints.put("SERVICE_DEFINITIONS:"+serverSettings.serverAddress, CREATE_ENDPOINT(serverSettings.serverAddress));
            }
            return endpoints.get("SERVICE_DEFINITIONS:"+serverSettings.serverAddress);
            
            default:
                Logger.getLogger(BufferedEndpointFactory.class.getName()).log(Level.SEVERE, "An unknown endpoint was requested: ''{0}''\n Returning ''null'': may result in crashing everything.", endpoint);
                return null;
        }
    }
    
    private static IBufferedEndpoint CREATE_ENDPOINT(String serverAddress) {
        IBufferedEndpoint endpoint = new BufferedEndpoint() {
            HashMap<String, IHttpResponse> cachedResponses = new HashMap<>();
            HashMap<Integer, HttpAccessRequest> cachedRequests = new HashMap<>();
            
            @Override
            public IHttpResponse getCachedAndFireHttpRequest(RegisteredHttpAccessRequest registeredHttpAccessRequest, ArrayList<IHttpResponseReceiver> receivers) {
                IHttpResponse cachedResponse=cachedResponses.get(registeredHttpAccessRequest.getCompiledEndpoint());
                ArrayList<IHttpResponseReceiver> receiversExt = (ArrayList<IHttpResponseReceiver>)receivers.clone();
                receiversExt.add(this);
                
                cachedRequests.put(registeredHttpAccessRequest.getId(), registeredHttpAccessRequest);
                String httpMethod;
                try {
                    httpMethod=HTTP_ENDPOINT_TEMPLATES.get_HTTP_METHOD_FOR_ENDPOINT(registeredHttpAccessRequest.getEndpoint());
                } catch (Exception ex) {
                    Logger.getLogger(BufferedEndpointFactory.class.getName()).log(Level.SEVERE, "Cannot start HttpRequest worker, endpoint not supported: '"+registeredHttpAccessRequest.getEndpoint()+"'", ex);
                    cachedRequests.remove(registeredHttpAccessRequest.getId());
                    return null;
                }
                HttpRequestWorker worker=new HttpRequestWorker(registeredHttpAccessRequest, receiversExt, httpMethod);
                worker.start();
                
                return cachedResponse;
            }

            @Override
            public void receiveHttpResponse(IHttpResponse response) {
                // cache Response
                if( response.getResponseStatus()==200 ){
                    cachedResponses.put(cachedRequests.get(response.getRequestId()).getCompiledEndpoint(), response);
                }else{
                    cachedResponses.remove(cachedRequests.get(response.getRequestId()).getCompiledEndpoint());
                }
                cachedRequests.remove(response.getRequestId());
            }
        };
        
        return endpoint;
    }
    
    static private class HttpRequestWorker extends Thread{
        HttpAccessRequest request;
        List<IHttpResponseReceiver> receivers;
        final int ID;
        final String httpMethod;
        final ServerSettings serverSettings;
        private HttpRequestWorker(RegisteredHttpAccessRequest request, List<IHttpResponseReceiver> receivers, String httpMethod){
            this.ID = request.getId();
            this.request = request;
            this.receivers = receivers;
            this.httpMethod=httpMethod;
            this.serverSettings=request.getServerSettings();
        }
        
        @Override
        public void run(){
            org.jboss.logging.Logger.getLogger("global").info("New HttpRequest thread spawned.");
            URL url;
            HttpURLConnection con = null;
            try{
                url = new URL(serverSettings.serverAddress+request.getCompiledEndpoint());
                
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod(httpMethod);
                con.setRequestProperty("Content-Type", serverSettings.contentType);
                con.setConnectTimeout(serverSettings.connectionTimeOut);
                con.setReadTimeout(serverSettings.readTimeOut);
                con.setRequestMethod(httpMethod);
                con.setRequestProperty("Accept", serverSettings.contentType);
                
                // send http request body:
                if( "POST".equals( httpMethod ) || "PUT".equals( httpMethod ) ){
                    org.jboss.logging.Logger.getLogger("global").info( "Writing HTTP-body in Json-format to HttpURLConnection output stream.");
                    
                    con.setDoOutput(true);
                    try(OutputStream os = con.getOutputStream()) {
                        JsonWriter writer = Json.createWriter(os);
                        JsonStructure struct = request.toJsonStructure();
                        Logger.getLogger(getClass().getName()).info("HTTP_BODY: "+struct.toString());
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
                
                JsonObjectHttpResponse response = new JsonObjectHttpResponse( con, this.ID );
                for(IHttpResponseReceiver receiver : receivers){
                    receiver.receiveHttpResponse(response);
                }
            }catch(IOException ex){
                org.jboss.logging.Logger.getLogger("global").warn("URL malformed or problem with response, skipping request...");
                ex.printStackTrace();
            }finally{
                try{
                    if( con != null ){ con.disconnect(); }
                }catch(Exception ex){
                    
                }
            }
        }
    }
}
