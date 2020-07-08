/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls.elements;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import net.patho234.interfaces.IDataDisplay;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class HomeController implements Initializable {

    @FXML
    private Button caseButton;
    @FXML
    private Button twoDimButton;
    @FXML
    private Button threeDimButton;
    @FXML
    private Button fourDimButton;
    @FXML
    private Button genomicsButton;
    @FXML
    private Button methButton;
    @FXML
    private AnchorPane anchor;
    
    IDataDisplay display;
    HashMap<String,Integer> viewIDs;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void caseButtonClicked(ActionEvent event) {
    }

    @FXML
    private void twoDimButtonClicked(ActionEvent event) {
    }

    @FXML
    private void threeDimButtonClicked(ActionEvent event) {
    }

    @FXML
    private void fourDimButtonClicked(ActionEvent event) {
    }

    @FXML
    private void genomicsButtonClicked(ActionEvent event) {
    }

    @FXML
    private void methButtonClicked(ActionEvent event) {
    }

    @FXML
    private void inspectCaseGeneral(ActionEvent event) {
    }

    @FXML
    private void inspectTwoDimDiagnose(ActionEvent event) {
    }

    @FXML
    private void inspectTwoDimStaining(ActionEvent event) {
    }

    @FXML
    private void inspectThreeDimDiagnose(ActionEvent event) {
    }

    @FXML
    private void inspectThreeDimStaining(ActionEvent event) {
    }

    @FXML
    private void inspectFourDimDiagnose(ActionEvent event) {
    }

    @FXML
    private void inspectFourDimStaining(ActionEvent event) {
    }

    @FXML
    private void inspectGenomicsDiagnose(ActionEvent event) {
    }

    @FXML
    private void inspectMethDiagnose(ActionEvent event) {
    }
    
    public void setDisplay(IDataDisplay display){
        this.display = display;
        this.viewIDs = new HashMap<>();
        HashMap<Integer,String> views = this.display.getViews();
        for(Integer id: views.keySet()){
            this.viewIDs.put(views.get(id), id);
        }
    }
}
