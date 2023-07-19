/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.io.File;
import java.util.HashMap;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author patri
 */
public interface ImageObject {

    /**
     *
     * @return
     */
    public String getImageID();
    
    /**
     *
     * 
     * @param id
     */
    public void setImageID(String id);

    /**
     *
     * @return
     */
    public String getCaseID();
    
    /**
     *
     * 
     * @param id
     */
    public void setCaseID(String id);

    /**
     *
     * @return
     */
    public String getCaseNumber();
    
    /**
     *
     * 
     * @param nmr
     */
    public void setCaseNumber(String nmr);
    
    public String getPrimaryStaining();
    public void setPrimaryStaining(String stain);
    
    public String getType();
    
    /**
     *
     * @return
     */
    public String getFilePath();
    
    /**
     *
     * @param path
     */
    public void setFilePath(String path);
    
    public void setValue(String key, SimpleStringProperty value);
    
    /**
     *
     * @return
     */
    public HashMap<String,SimpleStringProperty> getAsHashMap();

    /**
     *
     * @param header
     * @return
     */
    public String toString(String[] header);
    
    public ImageObject getNewEmptyInstance();
    
    public void open(File imagefile);
}