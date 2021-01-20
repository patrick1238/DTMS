/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.patho234.entities.ClientCase;
import net.patho234.entities.ClientService;
import net.patho234.entities.filter.ServiceTypeFilter;
import net.patho234.entities.pool.CasePool;
import net.patho234.entities.pool.ClinicPool;
import net.patho234.entities.pool.ServicePool;
import net.patho234.gui.ClientPopup;
import net.patho234.gui.adapter.ClinicForCaseAdapter;
import net.patho234.gui.adapter.EntryDateForCaseAdapter;
import net.patho234.io.FilenameParser;
import net.patho234.utils.AutoCompleteBox;
import net.patho234.utils.TableViewerControllerFactory;
import net.patho234.webapp_client.APPLICATION_DEFAULTS;
import org.jboss.logging.Logger;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class CaseController implements Initializable {

    @FXML
    private GridPane casePane;
    @FXML
    private ComboBox<?> clinicBox;
    @FXML
    private TextField caseIDField;
    @FXML
    private DatePicker entryDatePicker;
    @FXML
    private ComboBox<?> diagnoseBox;
    @FXML
    private ScrollPane fileViewerScrollPane;
    @FXML
    private VBox fileViewerBox;

    ClientCase dataObject;
    ClinicForCaseAdapter clinicAdapter;
    EntryDateForCaseAdapter entryDateAdapter;
    TableView services2D;
    TableView services3D;
    TableView services4D;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void addTwoDimClicked(ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        Window stage;
        List<File> list
                = fileChooser.showOpenMultipleDialog(new Stage());
        if (list != null) {
            for (File file : list) {
                HashMap<String, String> info = FilenameParser.twoDimFrankfurtParser(file.getName());
                
                ClientService new2DService = ClientService.getServiceTemplate(-1,APPLICATION_DEFAULTS.SERVICE_DEFINITION_ID_2D);
                
                // check if case already exists by case number:
                String caseNumber = info.get(FilenameParser.CASE_ID);
                ClientCase caseForService = CasePool.createPool().getEntityByCaseNumber(caseNumber, false);
                if( caseForService == null ){
                    // TODO: create case here
                }
                
                // TODO: create service here
                
                // TODO: create metadata here
            }
        }
    }

    @FXML
    private void addThreeDimClicked(ActionEvent event) {
    }

    @FXML
    private void addFourDimClicked(ActionEvent event) {
    }

    @FXML
    private void addGenomicsClicked(ActionEvent event) {
    }

    @FXML
    private void addMethylationClicked(ActionEvent event) {
    }

    @FXML
    private void saveButtonClicked(ActionEvent event) throws IOException {
        if( dataObject.hasLocalChanges() ){
            // TODO: check new values and persist to data base
            new ClientPopup("Case object has changes", "TODO: check new values and persist...").show(this.casePane.getScene().getWindow());
        }else{
            new ClientPopup("Case object has no changes", "Object won't be persited...no changes found.").show(this.casePane.getScene().getWindow());
            System.out.println("CACHED: "+dataObject.getOriginalJson().toString());
            System.out.println("CURRENT: "+dataObject.toString());
        }
    }

    public void loadCase(ClientCase caseToLoad) {
        setUpDisplay();
        
        dataObject = caseToLoad;
        this.caseIDField.textProperty().bindBidirectional(dataObject.getCaseNumberProperty());
        this.diagnoseBox.editorProperty().getValue().textProperty().bindBidirectional( dataObject.getDiagnosisProperty() );
        
        this.clinicAdapter = new ClinicForCaseAdapter( dataObject );
        this.clinicAdapter.bindName(this.clinicBox.editorProperty().getValue().textProperty());
        
        this.entryDatePicker.setConverter(APPLICATION_DEFAULTS.DEFAULT_DATE_CONVERTER);
        String entryDateAsString = dataObject.getEntryDateProperty().getValue();
        this.entryDatePicker.setValue(APPLICATION_DEFAULTS.DEFAULT_DATE_CONVERTER.fromString(entryDateAsString));
        
        this.entryDateAdapter = new EntryDateForCaseAdapter( dataObject );
        this.entryDateAdapter.bindDate(this.entryDatePicker.valueProperty());
        
        loadServices();
    }
    
    private void loadServices(){
        ServiceTypeFilter only2DFilter = new ServiceTypeFilter("2D");
        ServiceTypeFilter only3DFilter = new ServiceTypeFilter("3D");
        ServiceTypeFilter only4DFilter = new ServiceTypeFilter("4D");
        
        services2D.setItems( only2DFilter.filterClientObjectList( ServicePool.createPool().getAllEntitiesForCase(dataObject) ) );
        services3D.setItems( only3DFilter.filterClientObjectList( ServicePool.createPool().getAllEntitiesForCase(dataObject) ) );
        services4D.setItems( only4DFilter.filterClientObjectList( ServicePool.createPool().getAllEntitiesForCase(dataObject) ) );
    }
    
    private void setUpDisplay(){
        javafx.beans.value.ChangeListener sizeChangedChangeListener = new javafx.beans.value.ChangeListener<Double>() {
            @Override
            public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
                Double newWidth;
                if( newValue > 300 ){ 
                    newWidth = newValue-10;
                }else{
                    newWidth = 280.;
                }
                fileViewerBox.setMinWidth(newWidth);
                fileViewerBox.setPrefWidth(newWidth);
                fileViewerBox.setMaxWidth(newWidth);
                services2D.setMinWidth(newWidth);
                services2D.setPrefWidth(newWidth);
                services2D.setMaxWidth(newWidth);
                services3D.setMinWidth(newWidth);
                services3D.setPrefWidth(newWidth);
                services3D.setMaxWidth(newWidth);
                services4D.setMinWidth(newWidth);
                services4D.setPrefWidth(newWidth);
                services4D.setMaxWidth(newWidth);
            }
        };
        
        casePane.widthProperty().addListener(sizeChangedChangeListener);
        // get all diagnoses from loaded cases
        List diagnosis=null;
        try{
            diagnosis=CasePool.createPool().getDiagnosesAsList();
        }
        catch(Exception ex){
            Logger.getLogger(getClass()).warn("Error while loading diagnosis list...combo box items might not be loaded correctly.");
        }
        
        this.diagnoseBox.setItems(FXCollections.observableArrayList(diagnosis));
        new AutoCompleteBox(this.diagnoseBox);
        
        // get all clinic names from loaded clinics
        List clinics=null;
        try{
            clinics=ClinicPool.createPool().getClinicsAsList();
        }
        catch(Exception ex){
            Logger.getLogger(getClass()).warn("Error while loading diagnosis list...combo box items might not be loaded correctly.");
        }
        
        this.clinicBox.setItems(FXCollections.observableArrayList(clinics));
        new AutoCompleteBox(this.clinicBox);
        
        this.clinicBox.setItems(FXCollections.observableArrayList(clinics));
        new AutoCompleteBox(this.clinicBox);
        try{
            services2D = new TableView();
            TableViewerControllerFactory.generateController("2D", services2D);
            fileViewerBox.getChildren().add(services2D);
            services2D.setItems( ServicePool.createPool().getAllEntitiesForCase(dataObject) );
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        try{
            services3D = new TableView();
            TableViewerControllerFactory.generateController("3D", services3D);
            fileViewerBox.getChildren().add(services3D);
            services3D.setItems( ServicePool.createPool().getAllEntitiesForCase(dataObject) );
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        try{
            services4D = new TableView();
            TableViewerControllerFactory.generateController("4D", services4D);
            fileViewerBox.getChildren().add(services4D);
            services4D.setItems( ServicePool.createPool().getAllEntitiesForCase(dataObject) );
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

}
