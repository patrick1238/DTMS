/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javafx.collections.ListChangeListener;
import net.patho234.entities.ClientObjectBase;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.interfaces.client.IClientObjectFilter;
import net.patho234.interfaces.client.IDtmsSearch;
import net.patho234.interfaces.client.IDtmsSearchListener;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class DtmsSearch<T extends ClientObjectBase> implements IDtmsSearch<T>{
    String identifier;
    List<IClientObjectFilter<? super ClientObjectBase>> filterList;
    ClientObjectList<T> originalList;
    ClientObjectList<T> resultList;
    
    HashSet<IDtmsSearchListener> resultListener = new HashSet<IDtmsSearchListener>();
    
    ListChangeListener originalListListener;
    
    public DtmsSearch(String name, ClientObjectList<T> originalList){
        this.identifier=name;
        filterList = new ArrayList<>();
        this.originalList=originalList;
        this.resultList = new ClientObjectList<>();
        this.resultList.addAll(originalList);
        
        this.originalListListener = new ListChangeListener<T>(){
                @Override
                public void onChanged(ListChangeListener.Change<? extends T> c) {
                    updateSearchResult();
                }
        };
        
        setOriniginalList(originalList);
    }
    
    public List<IClientObjectFilter<? super ClientObjectBase>> getFilterItems(){
        return filterList;
    }
    
    public ClientObjectList getSearchResult(){
        return resultList;
    }
    
    @Override
    public void updateSearchResult(){
        Logger.getLogger(getClass()).info("Updating search result - original list has "+originalList.size()+" entries.");
        ClientObjectList<T> workingList=new ClientObjectList<>();
        workingList.addAll(originalList);
        for( IClientObjectFilter filter : filterList ){
            workingList = (ClientObjectList<T>)filter.filterClientObjectList(workingList);
            Logger.getLogger(getClass()).info("Filter "+filter+" applied: ");
        }
        Logger.getLogger(getClass()).info("Updating search result - filtered list has "+workingList.size()+" entries.");
        
        notifyAllResultListener();
    }

    private void notifyAllResultListener(){
        for(IDtmsSearchListener resultReceiver : this.resultListener){ notifyResultListener(resultReceiver); }
    }
    private void notifyResultListener(IDtmsSearchListener resultReceiver){
        resultReceiver.receiveSearchResults(resultList);
    }
    
    
    @Override
    public void addDtmsSearchResultListener( IDtmsSearchListener newListener ) {
        resultListener.add(newListener);
        if( this.resultList!=null ){
            notifyResultListener(newListener);
        }
    }

    @Override
    public void removeDtmsSearchResultListener( IDtmsSearchListener toRemoveListener ) {
        resultListener.remove(toRemoveListener);
    }

    @Override
    public void setOriniginalList(ClientObjectList<T> newOriginalItems) {
        if(originalList!=null){
            originalList.removeListener( originalListListener );
        }
            
        this.originalList = newOriginalItems;
        originalList.addListener( originalListListener );
        updateSearchResult();
    }

    @Override
    public void setFilterItems(List<IClientObjectFilter<? super ClientObjectBase>> newFilterItems) {
        this.filterList.clear();
        this.filterList.addAll(newFilterItems);
        
        updateSearchResult();
    }
}
