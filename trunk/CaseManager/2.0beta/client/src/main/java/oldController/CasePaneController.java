/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oldController;

import net.patho234.entities.ClientService;
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
import net.patho234.entities.ClientCase;
import net.patho234.entities.ClientClinic;
import net.patho234.entities.pool.ClinicPool;
import net.patho234.gui.ClinicPicker;
import org.jboss.logging.Logger;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class CasePaneController extends ClientObjectController<ClientCase> implements Initializable {
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
    @FXML ClinicPicker editClinic;
    @FXML TextField editEntryDate;
    
    
    ChangeListener clinicNameChangedListener;
    ChangeListener clinicChangedListener;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void setCase( ClientCase theCase ){
        if ( item!=null ){
            try{ 
                Logger.getLogger(getClass()).info("setCase() will replace earlier setCase - calling dispose to unbind GUI properties and eventhandlers");
                dispose();
            }catch( Exception ex ){
                Logger.getLogger(getClass()).warn("Error during dispose() call.");
            }
        }
        
        item=theCase;
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
        
        editClinic.setReceiver(item);
    }
    
    @Override
    protected void bindGUIElements(){
        viewCaseId.textProperty().bindBidirectional(item.getIdProperty(), new NumberStringConverter());
        viewCaseNumber.textProperty().bindBidirectional(item.getCaseNumberProperty());
        viewDiagnosis.textProperty().bindBidirectional(item.getDiagnosisProperty());
        viewSubmitter.textProperty().bindBidirectional(item.getSubmitterIDProperty(), new NumberStringConverter());
        
        viewEntryDate.textProperty().bindBidirectional(item.getEntryDateProperty());
        
        editCaseId.textProperty().bindBidirectional(item.getIdProperty(), new NumberStringConverter());
        editCaseNumber.textProperty().bindBidirectional(item.getCaseNumberProperty());
        editDiagnosis.textProperty().bindBidirectional(item.getDiagnosisProperty());
        editSubmitter.textProperty().bindBidirectional(item.getSubmitterIDProperty(), new NumberStringConverter());
        
        editEntryDate.textProperty().bindBidirectional(item.getEntryDateProperty());
        
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
        if(item==null){ return; }
        viewCaseId.textProperty().unbindBidirectional(item.getIdProperty());
        viewCaseNumber.textProperty().unbindBidirectional(item.getCaseNumberProperty());
        viewDiagnosis.textProperty().unbindBidirectional(item.getDiagnosisProperty());
        viewSubmitter.textProperty().unbindBidirectional(item.getSubmitterIDProperty());
        viewClinic.textProperty().unbindBidirectional(item.getClinicIDProperty());
        viewEntryDate.textProperty().unbindBidirectional(item.getEntryDateProperty());
        
        editCaseId.textProperty().unbindBidirectional(item.getIdProperty());
        editCaseNumber.textProperty().unbindBidirectional(item.getCaseNumberProperty());
        editDiagnosis.textProperty().unbindBidirectional(item.getDiagnosisProperty());
        editSubmitter.textProperty().unbindBidirectional(item.getSubmitterIDProperty());
        editClinic.textProperty().unbindBidirectional(item.getClinicIDProperty());
        editEntryDate.textProperty().unbindBidirectional(item.getEntryDateProperty());
        
        editCaseId.textProperty().removeListener(statusChangedListener);
        editCaseNumber.textProperty().removeListener(statusChangedListener);
        editDiagnosis.textProperty().removeListener(statusChangedListener);
        editSubmitter.textProperty().removeListener(statusChangedListener);
        editClinic.textProperty().removeListener(statusChangedListener);
        editEntryDate.textProperty().removeListener(statusChangedListener);
        
        item.getClinic().removeListener(clinicNameChangedListener);
        item.removeListener(clinicChangedListener);
    }
    
    protected void onStatusChanged(){
        if( item.hasLocalChanges() ){
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
                ClientClinic clinic = item.getClinic();
                if(viewClinic.textProperty().getValue() != clinic.getName()){ 
                    viewClinic.textProperty().setValue(clinic.getName());
                }
                if(editClinic.textProperty().getValue() != clinic.getName()){ 
                    editClinic.textProperty().setValue(clinic.getName());
                }
            }
        };
        item.getClinic().addListener(clinicNameChangedListener);
        clinicNameChangedListener.changed(null, null, null);
    }
    
    private void bindClinic(){
        this.clinicChangedListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                System.out.println("clinic in case "+item.getId()+" changed to "+newValue);
                if(oldValue!=null){
                    ClinicPool.createPool().getEntity((Integer)oldValue).removeListener(clinicNameChangedListener);
                }
                ClinicPool.createPool().getEntity((Integer)newValue).addListener(clinicNameChangedListener);
                clinicNameChangedListener.changed(null, null, null);
            }
        };
        item.getClinicIDProperty().addListener(clinicChangedListener);
        clinicChangedListener.changed(item, null, item.getClinicIDProperty().getValue());
    }
}
