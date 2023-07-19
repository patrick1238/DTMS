/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windowcontroller;

import AutofillTable.AutoFillTable;
import BackgroundHandler.ErrorLog;
import BackgroundHandler.DataCache;
import BackgroundHandler.ViewController;
import Basicfunctions.Initializer;
import Database.Database;
import Listener.SelectionChangedListener;
import Listener.TableListChangedListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * FXML DTMSController class
 *
 * @author patri
 */
public class TableWindowController implements Initializable {

    @FXML
    private AnchorPane mainWindow;
    @FXML
    private Label ImageCounterLabel;
    @FXML
    private Button FilterButton;
    @FXML
    private Button ExportButton;
    @FXML
    private TableView<?> ImageTable;
    @FXML
    private HBox TableBox;
    @FXML
    private Button propertiesButton;

    ErrorLog errorlog;
    Database database;
    AutoFillTable autofilltable;
    SelectionChangedListener listener = null;
    ViewController viewController;

    String type;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public Database getDatabase() {
        return this.database;
    }

    /**
     *
     * @param type
     */
    public void initView(String type) {
        this.type = type;
        this.errorlog = ErrorLog.getErrorLog();
        this.database = DataCache.getDataCache().getDatabase(type);
        this.viewController = ViewController.getViewController();
        ImageCounterLabel.setText("Database consists of " + database.getDatabaseList().size() + " " + this.type + "-Images");
        database.getTableList().addListener(new TableListChangedListener(this.ImageCounterLabel, this.type));
        this.autofilltable = Initializer.initializeAutofillTable(this.type, this);
    }
    
    public void reduceView(ArrayList<String> visibleObjects){
        ObservableList list = FXCollections.observableArrayList();
        visibleObjects.stream().filter((id) -> (this.database.contains(id))).forEachOrdered((id) -> {
            list.add(this.database.get(id));
        });
        this.database.setTableList(list);
    }
    
    public void removePropertiesButton(){
        this.mainWindow.getChildren().remove(this.propertiesButton);
    }
    
    public void removeExportButton(){
        this.mainWindow.getChildren().remove(this.ExportButton);
    }
    
    public TableView getTableView(){
        return this.ImageTable;
    }
    
    public AutoFillTable getAutoFillTable(){
        return this.autofilltable;
    }
    
    public HBox getHBox(){
        return this.TableBox;
    }
    
    public String getType(){
        return this.type;
    }

    @FXML
    private void filterClicked(ActionEvent event) {
        this.viewController.setActiveTableWindowController(this);
        if(this.viewController.containsView("FilterNode"+this.type)){
            this.viewController.closeView("FilterNode"+this.type);
        }else{
            this.viewController.createView("FilterNode"+this.type);
        }
    }
    
    @FXML
    private void propertiesButtonClicked(ActionEvent event) {
        this.viewController.setActiveTableWindowController(this);
        if(this.viewController.containsView("PropertyNode"+this.type)){
            this.viewController.closeView("PropertyNode"+this.type);
        }else{
            this.viewController.createView("PropertyNode"+this.type);
        }
    }
    
    @FXML
    private void exportButtonClicked(ActionEvent event) {
        this.viewController.setActiveTableWindowController(this);
        if(this.viewController.containsView("ExportNode"+this.type)){
            this.viewController.closeView("ExportNode"+this.type);
        }else{
            this.viewController.createView("ExportNode"+this.type);
        }  
    }
}
