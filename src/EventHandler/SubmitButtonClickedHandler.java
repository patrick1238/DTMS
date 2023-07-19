/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EventHandler;

import BackgroundHandler.ErrorLog;
import BackgroundHandler.DataCache;
import BackgroundHandler.ViewController;
import Observer.ObserveObjectClass;
import Observer.ObservePropertyClass;
import ImageObjects.ImageObjectGeneral;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 *
 * @author patri
 */
public class SubmitButtonClickedHandler  implements EventHandler{
    DataCache cache;
    ImageObjectGeneral img;
    ErrorLog log;
    ViewController controller;
    
    /**
     *
     * @param img
     */
    public SubmitButtonClickedHandler(ImageObjectGeneral img) {
        this.img = img;
        
        this.cache = DataCache.getDataCache();
        this.log = ErrorLog.getErrorLog();
        this.controller = ViewController.getViewController();
    }
    
    @Override
    public void handle(Event event) {
        if(ObserveObjectClass.observeSubmit(img)){
            this.cache.getDatabase(img.getType()).addFile(this.img);
            ObservePropertyClass.createProperty(img);
            this.controller.closeView("AddImageStage");
        }
    }    
    
}
