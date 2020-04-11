/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import net.rehkind_mag.interfaces.ICase;
import net.rehkind_mag.interfaces.IClinic;
import net.rehkind_mag.interfaces.IService;
import net.rehkind_mag.interfaces.ISubmitter;
import net.rehkind_mag.entities.pool.ClinicPool;
import net.rehkind_mag.interfaces.IMetadata;
import net.rehkind_mag.interfaces.IServiceDefinition;
import net.rehkind_mag.interfaces.client.IClientObject;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

/**
 *
 * @author rehkind
 */
public class ClientService extends ClientObjectBase<ClientService> implements IService{
    ArrayList<ClientMetadata> metadata = new ArrayList<>();
    IntegerProperty caseId = new SimpleIntegerProperty();
    IntegerProperty definitionId = new SimpleIntegerProperty();
    
    public ClientService(JsonObject caseAsJson){
        original.setValue( caseAsJson );
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
            .add("serviceDefinition", definitionId)
            .add("serviceMetadata", Json.createArrayBuilder().build() );
        return new ClientService( builder.build() );
    }
    
    public IntegerProperty getIdProperty(){ return ID; }
    
    @Override
    public IServiceDefinition getServiceDefinition() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ICase getCase() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCase(ICase caseValue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setServiceDefinition(IServiceDefinition serviceDef) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<IMetadata> getMetadata() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * sets all attribute back to initial data stored in the caseOriginal attribute 
     * 
     */
    final public void resetService(){
        ID.setValue( getOriginalJson().getInt("id") );
        caseId.setValue( getOriginalJson().getInt("case") );
        definitionId.setValue(getOriginalJson().getInt("serviceDefinition") );
        loadMetadata( getOriginalJson().getJsonArray("serviceMetadata"));
        
        Logger.getLogger("global").info("------------ resetService() called -------------");
        Logger.getLogger("global").info("serviceOriginal: "+original.toString());
        Logger.getLogger("global").info("toString():   "+toString());
    }

    private void loadMetadata(JsonArray metadataJson){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        builder.add("serviceDefinition", getServiceDefinition().getId());
        builder.add("serviceMetadata", metadataToJsonArray());
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

    public Property<String> getCaseNumberProperty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private JsonValue metadataToJsonArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}