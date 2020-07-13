/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls.elements;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import net.patho234.entities.ClientCase;
import net.patho234.entities.ClientService;
import net.patho234.interfaces.IMetadata;
import net.patho234.views.CaseWindow;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class Image2DTableController implements Initializable{

    private TableView<ClientService> tableView;
    private TableColumn<ClientService, String> caseNumber;
    private TableColumn<ClientService, String> clinic;
    private TableColumn<ClientService, String> diagnosis;
    private TableColumn<ClientService, String> entryDate;
    private TableColumn<ClientService, String> serviceName;
    private TableColumn<ClientService, String> serviceNotes;
    
    
    Callback<TableColumn<ClientService, String>, TableCell<ClientService, String>> textCellCallback;
    
    EventHandler<Event> rowDoubleClickedHandler;
    
    ObservableList<ClientService> serviceList;
    
    public Image2DTableController(TableView view, ObservableList<ClientService> items){
        super();
        
        this.textCellCallback = new Callback<TableColumn<ClientService, String>, TableCell<ClientService, String>>() {
            @Override
            public TableCell<ClientService, String> call(TableColumn<ClientService, String> param) {
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
                    Logger.getLogger(getClass()).info("Double click on row performed. TODO: call ServiceWindow ?");
                    System.out.println("EventSrc = "+event.getSource());
                    TableRow<ClientCase> row = (TableRow)event.getSource();
                    // TODO: create ServiceWindow and include new ServiceWindow().show()
//                    ClientService selectedService = row.getItem();
//                    try { new ServiceWindow(selectedService).show(); }catch(IOException ioEx){
//                        Logger.getLogger(getClass()).warn("Could not load ServiceWindow for selected service '"+selectedService.getID()+"'", ioEx);
//                    }
                }else{
                    Logger.getLogger(getClass()).info("no double click (diff = "+diff+" ms)");
                }
                
            }
        };
        
        if( view==null ){ System.out.println("TableView is NULL...skipping"); return; }
        
        
        tableView = view;
        serviceList = items;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        caseNumber = new TableColumn<>("Case number");
        clinic = new TableColumn<>("Clinic");
        entryDate = new TableColumn<>("Entry date");
        diagnosis = new TableColumn<>("Diagnosis");
        serviceName = new TableColumn<>("Name");
        serviceNotes = new TableColumn<>("Notes");
        
        tableView.getColumns().clear();
        tableView.getColumns().add(caseNumber);
        tableView.getColumns().add(clinic);
        tableView.getColumns().add(diagnosis);
        tableView.getColumns().add(entryDate);
        tableView.getColumns().add(serviceName);
        tableView.getColumns().add(serviceNotes);
        
        
        caseNumber.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<ClientService, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientService, String> param) {
                return ((ClientCase)param.getValue().getCase()).getCaseNumberProperty();
            }
        });
        caseNumber.setCellFactory( textCellCallback );

        clinic.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<ClientService, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientService, String> param) {
                return new SimpleStringProperty( ((ClientCase)param.getValue().getCase()).getClinic().getName() );
            }
        });
        clinic.setCellFactory( textCellCallback );
        
        diagnosis.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<ClientService, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientService, String> param) {
                return ((ClientCase)param.getValue().getCase()).getDiagnosisProperty();
            }
        });
        diagnosis.setCellFactory( textCellCallback );

        entryDate.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<ClientService, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientService, String> param) {
                return ((ClientCase)param.getValue().getCase()).getEntryDateProperty();
            }
        });
        entryDate.setCellFactory( textCellCallback );

        serviceName.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<ClientService, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientService, String> param) {
                return new SimpleStringProperty( param.getValue().getServiceDefinition().getName() );
            }
        });
        serviceName.setCellFactory( textCellCallback );
        
        serviceNotes.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<ClientService, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientService, String> param) {
                for (IMetadata md : param.getValue().getMetadata() ){
                    if( md.getName().equals("Notes") ){ return new SimpleStringProperty((String)md.getData()); }
                }
                return new SimpleStringProperty(  );
            }
        });
        serviceName.setCellFactory( textCellCallback );
        
        // TODO: row listener maybe on click event open editor for case or show whole case view?
        tableView.setRowFactory(new Callback<TableView<ClientService>, TableRow<ClientService>>() {
            @Override
            public TableRow<ClientService> call(TableView<ClientService> param) {
                TableRow<ClientService> tableRow = new TableRow<>();
                tableRow.editableProperty().setValue(false);
                tableRow.setOnMouseClicked( rowDoubleClickedHandler );
                return tableRow;
            }
        });
        
        tableView.setItems(serviceList);
    }
    
}
