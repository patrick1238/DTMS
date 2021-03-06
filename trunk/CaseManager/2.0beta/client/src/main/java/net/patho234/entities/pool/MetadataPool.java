/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import net.patho234.entities.ClientCase;
import net.patho234.entities.ClientMetadata;
import net.patho234.entities.ClientService;
import net.patho234.entities.filter.ClientMetadatasForCaseFilter;
import net.patho234.entities.filter.ClientMetadatasForServiceFilter;
import net.patho234.entities.filter.ClientObjectLocalChangesFilter;
import net.patho234.http.HTTP_ENDPOINT_TEMPLATES;
import net.patho234.http.NotSignedInException;
import net.patho234.interfaces.IHttpResponse;
import net.patho234.interfaces.IMetadataValue;
import net.patho234.interfaces.IServiceDefinition;
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
public class MetadataPool extends AClientObjectPool<ClientMetadata> {
    
    private static MetadataPool singletonPool;
    private static ClientMetadata defaultMetadata;
    
    ClientObjectList<ClientMetadata> cachedMetadataPerServiceList=new ClientObjectList();
    ClientObjectList<ClientMetadata> cachedMetadataPerCaseList=new ClientObjectList();
    ClientObjectList<ClientMetadata> cachedMetadataList=new ClientObjectList();
    
    PerServiceMapUpdater perServiceMapUpdater;
    HashMap<Integer, ClientObjectList<ClientMetadata>> perServiceMap;
    
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
        
