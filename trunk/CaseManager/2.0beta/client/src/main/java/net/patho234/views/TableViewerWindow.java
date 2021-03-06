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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.patho234.controls.TableViewerController;
import net.patho234.entities.ClientCase;
import net.patho234.entities.ClientService;
import net.patho234.entities.filter.ClientObjectSearchManager;
import net.patho234.entities.filter.ClientServicesForDefinitionFilter;
import net.patho234.entities.filter.ServiceTypeFilter;
import net.patho234.entities.pool.ServiceDefinitionPool;
import net.patho234.entities.pool.ServicePool;
import net.patho234.interfaces.IDataDisplay;
import net.patho234.interfaces.IMetadata;
import net.patho234.interfaces.IService;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.interfaces.client.IDtmsSearchListener;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;
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
    ClientObjectList<ClientService> current2DServiceList;
    ClientObjectList<ClientService> current3DServiceList;
    ClientObjectList<ClientService> current4DServiceList;
    
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
        ClientObjectSearchManager.create().getSearch("global_cases").addDtmsSearchResultListener(this);
        ClientObjectSearchManager.create().getSearch("global_2D").addDtmsSearchResultListener(this);
        ClientObjectSearchManager.create().getSearch("global_3D").addDtmsSearchResultListener(this);
        ClientObjectSearchManager.create().getSearch("global_4D").addDtmsSearchResultListener(this);
    }

    @Override
    synchronized public void receiveSearchResults(ClientObjectList newResults, String searchIdentifier) {
        if(tableViews==null){ 
            Logger.getLogger(getClass().getName()).warning("TableViewerWindow received SearchResult before finished loading...skipping");
            return;
        }
        Logger.getLogger(getClass().getName()).warning("TableViewerWindow received SearchResult for "+searchIdentifier);
        long startTime;
        String strTime;
        switch( searchIdentifier ){
        
            case "global_cases":
                // ========= CaseTableView ===========
                startTime = System.currentTimeMillis();
                Integer caseViewIndex=views.get("Case");
                //System.out.println("caseViewIndex: "+caseViewIndex);
                TableView caseView = tableViews.get(caseViewIndex);
                //System.out.println("caseView: "+caseView);
                caseView.setItems(newResults);
                currentCaseList = newResults;
                strTime = String.format("%.3f", (System.currentTimeMillis()-startTime)/1000.d);
                Logger.getLogger(getClass().getName()).info("caseViewTable now has "+caseView.getItems().size()+" items (Search result processed in "+strTime+" seconds)");
                Logger.getLogger(getClass().getName()).info("Updating input list for global_2D...");
                
                ClientObjectList<ClientService> serviceList2D = new ClientObjectList<>();
                ClientObjectList<ClientService> serviceList3D = new ClientObjectList<>();
                ClientObjectList<ClientService> serviceList4D = new ClientObjectList<>();
                ServiceTypeFilter only2DFilter = new ServiceTypeFilter("2D");
                ServiceTypeFilter only3DFilter = new ServiceTypeFilter("3D");
                ServiceTypeFilter only4DFilter = new ServiceTypeFilter("4D");
                for(ClientCase c : currentCaseList){
//                    System.out.println("adding services for case: "+c.getCaseNumber());
//                    for( Object s : ServicePool.createPool().getAllEntitiesForCase(c) ){
//                        ClientService cs = (ClientService)s;
//                        System.out.println("--- | "+cs.getId()+" : "+cs.getServiceDefinition().getName());
//                        for(IMetadata m : cs.getMetadata()){
//                            System.out.println("    | --- "+m.getName()+" : "+m.getData());
//                        }
//                    }
                    serviceList2D.addAll( only2DFilter.filterClientObjectList( ServicePool.createPool().getAllEntitiesForCase(c)) );
                    serviceList3D.addAll( only3DFilter.filterClientObjectList( ServicePool.createPool().getAllEntitiesForCase(c)) );
                    serviceList4D.addAll( only4DFilter.filterClientObjectList( ServicePool.createPool().getAllEntitiesForCase(c)) );
                }
                ClientObjectSearchManager.create().getSearch("global_2D").setOriniginalList(serviceList2D);
                ClientObjectSearchManager.create().getSearch("global_3D").setOriniginalList(serviceList3D);
                ClientObjectSearchManager.create().getSearch("global_4D").setOriniginalList(serviceList4D);
                break;
            case "global_2D":
                // ========= 2DTableView ===========
//                System.out.println("----------------global_2D changed !!!----------------");
                startTime = System.currentTimeMillis();
                Integer image2DViewIndex=views.get("2D");
//                System.out.println("image2dViewIndex: "+image2DViewIndex);
                TableView image2DView = tableViews.get(image2DViewIndex);
//                System.out.println("image2DView: "+image2DView);
                ReadOnlyClientObjectList<ClientService> filtered2D = new ClientServicesForDefinitionFilter(ServiceDefinitionPool.createPool().getEntity(APPLICATION_DEFAULTS.SERVICE_DEFINITION_ID_2D)).filterClientObjectList((ReadOnlyClientObjectList<ClientService>)newResults);
                image2DView.setItems(filtered2D);
                current2DServiceList = (ClientObjectList)filtered2D;
                strTime = String.format("%.3f", (System.currentTimeMillis()-startTime)/1000.d);
                System.out.println("image2DViewTable now has "+image2DView.getItems().size()+" items (Search result processed in "+strTime+" seconds)");
                break;
            case "global_3D":
                // ========= 3DTableView ===========
                Integer image3DViewIndex=views.get("3D");
                System.out.println("image3dViewIndex: "+image3DViewIndex);
                TableView image3DView = tableViews.get(image3DViewIndex);
                System.out.println("image3DView: "+image3DView);
                ReadOnlyClientObjectList<ClientService> filtered3D = new ClientServicesForDefinitionFilter(ServiceDefinitionPool.createPool().getEntity(APPLICATION_DEFAULTS.SERVICE_DEFINITION_ID_3D)).filterClientObjectList((ReadOnlyClientObjectList<ClientService>)newResults);
                image3DView.setItems(filtered3D);
                current3DServiceList = (ClientObjectList)filtered3D;
                System.out.println("image3DViewTable now has "+image3DView.getItems().size()+" items");
                break;
            case "global_4D":
                // ========= 4DTableView ===========
                Integer image4DViewIndex=views.get("4D");
                System.out.println("image4dViewIndex: "+image4DViewIndex);
                TableView image4DView = tableViews.get(image4DViewIndex);
                System.out.println("image4DView: "+image4DView);
                ReadOnlyClientObjectList<ClientService> filtered4D = new ClientServicesForDefinitionFilter(ServiceDefinitionPool.createPool().getEntity(APPLICATION_DEFAULTS.SERVICE_DEFINITION_ID_4D)).filterClientObjectList((ReadOnlyClientObjectList<ClientService>)newResults);
                image4DView.setItems(filtered4D);
                current4DServiceList = (ClientObjectList)filtered4D;
                System.out.println("image4DViewTable now has "+image4DView.getItems().size()+" items - received list unfiltered: "+newResults.size()+" items");
                break;
        // *TODO
            default:
                Logger.getLogger("TableViewerWindow").warning("TableViewerWindow.receiveSearchResults() for unknown search result ("+searchIdentifier+")");
        }
    }

    @Override
    public Integer getVisibleDataCount(Integer id) {
        switch ( id ) {
            case 0:
                if(currentCaseList != null){
                    return currentCaseList.size();
                }
            case 1:
                if(current2DServiceList != null){
                    return current2DServiceList.size();
                }
            case 2:
                if(current3DServiceList != null){
                    return current3DServiceList.size();
                }
            case 3:
                if(current4DServiceList != null){
                    return current4DServiceList.size();
                }
            default:
                Logger.getLogger("global").warning("getVisibleDataCount(Integer id) can not handle id="+id+" yet.");
                return 0;
        }
        
    }
}
