/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.utils;

import javax.ejb.Local;
import javax.ejb.Singleton;

/**
 *
 * @author rehkind
 */
@Singleton
@Local
public interface LocalUUIDManager {
    public static final int UUID_NOT_VALID=-1;
    public static final int UUID_OPEN=0;
    public static final int UUID_PROCESSING=1;
    public static final int UUID_CLOSED=2;
    
    public int getStateUUID( String uuid );
    public boolean updateUUIDState( String uuid, int state ) throws Exception;
    public boolean isAvailableUUID(String uuidAsString);
    
    public void cleanPendingUUIDs();
    
    static public String getStateAsString(int stateAsInt){
        String stateAsString;
        switch(stateAsInt){
            case UUID_NOT_VALID:
                stateAsString = "UUID_NOT_VALID";
                break;
            case UUID_OPEN:
                stateAsString = "UUID_OPEN";
                break;
            case UUID_PROCESSING:
                stateAsString = "UUID_PROCESSING";
                break;
            default:
                stateAsString = "UUID_NOT_VALID";
                break;
        }
        return stateAsString;
    }
}