        /* old stuff - take all Metadata and filter manually -> slows everything down, if new method works remove this
        ReadOnlyClientObjectList<ClientMetadata> unfilteredList = new ClientObjectList();
        ClientMetadata[] asArray = getAllEntities(false).toArray(new ClientMetadata[]{});
        for(ClientMetadata cm : asArray){
            unfilteredList.add( cm );
        }
        //System.out.println("All loaded metadata="+unfilteredList.size());
        //ReadOnlyClientObjectList<ClientMetadata> filteredList = filter.filterClientObjectList(unfilteredList);
        //System.out.println("Filtered metadata="+filteredList.size());
        return filter.filterClientObjectList(unfilteredList);
        */
        if( perServiceMap==null ){ return new ClientObjectList<>(); }
        if( requestService.getId() == -1 ){ System.out.println("MetadataRequest for unpersisted service !!!!"); }
//        // if service is not yet persisted (id=-1): create all metadata fields from service def:
//        if( requestService.getId() == -1 ){
//            requestService.getServiceDefinition().getMetadataValues();
//            for()
//        }
//      
        ClientObjectList<ClientMetadata> returnList=null;
        if( perServiceMap.get(requestService.getId())==null ){
            System.out.println("complete service def");
            returnList = buildMetadataFromServiceDef(requestService);
        }else{
            System.out.println("partly service def");
            returnList = fillUpMetadataFromServiceDef( requestService, perServiceMap.get(requestService.getId()) );
        }
        return returnList;
    }
    
    public ClientObjectList<ClientMetadata> fillUpMetadataFromServiceDef(ClientService requestService, ClientObjectList<ClientMetadata> partList){
        ClientObjectList<ClientMetadata> serviceDefList = buildMetadataFromServiceDef(requestService);
        ReadOnlyClientObjectList<ClientMetadata> metaList = new ClientObjectList<>();
        System.out.println("size: "+serviceDefList.size());
        for( ClientMetadata cm : serviceDefList ){
            String tmpName = cm.getName();
            
            ClientMetadata partMetadata=null;
            inner: for( ClientMetadata tmpMeta : partList ){
                if( tmpName.equals(tmpMeta.getName()) ){
                    partMetadata = tmpMeta;
                    break inner;
                }
            }
            
            if( partMetadata!=null ){
                //System.out.println("adding "+cm.getName()+" [already set]");
                metaList.add(partMetadata);
            }else{
                //System.out.println("adding "+cm.getName()+" [from service def]");
                metaList.add(cm);
            }
        }
        return (ClientObjectList<ClientMetadata>) metaList;
    }
    
    public ClientObjectList<ClientMetadata> buildMetadataFromServiceDef(ClientService requestService){
        ReadOnlyClientObjectList<ClientMetadata> metaList = new ClientObjectList<>();
        //System.out.println("buildMetadataFromServiceDef("+requestService.getServiceDefinition().getName()+")");
        //System.out.println("num service def categories: "+requestService.getServiceDefinition().getMetadataValues().size()); 
               
        for( Entry<IServiceDefinition, List<IMetadataValue>> e: requestService.getServiceDefinition().getMetadataValues().entrySet()){
            //List<IMetadataValue> values = requestService.getServiceDefinition().getMetadataValues().get(requestService.getServiceDefinition());
            System.out.println("-- "+requestService.getServiceDefinition().getName()+": "+e.getValue().size()); 
            for(IMetadataValue mv : e.getValue()){
                if( !mv.isDepricated() ){
                    //System.out.println("adding metadataValue: "+mv);
                    ClientMetadata meta=null;
                    JsonObjectBuilder builder = Json.createObjectBuilder();
                    builder.add("id", -1);
                    builder.add("name", mv.getKey());
                    builder.add("serviceId", requestService.getId());
                    switch (mv.getValueType()) {
                        case "integer":
                        case "int":
                            builder.add("value", JsonValue.NULL);
                            builder.add("type", "int");
                            break;
                        case "double":
                            builder.add("value", JsonValue.NULL);
                            builder.add("type", "double");
                            break;
                        case "string":
                            builder.add("value", "");
                            builder.add("type", "string");
                            break;
                        case "text":
                            builder.add("value", "");
                            builder.add("type", "text");
                            break;
                        case "url":
                            builder.add("value", "");
                            builder.add("type", "url");
                            break;
                        default:
                            Logger.getLogger(getClass()).error("Unknown metadata type: "+mv.getValueType());
                    }
                    
                    meta=new ClientMetadata(builder.build());
                    metaList.add(meta);
                }else{
                    System.out.println(mv.getKey()+" will be ignored...deprecated.");
                }
            }
        }
        return (ClientObjectList<ClientMetadata>)metaList;
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
                if(newMetadata.getService().getCase().getCaseNumber().equals("L501-18")){
                    System.out.println("NEW LOADED METADATA (L501-18): "+newMetadata);
                }
                cachedMetadataList.add(newMetadata);
            });
            new PerServiceMapUpdater(this).start();
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
            new PerServiceMapUpdater(this).start();
            initialized = true;
        } else if(requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.DELETE)){ // no case as response (deleted)
            Logger.getLogger(getClass().getName()).info("Metadata deleted successfully.");
            new PerServiceMapUpdater(this).start();
        } else if(requestToFinish.getRequestType().equals(HTTP_REQUEST_TYPE.UPDATE)){ // no case as response (deleted)
            Logger.getLogger(getClass().getName()).info("Metadata updated successfully.");
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
    
    private class PerServiceMapUpdater extends Thread {
        MetadataPool parent;
        Boolean finished=false;
        public PerServiceMapUpdater(MetadataPool parent){
            this.parent = parent;
        }
        
        @Override
        public void run(){
            if( parent.perServiceMapUpdater != null ){
                while( !parent.perServiceMapUpdater.finished ){
                    try{
                        Thread.sleep(100);
                    }catch(InterruptedException iEx){
                        // if current Thread was interrupted we just proceed
                    }
                }
            }
            
            parent.perServiceMapUpdater = this;
            
            HashMap<Integer, ClientObjectList<ClientMetadata>> tmpMap = new HashMap<>();
            Integer serviceId;
            for( Object cm : getAllEntities(false) ){
                ClientMetadata castCM = (ClientMetadata)cm;
                serviceId = castCM.getServiceID();
                if( tmpMap.get(serviceId) == null ){
                    tmpMap.put(serviceId, new ClientObjectList<>());
                }
                tmpMap.get(serviceId).add(castCM);
            }
            parent.perServiceMap = tmpMap;
            finished=true;
        }
    }
}
