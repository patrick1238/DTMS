/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutofillTable;

import BackgroundHandler.ErrorLog;
import BackgroundHandler.Config;
import BackgroundHandler.DataCache;
import BackgroundHandler.ViewController;
import Database.Database;
import Interfaces.ImageObject;
import Observer.ObservePropertyClass;
import Windowcontroller.TableWindowController;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author patri
 * @param <T>
 */
public class AutofillContextMenu<T extends ImageObject> {

    ContextMenu menu = new ContextMenu();
    ArrayList<MenuItem> menuItems = new ArrayList();

    TableWindowController controller;
    DataCache cache;
    Database database;
    String type;
    ErrorLog log;
    Config config;
    ViewController viewcontroller;

    /**
     *
     * @param controller
     * @param type
     */
    public AutofillContextMenu(TableWindowController controller, String type) {
        this.controller = controller;
        this.cache = DataCache.getDataCache();
        this.database = this.cache.getDatabase(type);
        this.config = Config.getConfig();
        this.log = ErrorLog.getErrorLog();
        this.type = type;
        this.viewcontroller = ViewController.getViewController();
        initializeContextMenu();
    }

    /**
     *
     * @return
     */
    public ContextMenu getContextMenu() {
        return this.menu;
    }

    private void initializeContextMenu() {
        this.menu.getItems().add(OpenItem());
        this.menu.getItems().add(EditItem());
        if(this.type.equals("General")){
            this.menu.getItems().add(DeletetItem());
        }
        this.menu.getItems().add(FileItem());
    }
    
    private MenuItem OpenItem() {
        MenuItem item = new MenuItem("Open");
        item.setOnAction((ActionEvent event) -> {
            for (Object o : this.controller.getAutoFillTable().getSelectedItems()) {
                if (o != null) {
                    ImageObject img = (ImageObject) o;
                    img.open(new File(this.cache.getImageServerPath() + img.getFilePath()));
                }
            }
        });
        return item;
    }
    
    private MenuItem EditItem() {
        MenuItem item = new MenuItem("Edit");
        item.setOnAction((ActionEvent event) -> {
            this.viewcontroller.setActiveTableWindowController(controller);
            this.viewcontroller.createView("EditImageStage"+this.type);
        });
        return item;
    }

    private MenuItem DeletetItem() {
        MenuItem item = new MenuItem("Delete");
        item.setOnAction((ActionEvent event) -> {
            JOptionPane.setDefaultLocale(Locale.ENGLISH);
            int result = JOptionPane.showConfirmDialog(null, "Would You Like to delete the " + this.controller.getAutoFillTable().getSelectedItems().size() + "  selected items?", "Remove?", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                ObservableList list = this.controller.getAutoFillTable().getSelectedItems();
                ArrayList<T> toDelete = new ArrayList<>();
                for(Object o: list){
                    toDelete.add((T)o);
                }
                for (T t : toDelete) {
                    this.cache.getDatabase(type).deleteFile(t);
                    if (type.equals("General")) {
                        ObservePropertyClass.deleteProperty(t);
                    }
                }
            }
        });
        return item;
    }
    private MenuItem FileItem() {
        MenuItem item = new MenuItem("Open file location");
        item.setOnAction((ActionEvent event) -> {
            for (Object o : this.controller.getAutoFillTable().getSelectedItems()) {
                if (o != null) {
                    T t = (T) o;
                    File cur = new File(t.getFilePath());
                    try {
                        Runtime.getRuntime().exec("explorer.exe /select," + this.cache.getImageServerPath() + t.getFilePath());
                    } catch (IOException ex) {
                        Logger.getLogger(ContextMenu.class.getName()).log(Level.SEVERE, null, ex);
                        this.log.createLogEntry(2, "Displaying " + t.getCaseNumber() + " failed");
                    }

                }
            }
        });
        return item;
    }
}
