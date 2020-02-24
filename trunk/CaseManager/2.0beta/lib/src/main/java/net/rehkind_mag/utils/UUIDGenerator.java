/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.utils;

import java.util.UUID;
import org.jboss.logging.Logger;


/**
 * creates and validates UUID Strings
 * @author rehkind
 */
public class UUIDGenerator {
    public String getRandomUUIDString(){
        
        UUID newUUID = UUID.randomUUID();
        Logger.getLogger("global").info("New UUID generated: '"+newUUID.toString()+"'");
        return newUUID.toString();
    }
    
    /**
     * validates if given string is UUID V1-V5 according to RFC4122
     * @param strUUID
     * @return true: correct UUID false: doesnt match UUID pattern
     */
    public boolean isUUID(String strUUID){
        return strUUID.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$");
    }
    
}
