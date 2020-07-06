/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.pool;

import net.patho234.entities.pool.CasePool;
import net.patho234.entities.Mockups;
import com.sun.scenario.Settings;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.Assert.assertNotNull;
import net.patho234.entities.ClientCase;
import net.patho234.entities.ClientClinic;
import net.patho234.entities.ClientSubmitter;
import net.patho234.entities.UserLogin;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;
import net.patho234.webapp_client.WebappClientFXML;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;



/**
 *
 * @author rehkind
 */

public class CasePoolTest {
    static CasePool CASE_POOL;
    static int TIMEOUT=10000;
    public CasePoolTest() throws Exception{
        if(CASE_POOL==null){ setUpClass(); }
    }

    
    final public void setUpClass() throws Exception {
        WebappClientFXML.loadSettings();
        
        CASE_POOL=CasePool.createPool();
        CASE_POOL.getAllEntities(true);
        CASE_POOL.waitFor(TIMEOUT*3);
        System.out.println("ALL_POOL: "+CASE_POOL);
        System.out.println("ALL_POOL_ITEMS: "+CASE_POOL.getAllEntities().size());
        System.out.println("USER_LOGIN: "+UserLogin.getLoginAsJson());
    }
    
    
    /**
     * Test of createPool method, of class CasePool.
     */
    @Test
    public void testCreatePool() {
        System.out.println("\n\n########################## createPool ###################>>>>>\n\n");
        CasePool result = CasePool.createPool();
        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ Pool: "+result+"\n");
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
        
        ReadOnlyClientObjectList result = instance.getAllEntities(false);
        assertTrue(result.size()>0);
        
        try {
            System.out.println("WAITING FOR POOL");
            CASE_POOL.waitFor(TIMEOUT);
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
        ClientCase result = instance.getEntity(caseId, false);
        assertEquals(caseId, result.getId());
        
        try {
            System.out.println("WAITING FOR POOL");
            CASE_POOL.waitFor(TIMEOUT);
        } catch (TimeoutException ex) {
            Logger.getLogger(CasePoolTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\n\n<<<<<##################### getEntity #########################\n\n");
    }

    /**
     * Test of createEntity method, of class CasePool.
     */
    @Test
    public void testCreateAndDeleteEntity() throws TimeoutException{
        System.out.println("\n\n########################## create&deleteEntity ###################>>>>>\n\n");
        CasePool pool = CasePoolTest.CASE_POOL;
        System.out.println("[BEFORE_CREATE] _________________________________"+new Date().toGMTString());
        Integer caseCountBefore = pool.getAllEntities(false).size();
        
        
        System.out.println("[CREATE] _________________________________"+new Date().toGMTString());
        ClientCase caseToCreate = ClientCase.getCaseTemplate();
        Random rnd = new Random();
        String caseNumber = "TT_2020-"+rnd.nextInt(9999);
        caseToCreate.setCaseNumber(caseNumber);

        ClientClinic mockupClinic = Mockups.getClinicMockup(1);
        ClientSubmitter mockupSubmitter = Mockups.getSubmitterMockup(1);

        caseToCreate.setClinic(mockupClinic);
        caseToCreate.setDiagnose("cHL (MC)");
        caseToCreate.setEntryDate(new Date());
        caseToCreate.setSubmitter(mockupSubmitter);
        try {    
            pool.createEntity( caseToCreate );
        } catch (TimeoutException ex) {
            Logger.getLogger(CasePoolTest.class.getName()).log(Level.SEVERE, "Waiting for CREATE CASE failed somehow.", ex);
        }
        
        System.out.println("[AFTER_CREATE] _________________________________ "+new Date().toGMTString());
        System.out.println("PENDING IN POOL: "+pool.pendingHttpRequests.size());
        ReadOnlyClientObjectList<ClientCase> entities = pool.getAllEntities(false);
        ClientCase newCase=null;
        for(Object c : entities){
            ClientCase castC=(ClientCase)c;
            if(newCase==null){ newCase = castC; }
            else if (newCase.getId()<castC.getId()) {
                newCase = castC;
            }
        }
        
        Integer caseCountAfterCreate = entities.size();
        assertEquals((int)caseCountBefore+1, (int)caseCountAfterCreate);
        System.out.println("[BEFORE_DELETE] _________________________________ "+new Date().toGMTString());
        System.out.println("\n\n<<<<<##################### create&deleteEntity[CLEANUP] #########################\n\n");
        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ Cleaning created case: "+newCase.toString()+"\n");
        pool.deleteEntity(newCase);
        Integer caseCountAfterDelete = entities.size();
        
        assertEquals( (int)caseCountAfterCreate-1, (int)(caseCountAfterDelete) );
        System.out.println("[AFTER_DELETE] _________________________________ "+new Date().toGMTString());
        System.out.println("\n\n<<<<<##################### create&deleteEntity #########################\n\n");
       
    }


    /**
     * Test of persistEntity method, of class CasePool.
     */
    @Test
    public void testPersistEntity() {
        System.out.println("\n\n########################## persistEntity ###################>>>>>\n\n");
        
        CasePool pool = CasePoolTest.CASE_POOL;
        ClientCase entity = pool.getEntity(1);
        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ entity: "+entity.toString()+"\n");
        
        String caseNumber=entity.getCaseNumber();
        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ old number: "+caseNumber+"\n");
        String newCaseNumber= caseNumber.split("_T")[0];
        newCaseNumber=newCaseNumber+"_T"+new Random().nextInt(300);
        entity.setCaseNumber(newCaseNumber);
        
        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ new number: "+newCaseNumber+"\n");
        int requestId=-1;
        try{
            requestId = pool.persistEntity(entity, true);
        }catch( TimeoutException toEx ){
            System.out.println("TIMEOUT_EXCEPTION");
            toEx.printStackTrace();
        }
        assertTrue( requestId > 0 );
        if(CASE_POOL.pendingHttpRequests.keySet().contains( requestId )){
            System.out.println("UPDATE REQUEST STILL PENDING");
            try {
                System.out.println("WAITING FOR POOL");
                CASE_POOL.waitFor(TIMEOUT);
            } catch (TimeoutException ex) {
                Logger.getLogger(CasePoolTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ for assert: "+pool.getEntity(1).getCaseNumber()+"\n");
        assertTrue(pool.getEntity(1).getCaseNumber().equals(newCaseNumber));
        
        System.out.println("\n\n<<<<###################### persistEntity ######################\n\n");
    }

}
