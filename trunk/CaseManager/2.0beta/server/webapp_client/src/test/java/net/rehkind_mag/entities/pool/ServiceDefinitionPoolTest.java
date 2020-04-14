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
import net.rehkind_mag.entities.ClientService;
import net.rehkind_mag.entities.ClientServiceDefinition;
import net.rehkind_mag.entities.UserLogin;
import net.rehkind_mag.interfaces.client.ReadOnlyClientObjectList;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 *
 * @author rehkind
 */

public class ServiceDefinitionPoolTest {
    static ServiceDefinitionPool SERVICE_DEF_POOL;
    
    public ServiceDefinitionPoolTest() throws Exception{
        ServiceDefinitionPoolTest.setUpClass();
    }

    @BeforeAll
    public static void setUpClass() throws Exception {
        Settings.set("server.address", "http://192.168.31.1:8585/webapp/resources/");
        Settings.set("client.login", "guest");
        Settings.set("client.password", "123456");
        UserLogin.setLogin("guest", "123456");
        
        
        SERVICE_DEF_POOL=ServiceDefinitionPool.createPool();
        SERVICE_DEF_POOL.getAllEntities();
        SERVICE_DEF_POOL.waitFor(30000);
        
        System.out.println("ALL_POOL: "+SERVICE_DEF_POOL);
        System.out.println("USER_LOGIN: "+UserLogin.getLoginAsJson());
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
        try {
            System.out.println("WAITING FOR POOL");
            SERVICE_DEF_POOL.waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ServiceDefinitionPoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of createPool method, of class ServicePool.
     */
    @Test
    public void testCreatePool() {
        System.out.println("\n\n########################## createPool ###################>>>>>\n\n");
        ServiceDefinitionPool result = ServiceDefinitionPool.createPool();
        System.out.println("Pool: "+result);
        assertEquals(result, ServiceDefinitionPoolTest.SERVICE_DEF_POOL );
        assertNotNull( result );
        System.out.println("\n\n<<<<<##################### createPool #########################\n\n");
    }

    /**
     * Test of getAllEntities method, of class ServiceDefinitionPool.
     */
    @Test
    public void testGetAllEntities() {
        System.out.println("\n\n########################## getAllEntities ###################>>>>>\n\n");
        
        UserLogin.getLoginAsJson();
        ServiceDefinitionPool pool = ServiceDefinitionPoolTest.SERVICE_DEF_POOL;
        
        ReadOnlyClientObjectList result = pool.getAllEntities();
        assertTrue(result.size()>0);
        
        try {
            System.out.println("WAITING FOR POOL");
            SERVICE_DEF_POOL.waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ServicePoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\n\n<<<<<##################### getAllEntities #########################\n\n");
    }

    /**
     * Test of getEntity method, of class ServiceDefinitionPool.
     */
    @Test
    public void testGetEntity() {
        System.out.println("\n\n########################## getEntity ###################>>>>>\n\n");
        int serviceId = 1;
        ServiceDefinitionPool pool = ServiceDefinitionPoolTest.SERVICE_DEF_POOL;
        ClientServiceDefinition result = pool.getEntity(serviceId);
        assertEquals(serviceId, result.getId());
        
        try {
            System.out.println("WAITING FOR POOL");
            pool.waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ServicePoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\n\n<<<<<##################### getEntity #########################\n\n");
    }

}
