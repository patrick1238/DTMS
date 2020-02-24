/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windowcontroller;

import DataController.Config;
import Interfaces.View;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class MolecularScreenController implements Initializable, View {

    @FXML
    private Button FileSelect;
    @FXML
    private DatePicker CaptureDate;
    @FXML
    private ComboBox<?> FileType;
    @FXML
    private Button FinishButton;
    @FXML
    private Button CancelButton;
    @FXML
    private TextField CommentsField;
    
    private HashMap<String, String> file;
    private CaseScreenController parentController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Config config = Config.getConfig();
        this.file = new HashMap<>();
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.CaptureDate.getEditor().setText(date.format(formatter).toString());
        this.FileType.setItems(FXCollections.observableArrayList(new ArrayList( Arrays.asList(config.get("moltypes").split(",")))));
        this.FileType.getSelectionModel().select(0);
    }    

    @FXML
    private void select_mol(ActionEvent event) {
        Config config = Config.getConfig();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(config.get("folder_path")));
        fileChooser.setTitle("Open molecular file");
        File file = fileChooser.showOpenDialog(CancelButton.getScene().getWindow());
        if (file == null || file.getAbsolutePath().contains(" ")) {
            System.out.println("problem");
        } else {
            FileSelect.setText(file.getName());
            this.file.put("filepath", file.getName());
        }
    }

    @FXML
    private void finish_mol(ActionEvent event) {
        this.file.put("capturedate", this.CaptureDate.getEditor().getText());
        this.file.put("moltype", this.FileType.getEditor().getText());
        this.file.put("comments", this.CommentsField.getText());
    }
    
    @FXML
    private void CancelButton_pressed(ActionEvent event) {
        this.parentController.close_view("none", null);
    }

    @Override
    public void set_file_id(int id) {
        this.file.put("id", Integer.toString(id));
    }

    @Override
    public void set_parentcontroller(CaseScreenController parentController) {
        this.parentController = parentController;
    }

    @Override
    public void load_file(HashMap<String, String> file) {
        this.file = file;
    }    
}
