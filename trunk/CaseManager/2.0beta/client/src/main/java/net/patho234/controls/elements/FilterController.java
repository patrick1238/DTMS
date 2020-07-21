/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls.elements;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import net.patho234.elements.CaseFilterPane;

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
        CaseFilterPane caseFilterPane=null;
        
        try {
            caseFilterPane=new CaseFilterPane();
            AnchorPane.setBottomAnchor(caseFilterPane, 0.);
            AnchorPane.setLeftAnchor(caseFilterPane, 0.);
            AnchorPane.setTopAnchor(caseFilterPane, 0.);
            AnchorPane.setRightAnchor(caseFilterPane, 0.);
        } catch (IOException ex) {
            Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, "Could not load CaseFilterPane from fxml file.", ex);
        }
        
        System.out.println("casePane"+casePane);
        System.out.println("chlidren"+casePane.getChildren());
        
        casePane.getChildren().add( caseFilterPane );
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
