/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import net.patho234.interfaces.IService;
import net.patho234.interfaces.ISubmitter;

/**
 *
 * @author HS
 */
@Entity
@Table(name = "submitter")
@NamedQueries({
    @NamedQuery(name = "SubmitterEntity.findAll", query = "SELECT s FROM SubmitterEntity s")
    , @NamedQuery(name = "SubmitterEntity.findByIdsubmitter", query = "SELECT s FROM SubmitterEntity s WHERE s.idsubmitter = :idsubmitter")
})
public class SubmitterEntity implements Serializable, ISubmitter {

    @Id
    @Basic(optional = false)
    @NotNull
    private Integer idsubmitter;
    @Size(max = 45)
    private String surname;
    @Size(max = 45)
    private String forename;
    @Size(max = 45)
    private String title;
    @Size(max = 45)
    private String login;
    @Size(max = 45)
    private String password;
    @OneToMany(mappedBy = "fkeySubmitter")
    private List<LogEntryEntity> logEntryEntityList;
    
    private static final long serialVersionUID = 1L;
    

    public SubmitterEntity() {
    }

    public SubmitterEntity(Integer idsubmitter) {
        this.idsubmitter = idsubmitter;
    }

    @Override
    public int getId() {
        return idsubmitter;
    }

    public void setId(int idsubmitter) {
        this.idsubmitter = idsubmitter;
    }

    @Override
    public String getForename() {
        return forename;
    }
    
    @Override
    public void setForename(String forename) {
        this.forename = forename;
    }
    
    @Override
    public String getSurname() {
        return surname;
    }
    
    @Override
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    @Override
    public String getTitle() {
        return title;
    }
    
    @Override
    public void setTitle(String title) {
        this.title = title;
    }
    
        @Override
    public String getLogin() {
        return login;
    }
    
    @Override
    public void setLogin(String login) {
        this.login = login;
    }
    
        @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idsubmitter != null ? idsubmitter.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubmitterEntity)) {
            return false;
        }
        SubmitterEntity other = (SubmitterEntity) object;
        if ((this.idsubmitter == null && other.idsubmitter != null) || (this.idsubmitter != null && !this.idsubmitter.equals(other.idsubmitter))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.SupplierEntity[ id=" + idsubmitter + " ]";
    }

    @XmlTransient
    public List<LogEntryEntity> getLogEntryEntityList() {
        return logEntryEntityList;
    }

    public void setLogEntryEntityList(List<LogEntryEntity> logEntryEntityList) {
        this.logEntryEntityList = logEntryEntityList;
    }
}
