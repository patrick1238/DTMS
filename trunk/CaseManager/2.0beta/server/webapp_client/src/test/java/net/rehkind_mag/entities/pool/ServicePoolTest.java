/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.pool;

import com.sun.scenario.Settings;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.rehkind_mag.entities.ClientCase;
import net.rehkind_mag.entities.ClientService;
import net.rehkind_mag.entities.ClientServiceDefinition;
import net.rehkind_mag.entities.UserLogin;
import net.rehkind_mag.interfaces.client.ReadOnlyClientObjectList;
import net.rehkind_mag.webapp_client.WebappClientFXML;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;



/**
 *
 * @author rehkind
 */

public class ServicePoolTest {
    static ServicePool SERVICE_POOL;
    static CasePool CASE_POOL;
    static ServiceDefinitionPool SERVICE_DEFINITION_POOL;
    
    public ServicePoolTest() throws Exception{
        if(SERVICE_POOL==null){ setUpClass(); }
    }
    
    public final void setUpClass() throws Exception {
        WebappClientFXML.loadSettings();
        
        CASE_POOL=CasePool.createPool();
        CASE_POOL.getAllEntities(true);
        CASE_POOL.waitFor(30000);
        
        SERVICE_DEFINITION_POOL=ServiceDefinitionPool.createPool();
        SERVICE_DEFINITION_POOL.getAllEntities(true);
        SERVICE_DEFINITION_POOL.waitFor(30000);
        
        SERVICE_POOL=ServicePool.createPool();
        SERVICE_POOL.getAllEntities(true);
        SERVICE_POOL.waitFor(30000);
        
        System.out.println("ALL_POOL: "+SERVICE_POOL);
        System.out.println("USER_LOGIN: "+UserLogin.getLoginAsJson());
    }
    

    /**
     * Test of createPool method, of class ServicePool.
     */
    @Test
    public void testCreatePool() {
        System.out.println("\n\n########################## createPool ###################>>>>>\n\n");
        ServicePool result = ServicePool.createPool();
        System.out.println("Pool: "+result);
        assertEquals(result, ServicePoolTest.SERVICE_POOL );
        assertNotNull( result );
        System.out.println("\n\n<<<<<##################### createPool #########################\n\n");
    }

