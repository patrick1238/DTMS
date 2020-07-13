/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities;

import java.util.Objects;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import net.patho234.interfaces.IMetadataValue;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class ClientMetadataValue extends ClientObjectBase<ClientMetadataValue> implements IMetadataValue{
    SimpleStringProperty keyProperty=new SimpleStringProperty();
    SimpleStringProperty valueTypeProperty=new SimpleStringProperty();
    SimpleBooleanProperty deprecatedProperty=new SimpleBooleanProperty();
    SimpleStringProperty unitProperty=new SimpleStringProperty();
    
    public ClientMetadataValue( JsonObject asJson ){
        this.original.setValue(asJson);
        resetMetadataValue();
    }
    
    @Override
    public boolean hasLocalChanges() {
        Boolean keyChanged = Objects.equals( keyProperty.getValue(), original.getValue().getString("key") );
        Boolean valueTypeChanged = Objects.equals( valueTypeProperty.getValue(), original.getValue().getString("valueType") );
        Boolean deprecatedChanged = Objects.equals( deprecatedProperty.getValue(), original.getValue().getBoolean("deprecated") );
        Boolean unitChanged = Objects.equals( unitProperty.getValue(), original.getValue().getString("unit") );
        
        return keyChanged || valueTypeChanged || deprecatedChanged || unitChanged;
    }

    @Override
    public ClientMetadataValue getLocalClone() {
        return new ClientMetadataValue(original.getValue());
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        
        builder.add("key", keyProperty.getValue());
        builder.add("valueType", valueTypeProperty.getValue());
        builder.add("deprecated", deprecatedProperty.getValue());
        if( unitProperty.getValue() != null ){
            builder.add("unit", unitProperty.getValue());
        }else{
            builder.addNull("unit");
        }
        return builder.build();
    }
    
    public void resetMetadataValue(){
        Logger.getLogger(getClass()).info("Resetting ClientMetadataValue to original... >"+original.getValue().toString());
        keyProperty.setValue( original.getValue().getString("key") );
        valueTypeProperty.setValue( original.getValue().getString("valueType") );
        deprecatedProperty.setValue( original.getValue().getBoolean("deprecated") );
        try{
            unitProperty.setValue( original.getValue().getString("unit") );
        }catch( ClassCastException ccEx ){
            unitProperty.setValue( null );
        }
    }

    @Override
    public Integer getId() {
        return -1;
    }

    @Override
    public String getValueType() {
        return valueTypeProperty.getValue();
    }

    @Override
    public String getKey() {
        return keyProperty.getValue();
    }

    @Override
    public Boolean isDepricated() {
        return deprecatedProperty.getValue();
    }

    @Override
    public String getUnit() {
        return unitProperty.getValue();
    }

    @Override
    public void setValueType(String valueType) {
        valueTypeProperty.setValue(valueType);
    }

    @Override
    public void setKey(String key) {
        keyProperty.setValue(key);
    }

    @Override
    public void setDepricated(Boolean deprecated) {
        deprecatedProperty.setValue(deprecated);
    }

    @Override
    public void setUnit(String unit) {
        unitProperty.setValue(unit);
    }
}
