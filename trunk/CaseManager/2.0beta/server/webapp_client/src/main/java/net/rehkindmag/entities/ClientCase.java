/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkindmag.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import net.rehkind_mag.interfaces.ICase;
import net.rehkind_mag.interfaces.IClinic;
import net.rehkind_mag.interfaces.IService;
import net.rehkind_mag.interfaces.ISubmitter;
import net.rehkind_mag.interfaces.client.IClientObject;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

/**
 *
 * @author rehkind
 */
public class ClientCase implements ICase, IClientObject<ClientCase>{
    static final private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
    JsonObject caseOriginal;
    
    IntegerProperty ID = new SimpleIntegerProperty();
    StringProperty caseNumber = new SimpleStringProperty();
    StringProperty diagnosis = new SimpleStringProperty();
    StringProperty entryDate = new SimpleStringProperty();
    IntegerProperty clinicID = new SimpleIntegerProperty();
    IntegerProperty submitterID = new SimpleIntegerProperty();
    
    public ClientCase(JsonObject caseAsJson){
        caseOriginal = caseAsJson;
        resetCase();
    }
    
    static public ClientCase getCaseTemplate(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", -1);
        builder.add("caseNumber", "");
        builder.add("diagnose", "");
        builder.add("entryDate", formatter.format( new Date() ));
        builder.add("clinicId", -1);
        builder.add("submitterId", -1);
        return new ClientCase( builder.build() );
    }
    
    @Override
    public int getId(){ return ID.getValue(); }
    
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
            return formatter.parse(entryDate.getValue()) ;
        } catch (ParseException ex) {
            Logger.getLogger(ClientCase.class.getName()).log(Level.FATAL, null, ex);
            return null;
        }
    }
    
    public IntegerProperty getClinicIDProperty(){ return clinicID; }
    @Override
    public IClinic getClinic(){ return new ClientClinic( clinicID.getValue() ); }
    
    public IntegerProperty getSubmitterIDProperty(){ return submitterID; }
    @Override
    public ISubmitter getSubmitter(){ return new ClientSubmitter( submitterID.getValue() ); }
    
    @Override
    public void setCaseNumber( String newCaseNumber ){ caseNumber.setValue( newCaseNumber );}
    @Override
    public void setDiagnose( String newDiagnosis ){ diagnosis.setValue( newDiagnosis ); }
    @Override
    public void setEntryDate( Date newEntryDate ){ entryDate.setValue( formatter.format( newEntryDate )); }
    @Override
    public void setClinic( IClinic newClinic ){  clinicID.setValue( newClinic.getId() ); }
    @Override
    public void setSubmitter(ISubmitter submitter) { submitterID.setValue(submitter.getId()); }
    /**
     * sets all attribute back to initial data stored in the caseOriginal attribute 
     * 
     */
    final public void resetCase(){
        ID.setValue( caseOriginal.getInt("id") );
        setCaseNumber( caseOriginal.getString("caseNumber") );
        setDiagnose( caseOriginal.getString("diagnose") );
        try{
            setEntryDate( formatter.parse( caseOriginal.getString("entryDate")) );
        }catch(ParseException ex){
            Logger.getLogger("global").warn("Could not parse entryDate string: "+caseOriginal.getString("entryDate"));
        }
        setClinic( new ClientClinic( caseOriginal.getInt("clinicId") ) );
        submitterID.setValue( caseOriginal.getInt("submitterId") );
        Logger.getLogger("global").info("------------ resetCase() called -------------");
        Logger.getLogger("global").info("caseOriginal: "+caseOriginal.toString());
        Logger.getLogger("global").info("toString():   "+toString());
    }
    
    @Override
    public String toString(){
        return toJson().toString();
    }

    @Override
    public List<IService> getServices() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasLocalChanges() {
        Boolean hasChanges=false;
        Boolean hasChangesId = !ID.getValue().equals(caseOriginal.getInt("id"));
        Boolean hasChangescaseNumber = !caseNumber.getValue().equals(caseOriginal.getString("caseNumber"));
        Boolean hasChangesDiagnose = !diagnosis.getValue().equals(caseOriginal.getString("diagnose"));
        Boolean hasChangesEntryDate=true;
        try{
            hasChangesEntryDate = formatter.parse(entryDate.getValue()).equals(formatter.parse(caseOriginal.getString("entryDate")));
        }catch(ParseException ex){ 
            // ignored we just set it 'true'
        }
        
        Boolean hasChangesClinic = !clinicID.getValue().equals(caseOriginal.getInt("clinicId"));
        Boolean hasChangesSubmitter = !submitterID.getValue().equals(caseOriginal.getInt("submitterId"));
        
        Object[] results = new Object[]{hasChangesId,hasChangescaseNumber,hasChangesDiagnose,hasChangesEntryDate,hasChangesClinic,hasChangesSubmitter};
        Logger.getLogger(getClass()).info("id: {0} | caseNumber: {1} | diagnose: {2} | entryDate: {3} | clinic: {4} | submitter: {5}", results);
        if( hasChanges != (!toJson().equals(caseOriginal)) ){ Logger.getLogger(getClass()).info("hasLocalChanges() JSON_TEST != VALUE_TEST");}
        return hasChanges;
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", getId());
        builder.add("caseNumber", getCaseNumber());
        builder.add("diagnose", getDiagnose());
        builder.add("entryDate", formatter.format( getEntryDate() ));
        builder.add("clinicId", getClinic().getId());
        builder.add("submitterId", getSubmitter().getId());
        return builder.build(); 
    }

    public boolean wasIdenticalTo(JsonObject objToCompare){
        if(caseOriginal.getInt("id")!=objToCompare.getInt("id")){ return false; }
        if(!Objects.equals( caseOriginal.getString("caseNumber"),objToCompare.getString("caseNumber") )){ return false; }
        if(!Objects.equals( caseOriginal.getString("diagnose"),objToCompare.getString("diagnose") )){ return false; }
        if(!Objects.equals( caseOriginal.getString("entryDate"),objToCompare.getString("entryDate") )){ return false; }
        if(caseOriginal.getInt("clinicId")!=objToCompare.getInt("clinicId")){ return false; }
        if(caseOriginal.getInt("submitterId")!=objToCompare.getInt("submitterId")){ return false; }
        return true;
    }

    @Override
    public ClientCase getLocalClone() {
        return new ClientCase(caseOriginal);
    }

    @Override
    public void merge(ClientCase toMergeWith) {
        if( getId()!=toMergeWith.getId() ){
             Logger.getLogger(getClass()).warn("Cannot merge case objects: different identifiers");
        }
        this.caseOriginal=toMergeWith.caseOriginal;
        if(hasLocalChanges()){
            Logger.getLogger(getClass()).warn("Cannot merge case objects: has local changes\n\told: {0}\n\t+++: {1}\n\tnew: {2}", new String[]{toString(), caseOriginal.toString(), toMergeWith.toString()});
            
        }else{
            ID.setValue(toMergeWith.getId());
            caseNumber.setValue(toMergeWith.getCaseNumber());
            diagnosis.setValue(toMergeWith.getDiagnose());
            entryDate.setValue(ClientCase.formatter.format( toMergeWith.getEntryDate() ));
            clinicID.setValue(toMergeWith.getClinicIDProperty().getValue());
            submitterID.setValue(toMergeWith.getSubmitterIDProperty().getValue());
        }
    }
}
