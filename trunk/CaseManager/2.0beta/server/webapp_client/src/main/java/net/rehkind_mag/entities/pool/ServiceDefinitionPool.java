/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.pool;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;
import net.rehkind_mag.entities.ClientServiceDefinition;
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
    public ReadOnlyClientObjectList getAllEntities(){
        try {
            fireHTTPRequest(HTTP_ENDPOINT_TEMPLATES.GET_SERVICE_DEFINITIONS, HTTP_REQUEST_TYPE.GET_ALL);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not load service_definitions", ex);
        }
        return cachedServiceDefinitionList;
    }
    
    @Override
    public ClientServiceDefinition getEntity(int serviceId) {
        ClientServiceDefinition returnService = this.cachedServiceDefinitionList.getByID(serviceId);
        String templateEP = HTTP_ENDPOINT_TEMPLATES.GET_SERVICE_DEFINITION;
        String buildEP = templateEP.replace("{ID}", ""+serviceId);
        
        HashMap<String,Object> param = new HashMap<>();
        param.put("service_id", serviceId);
        try{
            fireHTTPRequest(templateEP, buildEP, HTTP_REQUEST_TYPE.GET, param);
        } catch (NotSignedInException ex) {
            Logger.getLogger(getClass()).log(Level.WARN, "could not load service", ex);
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
    public int persistEntity(ClientServiceDefinition entity) throws TimeoutException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receiveHttpResponse(IHttpResponse response) {
        
    }
    
}
