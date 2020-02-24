/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windowcontroller;

import DataController.ClinicalCase;
import DataController.Config;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class CaseScreenController implements Initializable {

    @FXML
    private TextField CaseField;
    @FXML
    private ComboBox DiagnoseBox;
    @FXML
    private TableView<?> CaseTable;
    @FXML
    private Button FinishButton;
    @FXML
    private Button ImageButton;
    @FXML
    private Button MolButton;
    @FXML
    private HBox CaseBox;
    @FXML
    private DatePicker EntryDate;
    
    private ClinicalCase clinicalCase;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.clinicalCase = new ClinicalCase();
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.EntryDate.getEditor().setText(date.format(formatter).toString());
        Config config = Config.getConfig();
        this.DiagnoseBox.setItems(FXCollections.observableArrayList(new ArrayList( Arrays.asList(config.get("diagnose").split(",")))));
        this.DiagnoseBox.getSelectionModel().select(0);
    }
    
    public void load_case(ClinicalCase ccase){
        this.clinicalCase = ccase;
    }

    @FXML
    private void add_image(ActionEvent event) {
        try {
            if(this.CaseBox.getChildren().size()==1){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ImageScreen.fxml"));
                Node node = fxmlLoader.load();
                ImageScreenController controller = fxmlLoader.getController();
                this.CaseBox.getChildren().add(node);
                controller.set_file_id(this.clinicalCase.get_new_id());
                controller.set_parentcontroller(this);
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    private void add_mol(ActionEvent event) {
        try {
            if(this.CaseBox.getChildren().size()==1){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MolecularScreen.fxml"));
                Node node = fxmlLoader.load();
                MolecularScreenController controller = fxmlLoader.getController();
                this.CaseBox.getChildren().add(node);
                controller.set_file_id(this.clinicalCase.get_new_id());
                controller.set_parentcontroller(this);
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }

    @FXML
    private void finish_case(ActionEvent event) {
        
    }
        
    public void close_view(String type, HashMap<String,String> file){
        if(type.equals("image")){
            this.clinicalCase.add_file(file.get("id"), type, file);
            this.CaseBox.getChildren().remove(1);
        }else if(type.equals("mol")){
            this.clinicalCase.add_file(file.get("id"), type, file);
            this.CaseBox.getChildren().remove(1);
        }else{
            this.CaseBox.getChildren().remove(1);
        }
    }
    
}
