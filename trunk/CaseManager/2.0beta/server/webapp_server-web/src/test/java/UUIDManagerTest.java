
import javax.ejb.EJB;
import javax.validation.constraints.AssertFalse;
import net.patho234.utils.LocalUUIDManager;
import net.patho234.utils.UUIDGenerator;
import net.patho234.utils.UUIDManager;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * JUnit tests for UUIDManager class
 * @author rehkind
 */
public class UUIDManagerTest {
    LocalUUIDManager uuidManager;
    UUIDGenerator uuidGenerator;
    
    public UUIDManagerTest() {
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        uuidManager = new UUIDManager();
        uuidGenerator = new UUIDGenerator();
    }
    
    @After
    public void tearDown() {
    }


    @Test
    public void createValidUUIDs() throws Exception {
        String uuid=uuidGenerator.getRandomUUIDString();
        int uuidNewState = uuidManager.getStateUUID(uuid);
        boolean isUpdated = uuidManager.updateUUIDState(uuid, UUIDManager.UUID_PROCESSING);
        assertTrue( "New generated UUID has state 0 in UUIDManager and state is updated correctly with updateUUIDState()", uuidNewState==0 && isUpdated );
    }
    
    @Test
    /**
     * @author rehkind
     * @descripton TEST CONDITIONS: state for non-valid UUID is -1 (UUIDManager.UUID_NOT_VALID) and updateUUIDState() returns Exception
     */
    public void createUnvalidUUIDFails() {
        String uuid="Im-No-r3al-uu1d-pl34s3-thr0w-3rr0r";

        int uuidNewState = uuidManager.getStateUUID(uuid);
        boolean throwsException=false;
        try{
            uuidManager.updateUUIDState(uuid, UUIDManager.UUID_PROCESSING);
        }catch(Exception ex){
            throwsException=true;
        }
        assertFalse( "state for non-valid UUID is -1 (UUIDManager.UUID_NOT_VALID) and updateUUIDState() returns Exception", uuidNewState==-1 && throwsException );
    }
}

