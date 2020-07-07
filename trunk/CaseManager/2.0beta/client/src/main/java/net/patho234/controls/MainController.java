/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class MainController implements Initializable {

    @FXML
    private MenuItem addCaseMenuItem;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private MenuItem preferencesMenuItem;
    @FXML
    private MenuItem profileMenuItem;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private TabPane mainTab;
    @FXML
    private Label loginLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
