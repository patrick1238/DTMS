/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import net.patho234.interfaces.ILogEntry;
import net.patho234.interfaces.ISubmitter;

/**
 *
 * @author MLH
 */
@Entity
@Table(name = "logs")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogEntryEntity.findAll", query = "SELECT l FROM LogEntryEntity l")
    , @NamedQuery(name = "LogEntryEntity.findByIdlog", query = "SELECT l FROM LogEntryEntity l WHERE l.idlog = :idlog")
    , @NamedQuery(name = "LogEntryEntity.findByTimestamp", query = "SELECT l FROM LogEntryEntity l WHERE l.timestamp = :timestamp")
    , @NamedQuery(name = "LogEntryEntity.findByAffectedTable", query = "SELECT l FROM LogEntryEntity l WHERE l.affectedTable = :affectedTable")
    , @NamedQuery(name = "LogEntryEntity.findByMessage", query = "SELECT l FROM LogEntryEntity l WHERE l.message = :message")
    , @NamedQuery(name = "LogEntryEntity.findBySubmitter", query = "SELECT l FROM LogEntryEntity l WHERE l.fkeySubmitter = :submitter")
})
public class LogEntryEntity implements Serializable, ILogEntry {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer idlog;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date timestamp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "affected_table")
    private String affectedTable;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    private String message;
    @JoinColumn(name = "fkey_submitter", referencedColumnName = "idsubmitter")
    @ManyToOne
    private SubmitterEntity fkeySubmitter;

    public LogEntryEntity() {
    }

    public LogEntryEntity(Integer idlog) {
        this.idlog = idlog;
    }

    public LogEntryEntity(Integer idlog, Date timestamp, String affectedTable, String message) {
        this.idlog = idlog;
        this.timestamp = timestamp;
        this.affectedTable = affectedTable;
        this.message = message;
    }

    @Override
    public int getId() {
        return idlog;
    }

    public void setId(int idlog) {
        this.idlog = idlog;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getAffectedTable() {
        return affectedTable;
    }

    @Override
    public void setAffectedTable(String affectedTable) {
        this.affectedTable = affectedTable;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public ISubmitter getSubmitter() {
        return fkeySubmitter;
    }

    @Override
    public void setSubmitter(ISubmitter submitter) {
        this.fkeySubmitter = (SubmitterEntity)submitter;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idlog != null ? idlog.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LogEntryEntity)) {
            return false;
        }
        LogEntryEntity other = (LogEntryEntity) object;
        if ((this.idlog == null && other.idlog != null) || (this.idlog != null && !this.idlog.equals(other.idlog))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.LogEntryEntity[ idlog=" + idlog + " ]";
    }
    
}