    /**
     * Test of getAllEntities method, of class ServicePool.
     */
    @Test
    public void testGetAllEntities() {
        System.out.println("\n\n########################## getAllEntities ###################>>>>>\n\n");
        
        UserLogin.getLoginAsJson();
        ServicePool pool = ServicePoolTest.SERVICE_POOL;
        
        ReadOnlyClientObjectList result = pool.getAllEntities();
        assertTrue(result.size()>0);
        
        try {
            System.out.println("WAITING FOR POOL");
            SERVICE_POOL.waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ServicePoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\n\n<<<<<##################### getAllEntities #########################\n\n");
    }

    /**
     * Test of getAllEntitiesForCase method, of class ServicePool.
     */
    @Test
    public void testGetAllEntitiesForCase() {
        System.out.println("\n\n########################## testGetAllEntitiesForCase ###################>>>>>\n\n");
        int CASE_ID=1;
        UserLogin.getLoginAsJson();
        ServicePool pool = ServicePoolTest.SERVICE_POOL;
        Integer poolTotalSize=pool.getAllEntities().size();
        ClientCase requestCase = CASE_POOL.getEntity(CASE_ID);
        ReadOnlyClientObjectList<ClientService> result = pool.getAllEntitiesForCase(requestCase);
        System.out.println(  String.format( "Found %d services for case[%d]...%d services in pool total.", new Object[]{result.size(), CASE_ID, poolTotalSize}) );
        assertTrue(result.size()>0);
        
        for(ClientService service : result.getAll()){
            assertTrue(service.getCase().getId()==CASE_ID);
        }
        try {
            System.out.println("WAITING FOR POOL");
            SERVICE_POOL.waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ServicePoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\n\n<<<<<##################### testGetAllEntitiesForCase #########################\n\n");
    }

    /**
     * Test of getAllEntitiesForDefinition method, of class ServicePool.
     */
    @Test
    public void testGetAllEntitiesForDefinition() {
        System.out.println("\n\n########################## testGetAllEntitiesForDefinition ###################>>>>>\n\n");
        int DEFINITION_ID=4;
        UserLogin.getLoginAsJson();
        ServicePool pool = ServicePoolTest.SERVICE_POOL;
        Integer poolTotalSize=pool.getAllEntities().size();
        ClientServiceDefinition requestDefinition = SERVICE_DEFINITION_POOL.getEntity(DEFINITION_ID);
        System.out.println("Searching for definition: "+requestDefinition.toString());
        ReadOnlyClientObjectList<ClientService> result = pool.getAllEntitiesForDefinition(requestDefinition);
        System.out.println(  String.format( "Found %d services for definition[%d]: %s...%d services in pool total.", new Object[]{result.size(), DEFINITION_ID, requestDefinition.getName() , poolTotalSize}) );
        
        assertTrue(result.size()>0);
        System.out.println("SERVICES ARE:");
        for(ClientService service : result.getAll()){
            System.out.println("  --- "+service.toString());
        }
        try {
            System.out.println("WAITING FOR POOL");
            SERVICE_POOL.waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ServicePoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\n\n<<<<<##################### testGetAllEntitiesForDefinition #########################\n\n");
    }
    
    /**
     * Test of getEntity method, of class ServicePool.
     */
    @Test
    public void testGetEntity() {
        System.out.println("\n\n########################## getEntity ###################>>>>>\n\n");
        int serviceId = 1;
        ServicePool pool = ServicePoolTest.SERVICE_POOL;
        ClientService result = pool.getEntity(serviceId);
        assertEquals(serviceId, result.getId());
        
        try {
            System.out.println("WAITING FOR POOL");
            SERVICE_POOL.waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ServicePoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\n\n<<<<<##################### getEntity #########################\n\n");
    }

    /**
     * Test of createEntity method, of class ServicePool.
     */
//    @Test
//    public void testCreateAndDeleteEntity() {
//        System.out.println("\n\n########################## create&deleteEntity ###################>>>>>\n\n");
//        ServicePool pool = ServicePoolTest.SERVICE_POOL;
//
//        Integer clinicCountBeforeCreate = pool.getAllEntities().size();
//
//        ClientService serviceToCreate = ClientService.getServiceTemplate(1, 2);
//        
//        System.out.println("[CREATE] ClientService: "+serviceToCreate.toString());
//        try {    
//            int requestId=pool.createEntity( serviceToCreate );
//        } catch (TimeoutException ex) {
//            Logger.getLogger(ServicePoolTest.class.getName()).log(Level.SEVERE, "Timed out while waiting for CREATE SERVICE", ex);
//        }    
//        
//        ReadOnlyClientObjectList<ClientService> entities = pool.getAllEntities();
//        ClientService newService=null;
//        for(Object c : entities){
//            ClientService castC=(ClientService)c;
//            if(newService==null){ newService = castC; }
//            else if (newService.getId()<castC.getId()) {
//                newService = castC;
//            }
//        }
//        
//        Integer clinicCountAfterCreate = entities.size();
//        assertEquals((int)clinicCountBeforeCreate, (int)clinicCountAfterCreate-1);
//        System.out.println("\n\n<<<<<##################### create&deleteEntity[CLEANUP] #########################\n\n");
//        System.out.println("Cleaning created service: "+newService.toString());
//        try {
//            pool.deleteEntity(newService);
//        } catch (TimeoutException ex) { 
//                Logger.getLogger(ServicePoolTest.class.getName()).log(Level.SEVERE, "Timed out whlie waiting for DELETE SERVICE", ex);
//        }
//        Integer clinicCountAfterDelete=pool.getAllEntities().size();
//        assertEquals((int)clinicCountAfterCreate, (int)clinicCountAfterDelete+1);
//        System.out.println("\n\n<<<<<##################### create&deleteEntity #########################\n\n");
//       
//    }

    /**
     * Test of persistEntity method, of class ServicePool.
     */
//    @Test
//    public void testPersistEntity() {
//        System.out.println("\n\n########################## persistEntity ###################>>>>>\n\n");
//        System.out.println("\n\n#####CURRENTLY DOING NOTHING - CHECK IF WE NEED TO ALLOW addMetadata() #####>>>>>\n\n");
//        assertTrue(true);
//        
//        System.out.println("\n\n<<<<###################### persistEntity ######################\n\n");
//    }

}
