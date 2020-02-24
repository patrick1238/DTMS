/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windowcontroller;

import AutofillTable.AutoFillTable;
import BackgroundHandler.Config;
import BackgroundHandler.ErrorLog;
import BackgroundHandler.DataCache;
import BackgroundHandler.Preloader;
import BackgroundHandler.ViewController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class RegisterScreenController implements Initializable {

    @FXML
    private MenuBar MenuBar;
    @FXML
    private AnchorPane DisplayAnchorPane;
    @FXML
    private Circle statusCircle;
    @FXML
    private MenuItem Settings;
    @FXML
    private MenuItem HelpItem;
    @FXML
    private MenuItem recentItem;

    Config config;
    ViewController viewcontroller;
    DataCache dataCache;
    ErrorLog log;

    TableWindowController mainTableController;

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
            this.mainTableController = loader.<TableWindowController>getController();
            this.mainTableController.initView("General");
            AnchorPane.setBottomAnchor(node, 0.d);
            AnchorPane.setTopAnchor(node, 0.d);
            AnchorPane.setLeftAnchor(node, 0.d);
            AnchorPane.setRightAnchor(node, 0.d);
            this.DisplayAnchorPane.getChildren().add(node);
            this.log.createLogEntry(0, "Case registration started");
        } catch (IOException ex) {
            Logger.getLogger(RegisterScreenController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void displayStatus(MouseEvent event) {
        Point2D point = this.statusCircle.localToScreen(0,0);
        this.log.display(point.getX(),point.getY());
    }

    @FXML
    private void closeStatus(MouseEvent event) {
        this.log.close();
    }
    
    @FXML
    private void recentClicked(ActionEvent event) {
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

}
