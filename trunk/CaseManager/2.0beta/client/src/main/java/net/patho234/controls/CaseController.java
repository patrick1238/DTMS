/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
import net.patho234.entities.pool.CasePool;
import net.patho234.entities.pool.ClinicPool;
import net.patho234.entities.pool.ServicePool;
import net.patho234.gui.ClientPopup;
import net.patho234.gui.adapter.ClinicForCaseAdapter;
import net.patho234.io.FilenameParser;
import net.patho234.utils.AutoCompleteBox;
import net.patho234.utils.TableViewerControllerFactory;
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

    private TableView services2D;
    
    ClientCase dataObject;
    ClinicForCaseAdapter clinicAdapter;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
        
        loadServices();
    }
    private void loadServices(){
        // TODO: load services + set service table views
    }
    
    private void setUpDisplay(){
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
        try{
            services2D = new TableView();
            TableViewerControllerFactory.generateController("2D", services2D);
            fileViewerBox.getChildren().add(services2D);
            services2D.setItems( ServicePool.createPool().getAllEntitiesForCase(dataObject) );
            }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

}
