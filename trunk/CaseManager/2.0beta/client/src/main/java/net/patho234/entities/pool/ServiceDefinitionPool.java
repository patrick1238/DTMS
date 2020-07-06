/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.pool;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.json.JsonArray;
import javax.json.JsonObject;
import net.patho234.entities.ClientServiceDefinition;
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
public class ServiceDefinitionPool extends AClientObjectPool<ClientServiceDefinition> {
    private static ServiceDefinitionPool singletonPool;
    
    private static ClientServiceDefinition defaultServiceDefinition;
    
    private ClientObjectList<ClientServiceDefinition> cachedServiceDefinitionList=new ClientObjectList<>();
    
    private ServiceDefinitionPool(){}
    
    static public ServiceDefinitionPool createPool() {
        if (ServiceDefinitionPool.singletonPool == null){ ServiceDefinitionPool.singletonPool=new ServiceDefinitionPool(); }
        defaultServiceDefinition = ClientServiceDefinition.getServiceDefinitionTemplate();
        return ServiceDefinitionPool.singletonPool;
    }
     
    @Override
    public ReadOnlyClientObjectList getAllEntities(Boolean updatePool){
        if(updatePool){
            try {
                fireHTTPRequest(HTTP_ENDPOINT_TEMPLATES.GET_SERVICE_DEFINITIONS, HTTP_REQUEST_TYPE.GET_ALL);
            } catch (NotSignedInException ex) {
                Logger.getLogger(getClass()).log(Level.WARN, "could not load service_definitions", ex);
            }
        }
        return cachedServiceDefinitionList;
    }
    
    @Override
    public ClientServiceDefinition getEntity(int serviceDefId, Boolean updatePool) {
        ClientServiceDefinition returnService = this.cachedServiceDefinitionList.getByID(serviceDefId);
        // System.out.println("ServiceDef["+serviceDefId+"] requested...returning cached object: "+returnService+ " (original: "+((returnService!=null)?returnService.getOriginalJson():"<NOT_DEFINED>")+")");
        String templateEP = HTTP_ENDPOINT_TEMPLATES.GET_SERVICE_DEFINITION;
        String buildEP = templateEP.replace("{ID}", ""+serviceDefId);
        
        HashMap<String,Object> param = new HashMap<>();
        param.put("service_id", serviceDefId);
        if( updatePool ){
            try{
                fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.GET, param);
            } catch (NotSignedInException ex) {
                Logger.getLogger(getClass()).log(Level.WARN, "could not load service", ex);
            }
        }
        return (returnService==null) ? defaultServiceDefinition.getLocalClone() : returnService;
    }


    @Override
    public int createEntity(ClientServiceDefinition toCreate) throws TimeoutException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int deleteEntity(ClientServiceDefinition entity) throws TimeoutException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int persistEntity(ClientServiceDefinition entity, boolean forcePersist) throws TimeoutException {
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
        PendingRequest requestToFinish = getPendingRequest(response.getRequestId());

        Logger.getLogger(getClass()).info("Finishing pendingRequest with id: {0}", new Object[]{ response.getRequestId() });
        Logger.getLogger(getClass()).info("Pending request is: {0}", new Object[]{ requestToFinish });

        if( requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.GET_ALL) ){
            Logger.getLogger(getClass()).info("Received JsonArray for service_definitions, updating local service_definition list");
            JsonArray service_definitionsAsJsonArray = (JsonArray)response.getContent();

            service_definitionsAsJsonArray.forEach((curDefinition) -> {
                JsonObject theDefinition=(JsonObject)curDefinition;
                Logger.getLogger(getClass()).info("Processing JsonObject service_definition: {0}", new String[]{ theDefinition.toString() });

                cachedServiceDefinitionList.add(new ClientServiceDefinition(theDefinition));
            });
        } else if(requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.DELETE)){ // no service as response (deleted)
            Logger.getLogger(getClass().getName()).info("Service deleted successfully.");
        } else{ // all other request result in a single service as JSON object
            Logger.getLogger(getClass().getName()).info("Received JsonObject for single service_definition, creating ClientServiceDefinition...");
            JsonObject serviceDefAsJsonObject = (JsonObject)response.getContent();

            cachedServiceDefinitionList.add(new ClientServiceDefinition( serviceDefAsJsonObject) );
        }
        
        if( response.getResponseStatus()!=HTTP_STATUS.CACHED ){
            finishPendingRequest(response.getRequestId());
        }
    }
    
}
