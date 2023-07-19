/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windowcontroller;

import BackgroundHandler.BackUpHandler;
import BackgroundHandler.Config;
import BackgroundHandler.ErrorLog;
import BackgroundHandler.DataCache;
import BackgroundHandler.Preloader;
import BackgroundHandler.ViewController;
import Basicfunctions.Debugging;
import Basicfunctions.Importer;
import Interfaces.ImageObject;
import Views.PropertyStorageStage;
import Basicfunctions.Exporter;
import Basicfunctions.ShortcutManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class MainWindowController implements Initializable {

    @FXML
    private MenuBar MenuBar;
    @FXML
    private MenuItem exportOverviewItem;
    @FXML
    private MenuItem addImageItem;
    @FXML
    private AnchorPane DisplayAnchorPane;
    @FXML
    private MenuItem DTMSFolder;
    @FXML
    private MenuItem Display2DAll;
    @FXML
    private MenuItem Display2DSelected;
    @FXML
    private MenuItem Display3DAll;
    @FXML
    private MenuItem Display3DSelected;
    @FXML
    private MenuItem Display4DAll;
    @FXML
    private MenuItem Display4DSelected;
    @FXML
    private Circle statusCircle;
    @FXML
    private MenuItem BackUp;
    @FXML
    private MenuItem Settings;
    @FXML
    private MenuItem HelpItem;
    @FXML
    private MenuItem caseOverviewItem;
    @FXML
    private MenuItem twoDImporter;
    @FXML
    private MenuItem MissingData;

    Config config;
    ViewController viewcontroller;
    DataCache dataCache;
    ErrorLog log;
    TableWindowController mainTableController;
    Stage stage;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TableWindow.fxml"));
            Node node = (Node) loader.load();
            Preloader.preloadBackgroundInformation(statusCircle, this);
            this.config = Config.getConfig();
            this.viewcontroller = ViewController.getViewController();
            this.dataCache = DataCache.getDataCache();
            this.log = ErrorLog.getErrorLog();
            ShortcutManager.initializeShortcuts(this.MenuBar);
            this.mainTableController = loader.<TableWindowController>getController();
            this.mainTableController.initView("General");
            AnchorPane.setBottomAnchor(node, 0.d);
            AnchorPane.setTopAnchor(node, 0.d);
            AnchorPane.setLeftAnchor(node, 0.d);
            AnchorPane.setRightAnchor(node, 0.d);
            this.DisplayAnchorPane.getChildren().add(node);
            this.log.createLogEntry(0, "DTMS started");
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void settingsClicked(ActionEvent event) {
        if (this.viewcontroller.containsView("SettingsStage")) {
            this.log.createLogEntry(1, "Settings window is still open");
        } else {
            this.viewcontroller.createView("SettingsStage");
        }
    }

    @FXML
    private void helpClicked(ActionEvent event) {
        if (this.viewcontroller.containsView("HelpStage")) {
            this.log.createLogEntry(1, "Help window is still open");
        } else {
            this.viewcontroller.createView("HelpStage");
        }
    }

    @FXML
    private void exportOverviewClicked(ActionEvent event) {
        Exporter.exportOverview();
    }

    @FXML
    private void addClicked(ActionEvent event) {
        if (this.viewcontroller.containsView("AddImageStage")) {
            this.log.createLogEntry(1, "Add image window is still open");
        } else {
            this.viewcontroller.createView("AddImageStage");
        }
    }

    @FXML
    private void display2DAllClicked(ActionEvent event) {
        if (this.viewcontroller.containsView("PropertyStorageStage2D")) {
            this.log.createLogEntry(1, "Property storage window 2D is still open");
        } else {
            this.viewcontroller.createView("PropertyStorageStage2D");
        }
    }

    @FXML
    private void display2DSelectedClicked(ActionEvent event) {
        if (this.viewcontroller.containsView("PropertyStorageStage2D")) {
            this.log.createLogEntry(1, "Property storage window 2D is still open");
        } else {
            this.viewcontroller.createView("PropertyStorageStage2D");
            TableWindowController controller = this.mainTableController;
            ObservableList selected = controller.autofilltable.getSelectedItems();
            ArrayList<String> visibleObjects = new ArrayList<>();
            for (Object o : selected) {
                ImageObject i = (ImageObject) o;
                visibleObjects.add(i.getImageID());
            }
            PropertyStorageStage stage = (PropertyStorageStage) this.viewcontroller.getView("PropertyStorageStage2D");
            stage.getWindowController().reduceView(visibleObjects);
        }
    }

    @FXML
    private void display3DAllClicked(ActionEvent event) {
        if (this.viewcontroller.containsView("PropertyStorageStage3D")) {
            this.log.createLogEntry(1, "Property storage window 3D is still open");
        } else {
            this.viewcontroller.createView("PropertyStorageStage3D");
        }
    }

    @FXML
    private void display3DSelectedClicked(ActionEvent event) {
        if (this.viewcontroller.containsView("PropertyStorageStage3D")) {
            this.log.createLogEntry(1, "Property storage window 3D is still open");
        } else {
            this.viewcontroller.createView("PropertyStorageStage3D");
            TableWindowController controller = this.mainTableController;
            ObservableList selected = controller.autofilltable.getSelectedItems();
            ArrayList<String> visibleObjects = new ArrayList<>();
            for (Object o : selected) {
                ImageObject i = (ImageObject) o;
                visibleObjects.add(i.getImageID());
            }
            PropertyStorageStage stage = (PropertyStorageStage) this.viewcontroller.getView("PropertyStorageStage3D");
            stage.getWindowController().reduceView(visibleObjects);
        }
    }

    @FXML
    private void display4DAllClicked(ActionEvent event) {
        if (this.viewcontroller.containsView("PropertyStorageStage4D")) {
            this.log.createLogEntry(1, "Property storage window 4D is still open");
        } else {
            this.viewcontroller.createView("PropertyStorageStage4D");
        }
    }

    @FXML
    private void display4DSelectedClicked(ActionEvent event) {
        if (this.viewcontroller.containsView("PropertyStorageStage4D")) {
            this.log.createLogEntry(1, "Property storage window 4D is still open");
        } else {
            this.viewcontroller.createView("PropertyStorageStage4D");
            TableWindowController controller = this.mainTableController;
            ObservableList selected = controller.autofilltable.getSelectedItems();
            ArrayList<String> visibleObjects = new ArrayList<>();
            for (Object o : selected) {
                ImageObject i = (ImageObject) o;
                visibleObjects.add(i.getImageID());
            }
            PropertyStorageStage stage = (PropertyStorageStage) this.viewcontroller.getView("PropertyStorageStage3D");
            stage.getWindowController().reduceView(visibleObjects);
        }
    }

    @FXML
    private void displayStatus(MouseEvent event) {
        Point2D point = this.statusCircle.localToScreen(0,0);
        this.log.display(point.getX(),point.getY());
    }

    @FXML
    private void closeStatus(MouseEvent event) {
        this.log.close();
    }

    @FXML
    private void doBackUpClicked(ActionEvent event) {
        BackUpHandler backup = BackUpHandler.getBackupHandler();
        backup.createBackup();
    }
    
    @FXML
    private void parseDTMSFolder(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(this.getWindow());
        if (selectedDirectory == null) {
            this.log.createLogEntry(1, "No outputpath selected");
        } else {
            Importer.parseDTMSFolder(selectedDirectory);
        }
    }
    
    @FXML
    private void caseOverviewItemclicked(ActionEvent event) {
        Exporter.exportCaseOverview();
    }

    @FXML
    private void importTwoD(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(config.get("InputImagePath")));
        File selectedDirectory = directoryChooser.showDialog(this.getWindow());
        if (selectedDirectory == null) {
            this.log.createLogEntry(1, "No inputpath selected");
        } else {
            Importer.parse2DFolder(selectedDirectory);
        }
    }
    
    @FXML
    private void missingDataClicked(ActionEvent event) {
        Debugging.search_for_missing_files();
    }

    public long deleteEmptyFolder(String dir) {
        File f = new File(dir);
        long totalSize = 0;
        if (f.exists()) {
            String listFiles[] = f.list();
            for (String file : listFiles) {
                File folder = new File(dir + "/" + file);
                if (folder.isDirectory()) {
                    totalSize += deleteEmptyFolder(folder.getAbsolutePath());
                } else {
                    totalSize += folder.length();
                }
            }

            if (totalSize == 0) {
                f.delete();
            }
        }
        return totalSize;
    }

    public Window getWindow() {
        return this.MenuBar.getScene().getWindow();
    }

    public MenuBar getMenuBar() {
        return this.MenuBar;
    }
    
    public void set_stage(Stage stage){
        this.stage = stage;
    }

    public void close() {
        this.log.close();
        this.viewcontroller.closeAllViews();
        this.dataCache.close();
        //this.deleteEmptyFolder(this.config.get("ImageServer"));
        this.stage.close();
    }

}
