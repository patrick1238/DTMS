package Basicfunctions;

import BackgroundHandler.DataCache;
import BackgroundHandler.ErrorLog;
import Interfaces.ImageObject;
import Observer.ObserveConnectedFilesClass;
import Database.Database;
import ImageObjects.ImageObject2D;
import ImageObjects.ImageObject3D;
import ImageObjects.ImageObjectGeneral;
import Observer.ObserveFilePathClass;
import Observer.ObserveObjectClass;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javax.swing.JFileChooser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author patri
 */
public class Importer {
    
    public static boolean parseDTMSFolder(File folder) {
        DataCache cache = DataCache.getDataCache();
        File databases = find("DTMSDatabases", folder);
        if (databases != null) {
            for (File database : databases.listFiles()) {
                String type = database.getName().replace(".csv", "");
                String path = database.getAbsolutePath();
                ObservableList images = Loader.loadFiles(path, type);
                Database base = cache.getDatabase(type);
                for (Object o : images) {
                    ImageObject img = (ImageObject) o;
                    if (!base.contains(img.getImageID())) {
                        base.addFile(ObserveConnectedFilesClass.convertRelativeToAbsoluteFilePaths(folder.getAbsolutePath() + File.separator, img));
                    } else {
                        ErrorLog.getErrorLog().createLogEntry(1, img.getImageID() + "skipped, already in DB");
                    }
                }
            }
        }
        return true;
    }

    public static boolean parse2DFolder(File folder) {
        DataCache cache = DataCache.getDataCache();
        Database propertyDatabase = cache.getDatabase("2D");
        Database db = cache.getDatabase("General");
        for (File img : folder.listFiles()) {
            if (ObserveFilePathClass.observer2DFilePath(img)) {
                ErrorLog.getErrorLog().createLogEntry(0, "Importing " + img.getName());
                String[] cutted = img.getName().split(" - ");
                String[] date_props = cutted[1].split(" ")[0].split("-");
                String capture_date = date_props[2] + "." + date_props[1] + "." + date_props[0];
                String[] img_props = cutted[0].split("_");
                ImageObjectGeneral general = new ImageObjectGeneral();
                general.setCaptureDate(capture_date);
                general.setCaseNumber(img_props[0] + "-" + img_props[1]);
                String add = "";
                if (img_props[3].toLowerCase().contains("rezidiv")) {
                    add = ", Rezidiv";
                }
                if (img_props[3].toLowerCase().contains("nekrosen")) {
                    add = add + ", Nekrosen";
                }
                if (img_props[3].toLowerCase().contains("initiale infiltration")) {
                    add = add + ", initiale Infiltration";
                }
                if (img_props[3].toLowerCase().contains("schwierig")) {
                    add = add + ", schwierig";
                }
                general.setDiagnose(ObserveFilePathClass.observeDiagnosis(img_props[3]));
                general.setFileType("2D");
                general.setPrimaryStaining(img_props[4]);
                general.setThirdStaining("Haematoxilin");
                String origin = "unknown";
                if (img_props.length > 5) {
                    origin = img_props[5];
                }
                general.setComment("BlockID: " + img_props[2] + ", Origin: " + origin + add);
                File renamed = new File(img.getAbsolutePath().replace(" ", ""));
                try {
                    Files.copy(img.toPath(), renamed.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
                }
                general.setFilePath(renamed.getAbsolutePath());
                if (ObserveObjectClass.observeSubmit(general)) {
                    db.addFile(general);
                }
                ImageObject2D property = new ImageObject2D();
                property.setCaseNumber(capture_date);
                for (String key : cache.getImageIdentifier()) {
                    property.setValue(key, general.getAsHashMap().get(key));
                }
                if (ObserveObjectClass.observeSubmit(property)) {
                    propertyDatabase.addFile(property);
                }
                try {
                    Files.delete(Paths.get(renamed.getAbsolutePath()));
                } catch (IOException ex) {
                    Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }

    private static File find(String filename, File dir) {
        for (File cur : dir.listFiles()) {
            if (cur.getName().equals(filename)) {
                return cur;
            }
        }
        return null;
    }

}
