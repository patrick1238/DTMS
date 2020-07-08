/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.views;

import com.sun.scenario.Settings;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import net.patho234.controls.TableViewerController;
import net.patho234.entities.ClientCase;
import net.patho234.entities.filter.ClientObjectSearchManager;
import net.patho234.interfaces.IDataDisplay;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.interfaces.client.IDtmsSearchListener;
import net.patho234.webapp_client.APPLICATION_DEFAULTS;
import net.patho234.webapp_client.FxmlManager;

/**
 *
 * @author rehkind
 */
public class TableViewerWindow extends Stage implements IDataDisplay, IDtmsSearchListener{
    
    TableViewerController controller;
    HashMap<Integer,String> views;
    
    public TableViewerWindow(){       
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fx_table_viewer_pane.fxml"));
        Parent root=null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(TableViewerWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for main pane.", ex);
            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
        }
        
        controller = loader.getController();
        
        //FxmlManager.applyDefaultStyle( root );
        Scene scene = new Scene(root);
        setScene(scene);
        setTitle(APPLICATION_DEFAULTS.APPLICATION_NAME + " - <todo: proper title>");
        setWidth(APPLICATION_DEFAULTS.MAIN_WINDOW_WIDTH);
        setHeight(APPLICATION_DEFAULTS.MAIN_WINDOW_HEIGHT);
        setScene(scene);
        
        bindTableViewToSearchManger();
    }
    
    public void initializeTables(){
        HashMap<Integer,TableView> tableViews = new HashMap<>();
        String[] availableServices = Settings.get("dtms.services").split(":");
        Integer iterator = 0;
        for(String service:availableServices){
            TableView view = new TableView<Object>();
            tableViews.put(iterator, view);
            iterator += 1;
        }
    }

    @Override
    public HashMap<Integer,String> getViews() {
        HashMap<Integer,String> tableViews = new HashMap<>();
        String[] availableServices = Settings.get("dtms.services").split(":");
        Integer iterator = 0;
        for(String service:availableServices){            
            tableViews.put(iterator,service);
            iterator += 1;
        }
        return tableViews;
    }

    @Override
    public boolean setVisible(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void bindTableViewToSearchManger(){
        ClientObjectSearchManager.create().getSearch("global").addDtmsSearchResultListener(this);
    }

    @Override
    public void receiveSearchResults(ClientObjectList newResults) {
        for( ClientCase resultCase : (ClientObjectList<ClientCase>)newResults ){
            System.out.println("todo: display case "+resultCase);
        }
    }
}
