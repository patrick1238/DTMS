/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls.elements;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class FilterController implements Initializable {

    @FXML
    private StackPane filterStack;
    @FXML
    private AnchorPane casePane;
    @FXML
    private AnchorPane twoDimPane;
    @FXML
    private AnchorPane threeDimPane;
    @FXML
    private AnchorPane fourDimPane;
    @FXML
    private AnchorPane genomicsPane;
    @FXML
    private AnchorPane methPane;
    @FXML
    private Button caseCounter;
    @FXML
    private Button twoDimCounter;
    @FXML
    private Button threeDimCounter;
    @FXML
    private Button fourDimCounter;
    @FXML
    private Button genomicsCounter;
    @FXML
    private Button methCounter;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void casesClicked(ActionEvent event) {
    }

    @FXML
    private void twoDimClicked(ActionEvent event) {
    }

    @FXML
    private void threeDimClicked(ActionEvent event) {
    }

    @FXML
    private void fourDimClicked(ActionEvent event) {
    }

    @FXML
    private void genomicsClicked(ActionEvent event) {
    }

    @FXML
    private void methClicked(ActionEvent event) {
    }

    @FXML
    private void caseCounterClicked(ActionEvent event) {
    }

    @FXML
    private void twoDimCounterClicked(ActionEvent event) {
    }

    @FXML
    private void threeDimCounterClicked(ActionEvent event) {
    }

    @FXML
    private void fourDimCounterClicked(ActionEvent event) {
    }

    @FXML
    private void genomicsCounterClicked(ActionEvent event) {
    }

    @FXML
    private void methCounterClicked(ActionEvent event) {
    }
    
}
