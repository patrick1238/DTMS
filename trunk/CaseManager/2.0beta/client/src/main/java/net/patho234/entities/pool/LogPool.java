/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.pool;

import java.util.concurrent.TimeoutException;
import net.patho234.entities.ClientLogEntry;
import net.patho234.interfaces.IHttpResponse;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;

/**
 *
 * @author rehkind
 */
public class LogPool  extends AClientObjectPool<ClientLogEntry> {

    @Override
    public ClientLogEntry getEntity(int ID, Boolean updatePool) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReadOnlyClientObjectList<ClientLogEntry> getAllEntities(Boolean updatePool) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int createEntity(ClientLogEntry toCreate) throws TimeoutException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int deleteEntity(ClientLogEntry entity) throws TimeoutException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int persistEntity(ClientLogEntry entity, boolean forcePersist) throws TimeoutException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receiveHttpResponse(IHttpResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
