/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windowcontroller;

import AutofillTable.AutoFillTable;
import BackgroundHandler.BackUpHandler;
import BackgroundHandler.Config;
import BackgroundHandler.ErrorLog;
import BackgroundHandler.DataCache;
import BackgroundHandler.Preloader;
import BackgroundHandler.ViewController;
import Basicfunctions.Importer;
import Database.Database;
import Interfaces.ImageObject;
import Views.PropertyStorageStage;
import Basicfunctions.Exporter;
import Basicfunctions.ShortcutManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
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
import javafx.stage.FileChooser;
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
    private MenuItem ParseSonja;
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
    private MenuItem SaveDatabase;
    @FXML
    private MenuItem BackUp;
    @FXML
    private MenuItem Settings;
    @FXML
    private MenuItem HelpItem;

    Config config;
    ViewController viewcontroller;
    DataCache dataCache;
    ErrorLog log;

    TableWindowController mainTableController;
    @FXML
    private MenuItem caseOverviewItem;

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
    private void parseSonjaClicked(ActionEvent event) {
        Stage openWindow = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Open .csv file");
        File file = fileChooser.showOpenDialog(openWindow);
        String csvPath = "";
        if (file != null) {
            this.log.createLogEntry(0, "Started importing " + file.getName());
            csvPath = file.getAbsolutePath();
            Importer.parseSonjaCSV(csvPath);
            this.log.createLogEntry(0, file.getName() + " imported");
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
    private void saveClicked(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(this.getWindow());
        if (selectedDirectory == null) {
            this.log.createLogEntry(2, "No outputpath selected");
        } else {
            String outputPath = selectedDirectory.getAbsolutePath();
            TableWindowController controller = this.mainTableController;
            Database database = controller.getDatabase();
            while (database.getUpdateThread().currentlyExporting()) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    log.createLogEntry(2, "Thread crashed while sleeping");
                }
            }
            File general = new File(this.config.get("csvPathGeneral"));
            File twoD = new File(this.config.get("csvPath2D"));
            File threeD = new File(this.config.get("csvPath3D"));
            File fourD = new File(this.config.get("csvPath4D"));
            File outDirectory = new File(outputPath + File.separator + "DTMSFiles" + File.separator);
            if (!outDirectory.exists()) {
                outDirectory.mkdirs();
            }
            try {
                Files.copy(general.toPath(), new File(outputPath + File.separator + "DTMSFiles" + File.separator + general.getName()).toPath(), REPLACE_EXISTING);
            } catch (IOException ex) {
                log.createLogEntry(2, "Failed copying "+general.getName());
            }
            try {
                Files.copy(twoD.toPath(), new File(outputPath + File.separator + "DTMSFiles" + File.separator + twoD.getName()).toPath(), REPLACE_EXISTING);
            } catch (IOException ex) {
                log.createLogEntry(2, "Failed copying "+twoD.getName());
            }
            try {
                Files.copy(threeD.toPath(), new File(outputPath + File.separator + "DTMSFiles" + File.separator + threeD.getName()).toPath(), REPLACE_EXISTING);
            } catch (IOException ex) {
                log.createLogEntry(2, "Failed copying "+threeD.getName());
            }
            try {
                Files.copy(fourD.toPath(), new File(outputPath + File.separator + "DTMSFiles" + File.separator + fourD.getName()).toPath(), REPLACE_EXISTING);
            } catch (IOException ex) {
                log.createLogEntry(2, "Failed copying "+fourD.getName());
            }
            this.log.createLogEntry(0, "Database exported to " + selectedDirectory.getAbsolutePath());
        }
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

    public AutoFillTable getMainTable() {
        return this.mainTableController.getAutoFillTable();
    }

    public MenuBar getMenuBar() {
        return this.MenuBar;
    }

    public void close() {
        this.log.close();
        this.viewcontroller.closeAllViews();
        this.dataCache.close();
        this.deleteEmptyFolder(this.config.get("ImageServer"));
    }

    @FXML
    private void caseOverviewItemclicked(ActionEvent event) {
        Exporter.exportCaseOverview();
    }

}
