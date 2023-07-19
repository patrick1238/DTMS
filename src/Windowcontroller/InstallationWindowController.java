/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windowcontroller;

import BackgroundHandler.Config;
import DTMS.DTMS;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class InstallationWindowController implements Initializable {

    @FXML
    private Button ServerButton;
    @FXML
    private Button StorageButton;
    @FXML
    private Button StartButton;
    
    private String serverPath;
    private String storagePath;
    
    private Stage primaryStage;
    private DTMS main;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        StartButton.setDisable(true);
    }

    public void setMain(Stage primaryStage, DTMS main){
        this.primaryStage = primaryStage;
        this.main = main;
    }

    @FXML
    private void ServerButtonClicked(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(ServerButton.getScene().getWindow());
        if (selectedDirectory != null) {
            ServerButton.setText(selectedDirectory.getName());
            this.serverPath = selectedDirectory.getAbsolutePath()+File.separator;
        }
        if(this.serverPath != null && this.storagePath != null){
            StartButton.setDisable(false);
        }
    }

    @FXML
    private void StorageButtonClicked(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(StorageButton.getScene().getWindow());
        if (selectedDirectory != null) {
            StorageButton.setText(selectedDirectory.getName());
            this.storagePath = selectedDirectory.getAbsolutePath();
        }
        if(this.serverPath != null && this.storagePath != null){
            StartButton.setDisable(false);
        }
    }

    @FXML
    private void StartButtonClicked(ActionEvent event) {
        Config config = Config.getConfig();
        File generalFile = new File(this.storagePath+File.separator+"DataStorage.csv");
        if(!generalFile.exists()){
            try {
                generalFile.createNewFile();
                String header = config.get("GeneralHeader")+"\n";
                try (PrintWriter writer = new PrintWriter(generalFile.getAbsolutePath(), "UTF-8")) {
                    writer.println(header);
                }
                config.replace("csvPathGeneral", generalFile.getAbsolutePath());
            }   catch (IOException ex) {
                Logger.getLogger(InstallationWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for(String type:config.get("PossibleTypes").split(",")){
            File propertyFile = new File(this.storagePath+File.separator+type+"PropertyStorage.csv");
            if(!propertyFile.exists()){
                try {
                    propertyFile.createNewFile();
                    String header = config.get(type+"Header")+"\n";
                    try (PrintWriter writer = new PrintWriter(propertyFile.getAbsolutePath(), "UTF-8")) {
                        writer.println(header);
                    }
                    config.replace("csvPath"+type, propertyFile.getAbsolutePath());
                }   catch (IOException ex) {
                    Logger.getLogger(InstallationWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        config.replace("installed", "yes");
        config.replace("ImageServer", serverPath);
    }
    
}
