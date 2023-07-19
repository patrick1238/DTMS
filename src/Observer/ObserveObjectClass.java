/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Observer;

import BackgroundHandler.DataCache;
import BackgroundHandler.ErrorLog;
import Interfaces.ImageObject;
import Database.Database;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author patri
 */
public class ObserveObjectClass {

    public static boolean observeSubmit(ImageObject img) {
        ErrorLog log = ErrorLog.getErrorLog();
        Database database = DataCache.getDataCache().getDatabase(img.getType());
        boolean observer = true;
        if (img.getCaseNumber().equals("") || img.getCaseNumber().contains(" ")) {
            log.createLogEntry(2, "CaseNumber missing or containing blanks");
            observer = false;
        }
        if (img.getPrimaryStaining().contains(" ")){
            log.createLogEntry(2, "Primary staining containing blanks");
            observer = false;
        }
        if (database.containsFile(img)) {
            log.createLogEntry(2, "File is still available");
            observer = false;
        }
        if (img.getFilePath().equals("") || img.getFilePath().contains(" ")) {
            log.createLogEntry(2, "File missing or containing blanks");
            observer = false;
        }
        for(SimpleStringProperty s:img.getAsHashMap().values()){
            if(s.get().contains("\"")){
                observer = false;
                log.createLogEntry(2, "Don't use \" inside cells.");
            }
        }
        return observer;
    }
    
    public static boolean observeUpdate(ImageObject img) {
        ErrorLog log = ErrorLog.getErrorLog();
        Database database = DataCache.getDataCache().getDatabase(img.getType());
        boolean observer = true;
        if (img.getCaseNumber().equals("") || img.getCaseNumber().contains(" ")) {
            log.createLogEntry(2, "CaseNumber missing or containing blanks");
            observer = false;
        }
        if (img.getPrimaryStaining().contains(" ")){
            log.createLogEntry(2, "Primary staining containing blanks");
            observer = false;
        }
        if (img.getFilePath().equals("") || img.getFilePath().contains(" ")) {
            log.createLogEntry(2, "File missing or containing blanks");
            observer = false;
        }
        for(SimpleStringProperty s:img.getAsHashMap().values()){
            if(s.get().contains("\"")){
                observer = false;
                log.createLogEntry(2, "Don't use \" inside cells.");
            }
        }
        return observer;
    }
    
}
