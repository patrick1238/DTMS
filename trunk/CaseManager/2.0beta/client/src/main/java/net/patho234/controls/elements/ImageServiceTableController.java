/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls.elements;

import com.sun.scenario.Settings;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import net.patho234.entities.ClientServiceDefinition;
import net.patho234.entities.filter.ClientServicesForDefinitionFilter;
import net.patho234.interfaces.IMetadata;
import net.patho234.interfaces.IMetadataValue;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;
import net.patho234.views.ServiceWindow;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class ImageServiceTableController implements Initializable{

    private TableView<ClientService> tableView;
    private TableColumn<ClientService, String> caseNumber;
    private TableColumn<ClientService, String> clinic;
    private TableColumn<ClientService, String> diagnosis;
    private TableColumn<ClientService, String> entryDate;
    private TableColumn<ClientService, String> serviceName;
    private TableColumn<ClientService, String> serviceNotes;
    private List<TableColumn<ClientService, String>> metadataColumns=new ArrayList<>();
    
    private boolean showCaseColumns;
    private HashMap<String, TableColumn> columnMap;
    
    Callback<TableColumn<ClientService, String>, TableCell<ClientService, String>> textCellCallback;
    
    EventHandler<Event> rowDoubleClickedHandler;
    
    ObservableList<ClientService> serviceList;
    
    private ClientServiceDefinition serviceDefinition;
    
    public ImageServiceTableController(TableView view, ObservableList<ClientService> items, ClientServiceDefinition serviceDef, Boolean showCaseColumns){
        super();
        serviceDefinition=serviceDef;
        
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
                    TableRow<ClientService> row = (TableRow)event.getSource();
                    ClientService selectedService = row.getItem();
                    try { new ServiceWindow(selectedService).show(); }catch(IOException ioEx){
                        Logger.getLogger(getClass()).warn("Could not load ServiceWindow for selected service '"+selectedService.getIdProperty().getValue()+"'", ioEx);
                    }
                }else{
                    Logger.getLogger(getClass()).info("no double click (diff = "+diff+" ms)");
                }
                
            }
        };
        
        if( view==null ){ System.out.println("TableView is NULL...skipping"); return; }
        
        
        tableView = view;
        serviceList = items;
        this.showCaseColumns = showCaseColumns;
    }
    
    public ImageServiceTableController(TableView view, ObservableList<ClientService> items, ClientServiceDefinition serviceDef){
        this(view, items, serviceDef, true);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnMap = new HashMap<String, TableColumn>();
        caseNumber = new TableColumn<>("Case number");
        clinic = new TableColumn<>("Clinic");
        entryDate = new TableColumn<>("Entry date");
        diagnosis = new TableColumn<>("Diagnosis");
        serviceName = new TableColumn<>("Name");
        
        columnMap.put("Case number", caseNumber);
        columnMap.put("Clinic", clinic);
        columnMap.put("Entry date", entryDate);
        columnMap.put("Diagnosis", diagnosis);
        columnMap.put("Name", serviceName);
        
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
        
        initializeMetadataFields();
        loadColumns();
        
        System.out.println("ALL SERVICES "+serviceList.size());
        ReadOnlyClientObjectList<ClientService> filtered = new ClientServicesForDefinitionFilter(serviceDefinition).filterClientObjectList((ReadOnlyClientObjectList<ClientService>)serviceList);
        System.out.println("SERVICE DEF FILTERED "+filtered.size());
        tableView.setItems(filtered);
    }
    
    private void initializeMetadataFields(){
        List<IMetadataValue> fields = this.serviceDefinition.getFields();
        Logger.getLogger(getClass()).info("Creating columns for Metadata fields of service definition '"+serviceDefinition.getName()+"'. Will add "+fields.size()+" columns.");
        Logger.getLogger(getClass()).info("ServiceDef Json="+serviceDefinition.getOriginalJson().toString());
        for( IMetadataValue mv : fields ){
            Logger.getLogger(getClass()).info("Adding new column to 2D ListView for metadata '"+mv.getKey()+"'");
            entryDate = new TableColumn<>("Entry date");
            
            TableColumn<ClientService, String> newMetaColumn = new TableColumn<>(mv.getKey());
            newMetaColumn.setCellValueFactory( new MetadataCallback(mv.getKey()) );
            metadataColumns.add(newMetaColumn);
            columnMap.put(mv.getKey(), newMetaColumn);
        }
    }
    
    
    private class MetadataCallback implements Callback<TableColumn.CellDataFeatures<ClientService, String>, ObservableValue<String>> {
        String key;
        private MetadataCallback( String metaKey ){
            key=metaKey;
        }
        
        @Override
        public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientService, String> param) {
            for (IMetadata md : param.getValue().getMetadata() ){
                if( md.getName().equals(key) ){ 
                    return new SimpleStringProperty(  ""+md.getData() );
                }
            }
            return new SimpleStringProperty(  );
        }
    }
    
    private void loadColumns(){
        tableView.getColumns().clear();
        // first add columns specified in config:
        String strColumnOrder = Settings.get("dtms.gui.table_"+serviceDefinition.getName());
        HashSet<String> addedColumns=new HashSet<>();
        
        if( strColumnOrder!=null ){
            for(String key : strColumnOrder.split(";")){
                if( this.showCaseColumns && (key=="Case number" || key=="Clinic" || key=="Entry date" || key=="Diagnosis" || key=="Name") ){
                    continue;
                }
                TableColumn tmpCol = columnMap.get(key);
                if( tmpCol != null ){
                    tableView.getColumns().add(tmpCol);
                    addedColumns.add(key);
                }else{
                    Logger.getLogger(getClass()).warn("Settings file contains non-existing service table column: '"+key+"'");
                }
            }
        }
        
        // second: put all other columns at the end unordered:
        for( String key : columnMap.keySet() ){
            if( !addedColumns.contains(key) ){
                if( this.showCaseColumns && (key=="Case number" || key=="Clinic" || key=="Entry date" || key=="Diagnosis" || key=="Name") ){ continue; }
                tableView.getColumns().add( columnMap.get(key) );
            }
        }
        
    }
}
