/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class MainPaneController implements Initializable {

    @FXML
    private Label statusLabel;
    @FXML
    private Menu userMenu;
    @FXML
    private StackPane displayStack;

    Integer currentlyVisible;
    HashMap<Integer, AnchorPane> mainViewHandler;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Todo
    }


    @FXML
    private void twoDimAddProp(ActionEvent event) {
    }

    @FXML
    private void twoDimEditProp(ActionEvent event) {
    }

    @FXML
    private void twoDimDelProp(ActionEvent event) {
    }

    @FXML
    private void threeDimAddProp(ActionEvent event) {
    }

    @FXML
    private void threeDimEditProp(ActionEvent event) {
    }

    @FXML
    private void threeDimDelProp(ActionEvent event) {
    }

    @FXML
    private void fourDimAddProp(ActionEvent event) {
    }

    @FXML
    private void fourDimEditProp(ActionEvent event) {
    }

    @FXML
    private void fourDimDelProp(ActionEvent event) {
    }

    @FXML
    private void genAddProp(ActionEvent event) {
    }

    @FXML
    private void genEditProp(ActionEvent event) {
    }

    @FXML
    private void genDelProp(ActionEvent event) {
    }

    @FXML
    private void methAddProp(ActionEvent event) {
    }

    @FXML
    private void methEditProp(ActionEvent event) {
    }

    @FXML
    private void methDelProp(ActionEvent event) {
    }

    @FXML
    private void addCaseClicked(ActionEvent event) {
    }

    @FXML
    private void reloadDatabaseClicked(ActionEvent event) {
    }

    @FXML
    private void preferencesClicked(ActionEvent event) {
    }

    @FXML
    private void aboutClicked(ActionEvent event) {
    }

    @FXML
    private void closeClicked(ActionEvent event) {
    }

    @FXML
    private void profileClicked(ActionEvent event) {
        
    }

    @FXML
    private void logoutClicked(ActionEvent event) {
    }

    @FXML
    private void homeButtonClicked(ActionEvent event) {
        this.mainViewHandler.get(currentlyVisible).setVisible(false);
        this.mainViewHandler.get(0).setVisible(true);
        currentlyVisible = 0;
    }

    @FXML
    private void filterButtonClicked(ActionEvent event) {
        this.mainViewHandler.get(currentlyVisible).setVisible(false);
        this.mainViewHandler.get(1).setVisible(true);
        currentlyVisible = 1;
    }

    @FXML
    private void exportButtonClicked(ActionEvent event) {
        this.mainViewHandler.get(currentlyVisible).setVisible(false);
        this.mainViewHandler.get(2).setVisible(true);
        currentlyVisible = 2;
    }

    public StackPane getDisplayStack() {
        return this.displayStack;
    }

    public void setPossibleDisplays(HashMap<Integer, AnchorPane> mainViewHandler) {
        this.mainViewHandler = mainViewHandler;
        mainViewHandler.get(0).setVisible(true);
        this.currentlyVisible = 0;
        this.displayStack.getChildren().addAll(this.mainViewHandler.values());
    }

    @FXML
    private void addClinicClicked(ActionEvent event) {
    }

    @FXML
    private void importTwoDimClicked(ActionEvent event) {
    }

    @FXML
    private void importDTMSFolderClicked(ActionEvent event) {
    }
}
