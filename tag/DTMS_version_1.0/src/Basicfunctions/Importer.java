package Basicfunctions;

import BackgroundHandler.DataCache;
import BackgroundHandler.ErrorLog;
import Interfaces.ImageObject;
import Observer.ObserveConnectedFilesClass;
import Database.Database;
import ImageObjects.ImageObject3D;
import ImageObjects.ImageObjectGeneral;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.ObservableList;

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

    public static void parseSonjaCSV(String csvPath) {
        DataCache cache = DataCache.getDataCache();
        if (new File(csvPath).exists()) {
            try (final BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
                String line = br.readLine();
                line = br.readLine();
                Database propertyDatabase = cache.getDatabase("3D");
                Database db = cache.getDatabase("General");
                while (line != null) {
                    ArrayList<String> imageProps = Basicfunctions.splitString(line);
                    int counter = 0;
                    for(String s:imageProps){
                        if(imageProps.isEmpty()){
                            imageProps.set(counter, "");
                        }
                        counter++;
                    }
                    ArrayList<String> general = new ArrayList<>(Arrays.asList("", "", imageProps.get(2), imageProps.get(3),
                            imageProps.get(4), imageProps.get(5), "","","", "3D", imageProps.get(8),imageProps.get(10)));
                    String[] stainList = imageProps.get(6).split("/");
                    counter=6;
                    for(String stain:stainList){
                        if(stain.equals("DAPI")){
                            general.set(8, stain);
                        }else if(stain.toLowerCase().contains("negco")){
                            imageProps.set(11, "negCo");
                        }else{
                            general.set(counter, stain);
                            counter++;
                        }
                    }
                    ImageObjectGeneral img = new ImageObjectGeneral(general);
                    db.addFile(img);
                    ImageObject3D property = new ImageObject3D();
                    for (String key : cache.getImageIdentifier()) {
                        property.setValue(key, img.getAsHashMap().get(key));
                    }
                    property.setTiles(imageProps.get(7));
                    File imarisFile = new File(imageProps.get(9));
                    if(imarisFile.exists()){
                        property.setImarisFile(imageProps.get(9));
                    }
                    propertyDatabase.addFile(property);
                    line = br.readLine();
                }
            } catch (FileNotFoundException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "Can't find "+new File(csvPath).getName());
            } catch (IOException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "Can't read "+new File(csvPath).getName());
            }
        }
    }

    public static boolean parseDTMSFolder(File folder) {
        DataCache cache = DataCache.getDataCache();
        File databases = find("DTMSDatabases",folder);
        if(databases != null){
            for(File database:databases.listFiles()){
                String type = database.getName().replace(".csv", "");
                String path = database.getAbsolutePath();
                ObservableList images = Loader.loadFiles(path, type);
                Database base = cache.getDatabase(type);
                for(Object o:images){
                    ImageObject img = (ImageObject) o;
                    if(!base.contains(img.getImageID())){
                        base.addFile(ObserveConnectedFilesClass.convertRelativeToAbsoluteFilePaths(folder.getAbsolutePath()+File.separator, img));
                    }else{
                        ErrorLog.getErrorLog().createLogEntry(1, img.getImageID() + "skipped, already in DB");
                    }
                }
            }
        }
        return true;
    }

    private static File find(String filename, File dir) {
        for(File cur:dir.listFiles()){
            if(cur.getName().equals(filename)){
                return cur;
            }
        }
        return null;
    }
}
