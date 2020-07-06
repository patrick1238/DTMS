/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.pool;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import javax.json.JsonArray;
import javax.json.JsonObject;
import net.patho234.entities.ClientSubmitter;
import net.patho234.http.HTTP_ENDPOINT_TEMPLATES;
import net.patho234.http.NotSignedInException;
import net.patho234.interfaces.IHttpResponse;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;
import net.patho234.utils.HTTP_REQUEST_TYPE;
import net.patho234.utils.HTTP_STATUS;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

/**
 *
 * @author rehkind
 */
public class SubmitterPool extends AClientObjectPool<ClientSubmitter>{

    private static SubmitterPool singletonPool;
    private static ClientSubmitter defaultSubmitter;
    
    ClientObjectList<ClientSubmitter> cachedSubmitterList=new ClientObjectList();
    
    private SubmitterPool(){
        defaultSubmitter = ClientSubmitter.getSubmitterTemplate();
    }
    
    static public SubmitterPool createPool() {
        if (SubmitterPool.singletonPool == null){ SubmitterPool.singletonPool=new SubmitterPool(); }
        
        return SubmitterPool.singletonPool;
    }
    
    @Override
    public ClientSubmitter getEntity(int submitterId, Boolean updatePool) {
        if(submitterId<1){ return null; }
        ClientSubmitter returnSubmitter = this.cachedSubmitterList.getByID(submitterId);
        String templateEP = HTTP_ENDPOINT_TEMPLATES.GET_SUBMITTER;
        String buildEP = templateEP.replace("{SUBMITTERID}", ""+submitterId);
        
        HashMap<String,Object> param = new HashMap<>();
        param.put("submitter_id", submitterId);
        if(updatePool){
        try{
            fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.GET, param);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not load Submitter", ex);
        }}
        return (returnSubmitter==null) ? (ClientSubmitter)defaultSubmitter.getLocalClone() : returnSubmitter;
    }

    @Override
    public ReadOnlyClientObjectList<ClientSubmitter> getAllEntities(Boolean updatePool) {
        if(updatePool){
            try{    
                fireHTTPRequest(HTTP_ENDPOINT_TEMPLATES.GET_SUBMITTERS, HTTP_REQUEST_TYPE.GET_ALL);
            } catch (NotSignedInException ex) {
                Logger.getLogger(getClass()).log(Level.WARN, "could not load submitters", ex);
            }
        }
        return cachedSubmitterList;
    }

    @Override
    public int createEntity(ClientSubmitter toCreate) throws TimeoutException {
        String templateEP = HTTP_ENDPOINT_TEMPLATES.CREATE_SUBMITTER;
        String buildEP = templateEP;
        HashMap<String,Object> param = new HashMap<>();
        param.put("submitter_name", toCreate.getForename()+" "+toCreate.getSurname());
        Logger.getLogger(getClass()).info("About to create submitter: "+toCreate.getForename()+" "+toCreate.getSurname());
        JsonObject httpBody = toCreate.toJson();
        
        try{
            fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.CREATE, httpBody, param);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not create clinic", ex);
        }
        
        Integer[] requestIDs = pendingHttpRequests.keySet().toArray(new Integer[]{});
        
        Integer requestId = requestIDs[requestIDs.length-1];
        waitForRequest(requestId);
        return requestId;
    }

    @Override
    public int deleteEntity(ClientSubmitter entity) throws TimeoutException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int persistEntity(ClientSubmitter entity, boolean forcePersist) throws TimeoutException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receiveHttpResponse(IHttpResponse response) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "HttpResponse received: id={0} status: {1} message: {2}", new Object[]{response.getRequestId(), response.getResponseStatus(), response.getMessage()});

        
        if( response.getResponseStatus()!=HTTP_STATUS.OK && response.getResponseStatus()!=HTTP_STATUS.CACHED ){
            handleHttpResponseError(response.getRequestId(), response);
            return;
        }
        
        printPendingRequests();
        PendingRequest requestToFinish = getPendingRequest(response.getRequestId());;
        
        long timeMS=System.currentTimeMillis()-requestToFinish.getCreationTime();
        Boolean isCached=false;
        if( response.getResponseStatus()==HTTP_STATUS.CACHED ){
            Logger.getLogger(getClass().getName()).log(Level.INFO, "[CACHED] HttpResponse {0} ms.", new Object[]{timeMS});
            isCached=true;
        }else{
            Logger.getLogger(getClass().getName()).log(Level.INFO, "[HTTP] HttpResponse {0} ms.", new Object[]{timeMS});
        }
        
        
        Logger.getLogger(getClass()).info("Finishing pendingRequest with id: {0}", new Object[]{ response.getRequestId() });
        Logger.getLogger(getClass()).info("Pending request is: {0}", new Object[]{ requestToFinish });

        if( requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.GET_ALL) ){
            Logger.getLogger(getClass()).info("Received JsonArray for cases, updating local submitter list");
            JsonArray submittersAsJsonArray = (JsonArray)response.getContent();

            submittersAsJsonArray.forEach((clinic) -> {
                JsonObject joSubmitter=(JsonObject)clinic;
                Logger.getLogger(getClass()).info("Processing JsonObject submitter: {0}", new String[]{ joSubmitter.toString() });

                cachedSubmitterList.add(new ClientSubmitter(joSubmitter));
            });
        } else{ // all other request result in a single submitter as JSON object
            Logger.getLogger(getClass().getName()).info("Received JsonObject for single submitter, creating view...");
            JsonObject clinicAsJsonObject = (JsonObject)response.getContent();

            cachedSubmitterList.add(new ClientSubmitter( clinicAsJsonObject) );
        }

        if(! (response.getResponseStatus()==HTTP_STATUS.CACHED) ){ finishPendingRequest(response.getRequestId()); }
    }
    
}
