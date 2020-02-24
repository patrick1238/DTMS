/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BackgroundHandler;

import Windowcontroller.RegisterScreenController;
import javafx.scene.shape.Circle;

/**
 *
 * @author patri
 */
public class Preloader {
    
    public static void preloadBackgroundInformation(Circle statusCircle, RegisterScreenController controller) {
        ErrorLog.initializeErrorLog(statusCircle);
        Config.getConfig();
        DataCache.getDataCache();
        BackUpHandler.getBackupHandler();
        ViewController.initializeViewController(controller);
    }
}
