/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls.elements;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class ExportController implements Initializable {

    @FXML
    private TextField projectID;
    @FXML
    private Button folderButton;
    @FXML
    private TableView<?> exportTable;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void exportButtonClicked(ActionEvent event) {
    }

    @FXML
    private void folderButtonClicked(ActionEvent event) {
    }
    
}
