/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.pool;

import com.sun.scenario.Settings;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.rehkind_mag.entities.ClientClinic;
import net.rehkind_mag.entities.UserLogin;
import net.rehkind_mag.interfaces.client.ReadOnlyClientObjectList;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author rehkind
 */

public class ClinicPoolTest {
    static ClinicPool CLINIC_POOL;
    
    public ClinicPoolTest() throws Exception{
        setUpClass();
    }

    @Before
    public void setUpClass() throws Exception {
        Settings.set("server.address", "http://192.168.31.1:8585/webapp/resources/");
        Settings.set("client.login", "guest");
        Settings.set("client.password", "123456");
        UserLogin.setLogin("guest", "123456");
        
        CLINIC_POOL=ClinicPool.createPool();
        CLINIC_POOL.getAllEntities(true);
        CLINIC_POOL.waitFor(30000);
        System.out.println("ALL_POOL: "+CLINIC_POOL);
        System.out.println("USER_LOGIN: "+UserLogin.getLoginAsJson());
    }
    

    
    @After
    public void tearDown() {
        try {
            System.out.println("WAITING FOR POOL");
            CLINIC_POOL.waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ClinicPoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of createPool method, of class ClinicPool.
     */
    @Test
    public void testCreatePool() {
        System.out.println("\n\n########################## createPool ###################>>>>>\n\n");
        ClinicPool result = ClinicPool.createPool();
        System.out.println("Pool: "+result);
        assertEquals(result, ClinicPoolTest.CLINIC_POOL );
        assertNotNull( result );
        System.out.println("\n\n<<<<<##################### createPool #########################\n\n");
    }

    /**
     * Test of getAllEntities method, of class ClinicPool.
     */
    @Test
    public void testGetAllEntities() {
        System.out.println("\n\n########################## getAllEntities ###################>>>>>\n\n");
        
        UserLogin.getLoginAsJson();
        ClinicPool pool = ClinicPoolTest.CLINIC_POOL;
        
        ReadOnlyClientObjectList<ClientClinic> result = pool.getAllEntities();
        assertEquals(true, (result.size()>0));
        
        try {
            System.out.println("WAITING FOR POOL");
            CLINIC_POOL.waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ClinicPoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\n\n<<<<<##################### getAllEntities #########################\n\n");
    }

    /**
     * Test of getEntity method, of class ClinicPool.
     */
    @Test
    public void testGetEntity() {
        System.out.println("\n\n########################## getEntity ###################>>>>>\n\n");
        int clinicId = 1;
        ClinicPool pool = ClinicPoolTest.CLINIC_POOL;
        ClientClinic result = pool.getEntity(clinicId);
        assertEquals(clinicId, result.getId());
        
        try {
            System.out.println("WAITING FOR POOL");
            CLINIC_POOL.waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(ClinicPoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\n\n<<<<<##################### getEntity #########################\n\n");
    }

    /**
     * Test of createEntity method, of class ClinicPool.
     */
    @Test
    public void testCreateAndDeleteEntity() {
        System.out.println("\n\n########################## create&deleteEntity ###################>>>>>\n\n");
        ClinicPool pool = ClinicPoolTest.CLINIC_POOL;

        Integer clinicCountBeforeCreate = pool.getAllEntities().size();

        ClientClinic clinicToCreate = ClientClinic.getClinicTemplate();
        clinicToCreate.setName("Testklinik");
        clinicToCreate.setZipCode("770022");
        clinicToCreate.setCity("Musterstadt");
        clinicToCreate.setStreet("Teststraße 25");
        
        try {    
            int requestId=pool.createEntity( clinicToCreate );
        } catch (TimeoutException ex) {
            Logger.getLogger(ClinicPoolTest.class.getName()).log(Level.SEVERE, "Timed out while waiting for CREATE CLINIC", ex);
        }    
        
        ReadOnlyClientObjectList<ClientClinic> entities = pool.getAllEntities();
        ClientClinic newClinic=null;
        for(Object c : entities){
            ClientClinic castC=(ClientClinic)c;
            if(newClinic==null){ newClinic = castC; }
            else if (newClinic.getId()<castC.getId()) {
                newClinic = castC;
            }
        }
        
        Integer clinicCountAfterCreate = entities.size();
        assertEquals((int)clinicCountBeforeCreate, (int)clinicCountAfterCreate-1);
        System.out.println("\n\n<<<<<##################### create&deleteEntity[CLEANUP] #########################\n\n");
        System.out.println("Cleaning created case: "+newClinic.toString());
        try {
            pool.deleteEntity(newClinic);
        } catch (TimeoutException ex) { 
                Logger.getLogger(ClinicPoolTest.class.getName()).log(Level.SEVERE, "Timed out whlie waiting for DELETE CLINIC", ex);
        }
        Integer clinicCountAfterDelete=pool.getAllEntities().size();
        assertEquals((int)clinicCountAfterCreate, (int)clinicCountAfterDelete+1);
        System.out.println("\n\n<<<<<##################### create&deleteEntity #########################\n\n");
       
    }

    /**
     * Test of persistEntity method, of class ClinicPool.
     */
    @Test
    public void testPersistEntity() {
        System.out.println("\n\n########################## persistEntity ###################>>>>>\n\n");
        
        ClinicPool pool = ClinicPoolTest.CLINIC_POOL;
        ClientClinic entity = pool.getEntity(1);
        System.out.println("entity: "+entity.toString());
        
        String clinicName=entity.getName();
        System.out.println("old name: "+clinicName);
        String newClinicName= clinicName.split("_T")[0];
        newClinicName=newClinicName+"_T"+new Random().nextInt(300);
        entity.setName(newClinicName);
        
        System.out.println("new name: "+newClinicName);
        int requestId = -1;
        try{
            requestId = pool.persistEntity(entity);
        }catch( TimeoutException toEx ){
            System.out.println("TIMEOUT_EXCEPTION");
            toEx.printStackTrace();
        }
        assertEquals( true, requestId > 0 );
        
        System.out.println("for assert: "+pool.getEntity(1).getName());
        assertEquals( pool.getEntity(1).getName(), newClinicName);
        
        System.out.println("\n\n<<<<###################### persistEntity ######################\n\n");
    }

}
