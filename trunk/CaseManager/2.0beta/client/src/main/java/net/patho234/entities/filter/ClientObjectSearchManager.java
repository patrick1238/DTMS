/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.AnchorPane;
import net.patho234.entities.ClientCase;
import net.patho234.entities.ClientObjectBase;
import net.patho234.entities.ClientService;
import net.patho234.entities.pool.CasePool;
import net.patho234.entities.pool.ServicePool;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.interfaces.client.IDtmsSearch;
import net.patho234.interfaces.client.IDtmsSearchListener;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class ClientObjectSearchManager implements ListChangeListener<ClientObjectBase> {
    
    static private ClientObjectSearchManager singleton;
    CasePool casePool;
    ServicePool servicePool;
    
    ReadOnlyClientObjectList<ClientCase> completeCaseList;
    ReadOnlyClientObjectList<ClientService> completeServiceList;
    
    HashMap<String, IDtmsSearch> dtmsSearches=new HashMap<>();
    
    private ClientObjectSearchManager(){
        bindToClientObjectPools();
    }
    
    static public ClientObjectSearchManager create(){
        if( singleton==null ){
            singleton = new ClientObjectSearchManager();
        }
        
        return singleton;
    }
    
    private void bindToClientObjectPools(){
        casePool=CasePool.createPool();
        servicePool=ServicePool.createPool();
        
        completeCaseList = casePool.getAllEntities();
        completeServiceList = servicePool.getAllEntities();
        
        completeCaseList.addListener(this);
        completeServiceList.addListener(this);
    }

    @Override
    public void onChanged(Change<? extends ClientObjectBase> c) {
        Logger.getLogger(getClass()).info("Global ClientCase or ServiceCase list changed.");
    }
    
    public IDtmsSearch createSearch( String name, ClientObjectList originalItems ){
        DtmsSearch newSearch=new DtmsSearch(name, originalItems);
        dtmsSearches.put(name, newSearch);
        return newSearch;
    }
    
    public IDtmsSearch getSearch( String name ){
        Logger.getLogger(getClass()).info("DtmsSearch[ '"+name+"' ] was requested.");
        return dtmsSearches.get(name);
    }
    
    public void addResultListener(IDtmsSearchListener listener){
        for( IDtmsSearch s : this.dtmsSearches.values()){
            s.addDtmsSearchResultListener(listener);
        }
    }
    
    public void removeResultListener(IDtmsSearchListener listener){
        for( IDtmsSearch s : this.dtmsSearches.values()){
            s.removeDtmsSearchResultListener(listener);
        }
    }
    
}
