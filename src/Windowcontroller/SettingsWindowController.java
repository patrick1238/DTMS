/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windowcontroller;

import BackgroundHandler.Config;
import BackgroundHandler.ViewController;
import Basicfunctions.ShortcutManager;
import Settings.PathObject;
import Settings.ShortCutObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class SettingsWindowController implements Initializable {

    Config config;
    MainWindowController controller;
    ObservableList shortcuts = FXCollections.observableArrayList();
    HashMap<String,MenuItem> items;

    @FXML
    private Label PossibleTypes;
    @FXML
    private Label ImageServerLogic;
    @FXML
    private Label AddressID;
    @FXML
    private Label BackUpDate;
    @FXML
    private TableView<?> pathTable;
    @FXML
    private GridPane SettingsPane;
    @FXML
    private ComboBox<?> MenuItems;
    @FXML
    private TableView<?> Shortcuttable;
    @FXML
    private Button SaveButton;
    @FXML
    private Label infoLabel;
    @FXML
    private ComboBox<?> CtrlBox;
    @FXML
    private ComboBox<?> KeyBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.config = Config.getConfig();
        this.controller = ViewController.getViewController().getMainWindowController();
        this.items = ShortcutManager.identifyItems(this.controller.getMenuBar());
    }

    public void initView() {
        prepareGridpane();
        displayPaths();
        initShortCutWindow();
    }

    private void displayPaths() {
        ObservableList paths = FXCollections.observableArrayList();
        PathObject imageserver = new PathObject("Path to the Imageserver", this.config.get("ImageServer"));
        paths.add(imageserver);
        PathObject general = new PathObject("Path to the general database", this.config.get("csvPathGeneral"));
        paths.add(general);
        for (String type : this.config.get("PossibleTypes").split(",")) {
            PathObject typePath = new PathObject("Path to the " + type + "database", this.config.get("csvPath" + type));
            paths.add(typePath);
        }
        this.pathTable.getColumns().clear();
        TableColumn column = new TableColumn("Identifier");
        column.setCellValueFactory(new PropertyValueFactory("Identifier"));
        this.pathTable.getColumns().add(column);
        TableColumn pathcolumn = new TableColumn("Path");
        pathcolumn.setCellValueFactory(new PropertyValueFactory("Path"));
        this.pathTable.getColumns().add(pathcolumn);
        this.pathTable.setItems(paths);
    }

    private void prepareGridpane() {
        ArrayList<Label> labelsToAdd = new ArrayList<>();
        for (Node node : this.SettingsPane.getChildren()) {
            labelsToAdd.add(new Label(this.config.get(node.getId())));
        }
        int counter = 0;
        for (Label label : labelsToAdd) {
            this.SettingsPane.add(label, 1, counter);
            counter += 1;
        }
    }

    private void initShortCutWindow() {
        initComboboxes();
        initTableView();
    }

    private void initComboboxes() {
        ObservableList itemList = FXCollections.observableArrayList();
        for(String key: this.items.keySet()){
            itemList.add(key);
        }
        ObservableList keyList = FXCollections.observableArrayList();
        for(KeyCode k:KeyCode.values()){
            keyList.add(k.toString());
        }
        this.KeyBox.setItems(keyList);
        this.KeyBox.getSelectionModel().selectFirst();
        this.MenuItems.setItems(itemList);
        this.MenuItems.getSelectionModel().selectFirst();
        ObservableList ctrlList = FXCollections.observableArrayList();
        ctrlList.add("Ctrl");
        ctrlList.add("Alt");
        ctrlList.add("Shift");
        this.CtrlBox.setItems(ctrlList);
        this.CtrlBox.getSelectionModel().selectFirst();
    }

    private void initTableView() {
        shortcuts = initAvailableShortCuts();
        this.Shortcuttable.getColumns().clear();
        TableColumn description = new TableColumn("Description");
        description.setCellValueFactory(new PropertyValueFactory("Description"));
        this.Shortcuttable.getColumns().add(description);
        TableColumn item = new TableColumn("Item");
        item.setCellValueFactory(new PropertyValueFactory("Item"));
        this.Shortcuttable.getColumns().add(item);
        TableColumn key = new TableColumn("Key");
        key.setCellValueFactory(new PropertyValueFactory("Key"));
        this.Shortcuttable.getColumns().add(key);
        this.Shortcuttable.setItems(shortcuts);
        addContextMenu();

    }

    private ObservableList initAvailableShortCuts() {
        ObservableList shortcutList = FXCollections.observableArrayList();
        for (String shortcut : this.config.get("shortcuts").split(",")) {
            if(!shortcut.equals("empty")){
                String[] cuttedFirst = shortcut.split("-");
                String description = cuttedFirst[0];
                String[] cuttedSecond = cuttedFirst[1].split("\\+");
                String ctrl = cuttedSecond[0];
                String key = cuttedSecond[1];
                shortcutList.add(new ShortCutObject(description, ctrl, key));
            }
        }
        return shortcutList;
    }

    private void addContextMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem item = new MenuItem("Delete");
        item.setOnAction((ActionEvent event) -> {
            Object o = this.Shortcuttable.getSelectionModel().getSelectedItem();
            if (o != null) {
                this.shortcuts.remove(o);
            }
            ShortCutObject shortcut = (ShortCutObject)o;
            this.items.get(shortcut.getDescription()).setAccelerator(null);
            saveShortCuts();
        });
        menu.getItems().add(item);
        this.Shortcuttable.setContextMenu(menu);
    }
    
    private void saveShortCuts(){
        String output = "";
        if(this.shortcuts.isEmpty()){
            this.config.replace("shortcuts", "empty");
        }else{
            for (Object o : this.shortcuts) {
                ShortCutObject shortcut = (ShortCutObject) o;
                this.items.get(shortcut.getDescription()).setAccelerator(KeyCombination.keyCombination(shortcut.getItem()+"+"+shortcut.getKey()));
                output = output + "," + shortcut.toString();
            }
            output = output.replaceFirst(",", "");
            this.config.replace("shortcuts", output);
        }
    }

    @FXML
    private void SaveButtonClicked(ActionEvent event) {
        String description = (String) this.MenuItems.getSelectionModel().getSelectedItem();
        String item = (String) this.CtrlBox.getSelectionModel().getSelectedItem();
        String key = (String) this.KeyBox.getSelectionModel().getSelectedItem();
        this.shortcuts.add(new ShortCutObject(description, item, key));
        saveShortCuts();
        this.infoLabel.setText("Please restart DTMS to activate the shortcuts");
    }

}
