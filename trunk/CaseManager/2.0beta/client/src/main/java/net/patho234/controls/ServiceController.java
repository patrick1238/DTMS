/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import net.patho234.elements.MetadataPane;
import net.patho234.entities.ClientService;
import net.patho234.gui.ClientPopup;
import net.patho234.interfaces.IMetadata;
import net.patho234.webapp_client.APPLICATION_DEFAULTS;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class ServiceController implements Initializable {
    
    ClientService dataObject;
    @FXML
    private AnchorPane servicePane;
    @FXML
    private Button bttReset;
    @FXML
    private Button bttSave;
    
    @FXML
    private TextField txtCaseNumber;
    @FXML
    private TextField txtCaptureDate;
    @FXML
    private TextField txtServiceDefinition;
    @FXML
    private VBox vbMetadataBox;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtServiceDefinition.setStyle("-fx-text-inner-color: #eeeeee;"+txtServiceDefinition.styleProperty().getValue());
        servicePane.setMinWidth(350);
        servicePane.setMinHeight(300);
    }

    @FXML
    private void onSaveClicked(ActionEvent event) throws IOException {
        if( dataObject.hasLocalChanges() ){
            // TODO: check new values and persist to data base
            new ClientPopup("Service object has changes", "TODO: check new values and persist...").show(this.servicePane.getScene().getWindow());
        }else{
            new ClientPopup("Service object has no changes", "Object won't be persited...no changes found.").show(this.servicePane.getScene().getWindow());
        }
    }
    
    @FXML
    private void onResetClicked(ActionEvent event) throws IOException {
        if( dataObject.hasLocalChanges() ){
            // TODO: check new values and persist to data base
            new ClientPopup("Service object has changes", "TODO: reset changes...").show(this.servicePane.getScene().getWindow());
        }else{
            new ClientPopup("Service object has no changes", "no changes found...nothing todo").show(this.servicePane.getScene().getWindow());
        }
    }

    public void loadService(ClientService serviceToLoad) {
        dataObject = serviceToLoad;
        // todo: bind service property objects and gui
        this.txtCaseNumber.setText(dataObject.getCase().getCaseNumber());
        this.txtCaptureDate.setText(APPLICATION_DEFAULTS.DEFAULT_DATE_SHORT_FORMATTER.format( dataObject.getCase().getEntryDate()) );
        
        this.txtServiceDefinition.setText( dataObject.getServiceDefinition().getName() );
        this.bttSave.requestFocus();
        setUpDisplay();
    }
    
    private void setUpDisplay(){
        // check service definition and create gui elements for metadata fields
        List<IMetadata> metadata = dataObject.getMetadata();
        vbMetadataBox.getChildren().clear();
        for(IMetadata md : metadata){
            MetadataPane newPane = new MetadataPane(md);
            newPane.setPrefSize(200, 50);
            vbMetadataBox.getChildren().add(newPane);
        }
    }

}
