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
public class ImageObject4D implements ImageObject{
    private SimpleStringProperty ImageID;
    private SimpleStringProperty CaseID;
    private SimpleStringProperty CaseNumber;
    private SimpleStringProperty PrimaryStaining;
    private SimpleStringProperty Tiles;
    private SimpleStringProperty ImarisFile;
    private SimpleStringProperty OverviewFile;
    private SimpleStringProperty FilePath;
    
    public ImageObject4D(ArrayList<String> imageProps){
        this.ImageID = new SimpleStringProperty(imageProps.get(0));
        this.CaseID = new SimpleStringProperty(imageProps.get(1));
        this.CaseNumber = new SimpleStringProperty(imageProps.get(2));
        this.PrimaryStaining = new SimpleStringProperty(imageProps.get(3));
        this.Tiles = new SimpleStringProperty(imageProps.get(4));
        this.ImarisFile = new SimpleStringProperty(imageProps.get(5));
        this.OverviewFile = new SimpleStringProperty(imageProps.get(6));
        this.FilePath = new SimpleStringProperty(imageProps.get(7));
    }
    
    public ImageObject4D(){
        this.ImageID = new SimpleStringProperty("");
        this.CaseID = new SimpleStringProperty("");
        this.CaseNumber = new SimpleStringProperty("");
        this.PrimaryStaining = new SimpleStringProperty("");
        this.Tiles = new SimpleStringProperty("");
        this.ImarisFile = new SimpleStringProperty("");
        this.OverviewFile = new SimpleStringProperty("");
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
    public String getType() {
        return "4D";
    }
    
    public String getTiles(){
        return this.Tiles.get();
    }
    
    public void setTiles(String tiles){
        this.Tiles.set(tiles);
    }
    
    public String getImarisFile(){
        return this.ImarisFile.get();
    }
    
    public void setImarisFile(String imarisFile){
        this.ImarisFile.set(imarisFile);
    }
    
    public String getOverviewFile(){
        return this.OverviewFile.get();
    }
    
    public void setOverviewFile(String overviewFile){
        this.OverviewFile.set(overviewFile);
    }

    @Override
    public String getFilePath(){
        return this.FilePath.get();
    }
    
    @Override
    public void setFilePath(String path){
        this.FilePath.set(path);
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
        Field[] fields = ImageObject4D.class.getDeclaredFields();
        for(Field field:fields){
            try {
                SimpleStringProperty output = (SimpleStringProperty) field.get(this);
                map.put(field.getName(), output);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(ImageObject3D.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }

    @Override
    public void setValue(String key, SimpleStringProperty value){
        HashMap<String,SimpleStringProperty> map = new HashMap<>();
        Field[] fields = ImageObject4D.class.getDeclaredFields();
        for(Field field:fields){
            try {
                if(field.getName().equals(key)){
                    SimpleStringProperty cur = (SimpleStringProperty) field.get(this);
                    cur.set(value.get());
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(ImageObject4D.class.getName()).log(Level.SEVERE, null, ex);
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
        return new ImageObject4D();
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
