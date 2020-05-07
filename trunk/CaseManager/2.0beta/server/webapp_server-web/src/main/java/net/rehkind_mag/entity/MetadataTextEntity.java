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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import net.rehkind_mag.interfaces.IMetadataText;

/**
 *
 * @author HS
 */
@Entity
@Table(name = "metadata_text")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MetadataTextEntity.findAll", query = "SELECT m FROM MetadataTextEntity m")
    , @NamedQuery(name = "MetadataTextEntity.findByFkeyService", query = "SELECT m FROM MetadataTextEntity m WHERE m.metadataTextEntityPK.fkeyService = :fkeyService")
    , @NamedQuery(name = "MetadataTextEntity.findByKey", query = "SELECT m FROM MetadataTextEntity m WHERE m.metadataTextEntityPK.meta_key = :meta_key")
    , @NamedQuery(name = "MetadataTextEntity.findByValue", query = "SELECT m FROM MetadataTextEntity m WHERE m.meta_value = :meta_value")})
public class MetadataTextEntity implements Serializable, IMetadataText {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MetadataTextEntityPK metadataTextEntityPK;
    @Size(max = 1000)
    private String meta_value;
    @JoinColumn(name = "fkey_service", referencedColumnName = "idservice", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ServiceEntity serviceEntity;

    public MetadataTextEntity() {
    }

    public MetadataTextEntity(MetadataTextEntityPK metadataTextEntityPK) {
        this.metadataTextEntityPK = metadataTextEntityPK;
    }

    public MetadataTextEntity(int fkeyService, String key) {
        this.metadataTextEntityPK = new MetadataTextEntityPK(fkeyService, key);
    }

    public MetadataTextEntityPK getMetadataTextEntityPK() {
        return metadataTextEntityPK;
    }

    public void setMetadataTextEntityPK(MetadataTextEntityPK metadataTextEntityPK) {
        this.metadataTextEntityPK = metadataTextEntityPK;
    }

    @Override
    public String getData() {
        return meta_value;
    }
    @Override
    public void setData(String value) {
        this.meta_value = value;
    }
    @Override
    public String getName() {
        return this.metadataTextEntityPK.getMeta_key();
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
        hash += (metadataTextEntityPK != null ? metadataTextEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetadataTextEntity)) {
            return false;
        }
        MetadataTextEntity other = (MetadataTextEntity) object;
        if ((this.metadataTextEntityPK == null && other.metadataTextEntityPK != null) || (this.metadataTextEntityPK != null && !this.metadataTextEntityPK.equals(other.metadataTextEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.MetadataTextEntity[ metadataTextEntityPK=" + metadataTextEntityPK + " ]";
    }

    @Override
    public METADATA_TYPE getType() {
        return METADATA_TYPE.TEXT;
    }
}
