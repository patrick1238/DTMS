/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities;

import java.util.HashMap;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javax.json.Json;
import javax.json.JsonObject;
import net.rehkind_mag.entities.pool.ServiceDefinitionPool;
import net.rehkind_mag.interfaces.IMetadataValue;
import net.rehkind_mag.interfaces.IService;
import net.rehkind_mag.interfaces.IServiceDefinition;
import net.rehkind_mag.interfaces.client.IClientObject;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class ClientServiceDefinition extends ClientObjectBase<ClientServiceDefinition> implements IServiceDefinition{

    public static ClientServiceDefinition getServiceDefinitionTemplate() {
        JsonObject asJson = Json.createObjectBuilder()
            .add("id", -1)
            .add("name", "[TEMPLATE] TemplateServiceDefinition")
            .add("description", "[TEMPLATE] No actual service...please replace name and description.")
            .add("parentDefinition", 1).build(); // setting to parent to Service
        return new ClientServiceDefinition(asJson);
    }
    
    
    StringProperty name = new SimpleStringProperty();
    StringProperty description = new SimpleStringProperty();
    IntegerProperty parentDefId = new SimpleIntegerProperty();
    
    
    public ClientServiceDefinition(JsonObject defAsJson){
        original.setValue( defAsJson );
        ClientServiceDefinition self=this;
        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                IClientObject.notityAllListeners(changeListener, self, self, self);
            }
        };
        ID.addListener(listener);
        
        
        //original.addListener(listener);
        resetServiceDefinition();
    }
    
    @Override
    public IServiceDefinition getParentDefinition() {
        if( this.parentDefId.getValue()<=0 ){ return null; }
        return ServiceDefinitionPool.createPool().getEntity(parentDefId.getValue());
    }

    @Override
    public String getName() {
        return name.getValue();
    }

    @Override
    public String getDescription() {
        return description.getValue();
    }

    @Override
    public List<IService> getServices() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HashMap<IServiceDefinition, List<IMetadataValue>> getMetadataValues() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setName(String name) {
        this.name.setValue(name);
    }

    @Override
    public void setDescription(String description) {
        this.description.setValue(description);
    }

    @Override
    public void setParentDefinition(IServiceDefinition parentDef) {
        if(parentDef.getId()<=0){ System.out.println("EXCEPTION: parent ID <= 0"); }
        this.parentDefId.setValue(parentDef.getId());
    }

    @Override
    public void merge(ClientServiceDefinition toMergeWith) {
        if( getId()!=toMergeWith.getId() ){
             Logger.getLogger(getClass()).warn("Cannot merge service_definition objects: different identifiers");
             return;
        }
        this.original=toMergeWith.original;
        if(hasLocalChanges()){
            Logger.getLogger(getClass()).warn("Cannot merge service_definition objects: has local changes\n\told: {0}\n\t+++: {1}\n\tnew: {2}", new String[]{toString(), original.toString(), toMergeWith.toString()});
        }else{
            ID.setValue(toMergeWith.getId());
            name.setValue(toMergeWith.getName());
            description.setValue(toMergeWith.getDescription());
            if(toMergeWith.getParentDefinition()==null){
                parentDefId.setValue(-1);
            }else{
                parentDefId.setValue(toMergeWith.getParentDefinition().getId());
            }
            Logger.getLogger(getClass()).info("Successfully merged...");
        }
    }
    
    @Override
    public boolean hasLocalChanges() {
        boolean nameChanged = ! name.getValue().equals(original.getValue().getString("name"));
        boolean descriptionChanged = ! description.getValue().equals(original.getValue().getString("description"));
        boolean parentDefChanged = ! parentDefId.getValue().equals(getParentDefIDFromJson( original.getValue() ));
        boolean hasLocalChanges = nameChanged || descriptionChanged || parentDefChanged;
        
        return hasLocalChanges;
    }

    @Override
    public ClientServiceDefinition getLocalClone() {
        ClientServiceDefinition clone = new ClientServiceDefinition(original.getValue());
        clone.name.setValue(name.getValue());
        clone.description.setValue(description.getValue());
        clone.parentDefId.setValue(parentDefId.getValue());
        return clone;
    }

    @Override
    public JsonObject toJson() {
        JsonObject asJson = Json.createObjectBuilder()
                .add("id", ID.getValue())
                .add("name", name.getValue())
                .add("description", description.getValue())
                .add("parentDefinition", parentDefId.getValue()).build();
        return asJson;
    }

    private void resetServiceDefinition() {
        ID.setValue(original.getValue().getInt("id"));
        name.setValue(original.getValue().getString("name"));
        description.setValue(original.getValue().getString("description"));
        try{
            parentDefId.setValue(getParentDefIDFromJson(original.getValue()));
        }catch(ClassCastException ex){
            parentDefId.setValue(original.getValue().getInt("parentDefinition"));
        }
    }
    
    private Integer getParentDefIDFromJson(JsonObject json){
        Integer resultID;
        if(json==null){ return -1; }
        try{ // Json contains ID only
            if(json.isNull("parentDefinition")){
                resultID=-1;
            }else{
                resultID=json.getInt("parentDefinition");
            }
        }catch(ClassCastException ex){ // Json contains parent defintion as IServiceDefinition
            resultID=json.getJsonObject("parentDefinition").getInt("id");
        }catch(NullPointerException ex){
            resultID=-1;
        }
        //if( resultID==-1 ){ resultID=null; } // no parent definition - definition Service (every Service should be inherited from Service)
        
        return resultID;
    }
}
