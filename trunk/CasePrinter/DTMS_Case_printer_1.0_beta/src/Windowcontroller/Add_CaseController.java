/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windowcontroller;

import Background_handler.CaseDatabase;
import Background_handler.ErrorLog;
import Background_handler.StainDatabase;
import Background_handler.ViewController;
import EventHandler.AutoCompleteHandler;
import Interfaces.DTMSView;
import Objects.Case;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class Add_CaseController implements Initializable,DTMSView {

    @FXML
    private Button Add_Button;
    @FXML
    private TextField Case_Field;
    @FXML
    private ComboBox<String> Diagnosis_Box;
    
    private String viewid;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.viewid = "AddView";
        ObservableList<String> diagnosis = FXCollections.observableArrayList(StainDatabase.get_Stain_Database().get_all_possible_diagnosis());
        this.Diagnosis_Box.setItems(diagnosis);
        new AutoCompleteHandler(this.Diagnosis_Box);
    }    

    @FXML
    private void add_button_clicked(ActionEvent event) {
        if(Case_Field.getText().length()==0){
            ErrorLog.getErrorLog().createLogEntry(2, "CaseID is empty");
        }else if(CaseDatabase.getCaseDatabase().contains_case(Case_Field.getText())){
            ErrorLog.getErrorLog().createLogEntry(2, Case_Field.getText() + " still exists");
        }else if(Case_Field.getText().split("-").length!=2){
            ErrorLog.getErrorLog().createLogEntry(2, "CaseID field has to look like \"PrimaryKey-Year\"");
        }else if(Diagnosis_Box.getEditor().getText().length()==0){
            ErrorLog.getErrorLog().createLogEntry(2, "Diagnosis is empty");
        }else{
            Case new_case = new Case(Case_Field.getText(),Diagnosis_Box.getEditor().getText());
            new_case.add_block(1);
            new_case.setStatus("pending");
            CaseDatabase database = CaseDatabase.getCaseDatabase();
            database.add_case(new_case);
            ViewController.getViewController().closeView(this.viewid);
            ErrorLog.getErrorLog().createLogEntry(0, new_case.getCaseID() + " added");
        }
    }

    @Override
    public String getViewID() {
        return this.viewid;
    }

    @Override
    public void updateView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    
    
    @Override
    public void close() {
        Stage stage = (Stage) Diagnosis_Box.getScene().getWindow();
        stage.close();
    }
}
