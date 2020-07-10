/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import net.patho234.entities.ClientCase;

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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addTwoDimClicked(ActionEvent event) {
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
    private void saveButtonClicked(ActionEvent event) {
    }
    
    public void loadCase( ClientCase caseToLoad ){
        dataObject = caseToLoad;
        this.caseIDField.textProperty().bindBidirectional(dataObject.getCaseNumberProperty());
    }
    
}
