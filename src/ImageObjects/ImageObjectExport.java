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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author patri
 */
public class ImageObjectExport implements ImageObject {

    private SimpleStringProperty ImageID;
    private SimpleStringProperty CaseID;
    private SimpleStringProperty CaseNumber;
    private SimpleStringProperty PrimaryStaining;
    private SimpleStringProperty FileType;
    private SimpleStringProperty FilePath;

    public ImageObjectExport(String ImageID, String CaseID, String CaseNumber, String PrimaryStaining, String FileType, String FilePath) {
        this.ImageID = new SimpleStringProperty(ImageID);
        this.CaseID = new SimpleStringProperty(CaseID);
        this.CaseNumber = new SimpleStringProperty(CaseNumber);
        this.PrimaryStaining = new SimpleStringProperty(PrimaryStaining);
        this.FileType = new SimpleStringProperty(FileType);
        this.FilePath = new SimpleStringProperty(FilePath);
    }

    public ImageObjectExport() {
        this.ImageID = new SimpleStringProperty("");
        this.CaseNumber = new SimpleStringProperty("");
        this.PrimaryStaining = new SimpleStringProperty("");
        this.FileType = new SimpleStringProperty("");
    }

    @Override
    public String getImageID() {
        return this.ImageID.get();
    }

    @Override
    public void setImageID(String id) {
        this.ImageID.set(id);
    }
    
    @Override
    public String getCaseID() {
        return this.CaseID.get();
    }

    @Override
    public void setCaseID(String id) {
        this.CaseID.set(id);
    }

    @Override
    public String getCaseNumber() {
        return this.CaseNumber.get();
    }

    @Override
    public void setCaseNumber(String nmr) {
        this.CaseNumber.set(nmr);
    }

    @Override
    public String getPrimaryStaining() {
        return this.PrimaryStaining.get();
    }

    @Override
    public void setPrimaryStaining(String stain) {
        this.PrimaryStaining.set(stain);
    }

    public String getFileType() {
        return this.FileType.get();
    }

    public void setFileType(String FileType) {
        this.FileType.set(FileType);
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
        return "Export";
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
    public ImageObjectExport getNewEmptyInstance() {
        return new ImageObjectExport();
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
