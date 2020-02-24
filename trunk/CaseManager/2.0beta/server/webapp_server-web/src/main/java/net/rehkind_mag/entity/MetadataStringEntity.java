/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import net.rehkind_mag.interfaces.IMetadataString;

/**
 *
 * @author HS
 */
@Entity
@Table(name = "metadata_string")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MetadataStringEntity.findAll", query = "SELECT m FROM MetadataStringEntity m")
    , @NamedQuery(name = "MetadataStringEntity.findByFkeyService", query = "SELECT m FROM MetadataStringEntity m WHERE m.metadataStringEntityPK.fkeyService = :fkeyService")
    , @NamedQuery(name = "MetadataStringEntity.findByKey", query = "SELECT m FROM MetadataStringEntity m WHERE m.metadataStringEntityPK.metaKey = :metaKey")
    , @NamedQuery(name = "MetadataStringEntity.findByValue", query = "SELECT m FROM MetadataStringEntity m WHERE m.metaValue = :metaValue")})
public class MetadataStringEntity implements Serializable, IMetadataString {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MetadataStringEntityPK metadataStringEntityPK;
    @Size(max = 100)
    @Column( name = "meta_value" )
    private String metaValue;
    @JoinColumn(name = "fkey_service", referencedColumnName = "idservice", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ServiceEntity serviceEntity;

    public MetadataStringEntity() {
    }

    public MetadataStringEntity(MetadataStringEntityPK metadataStringEntityPK) {
        this.metadataStringEntityPK = metadataStringEntityPK;
    }

    public MetadataStringEntity(int fkeyService, String key) {
        this.metadataStringEntityPK = new MetadataStringEntityPK(fkeyService, key);
    }

    public MetadataStringEntityPK getMetadataStringEntityPK() {
        return metadataStringEntityPK;
    }

    public void setMetadataStringEntityPK(MetadataStringEntityPK metadataStringEntityPK) {
        this.metadataStringEntityPK = metadataStringEntityPK;
    }

    @Override
    public String getData() {
        return metaValue;
    }

    @Override
    public void setData(String value) {
        this.metaValue = value;
    }
    
    @Override
    public String getName() {
        return this.metadataStringEntityPK.getKey();
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
        hash += (metadataStringEntityPK != null ? metadataStringEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetadataStringEntity)) {
            return false;
        }
        MetadataStringEntity other = (MetadataStringEntity) object;
        if ((this.metadataStringEntityPK == null && other.metadataStringEntityPK != null) || (this.metadataStringEntityPK != null && !this.metadataStringEntityPK.equals(other.metadataStringEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.MetadataStringEntity[ metadataStringEntityPK=" + metadataStringEntityPK + " ]";
    }
    
}
