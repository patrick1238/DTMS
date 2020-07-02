/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.pool;

import com.sun.scenario.Settings;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import net.rehkind_mag.entities.ClientCase;
import net.rehkind_mag.entities.ClientMetadata;
import net.rehkind_mag.entities.ClientService;
import net.rehkind_mag.entities.UserLogin;
import net.rehkind_mag.interfaces.IService;
import net.rehkind_mag.interfaces.client.ReadOnlyClientObjectList;
import net.rehkind_mag.webapp_client.WebappClientFXML;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author rehkind
 */
public class MetadataPoolTest {
    static MetadataPool METADATA_POOL;
    static ServicePool SERVICE_POOL;
    static CasePool CASE_POOL;
    static ServiceDefinitionPool DEFINITION_POOL;
    public MetadataPoolTest() throws Exception{
        if(METADATA_POOL==null){setUpClass();}
    }
    
    final public void setUpClass() throws Exception {
        WebappClientFXML.loadSettings();
        
        CASE_POOL=CasePool.createPool();
        CASE_POOL.getAllEntities(true);
        try {
            CASE_POOL.waitFor(15000);
        } catch (TimeoutException ex) {
            java.util.logging.Logger.getLogger(MetadataPoolTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }  
        
        DEFINITION_POOL = ServiceDefinitionPool.createPool();
        DEFINITION_POOL.getAllEntities(true);
        try {
            DEFINITION_POOL.waitFor(15000);
        } catch (TimeoutException ex) {
            java.util.logging.Logger.getLogger(MetadataPoolTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }  
        
        SERVICE_POOL = ServicePool.createPool();
        SERVICE_POOL.getAllEntities(true);
        try {
            SERVICE_POOL.waitFor(15000);
        } catch (TimeoutException ex) {
            java.util.logging.Logger.getLogger(MetadataPoolTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }      
        
        METADATA_POOL=MetadataPool.createPool();
        METADATA_POOL.getAllEntities(true);
        try {
            METADATA_POOL.waitFor(15000);
        } catch (TimeoutException ex) {
            java.util.logging.Logger.getLogger(MetadataPoolTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }



    System.out.println("ALL_POOL: "+METADATA_POOL);
    System.out.println("SERVICE_POOL: "+SERVICE_POOL);
    System.out.println("USER_LOGIN: "+UserLogin.getLoginAsJson());
    }
    
    public void tearDown() {
        try {
            System.out.println("WAITING FOR POOL");
            METADATA_POOL.waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ClinicPoolTest.class.getName()).log(Level.FATAL, null, ex);
        }
    }

    /**
     * Test of createPool method, of class ClinicPool.
     */
    @Test
    public void testCreatePool() {
        System.out.println("\n\n########################## createPool ###################>>>>>\n\n");
        MetadataPool result = MetadataPool.createPool();
        System.out.println("Pool: "+result);
        assertEquals(result, MetadataPoolTest.METADATA_POOL );
        assertNotNull( result );
        System.out.println("\n\n<<<<<##################### createPool #########################\n\n");
    }

    /**
     * Test of getAllEntities method, of class MetadataPool.
     */
    @Test
    public void testGetAllEntities() throws Exception {
        System.out.println("\n\n########################## getAllEntities ###################>>>>>\n\n");
        if(METADATA_POOL==null){
            setUpClass();
        }
        
        UserLogin.getLoginAsJson();
        MetadataPool pool = MetadataPoolTest.METADATA_POOL;
        
        ReadOnlyClientObjectList<ClientMetadata> result = pool.getAllEntities();
        assertEquals(true, (result.size()>0));
        
        try {
            System.out.println("WAITING FOR POOL");
            METADATA_POOL.waitFor(10000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ClinicPoolTest.class.getName()).log(Level.FATAL, null, ex);
        }
        System.out.println("\n\n<<<<<#### LOADED METADATA COUNT: "+result.size()+"######\n\n");
        System.out.println("\n\n<<<<<##################### getAllEntities #########################\n\n");
    }

    /**
     * Test of getAllEntities method, of class MetadataPool.
     */
    @Test
    public void testGetEntitiesForService() throws Exception {
        System.out.println("\n\n########################## getMetadataForService ###################>>>>>\n\n");
        if(METADATA_POOL==null){setUpClass();}
        
        UserLogin.getLoginAsJson();
        MetadataPool pool = MetadataPoolTest.METADATA_POOL;
        ClientService requestService = SERVICE_POOL.getEntity(1);
        
        ReadOnlyClientObjectList<ClientMetadata> result = pool.getMetadataForService(requestService, Boolean.FALSE);
        assertEquals(true, (result.size()>0));
        
        try {
            System.out.println("WAITING FOR POOL");
            METADATA_POOL.waitFor(10000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ClinicPoolTest.class.getName()).log(Level.FATAL, null, ex);
        }
        System.out.println("\n\n<<<<<#### LOADED METADATA FOR SERVICE TEST COUNT: "+result.size()+"######\n\n");
        System.out.println("\n\n<<<<<##################### getMetadataForService #########################\n\n");
    }

    
        /**
     * Test of getAllEntities method, of class MetadataPool.
     */
    @Test
    public void testGetEntitiesForCase() throws Exception {
        System.out.println("\n\n########################## getMetadataForCase ###################>>>>>\n\n");
        if(METADATA_POOL==null){setUpClass();}
        
        UserLogin.getLoginAsJson();
        MetadataPool pool = MetadataPoolTest.METADATA_POOL;
        ClientCase requestCase = CASE_POOL.getEntity(1);
        
        int perServiceCount = 0;
        for (IService s : requestCase.getServices()){
            perServiceCount += pool.getMetadataForService((ClientService)s, Boolean.FALSE).size();
            System.out.println("Service for case [1]: "+s.toString());
            
        }
        
        ReadOnlyClientObjectList<ClientMetadata> result = pool.getMetadataForCase(requestCase, Boolean.FALSE);
        assertEquals(result.size(), perServiceCount);
        assertTrue(perServiceCount>0);
        
        try {
            System.out.println("WAITING FOR POOL");
            METADATA_POOL.waitFor(10000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ClinicPoolTest.class.getName()).log(Level.FATAL, null, ex);
        }
        System.out.println("\n\n<<<<<#### LOADED METADATA FOR CASE TEST COUNT: "+result.size() +" (per service= "+perServiceCount+") ######\n\n");
        System.out.println("\n\n<<<<<##################### getMetadataForCase #########################\n\n");
    }
    
        /**
     * Test of getAllEntities method, of class MetadataPool.
     */
    @Test
    public void testGetEntitiesFromClientService() throws Exception {
        System.out.println("\n\n########################## testGetEntitiesFromClientService ###################>>>>>\n\n");
        if(METADATA_POOL==null){ setUpClass(); }
        MetadataPool pool = METADATA_POOL;
        UserLogin.getLoginAsJson();
        ClientService requestService = SERVICE_POOL.getEntity(1);
        
        // direct call from pool
        int metaPoolCount = pool.getMetadataForService(requestService, Boolean.FALSE).size();
        // call from ClientService
        ReadOnlyClientObjectList<ClientMetadata> result = pool.getMetadataForService(requestService, Boolean.FALSE);
        int fromServiceCount = result.size();
        
        assertEquals(metaPoolCount, fromServiceCount);
        assertTrue(fromServiceCount>0);
        try {
            System.out.println("WAITING FOR POOL");
            METADATA_POOL.waitFor(10000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ClinicPoolTest.class.getName()).log(Level.FATAL, null, ex);
        }
        //System.out.println("\n\n<<<<<#### LOADED METADATA FOR CASE TEST COUNT: "+metaPoolCount +" == "+fromServiceCount+" ######\n\n");
        System.out.println("\n\n<<<<<##################### testGetEntitiesFromClientService #########################\n\n");
    }
    
    /**
     * Test of getAllEntities method, of class MetadataPool.
     */
    @Test
    public void testUpdateServiceMetadata() throws Exception {
        System.out.println("\n\n########################## testUpdateServiceMetadata ###################>>>>>\n\n");
        if(METADATA_POOL==null){ setUpClass(); }
        MetadataPool pool = METADATA_POOL;
        UserLogin.getLoginAsJson();
        ClientService requestService = SERVICE_POOL.getEntity(1);
        System.out.println("using service: "+requestService.getServiceDefinition().getName());
        // direct call from pool
        ReadOnlyClientObjectList<ClientMetadata> meta = pool.getMetadataForService(requestService, Boolean.FALSE);
        int metaCount = meta.size();
        System.out.println("service metadata count: "+metaCount);
        for( ClientMetadata cm : meta ){
            System.out.println(" +++ "+cm.getName()+" : "+cm.getData());
        }
        Random rnd = new Random();
        // change values randomly
        HashMap newValues = new HashMap();
        HashMap oldValues = new HashMap();
        for( ClientMetadata cm : meta ){
            switch( cm.getType() ){
                case STRING:
                case TEXT:
                    String oldData = (String)cm.getData();
                    String newData = (String)cm.getData();
                    newData = newData.split("TEST")[0]+"TEST"+rnd.nextInt(300);
                    System.out.println("   - switching "+ cm.getData() +" to "+newData);
                    cm.setData(newData);
                    newValues.put(cm.getMetadataKey(), newData);
                    oldValues.put(cm.getMetadataKey(), oldData);
                    break;
                case INTEGER:
                    Integer oldIntData = (Integer)cm.getData();
                    Integer newIntData = (Integer)cm.getData();
                    newIntData+= (rnd.nextInt(30)-10);
                    System.out.println("   - switching "+ cm.getData() +" to "+newIntData);
                    cm.setData(newIntData);
                    newValues.put(cm.getMetadataKey(), newIntData);
                    oldValues.put(cm.getMetadataKey(), oldIntData);
                    break;
            }
        }
        
        pool.persistMetadataForService(requestService, false);
        
        // call from ClientService
        ReadOnlyClientObjectList<ClientMetadata> result = pool.getMetadataForService(requestService, Boolean.FALSE);
        //int fromServiceCount = result.size();
        
        // check new values:
        boolean metadataHaveNewValues=true;
        int checkedKeys=0;
        for( ClientMetadata cm : result ){
            System.out.println("reloaded metadata: "+cm.getName()+" - "+cm.getData());
            if( newValues.keySet().contains( cm.getMetadataKey() ) ){
                System.out.println("should have been changed: "+oldValues.get(cm.getMetadataKey())+" -> "+newValues.get(cm.getMetadataKey()));
                metadataHaveNewValues = metadataHaveNewValues && cm.getData().equals(newValues.get(cm.getMetadataKey()));
                checkedKeys++;
            }
        }
        
        assertTrue(metadataHaveNewValues );
        assertTrue(checkedKeys>0);
        
        try {
            System.out.println("WAITING FOR POOL");
            METADATA_POOL.waitFor(10000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ClinicPoolTest.class.getName()).log(Level.FATAL, null, ex);
        }
        System.out.println("\n\n<<<<<#### UPDATED METADATA FOR SERVICE: "+ requestService.getId() + " ######\n\n");
        System.out.println("\n\n<<<<<##################### testUpdateServiceMetadata #########################\n\n");
    }
}
