/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BackgroundHandler;

import Views.AddImageStage;
import Views.ExportNode;
import Views.FilterNode;
import Views.EditImageStage;
import Views.HelpStage;
import Views.PropertyNode;
import Views.PropertyStorageStage;
import Views.SettingsStage;
import ImageObjects.ImageObjectGeneral;
import Windowcontroller.MainWindowController;
import Windowcontroller.TableWindowController;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import javafx.scene.layout.VBox;
import Interfaces.DTMSView;

/**
 *
 * @author patri
 */
public class ViewController {
    
    private static ViewController viewcontroller;

    ErrorLog errorlog;
    Config config;
    DataCache cache;
    ConcurrentHashMap<String, DTMSView> views;
    
    MainWindowController maincontroller;
    TableWindowController controller;

    private ViewController(MainWindowController maincontroller) {
        this.config = Config.getConfig();
        this.cache = DataCache.getDataCache();
        this.errorlog = ErrorLog.getErrorLog();
        this.maincontroller = maincontroller;
        this.views = new ConcurrentHashMap<>();
    }

    public static void initializeViewController(MainWindowController maincontroller){
        if(viewcontroller == null){
            viewcontroller = new ViewController(maincontroller);
        }
    }
    
    public static ViewController getViewController(){
        return viewcontroller;
    }
    
    public TableWindowController getActiveTableWindowController() {
        return this.controller;
    }
    
    public void setActiveTableWindowController(TableWindowController controller) {
        this.controller = controller;
    }
    
    public MainWindowController getMainWindowController(){
        return this.maincontroller;
    }

    public boolean containsView(String identifier) {
        return this.views.containsKey(identifier);
    }

    public void createView(String identifier) {
        if (identifier.contains("Stage")) {
            this.createStage(identifier);
        } else {
            this.createNode(identifier);
        }
    }

    public DTMSView getView(String identifier) {
        return this.views.get(identifier);
    }

    public void closeView(String identifier) {
        this.views.get(identifier).close();
        this.views.remove(identifier);
        this.cleanUpHBox(identifier);
    }

    public void closeAllViews() {
        Iterator it = this.views.keySet().iterator();
        while(it.hasNext()) {
            String key = (String) it.next();
            this.views.get(key).close();
        }
    }

    private void createStage(String identifier){
        DTMSView view = null;
        if (identifier.contains("AddImageStage")) {
            view = new AddImageStage();
            this.views.put(identifier, view);
        } else if (identifier.contains("PropertyStorageStage")) {
            view = new PropertyStorageStage(identifier.replace("PropertyStorageStage", ""));
            this.views.put(identifier, view);
        } else if (identifier.contains("EditImageStage")) {
            if(this.containsView(("PropertyNode" + this.controller.getType()))){
                this.closeView("PropertyNode" + this.controller.getType());
            }
            view = new EditImageStage(this.controller.getAutoFillTable().getTableView(),this.controller.getType());
            this.views.put(identifier, view);
        } else if (identifier.contains("SettingsStage")){
            view = new SettingsStage();
            this.views.put(identifier, view);
        } else if (identifier.contains("HelpStage")){
            view = new HelpStage();
            this.views.put(identifier, view);
        }
    }

    private void createNode(String identifier){
        VBox current = null;
        if (identifier.contains("ExportNode")) {
            current = new VBox();
            ExportNode exportNode = new ExportNode(this.controller.getAutoFillTable(), current);
            this.controller.getHBox().getChildren().add(0, current);
            this.views.put(identifier, exportNode);
        } else if (identifier.contains("FilterNode")) {
            String type = this.controller.getType();
            if (this.containsView("PropertyNode" + this.controller.getType())) {
                current = (VBox) this.controller.getHBox().getChildren().get(this.controller.getHBox().getChildren().size() - 1);
            } else {
                current = new VBox();
                this.controller.getHBox().getChildren().add(current);
            }
            FilterNode filterNode = new FilterNode(type, current);
            this.views.put(identifier, filterNode);
        } else if (identifier.contains("PropertyNode")) {
            if (this.controller.getAutoFillTable().getTableView().getSelectionModel().getSelectedItems().size() == 1){
                ImageObjectGeneral img = (ImageObjectGeneral) this.controller.getAutoFillTable().getTableView().getSelectionModel().getSelectedItem();
                if (this.containsView("FilterNode" + this.controller.getType())) {
                    current = (VBox) this.controller.getHBox().getChildren().get(this.controller.getHBox().getChildren().size() - 1);
                } else {
                    current = new VBox();
                    this.controller.getHBox().getChildren().add(current);
                }
                PropertyNode propertyNode = new PropertyNode(img, current);
                this.views.put(identifier, propertyNode);
            }else if (this.controller.getAutoFillTable().getTableView().getSelectionModel().getSelectedItems().isEmpty()){
                this.errorlog.createLogEntry(1, "No image selected");
            }else{
                this.errorlog.createLogEntry(1, "To much images selected");
            }
        }
    }

    private void cleanUpHBox(String identifier) {
        if (identifier.contains("ExportNode")) {
            this.controller.getHBox().getChildren().remove(0);
        } else if (identifier.contains("FilterNode")) {
            if (!this.containsView("PropertyNode" + this.controller.getType())) {
                this.controller.getHBox().getChildren().remove(this.controller.getHBox().getChildren().size() - 1);
            }
        } else if (identifier.contains("PropertyNode")) {
            if (!this.containsView("FilterNode" + this.controller.getType())) {
                this.controller.getHBox().getChildren().remove(this.controller.getHBox().getChildren().size() - 1);
            }
        }
    }
}
