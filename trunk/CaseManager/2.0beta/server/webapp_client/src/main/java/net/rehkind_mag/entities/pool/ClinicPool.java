/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.pool;

import java.util.HashMap;
import javafx.application.Platform;
import javax.json.JsonArray;
import javax.json.JsonObject;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.interfaces.client.ClientObjectList;
import net.rehkind_mag.interfaces.client.ReadOnlyClientObjectList;
import net.rehkind_mag.utils.HTTP_REQUEST_TYPE;
import net.rehkind_mag.entities.ClientClinic;
import net.rehkind_mag.http.HTTP_ENDPOINT_TEMPLATES;
import net.rehkind_mag.utils.HTTP_STATUS;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

/**
 *
 * @author rehkind
 */
public class ClinicPool extends AClientObjectPool<ClientClinic> {
    private final static String GET_CLINIC=HTTP_ENDPOINT_TEMPLATES.GET_CLINIC;
    private final static String GET_CLINICS=HTTP_ENDPOINT_TEMPLATES.GET_CLINICS;
    private final static String CREATE_CLINIC=HTTP_ENDPOINT_TEMPLATES.CREATE_CLINIC;
    private final static String UPDATE_CLINIC=HTTP_ENDPOINT_TEMPLATES.UPDATE_CLINIC;
    private final static String DELETE_CLINIC=HTTP_ENDPOINT_TEMPLATES.DELETE_CLINIC;
    
    private static ClinicPool singletonPool;
    
    private static ClientClinic defaultClinic;
    
    
    ClientObjectList<ClientClinic> cachedClinicList=new ClientObjectList();
    
    private ClinicPool(){
        //defaultClinic = ClientClinic.getClinicTemplate();
    }
    
    static public ClinicPool createPool() {
        if (ClinicPool.singletonPool == null){ ClinicPool.singletonPool=new ClinicPool(); }
        
        return ClinicPool.singletonPool;
    }
    
    @Override
    public ReadOnlyClientObjectList getAllEntities(){
        fireHTTPRequest(HTTP_ENDPOINT_TEMPLATES.GET_CLINICS, HTTP_REQUEST_TYPE.GET_ALL);
        return cachedClinicList;
    }
    
    @Override
    public ClientClinic getEntity(int clinicId) {
        if(clinicId<1){ return null; }
        ClientClinic returnClinic = this.cachedClinicList.get(clinicId);
        String templateEP = HTTP_ENDPOINT_TEMPLATES.GET_CLINIC;
        String buildEP = templateEP.replace("{ID}", ""+clinicId);
        
        HashMap<String,Object> param = new HashMap<>();
        param.put("clinic_id", clinicId);
        fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.GET, param);
        
        return (returnClinic==null) ? (ClientClinic)defaultClinic.getLocalClone() : returnClinic;
    }

    @Override
    public ClientClinic createEntity() {
        return (ClientClinic)defaultClinic.getLocalClone();
    }

    @Override
    public boolean deleteEntity(ClientClinic entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean persistEntity(ClientClinic entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            Platform.runLater(
            new Runnable() {
                @Override
                public void run() {}

            });
    }
    
    private void handleHttpResponseError(Integer requestID, IHttpResponse response){
        switch( response.getResponseStatus() ){
            
            default:
                Logger.getLogger(getClass().getName()).info("HttpResponse with status '"+response.getResponseStatus()+"' received. TODO: handle error");
        }
        
    }
}
