/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.pool;

import com.sun.scenario.Settings;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import net.rehkind_mag.entities.ClientCase;
import net.rehkind_mag.entities.ClientClinic;
import net.rehkind_mag.entities.ClientSubmitter;
import net.rehkind_mag.entities.UserLogin;
import net.rehkind_mag.interfaces.IHttpResponse;
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

public class CasePoolTest {
    static CasePool CASE_POOL;
    
    public CasePoolTest() throws Exception{
        CasePoolTest.setUpClass();
    }

    @BeforeAll
    public static void setUpClass() throws Exception {
        Settings.set("server.address", "http://192.168.31.1:8585/webapp/resources/");
        Settings.set("client.login", "guest");
        Settings.set("client.password", "123456");
        UserLogin.setLogin("guest", "123456");
        
        CASE_POOL=CasePool.createPool();
        CASE_POOL.getAllEntities();
        CASE_POOL.waitFor(30000);
        System.out.println("ALL_POOL: "+CASE_POOL);
        System.out.println("USER_LOGIN: "+UserLogin.getLoginAsJson());
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
        try {
            System.out.println("WAITING FOR POOL");
            CASE_POOL.waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(CasePoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of createPool method, of class CasePool.
     */
    @Test
    public void testCreatePool() {
        System.out.println("\n\n########################## createPool ###################>>>>>\n\n");
        CasePool result = CasePool.createPool();
        System.out.println("Pool: "+result);
        assertEquals(result, CasePoolTest.CASE_POOL );
        assertNotNull( result );
        System.out.println("\n\n<<<<<##################### createPool #########################\n\n");
    }

    /**
     * Test of getAllEntities method, of class CasePool.
     */
    @Test
    public void testGetAllEntities() {
        System.out.println("\n\n########################## getAllEntities ###################>>>>>\n\n");
        
        UserLogin.getLoginAsJson();
        CasePool instance = CasePoolTest.CASE_POOL;
        
        ReadOnlyClientObjectList result = instance.getAllEntities();
        assertTrue(result.size()>0);
        
        try {
            System.out.println("WAITING FOR POOL");
            CASE_POOL.waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(CasePoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\n\n<<<<<##################### getAllEntities #########################\n\n");
    }

    /**
     * Test of getEntity method, of class CasePool.
     */
    @Test
    public void testGetEntity() {
        System.out.println("\n\n########################## getEntity ###################>>>>>\n\n");
        int caseId = 1;
        CasePool instance = CasePoolTest.CASE_POOL;
        ClientCase result = instance.getEntity(caseId);
        assertEquals(caseId, result.getId());
        
        try {
            System.out.println("WAITING FOR POOL");
            CASE_POOL.waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(CasePoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\n\n<<<<<##################### getEntity #########################\n\n");
    }

    /**
     * Test of createEntity method, of class CasePool.
     */
    @Test
    public void testCreateEntity() {
        System.out.println("\n\n########################## createEntity ###################>>>>>\n\n");
        CasePool pool = CasePoolTest.CASE_POOL;

        Integer caseCountBefore = pool.getAllEntities().size();

        ClientCase caseToCreate = ClientCase.getCaseTemplate();
        caseToCreate.setCaseNumber("TT_2020-TEST");

        ClientClinic mockupClinic = Mockups.getClinicMockup(1);
        ClientSubmitter mockupSubmitter = Mockups.getSubmitterMockup(1);

        caseToCreate.setClinic(mockupClinic);
        caseToCreate.setDiagnose("cHL (MC)");
        caseToCreate.setEntryDate(new Date());
        caseToCreate.setSubmitter(mockupSubmitter);
        try {    
            int requestId=pool.createEntity( caseToCreate );
            pool.waitForRequest(requestId);
        } catch (TimeoutException ex) {
            Logger.getLogger(CasePoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
        ReadOnlyClientObjectList<ClientCase> entities = pool.getAllEntities();
        ClientCase newCase=null;
        for(Object c : entities){
            ClientCase castC=(ClientCase)c;
            if(newCase==null){ newCase = castC; }
            else if (newCase.getId()<castC.getId()) {
                newCase = castC;
            }
        }
        
        Integer caseCountAfter = entities.size();
        assertEquals((int)caseCountBefore, (int)caseCountAfter-1);
        System.out.println("\n\n<<<<<##################### createEntity[CLEANUP] #########################\n\n");
        System.out.println("Cleaning created case: "+newCase.toString());
        pool.deleteEntity(newCase);
        try { pool.waitFor(30000); }catch (TimeoutException ex) { Logger.getLogger(CasePoolTest.class.getName()).log(Level.SEVERE, null, ex); } 
        System.out.println("\n\n<<<<<##################### createEntity #########################\n\n");
       
    }

    /**
     * Test of deleteEntity method, of class CasePool.
     */
    @Test
    public void testDeleteEntity() {
        System.out.println("\n\n########################## deleteEntity ###################>>>>>\n\n");
        ClientCase entity = null;
        CasePool instance = null;
        boolean expResult = false;
        boolean result = instance.deleteEntity(entity);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        System.out.println("\n\n<<<<<##################### deleteEntity #########################\n\n");
    }

    /**
     * Test of persistEntity method, of class CasePool.
     */
    @Test
    public void testPersistEntity() {
        System.out.println("\n\n########################## persistEntity ###################>>>>>\n\n");
        
        CasePool pool = CasePoolTest.CASE_POOL;
        ClientCase entity = pool.getEntity(1);
        System.out.println("entity: "+entity.toString());
        boolean result = false;
        String caseNumber=entity.getCaseNumber();
        System.out.println("old number: "+caseNumber);
        String newCaseNumber= caseNumber.split("_T")[0];
        newCaseNumber=newCaseNumber+"_T"+new Random().nextInt(300);
        entity.setCaseNumber(newCaseNumber);
        
        System.out.println("new number: "+newCaseNumber);
        try{
            result = pool.persistEntity(entity);
        }catch( TimeoutException toEx ){
            System.out.println("TIMEOUT_EXCEPTION");
            toEx.printStackTrace();
        }
        assertTrue( result );
        
        try {
            System.out.println("WAITING FOR POOL");
            CASE_POOL.waitFor(30000);
        } catch (TimeoutException ex) {
            Logger.getLogger(CasePoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("for assert: "+pool.getEntity(1).getCaseNumber());
        assertTrue(pool.getEntity(1).getCaseNumber().equals(newCaseNumber),"Test if caseNumber is now set to new value");
        
        System.out.println("\n\n<<<<###################### persistEntity ######################\n\n");
    }

}
