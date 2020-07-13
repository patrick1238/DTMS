/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.patho234.entities.ClientCase;
import net.patho234.io.FilenameParser;

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
        System.out.println("hello");
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
    private void saveButtonClicked(ActionEvent event) {
    }

    public void loadCase(ClientCase caseToLoad) {
        dataObject = caseToLoad;
        this.caseIDField.textProperty().bindBidirectional(dataObject.getCaseNumberProperty());
    }

}
