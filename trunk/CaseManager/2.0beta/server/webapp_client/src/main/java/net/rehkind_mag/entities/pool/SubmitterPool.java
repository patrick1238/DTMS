/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.pool;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import javax.json.JsonObject;
import net.rehkind_mag.entities.ClientSubmitter;
import net.rehkind_mag.http.HTTP_ENDPOINT_TEMPLATES;
import net.rehkind_mag.http.NotSignedInException;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.interfaces.client.ClientObjectList;
import net.rehkind_mag.interfaces.client.ReadOnlyClientObjectList;
import net.rehkind_mag.utils.HTTP_REQUEST_TYPE;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

/**
 *
 * @author rehkind
 */
public class SubmitterPool extends AClientObjectPool<ClientSubmitter>{

    private static SubmitterPool singletonPool;
    private static ClientSubmitter defaultSubmitter;
    
    ClientObjectList<ClientSubmitter> cachedSubmitterList=new ClientObjectList();
    
    private SubmitterPool(){
        defaultSubmitter = ClientSubmitter.getSubmitterTemplate();
    }
    
    static public SubmitterPool createPool() {
        if (SubmitterPool.singletonPool == null){ SubmitterPool.singletonPool=new SubmitterPool(); }
        
        return SubmitterPool.singletonPool;
    }
    
    @Override
    public ClientSubmitter getEntity(int submitterId, Boolean updatePool) {
        if(submitterId<1){ return null; }
        ClientSubmitter returnSubmitter = this.cachedSubmitterList.getByID(submitterId);
        String templateEP = HTTP_ENDPOINT_TEMPLATES.GET_SUBMITTER;
        String buildEP = templateEP.replace("{SUBMITTERID}", ""+submitterId);
        
        HashMap<String,Object> param = new HashMap<>();
        param.put("submitter_id", submitterId);
        if(updatePool){
        try{
            fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.GET, param);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not load Submitter", ex);
        }}
        return (returnSubmitter==null) ? (ClientSubmitter)defaultSubmitter.getLocalClone() : returnSubmitter;
    }

    @Override
    public ReadOnlyClientObjectList<ClientSubmitter> getAllEntities(Boolean updatePool) {
        if(updatePool){
            try{    
                fireHTTPRequest(HTTP_ENDPOINT_TEMPLATES.GET_SUBMITTERS, HTTP_REQUEST_TYPE.GET_ALL);
            } catch (NotSignedInException ex) {
                Logger.getLogger(getClass()).log(Level.WARN, "could not load submitters", ex);
            }
        }
        return cachedSubmitterList;
    }

    @Override
    public int createEntity(ClientSubmitter toCreate) throws TimeoutException {
        String templateEP = HTTP_ENDPOINT_TEMPLATES.CREATE_SUBMITTER;
        String buildEP = templateEP;
        HashMap<String,Object> param = new HashMap<>();
        param.put("submitter_name", toCreate.getForename()+" "+toCreate.getSurname());
        Logger.getLogger(getClass()).info("About to create submitter: "+toCreate.getForename()+" "+toCreate.getSurname());
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
    public int deleteEntity(ClientSubmitter entity) throws TimeoutException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int persistEntity(ClientSubmitter entity) throws TimeoutException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receiveHttpResponse(IHttpResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
