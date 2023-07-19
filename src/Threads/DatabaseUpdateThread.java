/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import BackgroundHandler.ErrorLog;
import Database.Database;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author patri
 */
public class DatabaseUpdateThread extends Thread {

    boolean updated;
    boolean running;
    boolean exporting;
    Path databasePath;
    Database database;
    HashMap<String,String> changes;
    HashSet<String> newImages;
    boolean hashmapcopy;
    ErrorLog log;

    /**
     *
     * @param database
     */
    public DatabaseUpdateThread(Database database) {
        this.updated = false;
        this.running = true;
        this.hashmapcopy = false;
        this.exporting = false;
        this.databasePath = Paths.get(database.getDatabaseFilePath());
        this.database = database;
        this.changes = new HashMap<>();
        this.newImages = new HashSet<>();
        this.log = ErrorLog.getErrorLog();
    }

    /**
     *
     */
    @Override
    public void run() {
        int counter = 0;
        while (running) {
            if (counter > 5) {
                if (updated) {
                    this.updateCSV();
                    this.updated = false;
                }
                counter = 0;
            }
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "DatabaseUpdateThread crashed while sleeping");
            }
            counter++;
        }
    }

    private void updateCSV() {
        this.exporting = true;
        this.hashmapcopy = true;
        this.updated = false;
        HashMap<String,String> changeContainer = new HashMap<>();
        HashSet<String> newImageContainer = new HashSet<>();
        Iterator it = changes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            changeContainer.put((String)pair.getKey(), (String)pair.getValue());
            it.remove();
        }
        Iterator it2 = newImages.iterator();
        while(it2.hasNext()){
            String nextEntry = (String)it2.next();
            newImageContainer.add(nextEntry);
        }
        changes.clear();
        newImages.clear();
        this.hashmapcopy = false;
        List<String> fileContent = null;
        try {
            fileContent = new ArrayList<>(Files.readAllLines(this.databasePath, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            ErrorLog.getErrorLog().createLogEntry(2, "Reading "+this.databasePath.getFileName()+" failed");
        }
        for(String newImage:newImageContainer){
            fileContent.add(newImage);
        }
        for (int i = 0; i < fileContent.size(); i++) {
            String currentID = fileContent.get(i).split(",")[0];
            if(changeContainer.containsKey(currentID)){
                switch(changeContainer.get(currentID)){
                    case "edit":
                        fileContent.set(i,this.database.get(currentID).toString(this.database.getEntryHeader()));
                        changeContainer.remove(currentID);
                        break;
                    case "delete":
                        fileContent.remove(i);
                        changeContainer.remove(currentID);
                        i-=1;
                        break;
                }
            }
        }
        try {
            Files.write(this.databasePath, fileContent, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ErrorLog.getErrorLog().createLogEntry(2, "Writing content to "+this.databasePath.getFileName()+" failed");
        }
        this.exporting = false;
    }

    /**
     *
     * @param imageID
     */
    public void edit(String imageID) {
        updated = true;
        while(hashmapcopy){
            try {
                sleep(5);
            } catch (InterruptedException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "DatabaseUpdateThread crashed while sleeping");
            }
        }if(this.changes.containsKey(imageID)){
            this.changes.replace(imageID, "edit");
        }else{
            this.changes.put(imageID,"edit");
        }
    }
    
    public void add(String newEntry) {
        updated = true;
        while(hashmapcopy){
            try {
                sleep(5);
            } catch (InterruptedException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "DatabaseUpdateThread crashed while sleeping");
            }
        }
        this.newImages.add(newEntry);
    }
    
    public void delete(String imageID) {
        updated = true;
        while(hashmapcopy){
            try {
                sleep(5);
            } catch (InterruptedException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "DatabaseUpdateThread crashed while sleeping");
            }
        }
        if(this.changes.containsKey(imageID)){
            this.changes.replace(imageID, "delete");
        }else{
            this.changes.put(imageID,"delete");
        }
    }
    
    public boolean currentlyExporting(){
        return this.exporting;
    }

    /**
     *
     */
    public void kill() {
        running = false;
        while(exporting){
            try {
                sleep(10);
            } catch (InterruptedException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "DatabaseUpdateThread crashed while sleeping");
            }
        }
        if(updated){
            this.updateCSV();
        }
    }
}
