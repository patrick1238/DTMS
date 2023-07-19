/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutofillTable;

import BackgroundHandler.Config;
import BackgroundHandler.DataCache;
import Database.Database;
import Interfaces.ImageObject;
import java.io.File;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author patri
 * @param <T>
 */
public class AutoFillTable<T>{
    
    private final Database database;
    private final TableView tableView;
    private final String type;
    DataCache cache;
        
    /**
     *
     * @param tableView
     * @param type
     */
    public AutoFillTable(TableView tableView, String type) {
        this.tableView = tableView;
        this.tableView.getColumns().clear();
        this.database = DataCache.getDataCache().getDatabase(type);
        this.type = type;
        initializeTable();
    }
    
    /**
     *Initializes the AutofillTable by creating existing columns and setting click listener
     */
    private void initializeTable(){
        this.tableView.setItems(this.database.getTableList());
        for(String head:this.database.getEntryHeader()){
            TableColumn column = new TableColumn(head);
            column.setCellValueFactory(new PropertyValueFactory(head));
            this.tableView.getColumns().add(column);
        }
        this.tableView.setOnMouseClicked((MouseEvent event)->{
            if(event.getButton().equals(MouseButton.PRIMARY)&&event.getClickCount()==2){
                ImageObject o = (ImageObject)this.tableView.getSelectionModel().getSelectedItem();
                Config config = Config.getConfig();
                o.open(new File(config.get("ImageServer")+o.getFilePath()));
            }
        });
        this.tableView.getSelectionModel().selectFirst();
    }
    
    /**
     *Returns selected items as Observable list
     */
    public ObservableList getSelectedItems(){
        return this.tableView.getSelectionModel().getSelectedItems();
    }
    
    /**
     *Return the basis tableview object of the Autofilltable.
     */
    public TableView getTableView(){
        return this.tableView;
    }
}
