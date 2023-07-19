/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EventHandler;

import BackgroundHandler.Config;
import BackgroundHandler.ErrorLog;
import BackgroundHandler.DataCache;
import BackgroundHandler.ViewController;
import Interfaces.ImageObject;
import Observer.ObserveObjectClass;
import Observer.ObservePropertyClass;
import ImageObjects.ImageObjectGeneral;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 *
 * @author patri
 */
public class UpdateButtonClickedHandler implements EventHandler {

    DataCache cache;
    ImageObject old;
    ImageObject newImg;
    ErrorLog log;
    Config config;
    ViewController controller;

    public UpdateButtonClickedHandler(ImageObject old, ImageObject newImg) {
        this.old = old;
        this.newImg = newImg;
        
        this.cache = DataCache.getDataCache();
        this.log = ErrorLog.getErrorLog();
        this.config = Config.getConfig();
        this.controller = ViewController.getViewController();
    }

    @Override
    public void handle(Event event) {
        if(ObserveObjectClass.observeUpdate(newImg)){
            this.cache.getDatabase(old.getType()).replaceFile(old, newImg);
            if(old.getType().equals("General")){
                ObservePropertyClass.observeProperty((ImageObjectGeneral)old, (ImageObjectGeneral)newImg);
            }
            this.log.createLogEntry(0, old.getCaseNumber() + " updated");
            this.controller.closeView("EditImageStage"+old.getType());
        }
    }

}
