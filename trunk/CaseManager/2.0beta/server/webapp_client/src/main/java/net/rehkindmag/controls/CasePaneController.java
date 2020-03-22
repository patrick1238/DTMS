/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkindmag.controls;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.converter.NumberStringConverter;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class CasePaneController extends ClientObjectController implements Initializable {
    ClientCase myCase;
    
    // FXML view
    @FXML Label viewCaseId;
    @FXML Label viewCaseNumber;
    @FXML Label viewDiagnosis;
    @FXML Label viewSubmitter;
    @FXML Label viewClinic;
    @FXML Label viewEntryDate;
    
    // FXML edit
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void setCase( ClientCase theCase ){
        myCase=theCase;
        bindGUIElements();
    }
    
    @Override
    protected void bindGUIElements(){
        
        viewCaseNumber.textProperty().bindBidirectional(myCase.getCaseNumberProperty());
        viewDiagnosis.textProperty().bindBidirectional(myCase.getDiagnosisProperty());
        viewSubmitter.textProperty().bindBidirectional(myCase.getSubmitterIDProperty(), new NumberStringConverter());
        viewClinic.textProperty().bindBidirectional(myCase.getClinicIDProperty(), new NumberStringConverter());
        viewEntryDate.textProperty().bindBidirectional(myCase.getEntryDateProperty());
    
    }
}
