/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.controls;

import net.rehkind_mag.entities.ClientCase;
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
import net.rehkind_mag.entities.ClientClinic;
import net.rehkind_mag.entities.pool.ClinicPool;
import org.jboss.logging.Logger;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class CasePaneController extends ClientObjectController implements Initializable {
    ClientCase myCase;
    ChangeListener statusChangedListener;
    
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
    
    ChangeListener clinicNameChangedListener;
    ChangeListener clinicChangedListener;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void setCase( ClientCase theCase ){
        if ( myCase!=null ){
            try{ 
                Logger.getLogger(getClass()).info("setCase() will replace earlier setCase - calling dispose to unbind GUI properties and eventhandlers");
                dispose();
            }catch( Exception ex ){
                Logger.getLogger(getClass()).warn("Error during dispose() call.");
            }
        }
        
        myCase=theCase;
        if( theCase==null ){
            try{
                dispose();
            }catch(Exception ex){
                Logger.getLogger(getClass()).warn("setCase(null) was called...error while trying to dispose()");
            }
        }else{
            try{
                bindGUIElements();
            }catch( NullPointerException ex ){
                Logger.getLogger(getClass()).fatal("setCase(ClientCase) was called...error while trying to bindGUIElements()");
            }
        }
    }
    
    @Override
    protected void bindGUIElements(){
        viewCaseId.textProperty().bindBidirectional(myCase.getIdProperty(), new NumberStringConverter());
        viewCaseNumber.textProperty().bindBidirectional(myCase.getCaseNumberProperty());
        viewDiagnosis.textProperty().bindBidirectional(myCase.getDiagnosisProperty());
        viewSubmitter.textProperty().bindBidirectional(myCase.getSubmitterIDProperty(), new NumberStringConverter());
        
        viewEntryDate.textProperty().bindBidirectional(myCase.getEntryDateProperty());
        
        editCaseId.textProperty().bindBidirectional(myCase.getIdProperty(), new NumberStringConverter());
        editCaseNumber.textProperty().bindBidirectional(myCase.getCaseNumberProperty());
        editDiagnosis.textProperty().bindBidirectional(myCase.getDiagnosisProperty());
        editSubmitter.textProperty().bindBidirectional(myCase.getSubmitterIDProperty(), new NumberStringConverter());
        
        editEntryDate.textProperty().bindBidirectional(myCase.getEntryDateProperty());
        
        statusChangedListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                onStatusChanged();
            }
        };
        editCaseId.textProperty().addListener(statusChangedListener);
        editCaseNumber.textProperty().addListener(statusChangedListener);
        editDiagnosis.textProperty().addListener(statusChangedListener);
        editSubmitter.textProperty().addListener(statusChangedListener);
        editClinic.textProperty().addListener(statusChangedListener);
        editEntryDate.textProperty().addListener(statusChangedListener);
        
        bindClinicName();
        bindClinic();
    }
    
    @Override
    protected void dispose(){
        // remove all listener objects
        if(myCase==null){ return; }
        viewCaseId.textProperty().unbindBidirectional(myCase.getIdProperty());
        viewCaseNumber.textProperty().unbindBidirectional(myCase.getCaseNumberProperty());
        viewDiagnosis.textProperty().unbindBidirectional(myCase.getDiagnosisProperty());
        viewSubmitter.textProperty().unbindBidirectional(myCase.getSubmitterIDProperty());
        viewClinic.textProperty().unbindBidirectional(myCase.getClinicIDProperty());
        viewEntryDate.textProperty().unbindBidirectional(myCase.getEntryDateProperty());
        
        editCaseId.textProperty().unbindBidirectional(myCase.getIdProperty());
        editCaseNumber.textProperty().unbindBidirectional(myCase.getCaseNumberProperty());
        editDiagnosis.textProperty().unbindBidirectional(myCase.getDiagnosisProperty());
        editSubmitter.textProperty().unbindBidirectional(myCase.getSubmitterIDProperty());
        editClinic.textProperty().unbindBidirectional(myCase.getClinicIDProperty());
        editEntryDate.textProperty().unbindBidirectional(myCase.getEntryDateProperty());
        
        editCaseId.textProperty().removeListener(statusChangedListener);
        editCaseNumber.textProperty().removeListener(statusChangedListener);
        editDiagnosis.textProperty().removeListener(statusChangedListener);
        editSubmitter.textProperty().removeListener(statusChangedListener);
        editClinic.textProperty().removeListener(statusChangedListener);
        editEntryDate.textProperty().removeListener(statusChangedListener);
        
        myCase.getClinic().removeListener(clinicNameChangedListener);
        myCase.removeListener(clinicChangedListener);
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
    
    private void bindClinicName(){
        this.clinicNameChangedListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                ClientClinic clinic = myCase.getClinic();
                if(viewClinic.textProperty().getValue() != clinic.getName()){ 
                    viewClinic.textProperty().setValue(clinic.getName());
                }
                if(editClinic.textProperty().getValue() != clinic.getName()){ 
                    editClinic.textProperty().setValue(clinic.getName());
                }
            }
        };
    }
    
    private void bindClinic(){
        this.clinicChangedListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                ClinicPool.createPool().getEntity((Integer)oldValue).removeListener(clinicNameChangedListener);
                myCase.getClinic().addListener(clinicNameChangedListener);
            }
        };
        clinicNameChangedListener.changed(null, null, null);
    }
}
