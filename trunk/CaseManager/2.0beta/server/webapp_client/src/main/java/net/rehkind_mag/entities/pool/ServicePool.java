/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.pool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeoutException;
import javafx.application.Platform;
import javax.json.JsonArray;
import javax.json.JsonObject;
import net.rehkind_mag.entities.ClientCase;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.interfaces.client.ClientObjectList;
import net.rehkind_mag.interfaces.client.ReadOnlyClientObjectList;
import net.rehkind_mag.utils.HTTP_REQUEST_TYPE;
import net.rehkind_mag.entities.ClientService;
import net.rehkind_mag.entities.ClientServiceDefinition;
import net.rehkind_mag.entities.filter.ClientObjectFilterBase;
import net.rehkind_mag.entities.filter.ClientServicesForCaseFilter;
import net.rehkind_mag.entities.filter.ClientServicesForDefinitionFilter;
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
    
    private ServicePool(){ }
    
    static public ServicePool createPool() {
        if (ServicePool.singletonPool == null){ ServicePool.singletonPool=new ServicePool(); }
        defaultService = ClientService.getServiceTemplate(1, -1);
        return ServicePool.singletonPool;
    }
    
    @Override
    public ReadOnlyClientObjectList getAllEntities( Boolean updatePool ){
        if(updatePool){
            try {
                fireHTTPRequest(HTTP_ENDPOINT_TEMPLATES.GET_SERVICES, HTTP_REQUEST_TYPE.GET_ALL);
            } catch (NotSignedInException ex) {
                Logger.getLogger(getClass()).log(Level.WARN, "could not load services", ex);
            }
        }
        return cachedServiceList;
    }
    
    public ReadOnlyClientObjectList getAllEntitiesForCase(ClientCase requestCase) {
        return getAllEntitiesForCase(requestCase, false);
    }
    
    public ReadOnlyClientObjectList getAllEntitiesForCase(ClientCase requestCase, boolean updatePool) {
        ClientServicesForCaseFilter filter = new ClientServicesForCaseFilter(requestCase);
        if( updatePool ){
            try {
                String endpointCompiled = HTTP_ENDPOINT_TEMPLATES.GET_SERVICES_FOR_CASE.replace("{CASEID}", ""+requestCase.getId());
                HashMap<String,Object> param = new HashMap<>();
                param.put("case_id", requestCase.getId());
                param.put("list_filter", filter);
                fireHTTPRequest( HTTP_ENDPOINT_TEMPLATES.GET_SERVICES_FOR_CASE, endpointCompiled, HTTP_REQUEST_TYPE.GET_ALL_FILTERED, param );
            } catch (NotSignedInException ex) {
                Logger.getLogger(getClass()).log(Level.WARN, "could not load services", ex);
            }
        }
        ReadOnlyClientObjectList<ClientService> unfilteredList = getAllEntities(false);
        return filter.filterClientObjectList(unfilteredList);
    }
    
    public ReadOnlyClientObjectList getAllEntitiesForDefinition(ClientServiceDefinition requestDefinition) { return getAllEntitiesForDefinition(requestDefinition, false); }
    public ReadOnlyClientObjectList getAllEntitiesForDefinition(ClientServiceDefinition requestDefinition, boolean updatePool) {
        ClientServicesForDefinitionFilter filter = new ClientServicesForDefinitionFilter(requestDefinition);
        if( updatePool ){
            try {
                String endpointCompiled = HTTP_ENDPOINT_TEMPLATES.GET_SERVICES_FOR_DEFINITION.replace("{DEFINITIONID}", ""+requestDefinition.getId());
                HashMap<String,Object> param = new HashMap<>();
                param.put("service_definition_id", requestDefinition.getId());
                param.put("list_filter", filter);
                fireHTTPRequest( HTTP_ENDPOINT_TEMPLATES.GET_SERVICES_FOR_DEFINITION, endpointCompiled, HTTP_REQUEST_TYPE.GET_ALL_FILTERED, param );
            } catch (NotSignedInException ex) {
                Logger.getLogger(getClass()).log(Level.WARN, "could not load services", ex);
            }
        }
        ReadOnlyClientObjectList<ClientService> unfilteredList = getAllEntities(false);
        ClientObjectList<ClientService> returnList = new ClientObjectList<>();
        
        return filter.filterClientObjectList(unfilteredList);
    }
    @Override
    public ClientService getEntity(int serviceId, Boolean updatePool) {
        ClientService returnService = this.cachedServiceList.getByID(serviceId);
        String templateEP = HTTP_ENDPOINT_TEMPLATES.GET_SERVICE;
        String buildEP = templateEP.replace("{ID}", ""+serviceId);
        
        HashMap<String,Object> param = new HashMap<>();
        param.put("service_id", serviceId);
        if(updatePool){
            try{
                fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.GET, param);
            } catch (NotSignedInException ex) {
                Logger.getLogger(getClass()).log(Level.WARN, "could not load service", ex);
            }
        }
        return (returnService==null) ? defaultService.getLocalClone() : returnService;
    }

    @Override
    public int createEntity(ClientService toCreate) throws TimeoutException {
        String templateEP = HTTP_ENDPOINT_TEMPLATES.CREATE_SERVICE;
        String buildEP = templateEP;
        HashMap<String,Object> param = new HashMap<>();
        param.put("service_definition", toCreate.getServiceDefinition().getId());
        
        JsonObject httpBody = toCreate.toJson();
        
        try{
            fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.CREATE, httpBody, param);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not create service", ex);
        }

        Integer[] requestIDs = pendingHttpRequests.keySet().toArray(new Integer[]{});
        
        Integer requestId = requestIDs[requestIDs.length-1];
        waitForRequest(requestId);
        return requestId;
    }

    @Override
    public int deleteEntity(ClientService entity) throws TimeoutException {
        System.out.println("DELETE");
        return -1;
    }

    @Override
    public int persistEntity(ClientService entity) throws TimeoutException {
        System.out.println("UPDATE");
        return -1;
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
            Logger.getLogger(getClass()).info("Received JsonArray for services, updating local service list");
            JsonArray servicesAsJsonArray = (JsonArray)response.getContent();
            ClientObjectList<ClientService> servicesToDelete = new ClientObjectList<>();
            servicesToDelete.addAll(cachedServiceList.getAll());
            Integer oldSize = servicesToDelete.size();
            servicesAsJsonArray.forEach((curService) -> {
                JsonObject theService=(JsonObject)curService;
                Logger.getLogger(getClass()).info("Processing JsonObject service: {0}", new String[]{ theService.toString() });
                ClientService tmpService = new ClientService(theService);
                servicesToDelete.removeById(tmpService.getId());
                cachedServiceList.add(tmpService);
            });
            Logger.getLogger(getClass()).info("[Services@GET_ALL]: {0} services received. Cached list size was {1}...about to delete {2} cached services that does not exist anymore.", new Object[]{servicesAsJsonArray.size(), oldSize, servicesToDelete.size()});
            for(int i=0; i<servicesToDelete.size(); i++){
                cachedServiceList.removeById(servicesToDelete.get(i).getId());
            }
        } else if(requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.GET_ALL_FILTERED)){
            Logger.getLogger(getClass()).info("Received JsonArray for services, updating local service list");
            JsonArray servicesAsJsonArray = (JsonArray)response.getContent();

            servicesAsJsonArray.forEach((curService) -> {
                JsonObject theService=(JsonObject)curService;
                Logger.getLogger(getClass()).info("Processing JsonObject service: {0}", new String[]{ theService.toString() });

                cachedServiceList.add(new ClientService(theService));
            });
            ClientObjectFilterBase<ClientService> listFilter = (ClientObjectFilterBase<ClientService>)requestToFinish.getParameters().get("list_filter");
            
            ClientObjectList<ClientService> servicesToDelete = new ClientObjectList<>();
            servicesToDelete.addAll(listFilter.filterClientObjectList( cachedServiceList ));
            Integer oldSize = servicesToDelete.size();
            servicesAsJsonArray.forEach((curService) -> {
                JsonObject theService=(JsonObject)curService;
                Logger.getLogger(getClass()).info("Processing JsonObject service: {0}", new String[]{ theService.toString() });
                ClientService tmpService = new ClientService(theService);
                servicesToDelete.removeById(tmpService.getId());
                cachedServiceList.add(tmpService);
            });
            Logger.getLogger(getClass()).info("[Services@GET_ALL_FILTERED]: {0} services received. Cached filtered list size was {1}...about to delete {2} cached services that does not exist anymore.", new Object[]{servicesAsJsonArray.size(), oldSize, servicesToDelete.size()});
            for(int i=0; i<servicesToDelete.size(); i++){
                cachedServiceList.removeById(servicesToDelete.get(i).getId());
            }
        } else if(requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.DELETE)){ // no service as response (deleted)
            Logger.getLogger(getClass().getName()).info("Service deleted successfully.");
        } else{ // all other request result in a single service as JSON object
            Logger.getLogger(getClass().getName()).info("Received JsonObject for single service, creating view...");
            JsonObject serviceAsJsonObject = (JsonObject)response.getContent();

            cachedServiceList.add(new ClientService( serviceAsJsonObject) );
        }
        
        if( response.getResponseStatus()!=HTTP_STATUS.CACHED ){
            finishPendingRequest(response.getRequestId());
        }
    }

}
