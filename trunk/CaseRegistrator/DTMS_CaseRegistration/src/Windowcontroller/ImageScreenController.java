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
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class ImageScreenController implements Initializable, View {

    @FXML
    private Button ImageButton;
    @FXML
    private ComboBox<?> PrimaryStaining;
    @FXML
    private ComboBox<?> SecondaryStaining;
    @FXML
    private ComboBox<?> TertiaryStaining;
    @FXML
    private Button FinishButton;
    @FXML
    private ComboBox<?> ImageType;
    @FXML
    private DatePicker CaptureDate;
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
        this.PrimaryStaining.setItems(FXCollections.observableArrayList(new ArrayList( Arrays.asList(config.get("primary_staining").split(",")))));
        this.PrimaryStaining.getSelectionModel().select(0);
        this.SecondaryStaining.setItems(FXCollections.observableArrayList(new ArrayList( Arrays.asList(config.get("secondary_staining").split(",")))));
        this.SecondaryStaining.getSelectionModel().select(0);
        this.TertiaryStaining.setItems(FXCollections.observableArrayList(new ArrayList( Arrays.asList(config.get("tertiary_staining").split(",")))));
        this.TertiaryStaining.getSelectionModel().select(0);
        this.ImageType.setItems(FXCollections.observableArrayList(new ArrayList( Arrays.asList(config.get("imagetypes").split(",")))));
        this.ImageType.getSelectionModel().select(0);
    }

    @FXML
    private void select_image(ActionEvent event) {
        Config config = Config.getConfig();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(config.get("folder_path")));
        fileChooser.setTitle("Open image file");
        File file = fileChooser.showOpenDialog(CancelButton.getScene().getWindow());
        if (file == null || file.getAbsolutePath().contains(" ")) {
            System.out.println("problem");
        } else {
            ImageButton.setText(file.getName());
            this.file.put("filepath", file.getName());
        }
    }

    @FXML
    private void finish_image(ActionEvent event) {
        if (!this.PrimaryStaining.getEditor().getText().isEmpty()
                && !this.SecondaryStaining.getEditor().getText().isEmpty()
                && !this.TertiaryStaining.getEditor().getText().isEmpty()
                && !this.CaptureDate.getEditor().getText().isEmpty()) {
            this.file.put("primary_staining", this.PrimaryStaining.getEditor().getText());
            this.file.put("secondary_staining", this.SecondaryStaining.getEditor().getText());
            this.file.put("tertiary_staining", this.TertiaryStaining.getEditor().getText());
            this.file.put("capturedate", this.CaptureDate.getEditor().getText());
            this.file.put("imagetype", this.ImageType.getEditor().getText());
            this.file.put("comments", this.CommentsField.getText());
            this.parentController.close_view("image", file);
        }
    }

    @FXML
    private void CancelButton_pressed(ActionEvent event) {
        this.parentController.close_view("none", null);
    }

    public void set_file_id(int id) {
        this.file.put("id", Integer.toString(id));
    }

    public void set_parentcontroller(CaseScreenController parentController) {
        this.parentController = parentController;
    }

    public void load_file(HashMap<String, String> file) {
        this.file = file;
        this.CaptureDate.getEditor().setText(this.file.get("capturedate"));
    }
}
