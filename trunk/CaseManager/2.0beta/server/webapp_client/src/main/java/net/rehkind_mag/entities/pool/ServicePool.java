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
import net.rehkind_mag.entities.ClientService;
import net.rehkind_mag.http.HTTP_ENDPOINT_TEMPLATES;
import net.rehkind_mag.http.NotSignedInException;
import net.rehkind_mag.utils.HTTP_STATUS;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

/**
 *
 * @author rehkind
 */
public class ServicePool extends AClientObjectPool<ClientService> {
    
    private static ServicePool singletonPool;
    
    private static ClientService defaultService;
    
    
    ClientObjectList<ClientService> cachedServiceList=new ClientObjectList();
    
    private ServicePool(){
        defaultService = ClientService.getServiceTemplate(-1, -1);
    }
    
    static public ServicePool createPool() {
        if (ServicePool.singletonPool == null){ ServicePool.singletonPool=new ServicePool(); }
        
        return ServicePool.singletonPool;
    }
    
    @Override
    public ReadOnlyClientObjectList getAllEntities(){
        try {
            fireHTTPRequest(HTTP_ENDPOINT_TEMPLATES.GET_SERVICES, HTTP_REQUEST_TYPE.GET_ALL);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not load services", ex);
        }
        return cachedServiceList;
    }
    
    @Override
    public ClientService getEntity(int serviceId) {
        ClientService returnService = this.cachedServiceList.getByID(serviceId);
        String templateEP = HTTP_ENDPOINT_TEMPLATES.GET_SERVICE;
        String buildEP = templateEP.replace("{ID}", ""+serviceId);
        
        HashMap<String,Object> param = new HashMap<>();
        param.put("service_id", serviceId);
        try{
            fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.GET, param);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not load service", ex);
        }
        return (returnService==null) ? defaultService.getLocalClone() : returnService;
    }

    @Override
    public int createEntity(ClientService s) throws TimeoutException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteEntity(ClientService entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean persistEntity(ClientService entity) {
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
        
        Platform.runLater(new Runnable() {
            @Override
                public void run() {
            }
        });
                
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
            Logger.getLogger(getClass()).info("Received JsonArray for services, updating local service list");
            JsonArray servicesAsJsonArray = (JsonArray)response.getContent();

            servicesAsJsonArray.forEach((curService) -> {
                JsonObject theService=(JsonObject)curService;
                Logger.getLogger(getClass()).info("Processing JsonObject service: {0}", new String[]{ theService.toString() });

                cachedServiceList.put(new ClientService(theService));
            });
        } else if(requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.DELETE)){ // no service as response (deleted)
            Logger.getLogger(getClass().getName()).info("Service deleted successfully.");
        } else{ // all other request result in a single service as JSON object
            Logger.getLogger(getClass().getName()).info("Received JsonObject for single service, creating view...");
            JsonObject serviceAsJsonObject = (JsonObject)response.getContent();

            cachedServiceList.put(new ClientService( serviceAsJsonObject) );
        }

    }
    
    private void handleHttpResponseError(Integer requestID, IHttpResponse response){
        switch( response.getResponseStatus() ){
            
            default:
                Logger.getLogger(getClass().getName()).info("HttpResponse with status '"+response.getResponseStatus()+"' received. TODO: handle error");
        }
        
    }
}
