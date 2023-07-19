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
public class ImageObjectGeneral implements ImageObject {

    private SimpleStringProperty ImageID;
    private SimpleStringProperty CaseID;
    private SimpleStringProperty CaseNumber;
    private SimpleStringProperty Comment;
    private SimpleStringProperty EntryDate;
    private SimpleStringProperty CaptureDate;
    private SimpleStringProperty Diagnose;
    private SimpleStringProperty PrimaryStaining;
    private SimpleStringProperty SecondaryStaining;
    private SimpleStringProperty ThirdStaining;
    private SimpleStringProperty FileType;
    private SimpleStringProperty FilePath;

    public ImageObjectGeneral(ArrayList<String> imageProps) {
        this.ImageID = new SimpleStringProperty(imageProps.get(0));
        this.CaseID = new SimpleStringProperty(imageProps.get(1));
        this.CaseNumber = new SimpleStringProperty(imageProps.get(2));
        this.EntryDate = new SimpleStringProperty(imageProps.get(3));
        this.CaptureDate = new SimpleStringProperty(imageProps.get(4));
        this.Diagnose = new SimpleStringProperty(imageProps.get(5));
        this.PrimaryStaining = new SimpleStringProperty(imageProps.get(6));
        this.SecondaryStaining = new SimpleStringProperty(imageProps.get(7));
        this.ThirdStaining = new SimpleStringProperty(imageProps.get(8));
        this.FileType = new SimpleStringProperty(imageProps.get(9));
        this.FilePath = new SimpleStringProperty(imageProps.get(10));
        this.Comment = new SimpleStringProperty(imageProps.get(11));
    }

    public ImageObjectGeneral() {
        this.ImageID = new SimpleStringProperty("");
        this.CaseID = new SimpleStringProperty("");
        this.CaseNumber = new SimpleStringProperty("");
        this.EntryDate = new SimpleStringProperty("");
        this.CaptureDate = new SimpleStringProperty("");
        this.Diagnose = new SimpleStringProperty("");
        this.PrimaryStaining = new SimpleStringProperty("");
        this.SecondaryStaining = new SimpleStringProperty("");
        this.ThirdStaining = new SimpleStringProperty("");
        this.FileType = new SimpleStringProperty("2D");
        this.FilePath = new SimpleStringProperty("");
        this.Comment = new SimpleStringProperty("");
    }

    @Override
    public String getImageID() {
        return this.ImageID.get();
    }

    @Override
    public void setImageID(String newID) {
        this.ImageID.set(newID);
    }

    @Override
    public String getCaseID() {
        return this.CaseID.get();
    }

    @Override
    public void setCaseID(String newID) {
        this.CaseID.set(newID);
    }

    @Override
    public String getCaseNumber() {
        return this.CaseNumber.get();
    }

    @Override
    public void setCaseNumber(String newNumber) {
        this.CaseNumber.set(newNumber);
    }
    
    public String getEntryDate(){
        return this.EntryDate.get();
    }

    public void setEntryDate(String date) {
        this.EntryDate.set(date);
    }

    public String getCaptureDate() {
        return this.CaptureDate.get();
    }

    public void setCaptureDate(String date) {
        this.CaptureDate.set(date);
    }

    public String getDiagnose() {
        return this.Diagnose.get();
    }

    public void setDiagnose(String date) {
        this.Diagnose.set(date);
    }

    @Override
    public String getPrimaryStaining() {
        return this.PrimaryStaining.get();
    }

    @Override
    public void setPrimaryStaining(String staining) {
        this.PrimaryStaining.set(staining);
    }

    public String getSecondaryStaining() {
        return this.SecondaryStaining.get();
    }

    public void setSecondaryStaining(String staining) {
        this.SecondaryStaining.set(staining);
    }
    
    public String getThirdStaining() {
        return this.ThirdStaining.get();
    }

    public void setThirdStaining(String staining) {
        this.ThirdStaining.set(staining);
    }

    public String getFileType() {
        return this.FileType.get();
    }

    public void setFileType(String type) {
        this.FileType.set(type);
    }

    @Override
    public String getFilePath() {
        return this.FilePath.get();
    }

    @Override
    public void setFilePath(String path) {
        this.FilePath.set(path);
    }

    @Override
    public String getType() {
        return "General";
    }
    
    public String getComment(){
        return this.Comment.get();
    }
    
    public void setComment(String comment){
        this.Comment.set(comment);
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
    public void setValue(String key, SimpleStringProperty value) {
        Field[] fields = ImageObjectGeneral.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.getName().equals(key)) {
                    SimpleStringProperty cur = (SimpleStringProperty) field.get(this);
                    cur.set(value.get());
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(ImageObjectGeneral.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public HashMap<String, SimpleStringProperty> getAsHashMap() {
        HashMap<String, SimpleStringProperty> map = new HashMap<>();
        Field[] fields = ImageObjectGeneral.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                SimpleStringProperty output = (SimpleStringProperty) field.get(this);
                field.setAccessible(true);
                map.put(field.getName(), output);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(ImageObjectGeneral.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return map;
    }

    @Override
    public ImageObject getNewEmptyInstance() {
        return new ImageObjectGeneral();
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
