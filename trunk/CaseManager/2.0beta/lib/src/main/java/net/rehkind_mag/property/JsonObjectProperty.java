/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.property;

import java.util.HashSet;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javax.json.JsonObject;

/**
 *
 * @author rehkind
 */
public class JsonObjectProperty implements ObservableValue<JsonObject>{
    JsonObject json;
    String propertyName = "JsonObjectProperty";
    HashSet<InvalidationListener> invalListener = new HashSet<>();
    HashSet<ChangeListener> changeListener = new HashSet<>();
    
    public JsonObjectProperty(String name){
        super();
        this.propertyName=name;
    }
    public JsonObjectProperty(){
        super();
    }
    
    @Override
    public void addListener(ChangeListener<? super JsonObject> listener) {
        changeListener.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super JsonObject> listener) {
        changeListener.remove(listener);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalListener.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalListener.remove(listener);
    }

    @Override
    public JsonObject getValue() {
        return json;
    }
    
    public void setValue(JsonObject newJson) {
        this.json = newJson;
    }
    
    @Override public String toString(){
        return json.toString();
    }
}
