/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class MainPaneController implements Initializable {

    @FXML
    private Label statusLabel;
    @FXML
    private Menu userMenu;
    @FXML
    private StackPane displayStack;
    @FXML
    private AnchorPane homePane;
    @FXML
    private AnchorPane filterPane;
    @FXML
    private AnchorPane exportPane;
    @FXML
    private AnchorPane imageAnalysisPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @FXML
    private void caseAddProp(ActionEvent event) {
    }

    @FXML
    private void caseEditProp(ActionEvent event) {
    }

    @FXML
    private void caseDelProp(ActionEvent event) {
    }

    @FXML
    private void twoDimAddProp(ActionEvent event) {
    }

    @FXML
    private void twoDimEditProp(ActionEvent event) {
    }

    @FXML
    private void twoDimDelProp(ActionEvent event) {
    }

    @FXML
    private void threeDimAddProp(ActionEvent event) {
    }

    @FXML
    private void threeDimEditProp(ActionEvent event) {
    }

    @FXML
    private void threeDimDelProp(ActionEvent event) {
    }

    @FXML
    private void fourDimAddProp(ActionEvent event) {
    }

    @FXML
    private void fourDimEditProp(ActionEvent event) {
    }

    @FXML
    private void fourDimDelProp(ActionEvent event) {
    }

    @FXML
    private void genAddProp(ActionEvent event) {
    }

    @FXML
    private void genEditProp(ActionEvent event) {
    }

    @FXML
    private void genDelProp(ActionEvent event) {
    }

    @FXML
    private void methAddProp(ActionEvent event) {
    }

    @FXML
    private void methEditProp(ActionEvent event) {
    }

    @FXML
    private void methDelProp(ActionEvent event) {
    }

    @FXML
    private void addCaseClicked(ActionEvent event) {
    }

    @FXML
    private void reloadDatabaseClicked(ActionEvent event) {
    }

    @FXML
    private void preferencesClicked(ActionEvent event) {
    }

    @FXML
    private void aboutClicked(ActionEvent event) {
    }

    @FXML
    private void closeClicked(ActionEvent event) {
    }

    @FXML
    private void homeWindowClicked(ActionEvent event) {
    }


    @FXML
    private void exportWindowClicked(ActionEvent event) {
    }

    @FXML
    private void profileClicked(ActionEvent event) {
    }

    @FXML
    private void logoutClicked(ActionEvent event) {
    }

    @FXML
    private void filterWindowClicked(ActionEvent event) {
    }
    
    
    public void setEnabled( boolean enable ){ 
        if( this.exportPane.getScene() != null ){
            this.exportPane.getScene().getRoot().setDisable(!enable);
        }
    }
}
