/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities;

import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import net.patho234.entities.pool.SubmitterPool;
import net.patho234.interfaces.ILogEntry;
import net.patho234.interfaces.ISubmitter;
import net.patho234.interfaces.client.IClientObject;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class ClientLogEntry extends ClientObjectBase<ClientLogEntry> implements ILogEntry {
    
    Date timestamp;
    String affectedTable;
    String message;
    int submitterId;
    
    public ClientLogEntry( JsonObject logAsJson){
        original.setValue( logAsJson );
        ClientLogEntry self=this;
        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                IClientObject.notityAllListeners(changeListener, self, self, self);
            }
        };
        
        // original.addListener(listener);
        Logger.getLogger(getClass()).warn("---<ClientLogEntry(JsonObject logAsJson)>---\n\t\tconstructor done.\n\t\tJson: {0}", new Object[]{logAsJson.toString()});
        resetLogEntry();
    }
    
    @Override
    public boolean hasLocalChanges() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ClientLogEntry getLocalClone() {
        return new ClientLogEntry(toJson());
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", ID.getValue() );
        builder.add("timestamp", DATE_FORMATTER.format(timestamp) );
        builder.add("message", message);
        builder.add("affectedTable", affectedTable );
        builder.add("submitterId", submitterId);
        return builder.build();
    }

    @Override
    public Date getTimestamp() {
        return this.timestamp;
    }

    @Override
    public String getAffectedTable() {
        return this.affectedTable;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public ISubmitter getSubmitter() {
        return SubmitterPool.createPool().getEntity(submitterId);
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public void setAffectedTable(String table) {
        this.affectedTable = affectedTable;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setSubmitter(ISubmitter submitter) {
        this.submitterId  = submitter.getId();
    }

    private void resetLogEntry() {
        JsonObject orig = original.getValue();
        this.ID.setValue(orig.getInt("id"));
        this.affectedTable = orig.getString("affectedTable");
        this.message = orig.getString("message");
        this.submitterId = orig.getInt("submitterId");
        try {
            this.timestamp = DATE_FORMATTER.parse( orig.getString("timestamp") );
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(ClientLogEntry.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
