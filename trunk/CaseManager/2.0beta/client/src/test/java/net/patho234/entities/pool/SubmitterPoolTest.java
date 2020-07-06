/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.pool;

import net.patho234.entities.pool.SubmitterPool;
import com.sun.scenario.Settings;
import java.util.concurrent.TimeoutException;
import net.patho234.entities.ClientSubmitter;
import net.patho234.entities.UserLogin;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;
import net.patho234.webapp_client.WebappClientFXML;
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
public class SubmitterPoolTest {
    static SubmitterPool SUBMITTER_POOL;
    static int TIMEOUT=10000;
    public SubmitterPoolTest() throws Exception{
        if(SUBMITTER_POOL==null){ setUpClass(); }
    }

    
    final public void setUpClass() throws Exception {
        WebappClientFXML.loadSettings();
        
        SUBMITTER_POOL=SubmitterPool.createPool();
        SUBMITTER_POOL.getAllEntities(true);
        SUBMITTER_POOL.waitFor(TIMEOUT*3);
        System.out.println("ALL_POOL: "+SUBMITTER_POOL);
        System.out.println("ALL_POOL_ITEMS: "+SUBMITTER_POOL.getAllEntities().size());
        System.out.println("USER_LOGIN: "+UserLogin.getLoginAsJson());
    }
    
    /**
     * Test of createPool method, of class SubmitterPool.
     */
    @Test
    public void testCreatePool() {
        System.out.println("\n\n########################## createPool ###################>>>>>\n\n");
        SubmitterPool result = SubmitterPool.createPool();
        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ Pool: "+result+"\n");
        assertEquals(result, SubmitterPoolTest.SUBMITTER_POOL );
        assertNotNull( result );
        System.out.println("\n\n<<<<<##################### createPool #########################\n\n");
    }

    /**
     * Test of getAllEntities method, of class SubmitterPool.
     */
    @Test
    public void testGetAllEntities() {
        System.out.println("\n\n########################## getAllEntities ###################>>>>>\n\n");
        
        UserLogin.getLoginAsJson();
        SubmitterPool instance = SubmitterPoolTest.SUBMITTER_POOL;
        
        ReadOnlyClientObjectList result = instance.getAllEntities(false);
        assertTrue(result.size()>0);
        
        try {
            System.out.println("WAITING FOR POOL");
            SUBMITTER_POOL.waitFor(TIMEOUT);
        } catch (TimeoutException ex) {
            Logger.getLogger(CasePoolTest.class.getName()).log(Level.FATAL, null, ex);
        }
        System.out.println("\n\n<<<<<##################### getAllEntities #########################\n\n");
    }

    /**
     * Test of getEntity method, of class SubmitterPool.
     */
    @Test
    public void testGetEntity() {
        System.out.println("\n\n########################## getEntity ###################>>>>>\n\n");
        int caseId = 1;
        SubmitterPool instance = SubmitterPoolTest.SUBMITTER_POOL;
        ClientSubmitter result = instance.getEntity(caseId, false);
        assertEquals(caseId, result.getId());
        
        try {
            System.out.println("WAITING FOR POOL");
            SUBMITTER_POOL.waitFor(TIMEOUT);
        } catch (TimeoutException ex) {
            Logger.getLogger(CasePoolTest.class.getName()).log(Level.FATAL, null, ex);
        }
        System.out.println("\n\n<<<<<##################### getEntity #########################\n\n");
    }
}
