/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BackgroundHandler;

import Basicfunctions.Initializer;
import Database.Database;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author patri
 */
public class DataCache {
    
    private static DataCache cache;
    
    Config config;
    BackUpHandler backup;
    HashMap<String,Database> databases;
    ObservableList images;
    ErrorLog log;
    
    private DataCache() {
        this.config = Config.getConfig();
        this.log = ErrorLog.getErrorLog();
        loadDatabases();
    }
    
    public static DataCache getDataCache() {
        if(cache==null){
            cache = new DataCache();
        }
        return cache;
    }
    
    private void loadDatabases() {
        this.databases = new HashMap<>();
        Database general = Initializer.initializeDatabase("General");
        this.databases.put("General", general);
        for(String type:this.config.get("PossibleTypes").split(",")){
            Database current = Initializer.initializeDatabase(type);
            this.databases.put(type, current);
        }
    }
    
    public Database getDatabase(String type){
        return this.databases.get(type);
    }
    
    public void setDragAndDropItems(ObservableList images){
        this.images = images;
    }
    
    public ObservableList getDragAndDropItems(){
        return this.images;
    }
    
    public String[] getImageIdentifier(){
        return this.config.get("ImageIdentifier").split(",");
    }
    
    public String getImageServerPath(){
        return this.config.get("ImageServer");
    }
    
    public void close(){
        this.databases.values().forEach((database) -> {
            database.close();
        });
    }
    
}
