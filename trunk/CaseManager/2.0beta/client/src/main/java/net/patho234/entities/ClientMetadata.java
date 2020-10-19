/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import net.patho234.entities.pool.ServicePool;
import net.patho234.interfaces.IMetadata;
import net.patho234.interfaces.IService;
import net.patho234.interfaces.client.IClientObject;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class ClientMetadata<T> extends ClientObjectBase<ClientMetadata> implements IMetadata<T>{
    StringProperty name = new SimpleStringProperty();
    
    IntegerProperty serviceId = new SimpleIntegerProperty();
    ObjectProperty data = new SimpleObjectProperty();
    StringProperty type = new SimpleStringProperty();
    
    static private HashMap<ClientMetadataKey, Integer> keyToIdMap = new HashMap<>();
    
    public ClientMetadata(JsonObject asJson) {
        if(asJson==null){ Logger.getLogger(getClass()).fatal("ClientMetadata(): JSONObject is NULL"); }
        System.out.println("AS_JSON: "+asJson.toString());
        this.original.setValue(asJson);
        ID.setValue(asJson.getInt("serviceId"));
        this.name.setValue(asJson.getString("name"));
        this.type.setValue(asJson.getString("type"));
        
        this.data.setValue( castValue(asJson.getString("value")) );
        
        ClientMetadata self=this; 
        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                IClientObject.notityAllListeners(changeListener, self, self, self);
            }
        };
        data.addListener(listener);
        //this.data.setValue(getDataValue(asJson));
        serviceId.setValue(asJson.getInt("serviceId"));
        
        generateId();
        this.addListener(
            new ChangeListener<ClientMetadata>(){
            @Override
            public void changed(ObservableValue<? extends ClientMetadata> observable, ClientMetadata oldValue, ClientMetadata newValue) {
                System.out.println("METADATA_CHANGED to new value: "+getData().toString());
                Logger.getLogger(getClass()).info("METADATA_CHANGED to new value: "+getData().toString());
            }
        });
    }
    
    static public ClientMetadata createTemplate(String name, int serviceId ) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", name);
        builder.add("serviceId", serviceId);
        builder.add("value", "<template>");
        builder.add("type", "string");
        JsonObject json = builder.build();
        System.out.println("Creating template ClientMetadata with json: "+json.toString());
        return new ClientMetadata(json);
    }
    
    @Override
    public Integer getId(){
        if( this.ID.getValue() == null ){ generateId(); }
        return super.getId();
    }
    
    @Override
    public boolean hasLocalChanges() {
        if( original.getValue() == null ){ return true; }
        
        boolean nameUnchanged = name.getValue().equals(original.getValue().getString("name"));
        boolean serviceIdUnchanged = serviceId.getValue().equals(original.getValue().getInt("serviceId"));
        boolean dataUnchanged = Objects.equals(this.data.getValue().toString(), castValue(original.getValue().getString("value")).toString());
        boolean typeUnchanged = this.type.getValue() == original.getValue().getString("type");
        
//        if(nameUnchanged && serviceIdUnchanged && dataUnchanged && typeUnchanged){
//        if(!(nameUnchanged && serviceIdUnchanged && dataUnchanged && typeUnchanged)){
//            System.out.println(" ----- OLD:");
//            System.out.println(" -----     |"+this.original.getValue().toString());
//            System.out.println(" ----- NEW:");
//            System.out.println(" -----    |"+toJson().toString());
//            System.out.println(" ----- CHECK:");
//            System.out.println(" -----    |"+this.data.getValue().toString()+" VS "+castValue(original.getValue().getString("value")));
//            System.out.println("NAME "+nameUnchanged+"|SERVICE "+serviceIdUnchanged+"|VALUE "+dataUnchanged+"|TYPE ");
//        }
        return !(nameUnchanged && serviceIdUnchanged && dataUnchanged && typeUnchanged);
    }

    @Override
    public ClientMetadata getLocalClone() {
        ClientMetadata metadata=null;
        metadata = new ClientMetadata( original.getValue() );
        metadata.setData(getData());
        return metadata;
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", ID.getValue());
        builder.add("name", name.getValue());
        builder.add("serviceId", serviceId.getValue());
        switch(type.getValue()){
            case "integer":
            case "int":
                builder.add("value", (Integer)data.getValue());
                builder.add("type", "int");
                break;
            case "double":
                builder.add("value", (Double)data.getValue());
                builder.add("type", "double");
                break;
            case "string":
                builder.add("value", (String)data.getValue());
                builder.add("type", "string");
                break;
            case "text":
                builder.add("value", (String)data.getValue());
                builder.add("type", "text");
                break;
            case "url":
                builder.add("value", (String)data.getValue());
                builder.add("type", "url");
                break;
        }
        return builder.build();
    }

    @Override
    public void merge(ClientMetadata toMergeWith) {
        if( toMergeWith.getId() != this.getId() ){ Logger.getLogger(getClass()).info("Can not merge Metadata objects, their ID differs."); return; }
        original.setValue(toMergeWith.original.getValue());
        if( hasLocalChanges() ){ Logger.getLogger(getClass()).info("Can not merge Metadata objects...locale object has changes. Only original Json was updated..."); return; }
        
        name.setValue(toMergeWith.name.getValue());
        serviceId.setValue(toMergeWith.serviceId.getValue());
        
        Logger.getLogger(getClass()).info("Metadata object, was merged successfully.");
    }

    @Override
    public void addListener(ChangeListener<? super ClientMetadata> cl) {
       changeListener.add(cl);
    }

    @Override
    public void removeListener(ChangeListener<? super ClientMetadata> cl) {
        changeListener.remove(cl);
    }
    
    @Override
    public void addListener(InvalidationListener il) {
        invalListener.add(il);
    }

    @Override
    public void removeListener(InvalidationListener il) {
        invalListener.remove(il);
    }

    @Override
    public IService getService() {
        // System.out.println("requesting service for metadata ( serviceId="+serviceId.getValue()+")");
        return ServicePool.createPool().getEntity( serviceId.getValue(), false );
    }

    @Override
    public T getData() {
        return (T)data.getValue();
    }
    
    public ObjectProperty getDataProperty() {
        return data;
    }

    @Override
    public void setData(T newData) {
        data.setValue( newData );
    }

    @Override
    public String getName() {
        return name.getValue();
    }

    private Object getDataValue(JsonObject asJson) {
        Object dataObj=null;

        
        return dataObj;
    }

    @Override
    public METADATA_TYPE getType() {
        switch ( type.getValue() ){
            case "integer":
                return IMetadata.METADATA_TYPE.INTEGER;
            case "double":
                return IMetadata.METADATA_TYPE.DOUBLE;
            case "string":
                return IMetadata.METADATA_TYPE.STRING;
            case "text":
                return IMetadata.METADATA_TYPE.TEXT;
            case "url":
                return IMetadata.METADATA_TYPE.URL;
            default:
                return IMetadata.METADATA_TYPE.UNDEFINED;
        }
    }
    
    public ClientMetadataKey getMetadataKey(){
        return new ClientMetadataKey(this);
    }
    
    private void generateId(){
        Integer id = ClientMetadata.keyToIdMap.get(getMetadataKey());
        if( id == null ){
            ClientMetadata.keyToIdMap.put(getMetadataKey(), keyToIdMap.keySet().size()+1);
            id = keyToIdMap.get(getMetadataKey());
        }
        this.ID.setValue(id);
    }

    public Integer getServiceID() {
        return this.serviceId.getValue();
    }

    private Object castValue(String string) {
        switch( type.getValue() ){
            case "integer":
            case "int":
                return Integer.valueOf( string );
            case "double":
                return Double.valueOf( string );
            case "string":
            case "text":
            case "url":
                return string ;
            default:
                Logger.getLogger(getClass()).error("Unknown metadata type: "+type.getValue());
                return null;
        }
    }
}
