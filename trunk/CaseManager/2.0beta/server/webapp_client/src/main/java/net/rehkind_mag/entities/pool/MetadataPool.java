/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import net.rehkind_mag.entities.ClientCase;
import net.rehkind_mag.entities.ClientMetadata;
import net.rehkind_mag.entities.ClientService;
import net.rehkind_mag.entities.filter.ClientMetadatasForCaseFilter;
import net.rehkind_mag.entities.filter.ClientMetadatasForServiceFilter;
import net.rehkind_mag.entities.filter.ClientObjectLocalChangesFilter;
import net.rehkind_mag.http.HTTP_ENDPOINT_TEMPLATES;
import net.rehkind_mag.http.NotSignedInException;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.interfaces.client.ClientObjectList;
import net.rehkind_mag.interfaces.client.ReadOnlyClientObjectList;
import net.rehkind_mag.utils.HTTP_REQUEST_TYPE;
import net.rehkind_mag.utils.HTTP_STATUS;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

/**
 *
 * @author rehkind
 */
public class MetadataPool extends AClientObjectPool<ClientMetadata> {
    
    private static MetadataPool singletonPool;
    private static ClientMetadata defaultMetadata;
    
    ClientObjectList<ClientMetadata> cachedMetadataPerServiceList=new ClientObjectList();
    ClientObjectList<ClientMetadata> cachedMetadataPerCaseList=new ClientObjectList();
    ClientObjectList<ClientMetadata> cachedMetadataList=new ClientObjectList();
    
    private MetadataPool(){
        defaultMetadata=ClientMetadata.createTemplate("template", -1);
    }
    
    static public MetadataPool createPool() {
        if (MetadataPool.singletonPool == null){ MetadataPool.singletonPool=new MetadataPool(); }
        
        return MetadataPool.singletonPool;
    }
    
