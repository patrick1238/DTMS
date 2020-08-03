/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls.elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import net.patho234.entities.ClientCase;
import net.patho234.entities.filter.CaseNumberFilter;
import net.patho234.entities.filter.ClientObjectSearchManager;
import net.patho234.entities.filter.DtmsSearch;
import net.patho234.interfaces.client.IClientObjectFilter;
import net.patho234.interfaces.client.IDtmsSearch;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class CaseFilterPaneController implements Initializable {
    @FXML
    TextField txtCaseNumber;
    @FXML
    ComboBox cbDiagnosis;
    @FXML
    ComboBox cbClinic;
    
    UpdateFilterThread updateThread;
    
    CaseNumberFilter filterCaseNumber=new CaseNumberFilter();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtCaseNumber.setDisable(false);
        cbDiagnosis.setDisable(false);
        cbClinic.setDisable(false);
        txtCaseNumber.setEditable(true);
        cbDiagnosis.setEditable(true);
        cbClinic.setEditable(true);
        
        updateThread = new UpdateFilterThread( this );
        
        Thread terminateUpdateThread = new TerminateUpdateFilterThread( updateThread );
        Runtime.getRuntime().addShutdownHook(terminateUpdateThread);
        
        updateThread.start();
        
        ChangeListener<String> caseNumberChangedListener=new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterCaseNumber.setSearch(newValue);
                updateThread.update();
            }
        };
        txtCaseNumber.textProperty().addListener(caseNumberChangedListener);
    }    
    
    public List<IClientObjectFilter<ClientCase>> getFilter(){
        ArrayList<IClientObjectFilter<ClientCase>> currentFilterItems=new ArrayList<>();
        currentFilterItems.add(filterCaseNumber);
        // TODO: add more filter here
        return currentFilterItems;
    }
    
    private class UpdateFilterThread extends Thread{
        boolean isRunning=true;
        boolean performUpdate=true;
        CaseFilterPaneController parent;
        
        public UpdateFilterThread(CaseFilterPaneController parent){
            this.parent=parent;
        }
        
        public void update(){
            performUpdate=true;
        }
        
        @Override
        public void run(){

            while( isRunning ){
                if(performUpdate){
                    performUpdate=false;
                    IDtmsSearch caseSearch=ClientObjectSearchManager.create().getSearch("global_cases");
                    
                    caseSearch.setFilterItems(parent.getFilter());
                }else{
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CaseFilterPaneController.class.getName()).log(Level.SEVERE, "Thread could not sleep...", ex);
                    }
                }
            }
        }
        
        
    }
    private class TerminateUpdateFilterThread extends Thread{
        UpdateFilterThread threadToTerminate;
        public TerminateUpdateFilterThread(UpdateFilterThread threadToTerminate){
            this.threadToTerminate = threadToTerminate;
        }
        
        @Override
        public void run(){
            threadToTerminate.isRunning=false;
            while (threadToTerminate.isAlive() ){
                Logger.getLogger(getClass().getName()).info("Waiting for shut down of CaseFilterUpdateThread.");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CaseFilterPaneController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}