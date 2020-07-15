/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.utils;

import javafx.scene.control.TableView;
import net.patho234.controls.elements.CaseTableController;
import net.patho234.controls.elements.ImageServiceTableController;
import net.patho234.entities.ClientServiceDefinition;
import net.patho234.entities.pool.ServiceDefinitionPool;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.webapp_client.APPLICATION_DEFAULTS;
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
                Logger.getLogger(TableViewerControllerFactory.class.getClass()).warn( "Setting TableView controller to: ImageServiceTablewController(2d service def)" );
                createServiceTableViewController(table, ServiceDefinitionPool.createPool().getEntity(APPLICATION_DEFAULTS.SERVICE_DEFINITION_ID_2D) );
                break;
            case "3D":
                Logger.getLogger(TableViewerControllerFactory.class.getClass()).warn( "Setting TableView controller to: Image3DTableViewController(3d service def)" );
                createServiceTableViewController(table, ServiceDefinitionPool.createPool().getEntity(APPLICATION_DEFAULTS.SERVICE_DEFINITION_ID_3D) );
                break;
            case "4D":
                Logger.getLogger(TableViewerControllerFactory.class.getClass()).warn( "Setting TableView controller to: Image2DTableViewController(4d service def)" );
                createServiceTableViewController(table, ServiceDefinitionPool.createPool().getEntity(APPLICATION_DEFAULTS.SERVICE_DEFINITION_ID_4D) );
                break;
            default:
                Logger.getLogger(TableViewerControllerFactory.class.getClass()).warn( "TableView "+tableName+" unknown, could not generate controller for table." );
        }
    
    }
    
    static private void createCaseTableViewController( TableView table ){
        CaseTableController newController = new CaseTableController(table, new ClientObjectList<>());
        newController.initialize(null, null);
    }
    
    static private void createServiceTableViewController( TableView table, ClientServiceDefinition def ){
        ImageServiceTableController newController = new ImageServiceTableController(table, new ClientObjectList<>(), def);
        newController.initialize(null, null);
    }
}
