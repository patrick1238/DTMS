/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkindmag.controls;

import net.rehkindmag.entities.ClientCase;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
    @FXML Label editCaseId;
    @FXML TextField editCaseNumber;
    @FXML TextField editDiagnosis;
    @FXML TextField editSubmitter;
    @FXML TextField editClinic;
    @FXML TextField editEntryDate;
    
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
        viewCaseId.textProperty().bindBidirectional(myCase.getIdProperty(), new NumberStringConverter());
        viewCaseNumber.textProperty().bindBidirectional(myCase.getCaseNumberProperty());
        viewDiagnosis.textProperty().bindBidirectional(myCase.getDiagnosisProperty());
        viewSubmitter.textProperty().bindBidirectional(myCase.getSubmitterIDProperty(), new NumberStringConverter());
        viewClinic.textProperty().bindBidirectional(myCase.getClinicIDProperty(), new NumberStringConverter());
        viewEntryDate.textProperty().bindBidirectional(myCase.getEntryDateProperty());
        
        editCaseId.textProperty().bindBidirectional(myCase.getIdProperty(), new NumberStringConverter());
        editCaseNumber.textProperty().bindBidirectional(myCase.getCaseNumberProperty());
        editDiagnosis.textProperty().bindBidirectional(myCase.getDiagnosisProperty());
        editSubmitter.textProperty().bindBidirectional(myCase.getSubmitterIDProperty(), new NumberStringConverter());
        editClinic.textProperty().bindBidirectional(myCase.getClinicIDProperty(), new NumberStringConverter());
        editEntryDate.textProperty().bindBidirectional(myCase.getEntryDateProperty());
        
        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                onStatusChanged();
            }
        };
        editCaseId.textProperty().addListener(listener);
        editCaseNumber.textProperty().addListener(listener);
        editDiagnosis.textProperty().addListener(listener);
        editSubmitter.textProperty().addListener(listener);
        editClinic.textProperty().addListener(listener);
        editEntryDate.textProperty().addListener(listener);
    }
    
    protected void onStatusChanged(){
        if( myCase.hasLocalChanges() ){
            viewStatus.setImage( new Image("/changed_icon_30.png") );
            editStatus.setImage( new Image("/changed_icon_30.png") );
        }else{
            viewStatus.setImage( null );
            editStatus.setImage( null );
        }
    }
}
