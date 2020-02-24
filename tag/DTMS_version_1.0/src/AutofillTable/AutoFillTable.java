/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutofillTable;

import BackgroundHandler.DataCache;
import Database.Database;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
     *
     * @param enabled
     */
    private void initializeTable(){
        this.tableView.setItems(this.database.getTableList());
        for(String head:this.database.getEntryHeader()){
            TableColumn column = new TableColumn(head);
            column.setCellValueFactory(new PropertyValueFactory(head));
            this.tableView.getColumns().add(column);
        }  
        this.tableView.getSelectionModel().selectFirst();
    }
    
    public ObservableList getSelectedItems(){
        return this.tableView.getSelectionModel().getSelectedItems();
    }
    
    public TableView getTableView(){
        return this.tableView;
    }
}
