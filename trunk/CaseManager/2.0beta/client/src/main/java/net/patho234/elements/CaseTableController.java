/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import net.patho234.entities.ClientCase;

/**
 *
 * @author rehkind
 */
public class CaseTableController implements Initializable{

    private TableView<ClientCase> tableView;
    private TableColumn<ClientCase, String> caseNumber;
    private TableColumn<ClientCase, String> clinic;
    private TableColumn<ClientCase, String> diagnosis;
    private TableColumn<ClientCase, String> entryDate;
    
    
    ObservableList<ClientCase> caseList;
    
    public CaseTableController(TableView view, ObservableList<ClientCase> items){
        super();
        
        if( view==null ){ System.out.println("TableView is NULL...skipping"); return; }
        
        
        tableView = view;
        caseList = items;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        caseNumber = new TableColumn<>("Case number");
        clinic = new TableColumn<>("Clinic");
        entryDate = new TableColumn<>("Entry date");
        diagnosis = new TableColumn<>("Diagnosis");
        
        tableView.getColumns().clear();
        tableView.getColumns().add(caseNumber);
        tableView.getColumns().add(clinic);
        //tableView.getColumns().add(diagnosis);
        //tableView.getColumns().add(entryDate);
        
        caseNumber.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<ClientCase, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientCase, String> param) {
                System.out.println("returning table cell: "+param.getValue().getCaseNumberProperty().getValue());
                return param.getValue().getCaseNumberProperty();
            }
        });
        caseNumber.setCellFactory( new Callback<TableColumn<ClientCase, String>, TableCell<ClientCase, String>>() {
            @Override
            public TableCell<ClientCase, String> call(TableColumn<ClientCase, String> param) {
                return new TextFieldTableCell<ClientCase, String>();
            }
        });

        clinic.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<ClientCase, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientCase, String> param) {
                System.out.println("returning table cell: "+param.getValue().getClinic().getName());
                return new SimpleStringProperty( param.getValue().getClinic().getName() );
            }
        });
        clinic.setCellFactory( new Callback<TableColumn<ClientCase, String>, TableCell<ClientCase, String>>() {
            @Override
            public TableCell<ClientCase, String> call(TableColumn<ClientCase, String> param) {
                return new TextFieldTableCell<ClientCase, String>();
            }
        });
        
        tableView.setRowFactory(new Callback<TableView<ClientCase>, TableRow<ClientCase>>() {
            @Override
            public TableRow<ClientCase> call(TableView<ClientCase> param) {
                TableRow<ClientCase> tableRow = new TableRow<>();
                tableRow.editableProperty().setValue(false);
                System.out.println("creating new row ");
                return tableRow;
            }
        });
//        caseNumber.setCellValueFactory( (param) -> {
//            return param.getValue().getCaseNumberProperty();
//        } );
//        clinic.setCellValueFactory( (param) -> {
//            return new SimpleStringProperty(""+param.getValue().getClinic().getName());
//        } );
//        diagnosis.setCellValueFactory( (param) -> {
//            return param.getValue().getDiagnosisProperty();
//        } );
//        entryDate.setCellValueFactory( (param) -> {
//            return param.getValue().getEntryDateProperty(); 
//        });
        
        tableView.setItems(caseList);
    }
    
    
    private class ClinicNameTableCell extends TextFieldTableCell<ClientCase,String>{
    
    }
}
