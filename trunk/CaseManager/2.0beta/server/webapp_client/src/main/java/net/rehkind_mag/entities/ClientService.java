/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import net.rehkind_mag.entities.pool.CasePool;
import net.rehkind_mag.entities.pool.MetadataPool;
import net.rehkind_mag.entities.pool.ServiceDefinitionPool;
import net.rehkind_mag.interfaces.ICase;
import net.rehkind_mag.interfaces.IService;
import net.rehkind_mag.interfaces.IMetadata;
import net.rehkind_mag.interfaces.IServiceDefinition;
import net.rehkind_mag.interfaces.client.ClientObjectList;
import net.rehkind_mag.interfaces.client.IClientObject;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class ClientService extends ClientObjectBase<ClientService> implements IService{
    ClientObjectList<ClientMetadata> metadata = new ClientObjectList<>();
    IntegerProperty caseId = new SimpleIntegerProperty();
    IntegerProperty definitionId = new SimpleIntegerProperty();
    
    public ClientService(JsonObject caseAsJson){
        original.setValue( caseAsJson );
        System.out.println("creating ClientService from: "+original.getValue().toString());
        ClientService self=this;
        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                IClientObject.notityAllListeners(changeListener, self, self, self);
            }
        };
        ID.addListener(listener);
        caseId.addListener(listener);
        definitionId.addListener(listener);
        
        //original.addListener(listener);
        resetService();
    }
    
    static public ClientService getServiceTemplate(Integer caseId, Integer definitionId){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", -1)
            .add("case", caseId)
            .add("serviceDefinition", ServiceDefinitionPool.createPool().getEntity( definitionId ).toJson() )
            .add("serviceMetadata", Json.createArrayBuilder().build() );
        System.out.println("getServiceTemplate");
        return new ClientService( builder.build() );
    }
    
    public IntegerProperty getIdProperty(){ return ID; }
    
    @Override
    public IServiceDefinition getServiceDefinition() {
        return ServiceDefinitionPool.createPool().getEntity( this.definitionId.getValue() );
    }

    @Override
    public ICase getCase() {
        if( caseId.getValue() == -1 ){ System.out.println("CASE_ID FOR SERVICE IS -1"); }
        return CasePool.createPool().getEntity(caseId.getValue());
    }

    @Override
    public void setCase(ICase caseValue) {
        this.caseId.setValue( caseValue.getId() );
    }

    @Override
    public void setServiceDefinition(IServiceDefinition serviceDef) {
        this.definitionId.setValue(serviceDef.getId());
    }

    @Override
    public List<IMetadata> getMetadata() {
        return new ArrayList( MetadataPool.createPool().getMetadataForService(this, Boolean.FALSE) );
    }
    public JsonArray getMetadataJsonArray() {
        return Json.createArrayBuilder().build();
        //return MetadataPool.createPool().getMetadataForService(this, Boolean.FALSE).toJson();
    }
    /**
     * sets all attribute back to initial data stored in the caseOriginal attribute 
     * 
     */
    final public void resetService(){
        Logger.getLogger(getClass()).debug( "ORIGINAL: "+original.getValue().toString() );
        ID.setValue( getOriginalJson().getInt("id") );
        caseId.setValue( getOriginalJson().getInt("case") );
        definitionId.setValue(getOriginalJson().getJsonObject("serviceDefinition").getInt("id") );
        
        Logger.getLogger(getClass()).debug("------------ resetService() called -------------");
        Logger.getLogger(getClass()).debug("serviceOriginal: "+original.toString());
        Logger.getLogger(getClass()).debug("toString():   "+toString());
    }
    
    @Override
    public boolean hasLocalChanges() {
        Boolean hasChanges=false;
        Boolean hasChangesId = !ID.getValue().equals(getOriginalJson().getInt("id"));
        Boolean hasChangesCaseId = !caseId.getValue().equals(getOriginalJson().getInt("case"));
        Boolean hasChangesMetadata = false; // TODO: replace with correct check

        hasChanges = hasChangesId || hasChangesCaseId || hasChangesMetadata;
        return hasChanges;
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", getId());
        builder.add("case", getCase().getId());
        builder.add("serviceDefinition", ((ClientServiceDefinition)getServiceDefinition()).toJson());
        builder.add("serviceMetadata", getMetadataJsonArray());
        return builder.build(); 
    }

    public boolean wasIdenticalTo(JsonObject objToCompare){
        if(getOriginalJson().getInt("id")!=objToCompare.getInt("id")){ return false; }
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //return true;
    }

    @Override
    public ClientService getLocalClone() {
        return new ClientService(getOriginalJson());
    }

    @Override
    public void merge(ClientService toMergeWith) {
        if( getId()!=toMergeWith.getId() ){
             Logger.getLogger(getClass()).warn("Cannot merge case objects: different identifiers");
        }
        this.original=toMergeWith.original;
        if(hasLocalChanges()){
            Logger.getLogger(getClass()).warn("Cannot merge case objects: has local changes\n\told: {0}\n\t+++: {1}\n\tnew: {2}", new String[]{toString(), original.toString(), toMergeWith.toString()});
        }else{
            ID.setValue(toMergeWith.getId());
            caseId.setValue(toMergeWith.getCase().getId());
            definitionId.setValue(toMergeWith.getServiceDefinition().getId());
            metadata.clear();
            toMergeWith.getMetadata().forEach(
                    (item) -> { metadata.add( (ClientMetadata)item ); }
            );
            
            Logger.getLogger(getClass()).info("Successfully merged...");
        }
    }

    @Override
    public ClientService getValue() {
        return this;
    }

    @Override
    public void addListener(InvalidationListener il) {
        invalListener.add(il);
    }

    @Override
    public void removeListener(InvalidationListener il) {
        invalListener.remove(il);
    }
    
}