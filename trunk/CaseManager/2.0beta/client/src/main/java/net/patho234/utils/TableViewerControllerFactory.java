/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.utils;

import com.sun.istack.internal.logging.Logger;
import javafx.scene.control.TableView;
import jdk.nashorn.internal.objects.NativeDebug;
import net.patho234.elements.CaseTableController;
import net.patho234.entities.ClientCase;
import net.patho234.interfaces.client.ClientObjectList;

/**
 *
 * @author rehkind
 */
public class TableViewerControllerFactory {
    public static void generateController(String tableName, TableView table){
        switch( tableName ){
            case "Case":
                Logger.getLogger(TableViewerControllerFactory.class.getClass()).warning( "Setting TableView controller to: CaseTableViewController" );
                createCaseTableViewController(table);
                break;
            default:
                Logger.getLogger(TableViewerControllerFactory.class.getClass()).warning( "TableView "+tableName+" unknown, could not generate controller for table." );
        }
    
    }
    
    static private void createCaseTableViewController( TableView table ){
        CaseTableController newController = new CaseTableController(table, new ClientObjectList<ClientCase>());
        newController.initialize(null, null);
    }
}
