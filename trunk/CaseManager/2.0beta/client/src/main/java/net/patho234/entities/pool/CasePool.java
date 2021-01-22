/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.json.JsonArray;
import javax.json.JsonObject;
import net.patho234.interfaces.IHttpResponse;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;
import net.patho234.utils.HTTP_REQUEST_TYPE;
import net.patho234.entities.ClientCase;
import net.patho234.entities.ClientService;
import net.patho234.http.HTTP_ENDPOINT_TEMPLATES;
import net.patho234.http.NotSignedInException;
import net.patho234.utils.HTTP_STATUS;
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
    
    HashSet<String> loadedDiagnoses=new HashSet<>();
    
    private CasePool(){
        defaultCase = ClientCase.getCaseTemplate();
    }
    
    static public CasePool createPool() {
        if (CasePool.singletonPool == null){ CasePool.singletonPool=new CasePool(); }
        
        return CasePool.singletonPool;
    }
    
    @Override
    public ReadOnlyClientObjectList getAllEntities(Boolean updatePool){
        if(updatePool){
            try{
                fireHTTPRequest(HTTP_ENDPOINT_TEMPLATES.GET_CASES, HTTP_REQUEST_TYPE.GET_ALL);
            } catch (NotSignedInException ex) {
                Logger.getLogger(getClass()).log(Level.WARN, "could not load cases", ex);
            }
        }
        return cachedCaseList;
    }
     
    @Override
    public ClientCase getEntity(int caseId, Boolean updatePool) {
        ClientCase returnCase = this.cachedCaseList.getByID(caseId);
        String templateEP = HTTP_ENDPOINT_TEMPLATES.GET_CASE;
        String buildEP = templateEP.replace("{ID}", ""+caseId);
        
        HashMap<String,Object> param = new HashMap<>();
        param.put("case_id", caseId);
        if(updatePool){
            try{
                fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.GET, param);
            } catch (NotSignedInException ex) {
                Logger.getLogger(getClass()).log(Level.WARN, "could not load case", ex);
            }
        }
        return (returnCase==null) ? defaultCase.getLocalClone() : returnCase;
    }
    
    public ClientCase getEntityByCaseNumber(String caseNumber, Boolean updatePool) {
        ClientCase returnCase = null;
        
        for( ClientCase testCase : (ClientCase[])this.cachedCaseList.toArray(new ClientCase[]{})){
            if( testCase.getCaseNumber().equals(caseNumber) ){ returnCase=testCase; }
        }
        String templateEP = HTTP_ENDPOINT_TEMPLATES.GET_CASE_BY_CASE_NUMBER;
        String buildEP = templateEP.replace("{CASE_NUMBER}", ""+caseNumber);
        
        HashMap<String,Object> param = new HashMap<>();
        param.put("case_number", caseNumber);
        if(updatePool){
        try{
            fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.GET, param);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not load case", ex);
        }
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
            Logger.getLogger(getClass()).log(Level.WARN, "could not create case", ex);
        }

        Integer[] requestIDs = pendingHttpRequests.keySet().toArray(new Integer[]{});
        
        Integer requestId = requestIDs[requestIDs.length-1];
        waitForRequest(requestId);
        
        getEntityByCaseNumber(toCreate.getCaseNumber(), true);
        return requestId;
    }

    @Override
    public int deleteEntity(ClientCase toDelete) throws TimeoutException{
        String templateEP = HTTP_ENDPOINT_TEMPLATES.DELETE_CASE;
        String buildEP = templateEP;
        HashMap<String,Object> param = new HashMap<>();
        param.put("case_id", toDelete.getId());
        param.put("case_number", toDelete.getCaseNumber());
        
        JsonObject httpBody = toDelete.toJson();
        
        try{
            fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.DELETE, httpBody, param);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not load case", ex);
        }

        Integer[] requestIDs = pendingHttpRequests.keySet().toArray(new Integer[]{});
        Integer requestId = requestIDs[requestIDs.length-1];
        waitForRequest(requestId);

        this.cachedCaseList.removeById( toDelete.getId() );
        return requestId;
    }

    @Override
    public int persistEntity(ClientCase entity, boolean forcePersist) throws TimeoutException {
        int requestIdForReturn = -1;
        if( forcePersist || entity.hasLocalChanges() ){ 
            if (forcePersist){ Logger.getLogger(getClass()).info("Persisting case entity '"+entity.getCaseNumber()+"'. [FORCED PERSIST]"); }
            else { Logger.getLogger(getClass()).info("Persisting case entity '"+entity.getCaseNumber()+"'. [LOCAL CHANGES]"); }
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
            waitForRequest(requestId); // wait till update completed

            getEntity(entity.getId(), Boolean.TRUE);
            waitFor(); // wait till updated entity reloaded

            requestIdForReturn = requestId;
        }else{
            Logger.getLogger(getClass()).info("Persisting case entity '"+entity.getCaseNumber()+"' skipped. No local changes found...set forcePersist=TRUE to force a persist.");
        }
        
        // recursely call for child entities:
        ServicePool.createPool().persistAllEntitiesForCase(entity, forcePersist);
        return requestIdForReturn;
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
        PendingRequest requestToFinish = getPendingRequest(response.getRequestId());
        
        Logger.getLogger(getClass()).info("Finishing pendingRequest with id: {0}", new Object[]{ response.getRequestId() });
        Logger.getLogger(getClass()).info("Pending request is: {0}", new Object[]{ requestToFinish });

        if( requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.GET_ALL) ){
            Logger.getLogger(getClass()).info("Received JsonArray for cases, updating local case list");
            JsonArray casesAsJsonArray = (JsonArray)response.getContent();
            Set<Integer> cachedIds= new HashSet<>();
            cachedIds.addAll( cachedCaseList.getAllIDs() );
            Set<Integer> loadedIds=new HashSet<>();
            casesAsJsonArray.forEach((curCase) -> {
                JsonObject theCase=(JsonObject)curCase;
                Logger.getLogger(getClass()).info("Processing JsonObject case: {0}", new String[]{ theCase.toString() });
                ClientCase newCase = new ClientCase(theCase);
                loadedIds.add(newCase.getId());
                loadedDiagnoses.add(newCase.getDiagnose());
                System.out.println("\t[ALL]: adding ID "+newCase.getId());
                cachedCaseList.add(newCase);
            });
            cachedIds.removeAll(loadedIds);
            
            System.out.println("[ALL] JSON LIST SIZE: "+casesAsJsonArray.size());
            System.out.println("[ALL] REMOVING "+cachedIds.size()+" IDs");
            for( Integer idToRemove : cachedIds){
                System.out.println("[ALL] REMOVING CASE WITH ID: "+idToRemove);
                cachedCaseList.removeById(idToRemove);
            }
            initialized = true;
            System.out.println("[ALL] CACHED LIST SIZE NOW "+cachedCaseList.size()+" IDs");
        } else if(requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.DELETE)){ // no case as response (deleted)
            int deletedId = (Integer)requestToFinish.getParameters().get("case_id");
            cachedCaseList.removeById(deletedId);
            Logger.getLogger(getClass().getName()).info("Case deleted successfully.");
            
        } else if(requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.CREATE)){
            Logger.getLogger(getClass().getName()).info("Received created JsonObject adding to cached objects...");
            JsonObject caseAsJsonObject = (JsonObject)response.getContent();
            ClientCase newCase = new ClientCase( caseAsJsonObject);
            cachedCaseList.add( newCase );
            loadedDiagnoses.add(newCase.getDiagnose());
            System.out.println("[CREATE] CACHED LIST SIZE NOW "+cachedCaseList.size()+" IDs");
            System.out.println("[CREATE] NEW ID "+newCase.getId());
        }else{ // all other request result in a single case as JSON object
            Logger.getLogger(getClass().getName()).info("Received JsonObject for single case, creating view...");
            JsonObject caseAsJsonObject = (JsonObject)response.getContent();
            ClientCase newCase = new ClientCase( caseAsJsonObject);
            loadedDiagnoses.add(newCase.getDiagnose());
            cachedCaseList.add( newCase );
        }
        
        if( response.getResponseStatus()!=HTTP_STATUS.CACHED ){
            finishPendingRequest(response.getRequestId());
        }
    }
    
    public ObservableList<String> getDiagnosesAsList(){
        return FXCollections.observableArrayList(loadedDiagnoses);
    }
    
}
