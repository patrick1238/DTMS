/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import net.patho234.interfaces.IMetadataValue;

/**
 *
 * @author HS
 */
@Entity
@Table(name = "metadata_value")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MetadataValueEntity.findAll", query = "SELECT m FROM MetadataValueEntity m")
    , @NamedQuery(name = "MetadataValueEntity.findByMetadataValueId", query = "SELECT m FROM MetadataValueEntity m WHERE m.metadataValueId = :metadataValueId")
    , @NamedQuery(name = "MetadataValueEntity.findByValueType", query = "SELECT m FROM MetadataValueEntity m WHERE m.valueType = :valueType")
    , @NamedQuery(name = "MetadataValueEntity.findByKey", query = "SELECT m FROM MetadataValueEntity m WHERE m.meta_key = :key")
    , @NamedQuery(name = "MetadataValueEntity.findByDepricated", query = "SELECT m FROM MetadataValueEntity m WHERE m.depricated = :depricated")
    , @NamedQuery(name = "MetadataValueEntity.findByUnit", query = "SELECT m FROM MetadataValueEntity m WHERE m.unit = :unit")
    , @NamedQuery(name = "MetadataValueEntity.maxId", query = "SELECT MAX(mv.metadataValueId) FROM MetadataValueEntity mv")
})
public class MetadataValueEntity implements Serializable, IMetadataValue {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "metadata_value_id")
    private Integer metadataValueId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "value_type")
    private String valueType;
    @Size(max = 50)
    private String meta_key;
    private Short depricated;
    @Size(max = 10)
    private String unit;
    @ManyToMany(mappedBy = "metadataValueList")
    private List<ServiceDefinitionEntity> serviceDefinitionList;

    public MetadataValueEntity() {
    }

    public MetadataValueEntity(Integer metadataValueId) {
        this.metadataValueId = metadataValueId;
    }

    public MetadataValueEntity(Integer metadataValueId, String valueType) {
        this.metadataValueId = metadataValueId;
        this.valueType = valueType;
    }

    @Override
    public Integer getId() {
        return metadataValueId;
    }

    public void setId(Integer metadataValueId) {
        this.metadataValueId = metadataValueId;
    }

    @Override
    public String getValueType() {
        return valueType;
    }

    @Override
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    @Override
    public String getKey() {
        return meta_key;
    }

    @Override
    public void setKey(String key) {
        this.meta_key = key;
    }

    @Override
    public Boolean isDepricated() {
        Short no=0;
        return !(Objects.equals(depricated, no));
    }

    @Override
    public void setDepricated(Boolean depricated) {
        Short no=0; Short yes=1;
        
        this.depricated = (depricated) ? yes:no;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @XmlTransient
    public List<ServiceDefinitionEntity> getServiceDefinitionEntityList() {
        return serviceDefinitionList;
    }

    public void setServiceDefinitionEntityList(List<ServiceDefinitionEntity> serviceDefinitionList) {
        this.serviceDefinitionList = serviceDefinitionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (metadataValueId != null ? metadataValueId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetadataValueEntity)) {
            return false;
        }
        MetadataValueEntity other = (MetadataValueEntity) object;
        if ((this.metadataValueId == null && other.metadataValueId != null) || (this.metadataValueId != null && !this.metadataValueId.equals(other.metadataValueId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.MetadataValue[ metadataValueId=" + metadataValueId + " ]";
    }
    
}
