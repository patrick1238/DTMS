/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.interfaces.client;

import java.util.Collection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class ClientObjectList<T extends IClientObject> extends ReadOnlyClientObjectList<T> implements ChangeListener<IClientObject>{
    
    public void put(T object){
        beginChange();
        try{
            internalPut(object);
        }catch(IndexOutOfBoundsException ex){
            Logger.getLogger(getClass()).warn(ex.getMessage());
            Logger.getLogger(getClass()).warn(String.format( "caused by object: {0}", new Object[]{ object }) );
        }finally{
            endChange();
        }
    }
    
    public void putAll(Collection<T> objects){
        beginChange();
        objects.forEach((object) -> {
            try{
                internalPut(object);
            }catch(IndexOutOfBoundsException ex){
                Logger.getLogger(getClass()).warn(ex.getMessage());
                Logger.getLogger(getClass()).warn(String.format( "caused by object: {0}", new Object[]{ object }));
            }
        });
        endChange();
    }
    
    private void internalPut(T object) throws IndexOutOfBoundsException{
        int id = object.getId();
        if( id<=0 ){
            throw new IndexOutOfBoundsException("addOrMerge called for object with invalid ID '"+id+"'");
        }
        IClientObject alreadyInList = getByID(id);
        if( alreadyInList==null ){
            object.addListener(this);
            this.cachedObjects.put(id, object);
        }else{
            Logger.getLogger( getClass() ).info("ClientObject is already in list calling ClientObject.merge().");
            alreadyInList.merge(object);
        }
    }

    @Override
    public void changed(ObservableValue<? extends IClientObject> observable, IClientObject oldValue, IClientObject newValue) {
        System.out.println("["+getClass().getName()+"]: one of my objects changed: "+((IClientObject)observable).getId());
    }
    
    
    
}
