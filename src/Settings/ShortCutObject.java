/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Settings;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author patri
 */
public class ShortCutObject {
    
    SimpleStringProperty Description;
    SimpleStringProperty Item;
    SimpleStringProperty Key;
    
    public ShortCutObject(String description, String item, String key){
        this.Description = new SimpleStringProperty(description);
        this.Item = new SimpleStringProperty(item);
        this.Key = new SimpleStringProperty(key);
    }
    
    public String getDescription(){
        return this.Description.get();
    }
    
    public void setDescription(String description){
        this.Description.set(description);
    }
    
    public String getItem(){
        return this.Item.get();
    }
    
    public void setItem(String item){
        this.Item.set(item);
    }
    
    public String getKey(){
        return this.Key.get();
    }
    
    public void setKey(String key){
        this.Key.set(key);
    }
    
    @Override
    public String toString(){
        return this.getDescription()+"-"+this.getItem()+"+"+this.getKey();
    }
    
}
