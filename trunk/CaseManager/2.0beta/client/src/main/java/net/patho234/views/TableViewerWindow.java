/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.views;

import com.sun.scenario.Settings;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.patho234.controls.TableViewerController;
import net.patho234.elements.CaseTableController;
import net.patho234.entities.ClientCase;
import net.patho234.entities.filter.ClientObjectSearchManager;
import net.patho234.interfaces.IDataDisplay;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.interfaces.client.IDtmsSearchListener;
import net.patho234.utils.TableViewerControllerFactory;
import net.patho234.webapp_client.APPLICATION_DEFAULTS;
import net.patho234.webapp_client.FxmlManager;

/**
 *
 * @author rehkind
 */
public class TableViewerWindow extends Stage implements IDataDisplay, IDtmsSearchListener{
    
    TableViewerController controller;
    HashMap<String,Integer> views = new HashMap<>();
    HashMap<Integer,AnchorPane> viewableWindows;
    HashMap<Integer,TableView> tableViews;
    Integer currentlyVisible;
    
    ClientObjectList<ClientCase> currentCaseList;
    
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
    }
    
    public void initializeTables(){
        viewableWindows = new HashMap<>();
        tableViews = new HashMap<>();
        String[] availableServices = Settings.get("dtms.services").split(":");
        this.currentlyVisible = 0;
        Integer iterator = 0;
        for(String service:availableServices){
            AnchorPane anchor = buildUpTable(service);
            viewableWindows.put(iterator, anchor);
            iterator += 1;
        }
        viewableWindows.get(0).setVisible(true);
        
        
        bindTableViewToSearchManger();
    }
    
    private AnchorPane buildUpTable(String service){
        TableView view = new TableView<>();
        
        TableViewerControllerFactory.generateController(service, view);
        Logger.getLogger( getClass().getCanonicalName() ).info( "Initializing table "+service+" with index "+views.keySet().size() );
        views.put( service, tableViews.keySet().size() );
        this.tableViews.put(tableViews.keySet().size(), view);
        AnchorPane tableAnchor = new AnchorPane(view);
        tableAnchor.setVisible(false);
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        this.controller.addTableView(tableAnchor);
        return tableAnchor;
    }

    @Override
    public HashMap<String,Integer> getViews() {
        HashMap<String,Integer> tableViews = new HashMap<>();
        String[] availableServices = Settings.get("dtms.services").split(":");
        Integer iterator = 0;
        for(String service:availableServices){
            tableViews.put(service,iterator);
            iterator += 1;
        }
        return tableViews;
    }

    @Override
    public void setVisible(Integer id) {
        this.viewableWindows.get(currentlyVisible).setVisible(false);
        this.viewableWindows.get(id).setVisible(true);
        this.currentlyVisible = id;
    }
    
    private void bindTableViewToSearchManger(){
        ClientObjectSearchManager.create().getSearch("global").addDtmsSearchResultListener(this);
    }

    @Override
    public void receiveSearchResults(ClientObjectList newResults) {
        if(tableViews==null){ 
            Logger.getLogger("global").warning("TableViewerWindow received SearchResult before finished loading...skipping");
            return;
        }
        
        
        // ========= CaseTableView ===========
        
        Integer caseViewIndex=views.get("Case");
        for (Entry e : tableViews.entrySet()){
            System.out.println("---- "+e.getKey() + " > " + e.getValue());
        }
        
        System.out.println("caseViewIndex: "+caseViewIndex);
        TableView caseView = tableViews.get(caseViewIndex);
        System.out.println("caseView: "+caseView);
        caseView.setItems(newResults);
        currentCaseList = newResults;
        // ========= 2DTableView ===========
        // *TODO
        // ========= 3DTableView ===========
        // *TODO
        // ========= 4DTableView ===========
        // *TODO
        
        caseView.refresh();
        caseView.sort();
        System.out.println("caseViewTable now has "+caseView.getItems().size()+"items");
    }

    @Override
    public Integer getVisibleDataCount(Integer id) {
        switch ( id ) {
            case 1:
                if(currentCaseList != null){
                    return currentCaseList.size();
                }
            default:
                Logger.getLogger("global").warning("getVisibleDataCount(Integer id) can not handle id="+id+" yet.");
                return 5;
        }
        
    }
}
