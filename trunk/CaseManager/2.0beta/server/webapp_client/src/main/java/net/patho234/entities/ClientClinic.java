/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities;

import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import net.patho234.interfaces.IClinic;
import net.patho234.interfaces.IContactForClinic;
import net.patho234.interfaces.client.IClientObject;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class ClientClinic extends ClientObjectBase<ClientClinic> implements IClinic {

    public static ClientClinic getClinicTemplate() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", -1);
        builder.add("name", "");
        builder.add("zipCode", "");
        builder.add("city", "");
        builder.add("street", "");
        return new ClientClinic( builder.build() );
    }
    
    StringProperty name = new SimpleStringProperty();
    StringProperty zipCode = new SimpleStringProperty();
    StringProperty city = new SimpleStringProperty();
    StringProperty street = new SimpleStringProperty();
    
    public ClientClinic(JsonObject clinicAsJson){
        original.setValue( clinicAsJson );
        ClientClinic self=this;
        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                IClientObject.notityAllListeners(changeListener, self, self, self);
            }
        };
        ID.addListener(listener);
        name.addListener(listener);
        zipCode.addListener(listener);
        city.addListener(listener);
        street.addListener(listener);
        
        // original.addListener(listener);
        Logger.getLogger(getClass()).warn("---<ClientClinic(JsonObject clinicAsJson)>---\n\t\tconstructor done.\n\t\tJson: {0}", new Object[]{clinicAsJson.toString()});
        resetClinic();
    }
    
    @Override
    public String getName() {
        return name.getValue();
    }

    @Override
    public String getZipCode() {
        return zipCode.getValue();
    }

    @Override
    public String getCity() {
        return city.getValue();
    }

    @Override
    public String getStreet() {
        return street.getValue();
    }
    
    @Override
    public void setName(String name) {
        this.name.setValue(name);
    }

    @Override
    public void setZipCode(String zipcode) {
        this.zipCode.setValue(zipcode);
    }

    @Override
    public void setCity(String city) {
        this.city.setValue(city);
    }

    @Override
    public void setStreet(String street) {
        this.street.setValue(street);
    }

    @Override
    public List<IContactForClinic> getContactsForClinicList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasLocalChanges() {
        Boolean hasChanges=false;
        Boolean hasChangesId = !ID.getValue().equals(getOriginalJson().getInt("id"));
        Boolean hasChangesName = !name.getValue().equals(getOriginalJson().getString("name"));
        Boolean hasChangesZipCode = !zipCode.getValue().equals(getOriginalJson().getString("zipCode"));
        Boolean hasChangesStreet = !street.getValue().equals(getOriginalJson().getString("street"));
        Boolean hasChangesCity = !city.getValue().equals(getOriginalJson().getString("city"));
        
        Object[] results = new Object[]{hasChangesId, hasChangesName, hasChangesZipCode, hasChangesStreet, hasChangesCity};
        return hasChanges;
    }

    @Override
    public ClientClinic getLocalClone() {
        ClientClinic clone = new ClientClinic( Json.createObjectBuilder().build() );
        clone.ID.setValue(getId());
        clone.name.setValue(getName());
        clone.zipCode.setValue(getZipCode());
        clone.city.setValue(getCity());
        clone.street.setValue(getStreet());
        
        return clone;
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", getId());
        builder.add("name", getName());
        builder.add("zipCode", getZipCode());
        builder.add("city", getCity() );
        builder.add("street", getStreet());
        return builder.build(); 
    }
    
    /**
     * sets all attribute back to initial data stored in the caseOriginal attribute 
     * 
     */
    final public void resetClinic(){
        ID.setValue( getOriginalJson().getInt("id") );
        if(ID.getValue()<=0){
            Logger.getLogger(getClass()).warn("-------------------------------\n case id invalid!!");
        }
        setName( getOriginalJson().getString("name") );
        setStreet(getOriginalJson().getString("street") );
        setZipCode( getOriginalJson().getString("zipCode") );
        setCity( getOriginalJson().getString("city") );
        Logger.getLogger("global").info("------------ resetClinic() called -------------");
        Logger.getLogger("global").info("clinicOriginal: "+original.toString());
        Logger.getLogger("global").info("toString():   "+toString());
    }
    
    
    @Override
    public void merge(ClientClinic toMergeWith) {
        if( getId()!=toMergeWith.getId() ){
             Logger.getLogger(getClass()).warn("Cannot merge clinic objects: different identifiers");
        }
        this.original=toMergeWith.original;
        if(hasLocalChanges()){
            Logger.getLogger(getClass()).warn("Cannot merge clinic objects: has local changes\n\told: {0}\n\t+++: {1}\n\tnew: {2}", new String[]{toString(), original.toString(), toMergeWith.toString()});
        }else{
            ID.setValue(toMergeWith.getId());
            name.setValue(toMergeWith.getName());
            zipCode.setValue(toMergeWith.getZipCode());
            city.setValue(toMergeWith.getCity());
            street.setValue(toMergeWith.getStreet());
            Logger.getLogger(getClass()).info("Successfully merged...");
        }
    }
}
