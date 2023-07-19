/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basicfunctions;

import AutofillTable.AutoFillTable;
import AutofillTable.AutofillContextMenu;
import BackgroundHandler.Config;
import Database.Database;
import Windowcontroller.TableWindowController;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;

/**
 *
 * @author patri
 */
public class Initializer {

    public static Database initializeDatabase(String type) {
        Config config = Config.getConfig();
        Database database = new Database(Loader.loadFiles(config.get("csvPath" + type), type), type);
        return database;
    }

    public static AutoFillTable initializeAutofillTable(String type, TableWindowController controller) {
        TableView ImageTable = controller.getTableView();
        ImageTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        AutoFillTable autofilltable = new AutoFillTable(ImageTable, type);
        AutofillContextMenu contextMenu = new AutofillContextMenu(controller, type);
        ImageTable.setContextMenu(contextMenu.getContextMenu());
        return autofilltable;
    }
    
}
