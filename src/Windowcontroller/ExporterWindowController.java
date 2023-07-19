/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windowcontroller;

import AutofillTable.AutoFillTable;
import BackgroundHandler.Config;
import BackgroundHandler.DataCache;
import BackgroundHandler.ErrorLog;
import BackgroundHandler.ViewController;
import Database.ExporterDatabase;
import Interfaces.ImageObject;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class ExporterWindowController implements Initializable {

    @FXML
    private TextField titleField;
    @FXML
    private Button folderChooser;
    @FXML
    private TableView<?> fileViewer;
    @FXML
    private Button createButton;
    @FXML
    private ComboBox<?> Exporter_identifier;

    ExporterDatabase<ImageObject> database;
    String outputPath;
    TableView generalView;
    Config config;
    DataCache cache;
    ErrorLog log;

    ImageObject dragged;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void init(AutoFillTable table){
        this.cache = DataCache.getDataCache();
        this.log = ErrorLog.getErrorLog();
        this.config = Config.getConfig();
        this.database = new ExporterDatabase<>();
        this.generalView = table.getTableView();
        this.initializeTable();
        this.initDragAndDrop();
        this.initContextMenu();
        this.initializeExportoptions();
    }
    
    private void initializeExportoptions(){
        ObservableList options = FXCollections.observableArrayList(this.config.get("Exporter_options").split(","));
        this.Exporter_identifier.setItems(options);
        this.Exporter_identifier.getSelectionModel().selectFirst();
    }

    /**
     *
     * @param enabled
     */
    private void initializeTable() {
        this.fileViewer.setItems(this.database.getExporterList());
        for (String head : this.database.getEntryHeader()) {
            TableColumn column = new TableColumn(head);
            column.setCellValueFactory(new PropertyValueFactory(head));
            this.fileViewer.getColumns().add(column);
        }
    }

    private void initDragAndDrop() {
        this.generalView.setOnDragDetected((final MouseEvent event) -> {
            ObservableList objects = this.generalView.getSelectionModel().getSelectedItems();
            this.cache.setDragAndDropItems(objects);
            Dragboard db = generalView.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(objects.toString());
            db.setContent(content);
            event.consume();

        });
        this.fileViewer.setOnDragOver((final DragEvent event1) -> {
            event1.acceptTransferModes(TransferMode.COPY);
            event1.consume();
        });
        this.fileViewer.setOnDragDropped((final DragEvent event2) -> {
            for(Object o:this.cache.getDragAndDropItems()){
                ImageObject image = (ImageObject)o;
                this.database.addImage(image);
            }
        });
    }

    private void initContextMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem file = new MenuItem("delete");
        file.setOnAction((ActionEvent event) -> {
            if (this.fileViewer.getSelectionModel().getSelectedItems().size() == 1) {
                this.database.removeImage((ImageObject) this.fileViewer.getSelectionModel().getSelectedItem());
            }
        });
        this.fileViewer.setContextMenu(menu);
        menu.getItems().add(file);
    }

    @FXML
    private void folderChooserButtonPressed(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(folderChooser.getScene().getWindow());
        if (selectedDirectory == null) {
            this.log.createLogEntry(1, "No outputpath selected");
        } else {
            folderChooser.setText(selectedDirectory.getName());
            this.outputPath = selectedDirectory.getAbsolutePath();
        }
    }

    @FXML
    private void createButtonPressed(ActionEvent event) {
        if (this.outputPath != null && !this.titleField.getText().equals("") && this.database.getExporterList().size() > 0) {
            String identifier = (String) this.Exporter_identifier.getSelectionModel().getSelectedItem();
            if(identifier.equals("DTMS folder")){
                this.database.export_DTMS_folder(outputPath, this.titleField.getText());
                ViewController.getViewController().closeView("ExportNodeGeneral");
            }else if(identifier.equals("Diagnosis related")){
                this.database.export_single_images(outputPath, this.titleField.getText(),"Diagnose");
                ViewController.getViewController().closeView("ExportNodeGeneral");
            }else if(identifier.equals("Stain related")){
                this.database.export_single_images(outputPath, this.titleField.getText(),"PrimaryStaining");
                ViewController.getViewController().closeView("ExportNodeGeneral");
            }else if(identifier.equals("unrelated")){
                this.database.export_single_images(outputPath, this.titleField.getText(),"unrelated");
                ViewController.getViewController().closeView("ExportNodeGeneral");
            }else if(identifier.equals("anonymized")){
                this.database.export_single_images(outputPath, this.titleField.getText(),"anonymized");
                ViewController.getViewController().closeView("ExportNodeGeneral");
            }
        }else{
            if(this.outputPath == null){
                this.log.createLogEntry(1, "No exportdestination selected");
            }
            if(this.titleField.getText().equals("")){
                this.log.createLogEntry(1, "No projecttitle given");
            }
            if(this.database.getExporterList().isEmpty()){
                this.log.createLogEntry(1, "No image selected");
            }
        }
    }

}
