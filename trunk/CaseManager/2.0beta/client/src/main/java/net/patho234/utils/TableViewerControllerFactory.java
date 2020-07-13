/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.utils;

import javafx.scene.control.TableView;
import jdk.nashorn.internal.objects.NativeDebug;
import net.patho234.controls.elements.CaseTableController;
import net.patho234.controls.elements.Image2DTableController;
import net.patho234.entities.ClientCase;
import net.patho234.entities.ClientService;
import net.patho234.interfaces.client.ClientObjectList;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class TableViewerControllerFactory {
    public static void generateController(String tableName, TableView table){
        switch( tableName ){
            case "Case":
                Logger.getLogger(TableViewerControllerFactory.class.getClass()).warn( "Setting TableView controller to: CaseTableViewController" );
                createCaseTableViewController(table);
                break;
            case "2D":
                Logger.getLogger(TableViewerControllerFactory.class.getClass()).warn( "Setting TableView controller to: Image2DTableViewController" );
                create2DTableViewController(table);
                break;
            default:
                Logger.getLogger(TableViewerControllerFactory.class.getClass()).warn( "TableView "+tableName+" unknown, could not generate controller for table." );
        }
    
    }
    
    static private void createCaseTableViewController( TableView table ){
        CaseTableController newController = new CaseTableController(table, new ClientObjectList<>());
        newController.initialize(null, null);
    }
    
    static private void create2DTableViewController( TableView table ){
        Image2DTableController newController = new Image2DTableController(table, new ClientObjectList<>());
        newController.initialize(null, null);
    }
}
