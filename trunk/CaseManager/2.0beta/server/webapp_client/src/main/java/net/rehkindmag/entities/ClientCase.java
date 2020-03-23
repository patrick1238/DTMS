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
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 *
 * @author rehkind
 */
public class ClientCase implements ICase{
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
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
            Logger.getLogger(ClientCase.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger("global").warning("Could not parse entryDate string: "+caseOriginal.getString("entryDate"));
        }
        setClinic( new ClientClinic( caseOriginal.getInt("clinicId") ) );
        Logger.getLogger("global").info("------------ resetCase() called -------------");
        Logger.getLogger("global").info("caseOriginal: "+caseOriginal.toString());
        Logger.getLogger("global").info("toString():   "+toString());
    }
    
    @Override
    public String toString(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", getId());
        builder.add("caseNumber", getCaseNumber());
        builder.add("diagnose", getDiagnose());
        builder.add("entryDate", formatter.format( getEntryDate() ));
        builder.add("clinicId", getClinic().getId());
        builder.add("submitterId", getSubmitter().getId());
        return builder.build().toString();
    }

    @Override
    public List<IService> getServices() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
