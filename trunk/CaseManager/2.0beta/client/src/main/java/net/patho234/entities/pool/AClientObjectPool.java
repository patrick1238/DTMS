/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.pool;

import com.sun.scenario.Settings;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import net.patho234.interfaces.IHttpResponse;
import net.patho234.interfaces.IHttpResponseReceiver;
import net.patho234.interfaces.client.IClientObject;
import net.patho234.utils.HTTP_REQUEST_TYPE;
import net.patho234.utils.HttpAccessRequest;
import net.patho234.utils.UUIDGenerator;
import net.patho234.entities.UserLogin;
import net.patho234.http.HttpRequestManager;
import net.patho234.http.NotSignedInException;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;
import net.patho234.utils.HTTP_STATUS;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public abstract class AClientObjectPool<T extends IClientObject> implements IHttpResponseReceiver {
    static UUIDGenerator uuidGen = new UUIDGenerator();
    HashMap<Integer, PendingRequest> pendingHttpRequests=new HashMap<>();
    
    
    public T getEntity(int ID){ return getEntity(ID, Boolean.FALSE); }
    abstract public T getEntity(int ID, Boolean updatePool);
    public ReadOnlyClientObjectList<T> getAllEntities(){ return getAllEntities(Boolean.FALSE); }
    abstract public ReadOnlyClientObjectList<T> getAllEntities(Boolean updatePool);
    abstract public int createEntity(T toCreate) throws TimeoutException ;
    abstract public int deleteEntity(T entity) throws TimeoutException;
    abstract public int persistEntity(T entity, boolean forcePersist) throws TimeoutException;
    public int persistEntity(T entity) throws TimeoutException { return persistEntity(entity, Boolean.FALSE); }
    
    protected void fireHTTPRequest(String endpointCompiled, String type) throws NotSignedInException{
        fireHTTPRequest(endpointCompiled, endpointCompiled, type, Json.createObjectBuilder().build(), null);
    }
    protected void fireHTTPRequest(String endpointCompiled, String type, HashMap<String,Object> param) throws NotSignedInException{
        fireHTTPRequest(endpointCompiled, endpointCompiled, type, Json.createObjectBuilder().build(), param);
    }
    protected void fireHTTPRequest(String endpointCompiled, String type, JsonStructure httpBody) throws NotSignedInException{
        fireHTTPRequest(endpointCompiled, endpointCompiled, type, httpBody, null);
    }
    protected void fireHTTPRequest(String endpointCompiled, String type, JsonStructure httpBody, HashMap<String,Object> param) throws NotSignedInException{
        fireHTTPRequest(endpointCompiled, endpointCompiled, type, httpBody, param);
    }
    protected void fireHTTPRequest(String endpointTemplate, String endpointCompiled, String type, HashMap<String, Object> param) throws NotSignedInException{
        fireHTTPRequest(endpointTemplate, endpointCompiled, type, Json.createObjectBuilder().build(), param);
    }
    protected void fireHTTPRequest(String endpointTemplate, String endpointCompiled, String type, JsonStructure httpBody, HashMap<String, Object> additionalParameters) throws NotSignedInException{
        HttpRequestManager manager = HttpRequestManager.createHttpRequestManager(Settings.get("server.address"), HttpRequestManager.CONTANT_TYPE_JSON);
        String uuid = ""; // get request don't need uuid
        if( type.equals( HTTP_REQUEST_TYPE.CREATE ) ||
            type.equals( HTTP_REQUEST_TYPE.UPDATE ) ||
            type.equals( HTTP_REQUEST_TYPE.DELETE )    ) { uuid = AClientObjectPool.uuidGen.getRandomUUIDString(); } // uuid is required for request
        
        if( UserLogin.getLoginAsJson()==null ){ throw new NotSignedInException( getClass().getSimpleName()+": can not fire HTTPRequest, user is not logged in." ); }
        
        HttpAccessRequest request = new HttpAccessRequest( endpointTemplate, endpointCompiled, uuid, UserLogin.getLoginAsJson(), httpBody );
        Logger.getLogger(getClass()).info("About to fire HTTPRequest: ");
        Logger.getLogger(getClass()).info("\tendpoint: {0}",new Object[]{endpointCompiled});
        Logger.getLogger(getClass()).info("\tbody: {0}",new Object[]{httpBody});
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
        
        //if( cachedResponse.responseSucceeded() ){ this.receiveHttpResponse(cachedResponse); }
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
    
    public void waitFor() throws TimeoutException{
        waitFor(10000);
    }
    
    public void waitFor(int timeout) throws TimeoutException{
        long start = System.currentTimeMillis();
        int loopCounter=0;
        while( pendingHttpRequests.size()>0 ){
            loopCounter++;
            if( (System.currentTimeMillis()-start)>timeout ){
                throw new TimeoutException( String.format( "[%s] HTTP requests pending to long ( > %d ms )...and are timed out.", new Object[]{getClass().getName(), timeout}) );
            }
            try {
                if( loopCounter%10 == 0 ){
                    Logger.getLogger(getClass()).info( "{0} still waitFor() - pending requests {1}", new Object[]{getClass().getName(), pendingHttpRequests.size()});
                }
                Thread.sleep(200);
            } catch (Exception e) {
            }
        }
    }
    
    public void waitForRequest(Integer requestId) throws TimeoutException{
        long start = System.currentTimeMillis();
        int loopCounter=0;
        while( pendingHttpRequests.keySet().contains(requestId) ){
            loopCounter++;
            if( (System.currentTimeMillis()-start)>100000 ){
                throw new TimeoutException( String.format( "[%s] HTTP requests pending to long ( > %d ms )...and are timed out.", new Object[]{getClass().getName(), 100000}) );
            }
            try {
                if( loopCounter%10 == 0 ){
                    StringBuilder builder=new StringBuilder();
                    for( Integer i : pendingHttpRequests.keySet() ){
                        builder.append(i).append("|");
                    }
                    Logger.getLogger(getClass()).info( "{0} still waitForRequest() - pending request {1} / {2}", new Object[]{getClass().getName(), requestId, builder.toString()});
                }
                Thread.sleep(200);
            } catch (Exception e) {
            }
        }
    }
    
    protected void handleHttpResponseError(Integer requestID, IHttpResponse response){
        switch( response.getResponseStatus() ){
            case HTTP_STATUS.CONSTRAINTS_VIOLATED:
                System.out.println( "\n[CONSTRAINTS_VIOLATED_ERROR]:\n"+response.getMessage());
                finishPendingRequest(requestID);
                break;
            case HTTP_STATUS.BAD_REQUEST:
                System.out.println( "\n[BAD_REQUEST_ERROR]:\n"+response.getMessage());
                finishPendingRequest(requestID);
                break;
            case HTTP_STATUS.INTERNAL_SERVER_ERROR:
                System.out.println( "\n[INTERNAL_SERVER_ERROR]:\n"+response.getMessage());
                finishPendingRequest(requestID);
                break;
            case HTTP_STATUS.NOT_FOUND:
                System.out.println( "\n[NOT_FOUND_ERROR]:\n"+response.getMessage()+"\n --- "+response.getContent().toString() );
                finishPendingRequest(requestID);
                
                break;
            default:
                Logger.getLogger(getClass().getName()).info("HttpResponse with status '"+response.getResponseStatus()+"' received. TODO: handle error");
                finishPendingRequest(requestID);
        }
    }
    
    protected class PendingRequest{
        int requestId;
        String requestType;
        HashMap<String,Object> parameters;
        long creationTime;
        protected PendingRequest(int requestId, String requestType, HashMap<String,Object> parameters){
            this.requestId=requestId;
            this.requestType=requestType;
            this.parameters=parameters;
            creationTime=System.currentTimeMillis();
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

        long getCreationTime() {
            return creationTime;
        }
        
    }
}
