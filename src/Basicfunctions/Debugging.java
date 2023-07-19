/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basicfunctions;

import BackgroundHandler.Config;
import BackgroundHandler.DataCache;
import BackgroundHandler.ErrorLog;
import Database.Database;
import Interfaces.ImageObject;
import static Observer.ObserveConnectedFilesClass.getConnectedFiles;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;

/**
 *
 * @author MSO
 */
public class Debugging {

    public static void search_for_missing_files() {
        boolean go = false;
        Alert alert = new Alert(AlertType.CONFIRMATION, "This will take a while, are you sure you want to continue?");
        alert.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType response) {
                if (response == ButtonType.OK) {
                    ProgressBar progressBar = new ProgressBar(0);
                    ErrorLog log = ErrorLog.getErrorLog();
                    Config config = Config.getConfig();
                    DataCache cache = DataCache.getDataCache();
                    Database base = cache.getDatabase("General");
                    HashMap<String, ArrayList> cases = base.getCaseMap();
                    int counter = 0;
                    for (String caseID : cases.keySet()) {
                        ArrayList images = cases.get(caseID);
                        for (Object o : images) {
                            ImageObject img = (ImageObject) o;
                            HashMap<String, SimpleStringProperty> img_hash = img.getAsHashMap();
                            for (String item : getConnectedFiles(img)) {
                                if (!img_hash.get(item).get().equals("")) {
                                    File file = new File(config.get("ImageServer") + img_hash.get(item).get());
                                    if (!file.exists()) {
                                        log.createLogEntry(2,"Missing: " + img_hash.get("ImageID").get() + ", File: Main image");
                                        counter += 1;
                                    }
                                }
                            }
                            Database propertybase = cache.getDatabase(img_hash.get("FileType").get());
                            ImageObject propertyfile = propertybase.get(img_hash.get("ImageID").get());
                            HashMap<String, SimpleStringProperty> property_hash = propertyfile.getAsHashMap();
                            for (String item : getConnectedFiles(propertyfile)) {
                                if (!property_hash.get(item).get().equals("")) {
                                    File file = new File(config.get("ImageServer") + property_hash.get(item).get());
                                    if (!file.exists()) {
                                        log.createLogEntry(2,"Missing: " + property_hash.get("ImageID").get() + ", File: " + item);
                                        counter += 1;
                                    }
                                }
                            }
                        }
                    }
                    Alert alert = new Alert(AlertType.INFORMATION, "Files has been checked, " + Integer.toString(counter) + " Files missing. Please check logs for more information.");
                    alert.showAndWait();
                }
            }
        });

    }
}
