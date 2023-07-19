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
public class PathObject {
    
    SimpleStringProperty Identifier;
    SimpleStringProperty Path;
    
    public PathObject(String Identifier, String path){
        this.Identifier = new SimpleStringProperty(Identifier);
        this.Path = new SimpleStringProperty(path);
    }
    
    public String getIdentifier(){
        return this.Identifier.get();
    }
    
    public void setIdentifier(String identifier){
        this.Identifier.set(identifier);
    }
    
    public String getPath(){
        return this.Path.get();
    }
    
    public void setPath(String path){
        this.Path.set(path);
    }
}
