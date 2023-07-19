/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import BackgroundHandler.Config;
import BackgroundHandler.ErrorLog;
import Interfaces.ImageObject;
import Observer.ObserveConnectedFilesClass;
import Threads.DatabaseUpdateThread;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author patri
 * @param <T>
 */
public class Database<T extends ImageObject> {

    private ObservableList<T> databaseList;
    private final ObservableList<T> tableList;
    private final HashMap<String, T> imageIDConnector;
    private final HashMap<String, ArrayList<T>> caseIDConnector;
    private final HashSet<String> filePathConnector;
    private final ErrorLog log;
    private final Config config;
    private final String type;
    private DatabaseUpdateThread thread;

    /**
     *
     * @param list
     * @param type
     */
    public Database(ObservableList<T> list, String type) {
        this.log = ErrorLog.getErrorLog();
        this.config = Config.getConfig();
        this.tableList = list;
        this.type = type;
        this.imageIDConnector = new HashMap<>();
        this.caseIDConnector = new HashMap<>();
        this.filePathConnector = new HashSet();
        this.thread = new DatabaseUpdateThread(this);
        this.thread.start();
        initializedatabase();
    }

    private void initializedatabase() {
        this.databaseList = FXCollections.observableArrayList();
        this.tableList.forEach((t) -> {
            this.databaseList.add(t);
            this.imageIDConnector.put(t.getImageID(), t);
            this.filePathConnector.add(new File(t.getFilePath()).getName());
            this.updateCaseIDConnector(t);
        });
    }

    private void updateCaseIDConnector(T t) {
        if (this.caseIDConnector.containsKey(t.getCaseID())) {
            this.caseIDConnector.get(t.getCaseID()).add(t);
        } else {
            ArrayList<T> images = new ArrayList<>(Arrays.asList(t));
            this.caseIDConnector.put(t.getCaseID(), images);
        }
    }

    public ArrayList getAllImagesWithCaseID(String caseID) {
        return this.caseIDConnector.get(caseID);
    }

    public String getDatabaseFilePath() {
        return this.config.get("csvPath" + this.type);
    }

    public ObservableList getDatabaseList() {
        return this.databaseList;
    }

    public ObservableList getTableList() {
        return this.tableList;
    }

    public void setTableList(ObservableList list) {
        this.tableList.setAll(list);
    }

    public String getType() {
        return this.type;
    }

    public DatabaseUpdateThread getUpdateThread() {
        return this.thread;
    }

    public void setUpdateThread(DatabaseUpdateThread thread) {
        this.thread = thread;
        this.thread.start();
    }

    public int getIndexOf(T t) {
        return this.databaseList.indexOf(t);
    }
    
    public int getTableListIndexOf(T t){
        return this.tableList.indexOf(t);
    }

    public boolean contains(String id) {
        return this.imageIDConnector.containsKey(id);
    }

    public T get(String id) {
        return this.imageIDConnector.get(id);
    }

    public String[] getEntryHeader() {
        return this.config.get(type + "Header").split(",");
    }

    public boolean containsFile(T t) {
        return this.filePathConnector.contains(new File(t.getFilePath()).getName());
    }

    public ObservableList getPossibleEntries(String key) {
        ObservableList possibleEntries = FXCollections.observableArrayList();
        for (T t : this.databaseList) {
            String entry = t.getAsHashMap().get(key).get();
            if (entry != null) {
                if (!entry.equalsIgnoreCase("")) {
                    if (!possibleEntries.contains(entry)) {
                        possibleEntries.add(entry);
                    }
                }
            }
        }
        return possibleEntries;
    }

    /**
     *
     * @param img
     */
    public void addFile(T img) {
        ObserveConnectedFilesClass.observeConnectedFiles(img,true);
        if (img.getImageID().equals("")) {
            initImageID(img);
        }
        if (img.getCaseID().equals("")) {
            initCaseID(img);
        }
        this.databaseList.add(img);
        this.tableList.add(img);
        if(img.getType().equals("General")){
            this.log.createLogEntry(0, img.getImageID() + " added");
        }else{
            this.log.createLogEntry(0,"Properties of " + img.getImageID() + " added");
        }
        this.imageIDConnector.put(img.getImageID(), img);
        this.updateCaseIDConnector(img);
        this.filePathConnector.add(new File(img.getFilePath()).getName());
        this.thread.add(img.toString(this.getEntryHeader()));
    }

    /**
     *
     * @param img
     */
    public void deleteFile(T img) {
        this.delete(img);
    }

    public void deleteFile(String id) {
        T img = imageIDConnector.get(id);
        this.delete(img);
    }
    
    private void delete(T img) {
        ObserveConnectedFilesClass.deleteConnectedFiles(img);
        this.databaseList.remove(img);
        this.tableList.remove(img);
        this.thread.delete(img.getImageID());
        if(img.getType().equals("General")){
            this.log.createLogEntry(0, img.getImageID() + " removed from database");
        }else{
            this.log.createLogEntry(0,"Properties of " + img.getImageID() + " removed from database");
        }
        this.imageIDConnector.remove(img.getImageID());
        this.caseIDConnector.get(img.getCaseID()).remove(img);
        this.filePathConnector.remove(new File(img.getFilePath()).getName());
    }

    public void replaceFile(T oldImg, T newImg) {
        ObserveConnectedFilesClass.observeConnectedFiles(newImg,false);
        this.initCaseID(newImg);
        int dbListIndex = this.databaseList.indexOf(oldImg);
        int tableListIndex = this.getTableListIndexOf(oldImg);
        int caseIDListIndex = this.caseIDConnector.get(oldImg.getCaseID()).indexOf(oldImg);
        if(oldImg.getType().equals("General")){
            this.log.createLogEntry(0, oldImg.getImageID() + " edited");
        }else{
            this.log.createLogEntry(0,"Properties of " + oldImg.getCaseNumber() + " edited");
        }
        this.databaseList.set(dbListIndex, newImg);
        this.tableList.set(tableListIndex, newImg);
        this.imageIDConnector.replace(newImg.getImageID(), newImg);
        this.filePathConnector.remove(new File(oldImg.getFilePath()).getName());
        this.filePathConnector.add(new File(newImg.getFilePath()).getName());
        if (oldImg.getCaseID().equals(newImg.getCaseID())) {
            this.caseIDConnector.get(newImg.getCaseID()).set(caseIDListIndex, newImg);
        } else {
            this.caseIDConnector.get(oldImg.getCaseID()).remove(oldImg);
            this.updateCaseIDConnector(newImg);
        }
        this.thread.edit(oldImg.getImageID());
    }

    public void resetTableView() {
        this.tableList.setAll(this.databaseList);
    }

    private T initImageID(T img) {
        img.setImageID(config.getNewImageID());
        return img;
    }

    public T initCaseID(T img) {
        int id = -1;
        boolean available = false;
        for (T o : this.databaseList) {
            if (o.getCaseNumber().toLowerCase().equals(img.getCaseNumber().toLowerCase())) {
                id = Integer.parseInt(o.getCaseID());
                available = true;
                break;
            }
            id = Math.max(id, Integer.parseInt(o.getCaseID()));
        }
        if (!available) {
            id += 1;
        }
        img.setCaseID(Integer.toString(id));
        return img;
    }

    public void close() {
        this.thread.kill();
    }
    
    public HashMap getCaseMap(){
        return this.caseIDConnector;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        String stored = this.config.get(type + "Header") + "\n";
        for (T img : this.databaseList) {
            stored = stored + img.toString(this.getEntryHeader()) + "\n";
        }
        return stored;
    }
}
