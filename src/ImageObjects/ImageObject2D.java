/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageObjects;

import BackgroundHandler.ErrorLog;
import Interfaces.ImageObject;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author patri
 */
public class ImageObject2D implements ImageObject{
    private SimpleStringProperty ImageID;
    private SimpleStringProperty CaseID;
    private SimpleStringProperty CaseNumber;
    private SimpleStringProperty PrimaryStaining;
    private SimpleStringProperty Cellgraph;
    private SimpleStringProperty FilePath;
    
    public ImageObject2D(ArrayList<String> imageProps){
        this.ImageID = new SimpleStringProperty(imageProps.get(0));
        this.CaseID = new SimpleStringProperty(imageProps.get(1));
        this.CaseNumber = new SimpleStringProperty(imageProps.get(2));
        this.PrimaryStaining = new SimpleStringProperty(imageProps.get(3));
        this.Cellgraph = new SimpleStringProperty(imageProps.get(4));
        this.FilePath = new SimpleStringProperty(imageProps.get(5));
    }
    public ImageObject2D(){
        this.ImageID = new SimpleStringProperty("");
        this.CaseID = new SimpleStringProperty("");
        this.CaseNumber = new SimpleStringProperty("");
        this.PrimaryStaining = new SimpleStringProperty("");
        this.Cellgraph = new SimpleStringProperty("");
        this.FilePath = new SimpleStringProperty("");
    }

    @Override
    public String getImageID() {
        return this.ImageID.get();
    }
    
    @Override
    public void setImageID(String newID){
        this.ImageID.set(newID);
    }

    @Override
    public String getCaseID() {
        return this.CaseID.get();
    }
    
    @Override
    public void setCaseID(String newID){
        this.CaseID.set(newID);
    }

    @Override
    public String getCaseNumber() {
        return this.CaseNumber.get();
    }
    
    @Override
    public void setCaseNumber(String newNumber){
        this.CaseNumber.set(newNumber);
    }

    @Override
    public String getFilePath(){
        return this.FilePath.get();
    }
    
    @Override
    public void setFilePath(String path){
        this.FilePath.set(path);
    }
    
    public String getCellgraph(){
        return this.Cellgraph.get();
    }
    
    public void setCellgraph(String CellgraphPath){
        this.Cellgraph.set(CellgraphPath);
    }
    
    @Override
    public String getType() {
        return "2D";
    }
    
        
    @Override
    public String toString(String[] header) {
        HashMap<String, SimpleStringProperty> imgValues = this.getAsHashMap();
        String imgString = "";
        for (String key : header) {
            String value = imgValues.get(key).get();
            if (value != null) {
                if (value.contains(",")) {
                    value = "\"" + value + "\"";
                }
            } else {
                value = "";
            }
            imgString = imgString + "," + value;
        }
        return imgString.substring(1);
    }

    @Override
    public HashMap<String, SimpleStringProperty> getAsHashMap() {
        HashMap<String,SimpleStringProperty> map = new HashMap<>();
        Field[] fields = ImageObject2D.class.getDeclaredFields();
        for(Field field:fields){
            try {
                SimpleStringProperty output = (SimpleStringProperty) field.get(this);
                map.put(field.getName(), output);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(ImageObject2D.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }

    @Override
    public void setValue(String key, SimpleStringProperty value) {
        Field[] fields = ImageObject2D.class.getDeclaredFields();
        for(Field field:fields){
            try {
                if(field.getName().equals(key)){
                    SimpleStringProperty cur = (SimpleStringProperty) field.get(this);
                    cur.set(value.get());
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(ImageObject2D.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String getPrimaryStaining() {
        return this.PrimaryStaining.get();
    }

    @Override
    public void setPrimaryStaining(String stain) {
        this.PrimaryStaining.set(stain);
    }

    @Override
    public ImageObject getNewEmptyInstance() {
        return new ImageObject2D();
    }

    @Override
    public void open(File imagefile) {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(imagefile);
        } catch (IOException ex) {
            ErrorLog log = ErrorLog.getErrorLog();
            log.createLogEntry(1, "File does not exist.");
        }
    }
    
}
