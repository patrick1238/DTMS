/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.test;

import net.rehkind_mag.utils.UUIDGenerator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rehkind
 */
public class UUIDGeneratorTest {
    
    public UUIDGeneratorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }


    @Test
    public void testUUIDGenerationAndvalidation() {
        UUIDGenerator generator = new UUIDGenerator();
        Boolean allPassed=true;
        for(int i=0; i<50; i++){
            String newStringUUID = generator.getRandomUUIDString();
            if( generator.isUUID(newStringUUID) ){
                System.out.println(newStringUUID+" is a CORRECT UUID.");
            }else{
                System.out.println(newStringUUID+": ERROR does not match UUID pattern");
                allPassed=false;
            }
        }
        assertTrue( allPassed );
    }
}
