/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import net.rehkind_mag.interfaces.IMetadataDouble;

/**
 *
 * @author HS
 */
@Entity
@Table(name = "metadata_double")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MetadataDoubleEntity.findAll", query = "SELECT m FROM MetadataDoubleEntity m")
    , @NamedQuery(name = "MetadataDoubleEntity.findByFkeyService", query = "SELECT m FROM MetadataDoubleEntity m WHERE m.metadataDoubleEntityPK.fkeyService = :fkeyService")
    , @NamedQuery(name = "MetadataDoubleEntity.findByKey", query = "SELECT m FROM MetadataDoubleEntity m WHERE m.metadataDoubleEntityPK.meta_key = :meta_key")
    , @NamedQuery(name = "MetadataDoubleEntity.findByValue", query = "SELECT m FROM MetadataDoubleEntity m WHERE m.meta_value = :meta_value")})
public class MetadataDoubleEntity implements Serializable, IMetadataDouble {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MetadataDoubleEntityPK metadataDoubleEntityPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Double meta_value;
    @JoinColumn(name = "fkey_service", referencedColumnName = "idservice", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ServiceEntity serviceEntity;

    public MetadataDoubleEntity() {
    }

    public MetadataDoubleEntity(MetadataDoubleEntityPK metadataDoubleEntityPK) {
        this.metadataDoubleEntityPK = metadataDoubleEntityPK;
    }

    public MetadataDoubleEntity(int fkeyService, String key) {
        this.metadataDoubleEntityPK = new MetadataDoubleEntityPK(fkeyService, key);
    }

    public MetadataDoubleEntityPK getMetadataDoubleEntityPK() {
        return metadataDoubleEntityPK;
    }

    public void setMetadataDoubleEntityPK(MetadataDoubleEntityPK metadataDoubleEntityPK) {
        this.metadataDoubleEntityPK = metadataDoubleEntityPK;
    }

    @Override
    public Double getData() {
        return meta_value;
    }

    @Override
    public void setData(Double value) {
        this.meta_value = value;
    }

    @Override
    public ServiceEntity getService() {
        return serviceEntity;
    }

    public void setServiceEntity(ServiceEntity serviceEntity) {
        this.serviceEntity = serviceEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (metadataDoubleEntityPK != null ? metadataDoubleEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetadataDoubleEntity)) {
            return false;
        }
        MetadataDoubleEntity other = (MetadataDoubleEntity) object;
        if ((this.metadataDoubleEntityPK == null && other.metadataDoubleEntityPK != null) || (this.metadataDoubleEntityPK != null && !this.metadataDoubleEntityPK.equals(other.metadataDoubleEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.MetadataDoubleEntity[ metadataDoubleEntityPK=" + metadataDoubleEntityPK + " ]";
    }

    @Override
    public String getName() {
        return this.metadataDoubleEntityPK.getKey();
    }
    
}
