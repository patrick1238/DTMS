/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkindmag.entities.pool;

import java.util.HashMap;
import java.util.List;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.interfaces.client.AClientObjectPool;
import net.rehkindmag.entities.ClientCase;
import net.rehkindmag.http.HTTP_ENDPOINT_TEMPLATES;

/**
 *
 * @author rehkind
 */
public class CasePool extends AClientObjectPool<ClientCase>{
    private final static String GET_CASE=HTTP_ENDPOINT_TEMPLATES.GET_CASE;
    private final static String GET_CASES=HTTP_ENDPOINT_TEMPLATES.GET_CASES;
    private final static String CREATE_CASE=HTTP_ENDPOINT_TEMPLATES.CREATE_CASE;
    private final static String UPDATE_CASE=HTTP_ENDPOINT_TEMPLATES.UPDATE_CASE;
    private final static String DELETE_CASE=HTTP_ENDPOINT_TEMPLATES.DELETE_CASE;
    
    private static CasePool singletonPool;
    
    HashMap<Integer, ClientCase> cachedCases = new HashMap<>();
    
    private CasePool(){
        
    }
    
    @Override
    public AClientObjectPool<ClientCase> createPool() {
        if (CasePool.singletonPool == null){ CasePool.singletonPool=new CasePool(); }
        
        return CasePool.singletonPool;
    }
    
    public ClientCase getEntity(int caseId){
        throw new UnsupportedOperationException("CasePool.getEntity not yet implemented.");
    }
    
    @Override
    public List<ClientCase> getAllEntities(){
        throw new UnsupportedOperationException("CasePool.getAllEntities not yet implemented.");
    }



    @Override
    public ClientCase getEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ClientCase createEntity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteEntity(ClientCase entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean persistEntity(ClientCase entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receiveHttpResponse(IHttpResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
