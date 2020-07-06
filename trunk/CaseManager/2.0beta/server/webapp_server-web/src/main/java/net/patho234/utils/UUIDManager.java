/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.utils;

import java.util.HashMap;
import javax.ejb.Stateless;

/**
 *
 * @author rehkind
 */
@Stateless
public class UUIDManager implements LocalUUIDManager {
    // remember uuid states 24h after last update: 
    public static final int BUFFER_TIME_UUIDS=3600000*24; // time in milliseconds - how long to buffer UUIDs 3600000 ms = 1h
    public static final int BUFFER_TIME_CLEAN=3600000*24; // time in milliseconds - how long to buffer UUIDs 3600000 ms = 1h
    
    HashMap<String, UUIDEntry> uuidPool = new HashMap<>();
    long lastClean =- 1;
    
    @Override
    public boolean isAvailableUUID(String uuidAsString){
        return ( this.getStateUUID( uuidAsString ) == 0 );
    }
    
    @Override
    public int getStateUUID(String uuid) {
        if( uuidPool.get(uuid)==null ){
            return UUID_OPEN;
        }else{
            return uuidPool.get(uuid).state;
        }
    }

    @Override
    public boolean updateUUIDState(String uuid, int state) throws Exception {
        if( state < 0 || state > 2 ){
            throw new Exception("ERROR: only allowed states for UUID are: 1, 2, 3...request was: '"+state+"'");
        }
        if( uuidPool.get(uuid)==null ){
            this.uuidPool.put(uuid, new UUIDEntry(state));
        }else{
            this.uuidPool.get(uuid).setState(state);
        }
        
        return true;
    }

    @Override
    public void cleanPendingUUIDs() {
        int counter=0;
        for( String uuid : uuidPool.keySet() ){
            if( (System.currentTimeMillis() - uuidPool.get(uuid).lastUpdate) > BUFFER_TIME_UUIDS ){
                uuidPool.remove(uuid);
                counter++;
            }
        }
        System.out.println("cleanPendingUUIDs(): "+counter+" old UUIDs removed.");
        lastClean=System.currentTimeMillis();
    }


    private class UUIDEntry{
        int state=-1;
        long lastUpdate=-1;
        
        public UUIDEntry(int state){
            this.state=state;
            lastUpdate=System.currentTimeMillis();
        }
        
        public void setState(int newState){
            this.state = newState;
        }
    }
}

