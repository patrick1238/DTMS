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
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
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
import net.patho234.elements.validator.ClientCaseValidator;
import net.patho234.elements.validator.ValidationResult;
import net.patho234.entities.ClientCase;
import net.patho234.entities.ClientService;
import net.patho234.entities.filter.ClientObjectSearchManager;
import net.patho234.entities.filter.ServiceTypeFilter;
import net.patho234.entities.pool.CasePool;
import net.patho234.entities.pool.ClinicPool;
import net.patho234.entities.pool.ServicePool;
import net.patho234.gui.ClientPopup;
import net.patho234.gui.adapter.ClinicForCaseAdapter;
import net.patho234.gui.adapter.EntryDateForCaseAdapter;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;
import net.patho234.io.FilenameParser;
import net.patho234.utils.AutoCompleteBox;
import net.patho234.utils.TableViewerControllerFactory;
import net.patho234.views.ServiceWindow;
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
    @FXML
    private Button bttSave;
    @FXML
    private Menu menuAdd;
    
    ClientCase dataObject;
    ClinicForCaseAdapter clinicAdapter;
    EntryDateForCaseAdapter entryDateAdapter;
    TableView services2D;
    TableView services3D;
    TableView services4D;
    
    private boolean initialized=false;
    
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
        List<File> list = fileChooser.showOpenMultipleDialog(new Stage());
        if (list != null) {
            for (File file : list) {
                HashMap<String, String> info = FilenameParser.twoDimFrankfurtParser(file.getName());
                
                ClientService new2DService = ClientService.getServiceTemplate(-1,APPLICATION_DEFAULTS.SERVICE_DEFINITION_ID_2D);
                
                // check if case already exists by case number:
                String caseNumber = info.get(FilenameParser.CASE_ID);
                ClientCase caseForService = CasePool.createPool().getEntityByCaseNumber(caseNumber, false);
                if( caseForService.getId()==-1 ){ // case not found
                    try {
                        // TODO: create case here
                        // compare to loaded case and validate if case number match
                        // maybe remove cause not needed? -> create case forced before new 2D is added?
                        Logger.getLogger(getClass()).error("Case '"+caseNumber+"' not found in database...cannot add services for non-existing cases.");
                        new ClientPopup("Case number not found...", "A case with case number '"+caseNumber+"' wasn't found in database! Service cannot be created.").show(bttSave.getScene().getWindow());
                        return;
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(CaseController.class.getName()).log(Level.SEVERE, null, ex);
                    } finally{
                        //bttSave.getScene().getWindow().hide();
                    }
                }
                
                // TODO: create service here
                new2DService.setCase(caseForService);
                
                System.out.println("about to add new service: "+new2DService);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Create a new 2D Service");
                alert.setContentText("Create a new 2D service for the case: "+caseNumber+"?");
                ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("Cancel", ButtonBar.ButtonData.NO);

                alert.getButtonTypes().setAll(okButton, noButton);
                alert.showAndWait().ifPresent(type -> {
                        if (type == ButtonType.YES) {
                            try {
                                int requestId=ServicePool.createPool().createEntity(new2DService);
                                ServicePool.createPool().waitForRequest(requestId);
                                ReadOnlyClientObjectList<ClientService> allEntitiesForCase = ServicePool.createPool().getAllEntitiesForCase(caseForService);
                                ClientService createService=null;
                                for( ClientService cs : allEntitiesForCase ){
                                    if( createService == null ){  }
                                }
                            } catch (TimeoutException ex) {
                                java.util.logging.Logger.getLogger(CaseController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            return;
                        }
                });
                
                try{
                    ServiceWindow serviceWnd = new ServiceWindow(new2DService);
                    
                    serviceWnd.initView();
                    serviceWnd.showAndWait();
                }catch(IOException ioEx){
                    Logger.getLogger(getClass()).error("FXML file for ServiceWindow not found. Jar files seems to be broken.");
                }
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
        // case is new and needs to be created
        if( dataObject.getId()==-1 ){
            Logger.getLogger(getClass()).info("Create case was clicked...");
            createNewCase();
            return;
        }
            
        // case already exists and maybe needs to be persisted
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
        dataObject = caseToLoad;
        if( !initialized ){
            setUpDisplay();
        }else{
            updateDisplay();
        }
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
        if( dataObject.getId()==-1 ){
            Logger.getLogger(getClass()).info("Case is not persisted in data base, no services to load.");
            services2D.setItems(new ClientObjectList());
            services3D.setItems(new ClientObjectList());
            services4D.setItems(new ClientObjectList());
            return;
        }
        
        ServiceTypeFilter only2DFilter = new ServiceTypeFilter("2D");
        ServiceTypeFilter only3DFilter = new ServiceTypeFilter("3D");
        ServiceTypeFilter only4DFilter = new ServiceTypeFilter("4D");
        
        services2D.setItems( only2DFilter.filterClientObjectList( ServicePool.createPool().getAllEntitiesForCase(dataObject) ) );
        services3D.setItems( only3DFilter.filterClientObjectList( ServicePool.createPool().getAllEntitiesForCase(dataObject) ) );
        services4D.setItems( only4DFilter.filterClientObjectList( ServicePool.createPool().getAllEntitiesForCase(dataObject) ) );
    }
    
    private void updateDisplay(){
        if( dataObject.getId()==-1 ){
            bttSave.setText("Create case...");
            menuAdd.getItems().forEach((t) -> {
                t.setDisable(true);
            });
        }else{
            bttSave.setText("Save changes");
            menuAdd.getItems().forEach((t) -> {
                t.setDisable(false);
            });
        }
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
        
        updateDisplay();
        
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
        
        this.initialized = true;
    }
    
    
    private void createNewCase(){
        ValidationResult r = ClientCaseValidator.validateCreate(dataObject);
        switch( r.getValidationCode() ){
            case ValidationResult.CODE_INVALID:
                try{
                    new ClientPopup("Case entry is not valid and can not be persisted", "Invalidation reasons:\n"+r.toString(ValidationResult.CODE_MAJOR_CONCERNS)).show(this.casePane.getScene().getWindow());;
                }catch(IOException ioEx){ ioEx.printStackTrace(); }
                break;
            case ValidationResult.CODE_MINOR_CONCERNS:
            case ValidationResult.CODE_MAJOR_CONCERNS:
                try{
                    new ClientPopup("Case entry is valid but has concerns...", "Invalidation reasons:\n"+r.toString(ValidationResult.CODE_MAJOR_CONCERNS)).show(this.casePane.getScene().getWindow());
                }catch(IOException ioEx){ ioEx.printStackTrace(); }
                break;
            case ValidationResult.CODE_VALID:
            {
                try {
                    
                    try {
                        int requestId = CasePool.createPool().createEntity(dataObject);
                        CasePool.createPool().waitForRequest(requestId);
//                        System.out.println("DATA_BEFORE:"+dataObject.toString());
                        dataObject = CasePool.createPool().getEntityByCaseNumber(dataObject.getCaseNumber(), Boolean.FALSE);
//                        System.out.println("DATA_AFTER:"+dataObject.toString());
                    } catch (Exception e) {
                        new ClientPopup("Error while creating new case...", "Case could not be created...network connection might be unstable.").show(this.casePane.getScene().getWindow());
                    }finally{
                        loadCase(dataObject);
                        ClientObjectSearchManager.create().updateAll();
                    }
                    
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(CaseController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            default:
                
        }
        
        
        
    }
}