    @Override
    public ClientMetadata getEntity(int serviceID, Boolean updatePool) {
        throw new UnsupportedOperationException("Not supported...use getMetadataForService() instead"); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ReadOnlyClientObjectList<ClientMetadata> getAllEntities(Boolean updatePool) {
        if( updatePool ){
            try {
                String endpointCompiled = HTTP_ENDPOINT_TEMPLATES.GET_ALL_METADATA;
                HashMap<String,Object> param = new HashMap<>();
                fireHTTPRequest( HTTP_ENDPOINT_TEMPLATES.GET_ALL_METADATA, HTTP_ENDPOINT_TEMPLATES.GET_ALL_METADATA, HTTP_REQUEST_TYPE.GET_ALL, param );
            } catch (NotSignedInException ex) {
                Logger.getLogger(getClass()).log(Level.WARN, "could not load metadata", ex);
            }
        }
        
        return cachedMetadataList;
    }
    
    public synchronized ReadOnlyClientObjectList<ClientMetadata> getMetadataForService(ClientService requestService, Boolean updatePool){
        ClientMetadatasForServiceFilter filter = new ClientMetadatasForServiceFilter(requestService);
        if( updatePool ){
            try {
                String endpointCompiled = HTTP_ENDPOINT_TEMPLATES.GET_METADATA_FOR_SERVICE.replace("{SERVICEID}", ""+requestService.getId());
                HashMap<String,Object> param = new HashMap<>();
                param.put("service_id", requestService.getId());
                param.put("list_filter", filter);
                fireHTTPRequest( HTTP_ENDPOINT_TEMPLATES.GET_METADATA_FOR_SERVICE, endpointCompiled, HTTP_REQUEST_TYPE.GET_ALL_FILTERED, param );
            } catch (NotSignedInException ex) {
                Logger.getLogger(getClass()).log(Level.WARN, "could not load metadata", ex);
            }
        }
        ReadOnlyClientObjectList<ClientMetadata> unfilteredList = new ClientObjectList();
        ClientMetadata[] asArray = getAllEntities(false).toArray(new ClientMetadata[]{});
        for(ClientMetadata cm : asArray){
            unfilteredList.add( cm );
        }
        //System.out.println("All loaded metadata="+unfilteredList.size());
        //ReadOnlyClientObjectList<ClientMetadata> filteredList = filter.filterClientObjectList(unfilteredList);
        //System.out.println("Filtered metadata="+filteredList.size());
        return filter.filterClientObjectList(unfilteredList);
    }

    public ReadOnlyClientObjectList<ClientMetadata> getMetadataForCase(ClientCase requestCase, Boolean updatePool){
        ClientMetadatasForCaseFilter filter = new ClientMetadatasForCaseFilter(requestCase);
        if( updatePool ){
            try {
                String endpointCompiled = HTTP_ENDPOINT_TEMPLATES.GET_METADATA_FOR_CASE.replace("{CASEID}", ""+requestCase.getId());
                HashMap<String,Object> param = new HashMap<>();
                param.put("service_id", requestCase.getId());
                param.put("list_filter", filter);
                fireHTTPRequest( HTTP_ENDPOINT_TEMPLATES.GET_METADATA_FOR_CASE, endpointCompiled, HTTP_REQUEST_TYPE.GET_ALL_FILTERED, param );
            } catch (NotSignedInException ex) {
                Logger.getLogger(getClass()).log(Level.WARN, "could not load metadata for case", ex);
            }
        }
        ReadOnlyClientObjectList<ClientMetadata> unfilteredList = getAllEntities(false);
        
        return filter.filterClientObjectList(unfilteredList);
    }
    
    @Override
    public int createEntity(ClientMetadata toCreate) throws TimeoutException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int deleteEntity(ClientMetadata entity) throws TimeoutException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int persistEntity(ClientMetadata entity, boolean forcePersist) throws TimeoutException {
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
        
        long timeMS = System.currentTimeMillis()-requestToFinish.getCreationTime();
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
            Logger.getLogger(getClass()).info("Received JsonArray for metadata, updating local metadata list");
            JsonArray metadataAsJsonArray = (JsonArray)response.getContent();

            metadataAsJsonArray.forEach((metadata) -> {
                JsonObject joMeta=(JsonObject)metadata;
                Logger.getLogger(getClass()).info("Processing JsonObject metadata: {0}", new String[]{ joMeta.toString() });
                ClientMetadata newMetadata = new ClientMetadata( joMeta );                
                cachedMetadataList.add(newMetadata);
            });
        }
        else if( requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.GET_ALL_FILTERED) ){
            Logger.getLogger(getClass()).info("Received JsonArray for metadata, updating local metadata list");
            JsonArray clinicsAsJsonArray = (JsonArray)response.getContent();

            clinicsAsJsonArray.forEach((metadata) -> {
                JsonObject joMeta=(JsonObject)metadata;
                Logger.getLogger(getClass()).info("Processing JsonObject metadata: {0}", new String[]{ joMeta.toString() });
                ClientMetadata newMetadata = new ClientMetadata( joMeta );                
                cachedMetadataList.add(newMetadata);
            });
        } else if(requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.DELETE)){ // no case as response (deleted)
            Logger.getLogger(getClass().getName()).info("Metadata deleted successfully.");
        } else{ // all other request result in a single clinic as JSON object
            Logger.getLogger(getClass().getName()).info("Received JsonObject for single metadata, creating ClientMetadata...");
            JsonObject metadataAsJsonObject = (JsonObject)response.getContent();

            cachedMetadataList.add(new ClientMetadata( metadataAsJsonObject) );
        }

        if(! (response.getResponseStatus()==HTTP_STATUS.CACHED) ){ finishPendingRequest(response.getRequestId()); }
    }

    int persistMetadataForService( ClientService service ) throws TimeoutException { return persistMetadataForService( service, false ); }
    
    int persistMetadataForService( ClientService service, Boolean forcePersist ) throws TimeoutException {
        ClientObjectList<ClientService> convertedToList = new ClientObjectList<>();
        convertedToList.add(service);
        return persistMetadataForServices(convertedToList, forcePersist);
    }
    
    int persistMetadataForServices(ReadOnlyClientObjectList<ClientService> services ) throws TimeoutException { return persistMetadataForServices( services, false ); }
    
    int persistMetadataForServices(ReadOnlyClientObjectList<ClientService> services, Boolean forcePersist ) throws TimeoutException {
        int requestIdForReturn=-1;
        ClientObjectList metadataForService = new ClientObjectList();
        for( ClientService s : services ){
            
            if( forcePersist ){ // add all metadata
                metadataForService.addAll( getMetadataForService( s, Boolean.FALSE ) );
                Logger.getLogger(getClass()).info("Persisting all metadata entities ("+metadataForService.size()+") for requested services ("+services.size()+"). [FORCED PERSIST]");
            }
            else{ // add only metadata which have local changes
                metadataForService.addAll( new ClientObjectLocalChangesFilter<ClientMetadata>().filterClientObjectList( getMetadataForService( s, Boolean.FALSE ) ) );
                Logger.getLogger(getClass()).info("Persisting only metadata for services ("+ services.size() +") with local changes ("+metadataForService.size()+"). [LOCAL CHANGES]");
            }
            String templateEP = HTTP_ENDPOINT_TEMPLATES.UPDATE_SERVICE_METADATA;
            String buildEP = templateEP.replace("{SERVICEID}",""+s.getId());
            
            
            HashMap<String,Object> param = new HashMap<>();

            param.put("service_id", s.getId());
            
            JsonStructure httpBody = metadataForService.toJson(); // TODO: check if simple casting works
            try{
                fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.UPDATE, httpBody, param);
            } catch (NotSignedInException ex) {
                Logger.getLogger(getClass()).log(Level.WARN, "could not update metadata list, not signed in.", ex);
            }
            
            Integer[] requestIDs = pendingHttpRequests.keySet().toArray(new Integer[]{});
            Integer requestId = requestIDs[requestIDs.length-1];
            waitForRequest(requestId); // wait till update completed

//            for(ClientService cs : services){
//                getMetadataForService(cs, Boolean.TRUE);
//            }
//        
//            waitFor(); // wait till updated entity reloaded
            getAllEntities(true);
            requestIdForReturn = requestId;
        }
        
        return requestIdForReturn;





    }
}
