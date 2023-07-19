/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Observer;

import BackgroundHandler.Config;
import BackgroundHandler.DataCache;
import BackgroundHandler.ErrorLog;
import Interfaces.ImageObject;
import Database.Database;
import ImageObjects.ImageObject2D;
import ImageObjects.ImageObject3D;
import ImageObjects.ImageObject4D;
import ImageObjects.ImageObjectGeneral;
import java.util.ArrayList;

/**
 *
 * @author patri
 */
public class ObservePropertyClass {

    public static void deleteProperty(ImageObject i) {
        DataCache cache = DataCache.getDataCache();
        ImageObjectGeneral img = (ImageObjectGeneral) i;
        String fileType = img.getFileType();
        Database curdatabase = cache.getDatabase(fileType);
        if (curdatabase.contains(img.getImageID())) {
            curdatabase.deleteFile(img.getImageID());
        }else{
            ErrorLog.getErrorLog().createLogEntry(1, "Property is missing, canceling deletion");
        }
    }

    public static void updateProperty(ImageObject i) {
        DataCache cache = DataCache.getDataCache();
        ImageObjectGeneral img = (ImageObjectGeneral) i;
        String fileType = img.getFileType();
        Database curdatabase = cache.getDatabase(fileType);
        if (curdatabase.contains(img.getImageID())) {
            ImageObject property = curdatabase.get(img.getImageID());
            ImageObject updated = property.getNewEmptyInstance();
            for (String key : property.getAsHashMap().keySet()) {
                updated.setValue(key, property.getAsHashMap().get(key));
            }
            for (String key : cache.getImageIdentifier()) {
                updated.setValue(key, img.getAsHashMap().get(key));
            }
            curdatabase.replaceFile(property, updated);
        }else{
            ErrorLog.getErrorLog().createLogEntry(1, "Property is missing, canceling updating");
        }
    }

    public static void createProperty(ImageObjectGeneral img) {
        DataCache cache = DataCache.getDataCache();
        String fileType = img.getFileType();
        Database curdatabase = cache.getDatabase(fileType);
        if (!curdatabase.contains(img.getImageID())) {
            ImageObject property = null;
            switch (fileType) {
                case "2D":
                    property = new ImageObject2D();
                    break;
                case "3D":
                    property = new ImageObject3D();
                    break;
                case "4D":
                    property = new ImageObject4D();
                    break;
            }
            for (String key : cache.getImageIdentifier()) {
                property.setValue(key, img.getAsHashMap().get(key));
            }
            curdatabase.addFile(property);
        }else{
            ErrorLog.getErrorLog().createLogEntry(1, "Property is still available");
        }
    }
    
    private static ArrayList getPropertiesInCommon(String[] newProps, String[] oldProps, String[] identifier){
        
        ArrayList<String> list = new ArrayList<>();
        /*for(String newProp:newProps){
            for(String oldProp:)
        }*/
        return list;
    }
    
    public static void redirect_Propertyinformation(ImageObjectGeneral newImg,ImageObjectGeneral oldImg){
        DataCache cache = DataCache.getDataCache();
        Config config = Config.getConfig();
        String[] newPropsHeader = config.get(newImg.getType()+"Header").split(",");
        String[] oldPropsHeader = config.get(oldImg.getType()+"Header").split(",");
        ArrayList<String> propertiesToRedirect = ObservePropertyClass.getPropertiesInCommon(newPropsHeader, oldPropsHeader, config.get("ImageIdentifier").split(","));
        
    }

    public static void observeProperty(ImageObjectGeneral oldImg, ImageObjectGeneral newImg) {
        if (oldImg.getFileType().equals(newImg.getFileType())) {
            ObservePropertyClass.updateProperty(newImg);
        } else {
            ObservePropertyClass.createProperty(newImg);
            ObservePropertyClass.redirect_Propertyinformation(newImg,oldImg);
            ObservePropertyClass.deleteProperty(oldImg);
        }
    }

}
