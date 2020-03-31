/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkindmag.entities.pool;

import com.sun.scenario.Settings;
import java.util.HashMap;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.interfaces.IHttpResponseReceiver;
import net.rehkind_mag.interfaces.client.IClientObject;
import net.rehkind_mag.utils.HTTP_REQUEST_TYPE;
import net.rehkind_mag.utils.HttpAccessRequest;
import net.rehkind_mag.utils.UUIDGenerator;
import net.rehkindmag.entities.UserLogin;
import net.rehkindmag.http.HttpRequestManager;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public abstract class AClientObjectPool<T extends IClientObject> implements IHttpResponseReceiver {
    static UUIDGenerator uuidGen = new UUIDGenerator();
    HashMap<Integer, PendingRequest> pendingHttpRequests=new HashMap<>();
    
    
    abstract public T getEntity(int ID);
    abstract public List<T> getAllEntities();
    abstract public T createEntity();
    abstract public boolean deleteEntity(T entity);
    abstract public boolean persistEntity(T entity);
    
    protected void fireHTTPRequest(String endpointCompiled, String type){
        fireHTTPRequest(endpointCompiled, endpointCompiled, type, Json.createObjectBuilder().build(), null);
    }
    protected void fireHTTPRequest(String endpointCompiled, String type, HashMap<String,Object> param){
        fireHTTPRequest(endpointCompiled, endpointCompiled, type, Json.createObjectBuilder().build(), param);
    }
    protected void fireHTTPRequest(String endpointCompiled, String type, JsonObject httpBody){
        fireHTTPRequest(endpointCompiled, endpointCompiled, type, httpBody, null);
    }
    protected void fireHTTPRequest(String endpointCompiled, String type, JsonObject httpBody, HashMap<String,Object> param){
        fireHTTPRequest(endpointCompiled, endpointCompiled, type, httpBody, param);
    }
    protected void fireHTTPRequest(String endpointTemplate, String endpointCompiled, String type, JsonObject httpBody, HashMap<String, Object> additionalParameters){
        HttpRequestManager manager = HttpRequestManager.createHttpRequestManager(Settings.get("server.address"), HttpRequestManager.CONTANT_TYPE_JSON);
        String uuid = ""; // get request don't need uuid
        if( type.equals( HTTP_REQUEST_TYPE.CREATE ) ||
            type.equals( HTTP_REQUEST_TYPE.UPDATE ) ||
            type.equals( HTTP_REQUEST_TYPE.DELETE )    ) { uuid = AClientObjectPool.uuidGen.getRandomUUIDString(); } // uuid is required for request
        
        HttpAccessRequest request = new HttpAccessRequest( endpointTemplate, endpointCompiled, uuid, UserLogin.getLoginAsJson(), httpBody );
        IHttpResponse cachedResponse = manager.fireJsonHttpRequest(request, this);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("http_type", type);
        parameters.put("http_uuid", uuid);
        parameters.put("http_body", httpBody);
        parameters.put("http_endpoint_template", endpointTemplate);
        parameters.put("http_endpoint_compiled", endpointCompiled);
        
        if(additionalParameters != null){
            parameters.putAll(additionalParameters);
        }
        PendingRequest pr = new PendingRequest(cachedResponse.getRequestId(), type, parameters);
        pendingHttpRequests.put( cachedResponse.getRequestId(), pr );
        Logger.getLogger(getClass()).info("New pending request added: "+pr.toString());
        
        if( cachedResponse.responseSucceeded() ){ this.receiveHttpResponse(cachedResponse); }
    }
    
    protected PendingRequest finishPendingRequest(int id){
        PendingRequest requestToFinish = pendingHttpRequests.get(id);
        pendingHttpRequests.remove(id);
        return requestToFinish;
    }
    protected PendingRequest getPendingRequest(int id){
        return pendingHttpRequests.get(id);
    }
    
    protected void printPendingRequests(){
        StringBuilder builder = new StringBuilder();
        builder.append("All pending requests are:\n");
        for( Integer id : pendingHttpRequests.keySet() ){
            builder.append(pendingHttpRequests.get(id).toString());
        }
        Logger.getLogger(getClass()).info( builder.toString() );
    }
    
    protected class PendingRequest{
        int requestId;
        String requestType;
        HashMap<String,Object> parameters;
        
        protected PendingRequest(int requestId, String requestType, HashMap<String,Object> parameters){
            this.requestId=requestId;
            this.requestType=requestType;
            this.parameters=parameters;
        }

        public int getRequestId() {
            return requestId;
        }

        public String getRequestType() {
            return requestType;
        }

        public HashMap<String, Object> getParameters() {
            return parameters;
        }
        
        
        @Override public String toString(){
            return String.format("PendingRequest[%d]: type=%s parameter.size()=%d", new Object[]{ requestId, requestType, parameters.size()});
        }
    }
}
