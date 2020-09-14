/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import javafx.collections.ListChangeListener;
import net.patho234.controls.elements.CaseFilterPaneController;
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
    HashMap<String, List<IClientObjectFilter<? super ClientObjectBase>>> filterList;
    ClientObjectList<T> originalList;
    ClientObjectList<T> resultList;
    
    HashSet<IDtmsSearchListener> resultListener = new HashSet<IDtmsSearchListener>();
    
    ListChangeListener originalListListener;
    
    UpdateFilterThread updateThread; 
    
    public DtmsSearch(String name, ClientObjectList<T> originalList){
        this.identifier=name;
        filterList = new HashMap<>();
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
    
    @Override
    public HashMap<String, List<IClientObjectFilter<? super ClientObjectBase>>> getFilterItems(){
        return filterList;
    }
    
    @Override
    public ClientObjectList getSearchResult(){
        return resultList;
    }
    
    /**
     * queues an update via updateThread (will be performed immediatly if not update currently running or triggered afterwards)
    */
    @Override
    public void updateSearchResult(){
        System.out.println("UPDATE");
        if(updateThread == null){
            updateThread = new UpdateFilterThread(this);

            Thread terminateUpdateThread = new TerminateUpdateFilterThread( updateThread );
            Runtime.getRuntime().addShutdownHook(terminateUpdateThread);

            updateThread.start();
        }
        updateThread.update();
    }
    
    /**
     * performs the actual update, cannot be called directly -> updateSearchResult will forward update calls to updateThread which then calls the internalUpdate method
     */
    private void internalSearchResultUpdate(){
        Logger.getLogger(getClass()).info("Updating search result - original list has "+originalList.size()+" entries.");
        ClientObjectList<T> workingList=new ClientObjectList<>();
        workingList.addAll(originalList);
        for( List<IClientObjectFilter<? super ClientObjectBase>> categoryList : filterList.values() ){
            int count=0;
            for( IClientObjectFilter filter : categoryList ){
                count++;
                Logger.getLogger(getClass()).info("internalSearchResultUpdate(): Applying filter "+filter.toString()+" ("+count+"/"+categoryList.size()+")");
                workingList = (ClientObjectList<T>)filter.filterClientObjectList(workingList);
                Logger.getLogger(getClass()).info("Filter "+filter+" applied: ");
            }
        }
        Logger.getLogger(getClass()).info("Updating search result - filtered list has "+workingList.size()+" entries.");
        resultList = workingList;
        notifyAllResultListener();
    }

    private void notifyAllResultListener(){
        for(IDtmsSearchListener resultReceiver : this.resultListener){ notifyResultListener(resultReceiver); }
    }
    private void notifyResultListener(IDtmsSearchListener resultReceiver){
        Logger.getLogger(getClass()).info("Notifying search result updated to listener of class: "+resultReceiver.getClass().getName());
        resultReceiver.receiveSearchResults(resultList, identifier);
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
    public void setFilterItems(String category, List<IClientObjectFilter<? super ClientObjectBase>> newFilterItems) {
        if( filterList.get(category)==null ){
            filterList.put(category, newFilterItems);
        }else{
            this.filterList.get(category).clear();
            this.filterList.get(category).addAll(newFilterItems);
        }
        updateSearchResult();
    }
    
    private class UpdateFilterThread extends Thread{
        boolean isRunning=true;
        boolean performUpdate=true;
        DtmsSearch parent;
        
        public UpdateFilterThread(DtmsSearch parent){
            this.parent=parent;
        }
        
        public void update(){
            performUpdate=true;
        }
        
        @Override
        public void run(){

            while( isRunning ){
                if(performUpdate){
                    performUpdate=false;
                    Logger.getLogger(getClass()).info("Updating DtmsSearch with identifier "+parent.identifier);
                    parent.internalSearchResultUpdate();
                }else{
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(CaseFilterPaneController.class.getName()).log(Level.SEVERE, "Thread could not sleep...", ex);
                    }
                }
            }
        }
    }
    
    private class TerminateUpdateFilterThread extends Thread{
        UpdateFilterThread threadToTerminate;
        public TerminateUpdateFilterThread(UpdateFilterThread threadToTerminate){
            this.threadToTerminate = threadToTerminate;
        }
        
        @Override
        public void run(){
            threadToTerminate.isRunning=false;
            threadToTerminate.interrupt();
            while (threadToTerminate.isAlive() && ! threadToTerminate.isInterrupted() ){
                java.util.logging.Logger.getLogger(getClass().getName()).info("Waiting for shut down of CaseFilterUpdateThread.");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(CaseFilterPaneController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
