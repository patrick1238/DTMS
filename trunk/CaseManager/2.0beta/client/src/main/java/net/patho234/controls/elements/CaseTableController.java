/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls.elements;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import net.patho234.views.CaseWindow;
import org.jboss.logging.Logger;

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
    
    
    Callback<TableColumn<ClientCase, String>, TableCell<ClientCase, String>> textCellCallback;
    
    EventHandler<Event> rowDoubleClickedHandler;
    
    ObservableList<ClientCase> caseList;
    
    public CaseTableController(TableView view, ObservableList<ClientCase> items){
        super();
        
        this.textCellCallback = new Callback<TableColumn<ClientCase, String>, TableCell<ClientCase, String>>() {
            @Override
            public TableCell<ClientCase, String> call(TableColumn<ClientCase, String> param) {
                return new TextFieldTableCell<>();
            }
        };
        
        rowDoubleClickedHandler = new EventHandler<Event>() {
            long lastClick=Long.MIN_VALUE;
            int doubleClickTime = 200; // ms between two clicks that are interpreted as double click
            @Override
            public void handle(Event event) {
                long diff = System.currentTimeMillis()-lastClick;
                lastClick = System.currentTimeMillis();
                if( diff <= doubleClickTime ){
                    Logger.getLogger(getClass()).info("Double click on row performed. TODO: call CaseWindow");
                    System.out.println("EventSrc = "+event.getSource());
                    TableRow<ClientCase> row = (TableRow)event.getSource();
                    ClientCase selectedCase = row.getItem();
                    try {
                        CaseWindow caseWindow = new CaseWindow(selectedCase);
                        caseWindow.show();
                        caseWindow.initView();
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(CaseTableController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    Logger.getLogger(getClass()).info("no double click (diff = "+diff+" ms)");
                }
                
            }
        };
        
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
        tableView.getColumns().add(diagnosis);
        tableView.getColumns().add(entryDate);
        
        caseNumber.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<ClientCase, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientCase, String> param) {
                return param.getValue().getCaseNumberProperty();
            }
        });
        caseNumber.setCellFactory( textCellCallback );

        clinic.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<ClientCase, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientCase, String> param) {
                return new SimpleStringProperty( param.getValue().getClinic().getName() );
            }
        });
        clinic.setCellFactory( textCellCallback );
        
        diagnosis.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<ClientCase, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientCase, String> param) {
                return param.getValue().getDiagnosisProperty();
            }
        });
        diagnosis.setCellFactory( textCellCallback );

        entryDate.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<ClientCase, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientCase, String> param) {
                return param.getValue().getEntryDateProperty();
            }
        });
        entryDate.setCellFactory( textCellCallback );
        
        // TODO: row listener maybe on click event open editor for case or show whole case view?
        tableView.setRowFactory(new Callback<TableView<ClientCase>, TableRow<ClientCase>>() {
            @Override
            public TableRow<ClientCase> call(TableView<ClientCase> param) {
                TableRow<ClientCase> tableRow = new TableRow<>();
                tableRow.editableProperty().setValue(false);
                tableRow.setOnMouseClicked( rowDoubleClickedHandler );
                return tableRow;
            }
        });
        
        tableView.setItems(caseList);
    }
    
}
