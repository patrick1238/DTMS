/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EventHandler;

import BackgroundHandler.Config;
import BackgroundHandler.ErrorLog;
import Interfaces.ImageObject;
import java.io.File;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author patri
 */
public class FileChooserButtonClickedHandler implements EventHandler{
    ImageObject img;
    
    /**
     *
     * @param img
     */
    public FileChooserButtonClickedHandler(ImageObject img){
        this.img = img;
    }

    @Override
    public void handle(Event event) {
        ErrorLog log = ErrorLog.getErrorLog();
        Config config = Config.getConfig();
        String imagelibpath = config.get("InputImagePath");
        Stage openWindow = new Stage();
        FileChooser fileChooser = new FileChooser();
        //fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialDirectory(new File(imagelibpath));
        fileChooser.setTitle("Open image file");
        File file = fileChooser.showOpenDialog(openWindow);
        if (file == null || file.getAbsolutePath().contains(" ")) {            
            log.createLogEntry(2, "File is empty or the filepath contains blanks, remove blanks and retry");
        }else{
            Button button = (Button)event.getSource();
            button.setText(file.getName());
            this.img.setValue(button.getId(), new SimpleStringProperty(file.getAbsolutePath()));
        }
    }    
}
