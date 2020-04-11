/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.pool;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import javafx.application.Platform;
import javax.json.JsonArray;
import javax.json.JsonObject;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.interfaces.client.ClientObjectList;
import net.rehkind_mag.interfaces.client.ReadOnlyClientObjectList;
import net.rehkind_mag.utils.HTTP_REQUEST_TYPE;
import net.rehkind_mag.entities.ClientCase;
import net.rehkind_mag.http.HTTP_ENDPOINT_TEMPLATES;
import net.rehkind_mag.http.NotSignedInException;
import net.rehkind_mag.utils.HTTP_STATUS;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

/**
 *
 * @author rehkind
 */
public class CasePool extends AClientObjectPool<ClientCase> {
    
    private static CasePool singletonPool;
    
    private static ClientCase defaultCase;
    
    
    ClientObjectList<ClientCase> cachedCaseList=new ClientObjectList();
    
    private CasePool(){
        defaultCase = ClientCase.getCaseTemplate();
    }
    
    static public CasePool createPool() {
        if (CasePool.singletonPool == null){ CasePool.singletonPool=new CasePool(); }
        
        return CasePool.singletonPool;
    }
    
    @Override
    public ReadOnlyClientObjectList getAllEntities(){
        try{
            fireHTTPRequest(HTTP_ENDPOINT_TEMPLATES.GET_CASES, HTTP_REQUEST_TYPE.GET_ALL);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not load cases", ex);
        }
        return cachedCaseList;
    }
    
    @Override
    public ClientCase getEntity(int caseId) {
        ClientCase returnCase = this.cachedCaseList.getByID(caseId);
        String templateEP = HTTP_ENDPOINT_TEMPLATES.GET_CASE;
        String buildEP = templateEP.replace("{ID}", ""+caseId);
        
        HashMap<String,Object> param = new HashMap<>();
        param.put("case_id", caseId);
        try{
            fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.GET, param);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not load case", ex);
        }
        return (returnCase==null) ? defaultCase.getLocalClone() : returnCase;
    }

    @Override
    public int createEntity(ClientCase toCreate)  throws TimeoutException {
        String templateEP = HTTP_ENDPOINT_TEMPLATES.CREATE_CASE;
        String buildEP = templateEP;
        HashMap<String,Object> param = new HashMap<>();
        param.put("case_number", toCreate.getCaseNumber());
        
        JsonObject httpBody = toCreate.toJson();
        
        try{
            fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.CREATE, httpBody, param);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not reate case", ex);
        }

        Integer[] requestIDs = pendingHttpRequests.keySet().toArray(new Integer[]{});
        
        Integer requestId = requestIDs[requestIDs.length-1];
        return requestId;
    }

    @Override
    public boolean deleteEntity(ClientCase entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean persistEntity(ClientCase entity) throws TimeoutException {
        String templateEP = HTTP_ENDPOINT_TEMPLATES.UPDATE_CASE;
        String buildEP = templateEP;
        
        HashMap<String,Object> param = new HashMap<>();
        param.put("case_id", entity.getId());
        
        JsonObject httpBody = entity.toJson();
        try{
            fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.UPDATE, httpBody, param);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not update case", ex);
        }

        Integer[] requestIDs = pendingHttpRequests.keySet().toArray(new Integer[]{});
        
        Integer requestId = requestIDs[requestIDs.length-1];
        waitForRequest(requestId);
        return true;
    }

    @Override
    public void receiveHttpResponse(IHttpResponse response) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "HttpResponse received: id={0} status: {1} message: {2}", new Object[]{response.getRequestId(), response.getResponseStatus(), response.getMessage()});
        long timeMS=-1;
        Boolean isCached=false;
        if( response.getResponseStatus()==HTTP_STATUS.CACHED ){
            Logger.getLogger(getClass().getName()).log(Level.INFO, "[CACHED] HttpResponse {0} ms.", new Object[]{timeMS});
            isCached=true;
        }else{
            Logger.getLogger(getClass().getName()).log(Level.INFO, "[HTTP] HttpResponse {0} ms.", new Object[]{timeMS});
        }
        
        if( response.getResponseStatus()!=HTTP_STATUS.OK && response.getResponseStatus()!=HTTP_STATUS.CACHED ){
            handleHttpResponseError(response.getRequestId(), response);
            return;
        }
                
        printPendingRequests();
        PendingRequest requestToFinish;
        if( response.getResponseStatus()==HTTP_STATUS.CACHED ){
            requestToFinish = getPendingRequest(response.getRequestId());
        }else{
            requestToFinish = finishPendingRequest(response.getRequestId());
        }
        Logger.getLogger(getClass()).info("Finishing pendingRequest with id: {0}", new Object[]{ response.getRequestId() });
        Logger.getLogger(getClass()).info("Pending request is: {0}", new Object[]{ requestToFinish });

        if( requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.GET_ALL) ){
            Logger.getLogger(getClass()).info("Received JsonArray for cases, updating local case list");
            JsonArray casesAsJsonArray = (JsonArray)response.getContent();

            casesAsJsonArray.forEach((curCase) -> {
                JsonObject theCase=(JsonObject)curCase;
                Logger.getLogger(getClass()).info("Processing JsonObject case: {0}", new String[]{ theCase.toString() });

                cachedCaseList.put(new ClientCase(theCase));
            });
        } else if(requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.DELETE)){ // no case as response (deleted)
            Logger.getLogger(getClass().getName()).info("Case deleted successfully.");
        } else{ // all other request result in a single case as JSON object
            Logger.getLogger(getClass().getName()).info("Received JsonObject for single case, creating view...");
            JsonObject caseAsJsonObject = (JsonObject)response.getContent();

            cachedCaseList.put(new ClientCase( caseAsJsonObject) );
        }

    }
    
    private void handleHttpResponseError(Integer requestID, IHttpResponse response){
        switch( response.getResponseStatus() ){
            case HTTP_STATUS.CONSTRAINTS_VIOLATED:
                System.out.println( "\n[CONSTRAINTS_VIOLATED_ERROR]:\n"+response.getMessage());
            default:
                Logger.getLogger(getClass().getName()).info("HttpResponse with status '"+response.getResponseStatus()+"' received. TODO: handle error");
        }
        
    }
}
