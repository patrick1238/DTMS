/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import net.patho234.interfaces.ICase;
import net.patho234.interfaces.IClinic;
import net.patho234.interfaces.IService;
import net.patho234.interfaces.ISubmitter;
import net.patho234.entities.pool.ClinicPool;
import net.patho234.entities.pool.ServicePool;
import net.patho234.interfaces.client.IClientObject;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

/**
 *
 * @author rehkind
 */
public class ClientCase extends ClientObjectBase<ClientCase> implements ICase{

    StringProperty caseNumber = new SimpleStringProperty();
    StringProperty diagnosis = new SimpleStringProperty();
    StringProperty entryDate = new SimpleStringProperty();
    IntegerProperty clinicID = new SimpleIntegerProperty();
    IntegerProperty submitterID = new SimpleIntegerProperty();
    
    public ClientCase(JsonObject caseAsJson){
        original.setValue( caseAsJson );
        ClientCase self=this;
        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                IClientObject.notityAllListeners(changeListener, self, self, self);
            }
        };
        ID.addListener(listener);
        caseNumber.addListener(listener);
        diagnosis.addListener(listener);
        entryDate.addListener(listener);
        clinicID.addListener(listener);
        submitterID.addListener(listener);
        
        //original.addListener(listener);
        
        resetCase();
    }
    
    static public ClientCase getCaseTemplate(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", -1);
        builder.add("caseNumber", "");
        builder.add("diagnose", "");
        builder.add("entryDate", DATE_FORMATTER.format( new Date() ));
        builder.add("clinicId", -1);
        builder.add("submitterId", -1);
        return new ClientCase( builder.build() );
    }
    
    public IntegerProperty getIdProperty(){ return ID; }
    
    public StringProperty getCaseNumberProperty(){ return caseNumber; }
    @Override
    public String getCaseNumber(){ return caseNumber.getValue(); }
    
    public StringProperty getDiagnosisProperty(){ return diagnosis; }
    @Override
    public String getDiagnose(){ return diagnosis.getValue(); }
    
    public StringProperty getEntryDateProperty(){ return entryDate; }
    @Override
    public Date getEntryDate(){ try {
            return DATE_FORMATTER.parse(entryDate.getValue()) ;
        } catch (ParseException ex) {
            Logger.getLogger(ClientCase.class.getName()).log(Level.FATAL, null, ex);
            return null;
        }
    }
    
    public IntegerProperty getClinicIDProperty(){ return clinicID; }
    @Override
    public ClientClinic getClinic(){
        //Logger.getLogger(getClass()).info("XXX requesting clinic with id="+clinicID.getValue());
        return ClinicPool.createPool().getEntity( clinicID.getValue() );
    }
    
    public IntegerProperty getSubmitterIDProperty(){ return submitterID; }
    @Override
    public ISubmitter getSubmitter(){ return new ClientSubmitter( submitterID.getValue() ); }
    
    @Override
    public void setCaseNumber( String newCaseNumber ){ caseNumber.setValue( newCaseNumber );}
    @Override
    public void setDiagnose( String newDiagnosis ){ diagnosis.setValue( newDiagnosis ); }
    @Override
    public void setEntryDate( Date newEntryDate ){ entryDate.setValue( DATE_FORMATTER.format( newEntryDate )); }
    @Override
    public void setClinic( IClinic newClinic ){  clinicID.setValue( newClinic.getId() ); }
    @Override
    public void setSubmitter(ISubmitter submitter) { 
        submitterID.setValue(submitter.getId()); }
    /**
     * sets all attribute back to initial data stored in the caseOriginal attribute 
     * 
     */
    final public void resetCase(){
        ID.setValue( getOriginalJson().getInt("id") );
        setCaseNumber( getOriginalJson().getString("caseNumber") );
        setDiagnose( getOriginalJson().getString("diagnose") );
        try{
            setEntryDate( DATE_FORMATTER.parse( getOriginalJson().getString("entryDate")) );
        }catch(ParseException ex){
            Logger.getLogger("global").warn("Could not parse entryDate string: "+getOriginalJson().getString("entryDate"));
        }
        clinicID.setValue(getOriginalJson().getInt("clinicId"));
        Logger.getLogger("global").info("getOriginalJson(): "+getOriginalJson());
        Logger.getLogger("global").info("getOriginalJson().getInt(\"submitterId\": "+getOriginalJson().getInt("submitterId"));
        submitterID.setValue( getOriginalJson().getInt("submitterId") );
        Logger.getLogger("global").info("------------ resetCase() called -------------");
        Logger.getLogger("global").info("caseOriginal: "+original.toString());
        Logger.getLogger("global").info("toString():   "+toString());
    }

    @Override
    public List<IService> getServices() {
        ReadOnlyClientObjectList<ClientService> myServices = ServicePool.createPool().getAllEntitiesForCase(this);
        return (List)myServices;
    }

    @Override
    public boolean hasLocalChanges() {
        Boolean hasChanges=false;
        Boolean hasChangesId = !ID.getValue().equals(getOriginalJson().getInt("id"));
        Boolean hasChangescaseNumber = !caseNumber.getValue().equals(getOriginalJson().getString("caseNumber"));
        Boolean hasChangesDiagnose = !diagnosis.getValue().equals(getOriginalJson().getString("diagnose"));
        Boolean hasChangesEntryDate=true;
        try{
            hasChangesEntryDate = !(DATE_FORMATTER.parse(entryDate.getValue()).getTime()== DATE_FORMATTER.parse(getOriginalJson().getString("entryDate")).getTime());
        }catch(ParseException ex){ 
            // ignored we just set it 'true'
        }
        
        Boolean hasChangesClinic = !clinicID.getValue().equals(getOriginalJson().getInt("clinicId"));
        Boolean hasChangesSubmitter = !submitterID.getValue().equals(getOriginalJson().getInt("submitterId"));
        
        Object[] results = new Object[]{hasChangesId,hasChangescaseNumber,hasChangesDiagnose,hasChangesEntryDate,hasChangesClinic,hasChangesSubmitter};
        Logger.getLogger(getClass()).info("id: {0} | caseNumber: {1} | diagnose: {2} | entryDate: {3} | clinic: {4} | submitter: {5}", results);
        // if( hasChanges != (!toJson().equals(caseOriginal)) ){ Logger.getLogger(getClass()).info("hasLocalChanges() JSON_TEST != VALUE_TEST");}
        hasChanges = hasChangescaseNumber | hasChangesDiagnose | hasChangesEntryDate | hasChangesClinic | hasChangesSubmitter;
        return hasChanges;
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", getId());
        builder.add("caseNumber", getCaseNumber());
        builder.add("diagnose", getDiagnose());
        builder.add("entryDate", entryDate.getValue());
        builder.add("clinicId", clinicID.getValue());
        builder.add("submitterId", getSubmitter().getId());
        return builder.build();
    }

    public boolean wasIdenticalTo(JsonObject objToCompare){
        if(getOriginalJson().getInt("id")!=objToCompare.getInt("id")){ return false; }
        if(!Objects.equals( getOriginalJson().getString("caseNumber"),objToCompare.getString("caseNumber") )){ return false; }
        if(!Objects.equals( getOriginalJson().getString("diagnose"),objToCompare.getString("diagnose") )){ return false; }
        if(!Objects.equals( getOriginalJson().getString("entryDate"),objToCompare.getString("entryDate") )){ return false; }
        if(getOriginalJson().getInt("clinicId")!=objToCompare.getInt("clinicId")){ return false; }
        if(getOriginalJson().getInt("submitterId")!=objToCompare.getInt("submitterId")){ return false; }
        return true;
    }

    @Override
    public ClientCase getLocalClone() {
        return new ClientCase(getOriginalJson());
    }

    @Override
    public void merge(ClientCase toMergeWith) {
        if( getId()!=toMergeWith.getId() ){
             Logger.getLogger(getClass()).warn("Cannot merge case objects: different identifiers");
        }
        this.original=toMergeWith.original;
        if(hasLocalChanges()){
            Logger.getLogger(getClass()).warn("Cannot merge case objects: has local changes\n\told: {0}\n\t+++: {1}\n\tnew: {2}", new String[]{toString(), original.toString(), toMergeWith.toString()});
        }else{
            ID.setValue(toMergeWith.getId());
            caseNumber.setValue(toMergeWith.getCaseNumber());
            diagnosis.setValue(toMergeWith.getDiagnose());
            entryDate.setValue(ClientCase.DATE_FORMATTER.format( toMergeWith.getEntryDate() ));
            clinicID.setValue(toMergeWith.getClinicIDProperty().getValue());
            submitterID.setValue(toMergeWith.getSubmitterIDProperty().getValue());
            Logger.getLogger(getClass()).info("Successfully merged...");
        }
    }

    @Override
    public ClientCase getValue() {
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