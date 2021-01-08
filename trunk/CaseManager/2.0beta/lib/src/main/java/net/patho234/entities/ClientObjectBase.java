/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javax.json.JsonObject;
import net.patho234.interfaces.client.IClientObject;
import net.patho234.property.JsonObjectProperty;

/**
 *
 * @author rehkind
 */
public abstract class ClientObjectBase<T extends IClientObject> implements IClientObject<T>{
    static final protected SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
    static final protected SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
    
    IntegerProperty ID = new SimpleIntegerProperty();
    JsonObjectProperty  original = new JsonObjectProperty();
    
    HashSet<ChangeListener> changeListener = new HashSet<>();
    HashSet<InvalidationListener> invalListener = new HashSet<>();
    
    @Override
    public JsonObject getOriginalJson() {
        return original.getValue();
    }

    @Override
    public void setOriginalJson(JsonObject obj) {
        original.setValue(obj);
    }

    @Override
    public Integer getId(){ return ID.getValue(); }
    
    
    @Override
    public void addListener(ChangeListener<? super T> listener) {
        this.changeListener.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super T> listener) {
        this.changeListener.remove(listener);
    }

    @Override
    public T getValue() {
        return (T)this;
    }

    @Override
    public void addListener(InvalidationListener listener) {
        this.invalListener.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        this.invalListener.remove(listener);
    }

    @Override
    public void merge(T toMergeWith) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString(){
        return toJson().toString()+" (original: "+original.getValue().toString()+")";
    }
}
