/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.pool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.json.JsonArray;
import javax.json.JsonObject;
import net.patho234.interfaces.IHttpResponse;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;
import net.patho234.utils.HTTP_REQUEST_TYPE;
import net.patho234.entities.ClientClinic;
import net.patho234.http.HTTP_ENDPOINT_TEMPLATES;
import net.patho234.http.NotSignedInException;
import net.patho234.utils.HTTP_STATUS;
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
    
    HashSet<String> loadedClinicNames = new HashSet<>();
    
    private ClinicPool(){
        defaultClinic = ClientClinic.getClinicTemplate();
    }
    
    static public ClinicPool createPool() {
        if (ClinicPool.singletonPool == null){ ClinicPool.singletonPool=new ClinicPool(); }
        
        return ClinicPool.singletonPool;
    }
    
    @Override
    public ReadOnlyClientObjectList getAllEntities(Boolean updatePool){
        if(updatePool){
            try{    
                fireHTTPRequest(HTTP_ENDPOINT_TEMPLATES.GET_CLINICS, HTTP_REQUEST_TYPE.GET_ALL);
            } catch (NotSignedInException ex) {
                Logger.getLogger(getClass()).log(Level.WARN, "could not load clinics", ex);
            }
        }
        return cachedClinicList;
    }
    
    @Override
    public ClientClinic getEntity(int clinicId, Boolean updatePool) {
        if(clinicId<1){ return null; }
        ClientClinic returnClinic = this.cachedClinicList.getByID(clinicId);
        String templateEP = HTTP_ENDPOINT_TEMPLATES.GET_CLINIC;
        String buildEP = templateEP.replace("{ID}", ""+clinicId);
        
        HashMap<String,Object> param = new HashMap<>();
        param.put("clinic_id", clinicId);
        if(updatePool){
        try{
            fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.GET, param);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not load clinic", ex);
        }}
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
    public int persistEntity(ClientClinic entity, boolean forcePersist) throws TimeoutException{
        Integer requestId = -1;
        if( forcePersist || entity.hasLocalChanges() ){
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
            requestId = requestIDs[requestIDs.length-1];

            waitForRequest(requestId);
            getAllEntities();
            waitFor();
        }else{
            Logger.getLogger(getClass()).info("Persisting clinic entity '"+entity.getName()+"' [id="+entity.getId()+"] skipped. No local changes found...set forcePersist=TRUE to force a persist.");
        }
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
            Logger.getLogger(getClass()).info("Received JsonArray for clinics, updating local clinic list");
            JsonArray clinicsAsJsonArray = (JsonArray)response.getContent();

            clinicsAsJsonArray.forEach((clinic) -> {
                JsonObject joClinic=(JsonObject)clinic;
                Logger.getLogger(getClass()).info("Processing JsonObject clinic: {0}", new String[]{ joClinic.toString() });
                ClientClinic newClinic = new ClientClinic(joClinic);
                loadedClinicNames.add(newClinic.getName());
                cachedClinicList.add( newClinic );
            });
        } else if(requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.DELETE)){ // no case as response (deleted)
            Logger.getLogger(getClass().getName()).info("Clinic deleted successfully.");
        } else{ // all other request result in a single clinic as JSON object
            Logger.getLogger(getClass().getName()).info("Received JsonObject for single clinic, creating view...");
            JsonObject clinicAsJsonObject = (JsonObject)response.getContent();
            ClientClinic newClinic = new ClientClinic(clinicAsJsonObject);
            loadedClinicNames.add(newClinic.getName());
            cachedClinicList.add( newClinic );
        }

        if(! (response.getResponseStatus()==HTTP_STATUS.CACHED) ){ finishPendingRequest(response.getRequestId()); }
    }
    
    public ObservableList<String> getClinicsAsList(){
        return FXCollections.observableArrayList(loadedClinicNames);
    }

    public ClientClinic getClinicByName(String clinicName) {
        for( ClientClinic c : cachedClinicList ){
            if( c.getName().equals(clinicName) ){ return c; }
        }
        
        return null;
    }
}
