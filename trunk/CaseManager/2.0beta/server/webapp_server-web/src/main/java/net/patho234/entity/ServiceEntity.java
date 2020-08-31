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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import net.patho234.interfaces.ICase;
import net.patho234.interfaces.IMetadata;
import net.patho234.interfaces.IMetadataDouble;
import net.patho234.interfaces.IMetadataInt;
import net.patho234.interfaces.IMetadataString;
import net.patho234.interfaces.IMetadataText;
import net.patho234.interfaces.IService;
import net.patho234.interfaces.IServiceDefinition;

/**
 *
 * @author HS
 */
@Entity
@Table(name = "service")
@NamedQueries({
    @NamedQuery(name = "ServiceEntity.findAll", query = "SELECT s FROM ServiceEntity s")
    , @NamedQuery(name = "ServiceEntity.findByIdservice", query = "SELECT s FROM ServiceEntity s WHERE s.idservice = :idservice")
    , @NamedQuery(name = "ServiceEntity.findAllForCase",  query = "SELECT s FROM ServiceEntity s WHERE s.fkeyCase = :idcase")
    , @NamedQuery(name = "ServiceEntity.findAllForDefinition",  query = "SELECT s FROM ServiceEntity s WHERE s.fkeyServiceDefinition = :iddefinition")
    , @NamedQuery(name = "ServiceEntity.maxId", query = "SELECT MAX(s.idservice) FROM ServiceEntity s")
})
public class ServiceEntity implements Serializable, IService {
    
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer idservice;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceEntity")
    private List<MetadataDoubleEntity> metadataDoubleEntityList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceEntity")
    private List<MetadataTextEntity> metadataTextEntityList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceEntity")
    private List<MetadataIntEntity> metadataIntEntityList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceEntity")
    private List<MetadataStringEntity> metadataStringEntityList;


    private static final long serialVersionUID = 1L;
    @JoinColumn(name = "fkey_case", referencedColumnName = "idcase")
    @ManyToOne
    private CaseEntity fkeyCase;
    @JoinColumn(name = "fkey_service_definition", referencedColumnName = "idservice_definition")
    @ManyToOne
    private ServiceDefinitionEntity fkeyServiceDefinition;
    
    // TODO: add submitter
    
    

    public ServiceEntity() {
    }

    public ServiceEntity(Integer idservice) {
        this.idservice = idservice;
    }

    public void setId(Integer idservice) {
        this.idservice = idservice;
    }

    @Override
    public IServiceDefinition getServiceDefinition() {
        return this.fkeyServiceDefinition;
    }
    
    @Override
    public void setServiceDefinition(IServiceDefinition fkeyServiceDefinition) {
        this.fkeyServiceDefinition = (ServiceDefinitionEntity)fkeyServiceDefinition;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idservice != null ? idservice.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiceEntity)) {
            return false;
        }
        ServiceEntity other = (ServiceEntity) object;
        if ((this.idservice == null && other.idservice != null) || (this.idservice != null && !this.idservice.equals(other.idservice))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.ServiceEntity[ idservice=" + idservice + " ]";
    }

    @Override
    public Integer getId() {
        return this.idservice;
    }

    @Override
    public ICase getCase() {
        return (ICase) this.fkeyCase;
    }
    
    @Override
    public void setCase(ICase caseValue) {
        this.fkeyCase = (CaseEntity)caseValue;
    }

    @XmlTransient
    public List<MetadataDoubleEntity> getMetadataDoubleEntityList() {
        return metadataDoubleEntityList;
    }

    public void setMetadataDoubleEntityList(List<MetadataDoubleEntity> metadataDoubleEntityList) {
        this.metadataDoubleEntityList = metadataDoubleEntityList;
    }

    @XmlTransient
    public List<MetadataTextEntity> getMetadataTextEntityList() {
        return metadataTextEntityList;
    }

    public void setMetadataTextEntityList(List<MetadataTextEntity> metadataTextEntityList) {
        this.metadataTextEntityList = metadataTextEntityList;
    }

    @XmlTransient
    public List<MetadataIntEntity> getMetadataIntEntityList() {
        return metadataIntEntityList;
    }

    public void setMetadataIntEntityList(List<MetadataIntEntity> metadataIntEntityList) {
        this.metadataIntEntityList = metadataIntEntityList;
    }

    @XmlTransient
    public List<MetadataStringEntity> getMetadataStringEntityList() {
        return metadataStringEntityList;
    }

    public void setMetadataStringEntityList(List<MetadataStringEntity> metadataStringEntityList) {
        this.metadataStringEntityList = metadataStringEntityList;
    }
    
    @Override
    public List<IMetadata> getMetadata(){
        List<IMetadata> returnList = new ArrayList<>();
        
        if(metadataIntEntityList!=null){
            for ( IMetadataInt mdi : metadataIntEntityList){ returnList.add((IMetadata)mdi); }
        }
        if(metadataDoubleEntityList!=null){
            for ( IMetadataDouble mdd : metadataDoubleEntityList){ returnList.add((IMetadata)mdd); }
        }
        if(metadataStringEntityList!=null){
            for ( IMetadataString mds : metadataStringEntityList){ returnList.add((IMetadata)mds); } 
        }
        if(metadataTextEntityList!=null){
            for ( IMetadataText mdt : metadataTextEntityList){ returnList.add((IMetadata)mdt); }
        }
        
        return returnList;
    }
    
}
