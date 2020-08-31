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
import net.patho234.entities.filter.CaseClinicFilter;
import net.patho234.entities.filter.CaseDiagnosisFilter;
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
    
    CaseNumberFilter filterCaseNumber=new CaseNumberFilter();
    CaseDiagnosisFilter filterDiagnosis=new CaseDiagnosisFilter();
    CaseClinicFilter filterClinic=new CaseClinicFilter();
    
    IDtmsSearch caseSearch=ClientObjectSearchManager.create().getSearch("global_cases");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ChangeListener<String> caseNumberSearchListener=new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterCaseNumber.setSearch(newValue);
                caseSearch.updateSearchResult();
            }
        };
        txtCaseNumber.textProperty().addListener(caseNumberSearchListener);
        
        ChangeListener<String> caseDiagnoseSearchListener=new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterDiagnosis.setSearch(newValue);
                caseSearch.updateSearchResult();
            }
        };
        cbDiagnosis.getEditor().textProperty().addListener(caseDiagnoseSearchListener);
        
        ChangeListener<String> caseClinicSearchListener=new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterClinic.setSearch(newValue);
                caseSearch.updateSearchResult();
            }
        };
        cbClinic.getEditor().textProperty().addListener(caseClinicSearchListener);
        
        caseSearch.setFilterItems("case_filter", getFilter());
    }    
    
    public List<IClientObjectFilter<ClientCase>> getFilter(){
        ArrayList<IClientObjectFilter<ClientCase>> currentFilterItems=new ArrayList<>();
        currentFilterItems.add(filterCaseNumber);
        currentFilterItems.add(filterDiagnosis);
        currentFilterItems.add(filterClinic);
        return currentFilterItems;
    }
    
}