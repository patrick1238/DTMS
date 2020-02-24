/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windowcontroller;

import DataController.Config;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class StartScreenController implements Initializable {

    @FXML
    private Button StartButton;
    @FXML
    private Button FolderButton;

    private File folder;
    @FXML
    private TextField ClinicField;
    @FXML
    private TextField SupplierField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Config config = Config.getConfig();
        this.ClinicField.setText(config.get("clinic"));
        this.SupplierField.setText(config.get("supplier"));
        this.folder = new File("");
    }

    @FXML
    private void start_register(ActionEvent event) {
        if (ClinicField.getText().length() != 0 && SupplierField.getText().length() != 0 && this.folder != null) {
            Config config = Config.getConfig();
            config.replace("clinic",ClinicField.getText());
            config.replace("supplier",SupplierField.getText());
            config.replace("folder_path",this.folder.getAbsolutePath());
            Stage stage = new Stage();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/fxml/RegisterScreen.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(CaseScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            stage.setTitle(SupplierField.getText() + ", " + ClinicField.getText());
            stage.setScene(scene);
            stage.show();
            ClinicField.getScene().getWindow().hide();
        }
    }

    @FXML
    private void select_folder(ActionEvent event) {
        Config config = Config.getConfig();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(FolderButton.getScene().getWindow());
        if (selectedDirectory == null) {
            //No Directory selected
        } else {
            this.folder = selectedDirectory;
            this.FolderButton.setText(this.folder.getName());
        }
    }

}
