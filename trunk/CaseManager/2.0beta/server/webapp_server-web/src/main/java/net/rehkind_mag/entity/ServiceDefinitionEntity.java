/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import net.rehkind_mag.interfaces.IMetadataValue;
import net.rehkind_mag.interfaces.IService;
import net.rehkind_mag.interfaces.IServiceDefinition;

/**
 *
 * @author HS
 */
@Entity
@Table(name = "service_definition")
@NamedQueries({
    @NamedQuery(name = "ServiceDefinitionEntity.findAll", query = "SELECT s FROM ServiceDefinitionEntity s")
    , @NamedQuery(name = "ServiceDefinitionEntity.findByServiceDefinition", query = "SELECT s FROM ServiceDefinitionEntity s WHERE s.idServiceDefinition = :idserviceDefinition")
    , @NamedQuery(name = "ServiceDefinitionEntity.findByName", query = "SELECT s FROM ServiceDefinitionEntity s WHERE s.name = :name")
    , @NamedQuery(name = "ServiceDefinitionEntity.findByDescription", query = "SELECT s FROM ServiceDefinitionEntity s WHERE s.description = :description")})
public class ServiceDefinitionEntity implements Serializable, IServiceDefinition {

    @Size(max = 100)
    private String name;
    @Size(max = 400)
    private String description;
    @OneToMany(mappedBy = "parentDefinitionId")
    private List<ServiceDefinitionEntity> serviceDefinitionEntityList;
    @JoinColumn(name = "parent_definition_id", referencedColumnName = "idservice_definition")
    @ManyToOne
    private ServiceDefinitionEntity parentDefinitionId;

    @JoinTable(name = "metadata_defaults", joinColumns = {
        @JoinColumn(name = "fkey_service_definition", referencedColumnName = "idservice_definition")}, inverseJoinColumns = {
        @JoinColumn(name = "fkey_default_value", referencedColumnName = "metadata_value_id")})
    @ManyToMany
    private List<MetadataValueEntity> metadataValueList;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idservice_definition")
    private Integer idServiceDefinition;
    @OneToMany(mappedBy = "fkeyServiceDefinition")
    private List<ServiceEntity> serviceEntityList;
    
    public ServiceDefinitionEntity() {
    }

    public ServiceDefinitionEntity(Integer idServiceDefinition) {
        this.idServiceDefinition = idServiceDefinition;
    }

    @Override
    public int getId() {
        return idServiceDefinition;
    }

    public void setServiceId(Integer id) {
        this.idServiceDefinition = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public List<IService> getServices() {
        List<IService> returnList = new ArrayList<>();
        for(ServiceEntity se : serviceEntityList){ returnList.add((IService)se); }
        return returnList;
    }

    public void setServices(List<ServiceEntity> serviceEntityList) {
        this.serviceEntityList = serviceEntityList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idServiceDefinition != null ? idServiceDefinition.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiceDefinitionEntity)) {
            return false;
        }
        ServiceDefinitionEntity other = (ServiceDefinitionEntity) object;
        if ((this.idServiceDefinition == null && other.idServiceDefinition != null) || (this.idServiceDefinition != null && !this.idServiceDefinition.equals(other.idServiceDefinition))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.ServiceDefinitionEntity[ idserviceDefinition=" + idServiceDefinition + " ]";
    }

    @XmlTransient
    @Override
    public HashMap<IServiceDefinition, List<IMetadataValue>> getMetadataValues() {
        List<IMetadataValue> resList = new ArrayList<>();
        HashMap<IServiceDefinition, List<IMetadataValue>> resMap = new HashMap<>();
        if(getParentDefinition()!=null){
            for(Entry<IServiceDefinition, List<IMetadataValue>> e : getParentDefinition().getMetadataValues().entrySet())
            {
                resMap.put(e.getKey(), e.getValue());
            }
        }
        for( MetadataValueEntity mve : metadataValueList ){
            resList.add((IMetadataValue)mve);
        }
        resMap.put(this, resList);
        return resMap;
    }

    public void setMetadataValueList(List<IMetadataValue> metadataValueList) {
        List<MetadataValueEntity> list = new ArrayList<>();
        for( IMetadataValue imv : metadataValueList ){
            list.add((MetadataValueEntity)imv);
        }
        this.metadataValueList = list;
    }

    @Override
    public IServiceDefinition getParentDefinition() {
        IServiceDefinition parent=(IServiceDefinition)parentDefinitionId;
        return parent;
    }

    @Override
    public void setParentDefinition(IServiceDefinition parentDef) {
        try{
            ServiceDefinitionEntity newParent = (ServiceDefinitionEntity)parentDef;
            this.parentDefinitionId=newParent;
        }catch( ClassCastException ex ){
            Logger.getGlobal().warning("Could not set new parent Definition: currently only ServiceDefinitionEntity objects are supported");
        }
        Logger.getGlobal().warning("TODO: setParentDefinition(IServiceDefinition parentDef) - if no cast to entity possible load by getId()");
    }

    @XmlTransient
    public List<ServiceDefinitionEntity> getServiceDefinitionEntityList() {
        return serviceDefinitionEntityList;
    }

    public void setServiceDefinitionEntityList(List<ServiceDefinitionEntity> serviceDefinitionEntityList) {
        this.serviceDefinitionEntityList = serviceDefinitionEntityList;
    }

    public ServiceDefinitionEntity getParentDefinitionId() {
        return parentDefinitionId;
    }

    public void setParentDefinitionId(ServiceDefinitionEntity parentDefinitionId) {
        this.parentDefinitionId = parentDefinitionId;
    }
    
}
