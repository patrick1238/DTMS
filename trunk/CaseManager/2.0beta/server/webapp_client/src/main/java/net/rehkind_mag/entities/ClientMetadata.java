/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities;

import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javax.json.JsonObject;
import net.rehkind_mag.entities.pool.ServicePool;
import net.rehkind_mag.interfaces.IMetadata;
import net.rehkind_mag.interfaces.IService;

/**
 *
 * @author rehkind
 */
public class ClientMetadata<T> extends ClientObjectBase<ClientMetadata> implements IMetadata<T>{
    StringProperty name = new SimpleStringProperty();
    
    IntegerProperty serviceId = new SimpleIntegerProperty();
    ObjectProperty data = new SimpleObjectProperty();
    
    @Override
    public boolean hasLocalChanges() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ClientMetadata getLocalClone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JsonObject toJson() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void merge(ClientMetadata toMergeWith) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addListener(ChangeListener<? super ClientMetadata> cl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeListener(ChangeListener<? super ClientMetadata> cl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void addListener(InvalidationListener il) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeListener(InvalidationListener il) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IService getService() {
        return ServicePool.createPool().getEntity( serviceId.getValue() );
    }

    @Override
    public T getData() {
        return (T)data.getValue();
    }

    @Override
    public void setData(T newData) {
        data.setValue( newData );
    }

    @Override
    public String getName() {
        return name.getValue();
    }
    
}
