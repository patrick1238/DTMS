/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.pool;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import javafx.application.Platform;
import javax.json.JsonArray;
import javax.json.JsonObject;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.interfaces.client.ClientObjectList;
import net.rehkind_mag.interfaces.client.ReadOnlyClientObjectList;
import net.rehkind_mag.utils.HTTP_REQUEST_TYPE;
import net.rehkind_mag.entities.ClientClinic;
import net.rehkind_mag.http.HTTP_ENDPOINT_TEMPLATES;
import net.rehkind_mag.http.NotSignedInException;
import net.rehkind_mag.utils.HTTP_STATUS;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

/**
 *
 * @author rehkind
 */
public class ClinicPool extends AClientObjectPool<ClientClinic> {
    
    private static ClinicPool singletonPool;
    
    private static ClientClinic defaultClinic;
    
    
    ClientObjectList<ClientClinic> cachedClinicList=new ClientObjectList();
    
    private ClinicPool(){
        defaultClinic = ClientClinic.getClinicTemplate();
    }
    
    static public ClinicPool createPool() {
        if (ClinicPool.singletonPool == null){ ClinicPool.singletonPool=new ClinicPool(); }
        
        return ClinicPool.singletonPool;
    }
    
    @Override
    public ReadOnlyClientObjectList getAllEntities(){
        try{    
            fireHTTPRequest(HTTP_ENDPOINT_TEMPLATES.GET_CLINICS, HTTP_REQUEST_TYPE.GET_ALL);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not load clinics", ex);
        }
        return cachedClinicList;
    }
    
    @Override
    public ClientClinic getEntity(int clinicId) {
        if(clinicId<1){ return null; }
        ClientClinic returnClinic = this.cachedClinicList.getByID(clinicId);
        String templateEP = HTTP_ENDPOINT_TEMPLATES.GET_CLINIC;
        String buildEP = templateEP.replace("{ID}", ""+clinicId);
        
        HashMap<String,Object> param = new HashMap<>();
        param.put("clinic_id", clinicId);
        try{
            fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.GET, param);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not load clinic", ex);
        }
        return (returnClinic==null) ? (ClientClinic)defaultClinic.getLocalClone() : returnClinic;
    }

    @Override
    public int createEntity(ClientClinic toCreate)  throws TimeoutException {
        String templateEP = HTTP_ENDPOINT_TEMPLATES.CREATE_CLINIC;
        String buildEP = templateEP;
        HashMap<String,Object> param = new HashMap<>();
        param.put("case_name", toCreate.getName());
        
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
    public int deleteEntity(ClientClinic toDelete) throws TimeoutException{
        String templateEP = HTTP_ENDPOINT_TEMPLATES.DELETE_CLINIC;
        String buildEP = templateEP;
        HashMap<String,Object> param = new HashMap<>();
        param.put("clinic_id", toDelete.getId());
        param.put("clinic_name", toDelete.getName());
        
        JsonObject httpBody = toDelete.toJson();
        
        try{
            fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.DELETE, httpBody, param);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not delete clinic", ex);
        }

        Integer[] requestIDs = pendingHttpRequests.keySet().toArray(new Integer[]{});
        Integer requestId = requestIDs[requestIDs.length-1];
        waitForRequest(requestId);

        this.cachedClinicList.removeById( toDelete.getId() );
        return requestId;
    }

    @Override
    public int persistEntity(ClientClinic entity) throws TimeoutException{
        String templateEP = HTTP_ENDPOINT_TEMPLATES.UPDATE_CLINIC;
        String buildEP = templateEP;
        
        HashMap<String,Object> param = new HashMap<>();
        param.put("clinic_id", entity.getId());
        
        JsonObject httpBody = entity.toJson();
        try{
            fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.UPDATE, httpBody, param);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not update clinic", ex);
        }

        Integer[] requestIDs = pendingHttpRequests.keySet().toArray(new Integer[]{});
        Integer requestId = requestIDs[requestIDs.length-1];
        
        waitForRequest(requestId);
        getAllEntities();
        waitFor();
        
        return requestId;
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
        PendingRequest requestToFinish = getPendingRequest(response.getRequestId());;
                
        Logger.getLogger(getClass()).info("Finishing pendingRequest with id: {0}", new Object[]{ response.getRequestId() });
        Logger.getLogger(getClass()).info("Pending request is: {0}", new Object[]{ requestToFinish });

        if( requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.GET_ALL) ){
            Logger.getLogger(getClass()).info("Received JsonArray for cases, updating local clinic list");
            JsonArray casesAsJsonArray = (JsonArray)response.getContent();

            casesAsJsonArray.forEach((clinic) -> {
                JsonObject joClinic=(JsonObject)clinic;
                Logger.getLogger(getClass()).info("Processing JsonObject clinic: {0}", new String[]{ joClinic.toString() });

                cachedClinicList.put(new ClientClinic(joClinic));
            });
        } else if(requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.DELETE)){ // no case as response (deleted)
            Logger.getLogger(getClass().getName()).info("Clinic deleted successfully.");
        } else{ // all other request result in a single clinic as JSON object
            Logger.getLogger(getClass().getName()).info("Received JsonObject for single clinic, creating view...");
            JsonObject clinicAsJsonObject = (JsonObject)response.getContent();

            cachedClinicList.put(new ClientClinic( clinicAsJsonObject) );
        }

        if(! (response.getResponseStatus()==HTTP_STATUS.CACHED) ){ finishPendingRequest(response.getRequestId()); }
    }
    
    private void handleHttpResponseError(Integer requestID, IHttpResponse response){
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
            default:
                Logger.getLogger(getClass().getName()).info("HttpResponse with status '"+response.getResponseStatus()+"' received. TODO: handle error");
        }
    }
}
