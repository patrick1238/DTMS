/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import net.patho234.interfaces.IMetadataUrl;
import net.patho234.interfaces.IService;

/**
 *
 * @author MLH
 */
@Entity
@Table(name = "metadata_url")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MetadataUrlEntity.findAll", query = "SELECT m FROM MetadataUrlEntity m")
    , @NamedQuery(name = "MetadataUrlEntity.findByFkeyService", query = "SELECT m FROM MetadataUrlEntity m WHERE m.metadataUrlEntityPK.fkeyService = :fkeyService")
    , @NamedQuery(name = "MetadataUrlEntity.findByMetaKey", query = "SELECT m FROM MetadataUrlEntity m WHERE m.metadataUrlEntityPK.metaKey = :metaKey")
    , @NamedQuery(name = "MetadataUrlEntity.findByMetaValue", query = "SELECT m FROM MetadataUrlEntity m WHERE m.metaValue = :metaValue")})
public class MetadataUrlEntity implements Serializable, IMetadataUrl {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MetadataUrlEntityPK metadataUrlEntityPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 200)
    @Column(name = "meta_value")
    private String metaValue;
    @JoinColumn(name = "fkey_service", referencedColumnName = "idservice", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ServiceEntity serviceEntity;
    
    public MetadataUrlEntity() {
    }

    public MetadataUrlEntity(MetadataUrlEntityPK metadataUrlEntityPK) {
        this.metadataUrlEntityPK = metadataUrlEntityPK;
    }

    public MetadataUrlEntity(MetadataUrlEntityPK metadataUrlEntityPK, String metaValue) {
        this.metadataUrlEntityPK = metadataUrlEntityPK;
        this.metaValue = metaValue;
    }

    public MetadataUrlEntity(int fkeyService, String metaKey) {
        this.metadataUrlEntityPK = new MetadataUrlEntityPK(fkeyService, metaKey);
    }

    public MetadataUrlEntityPK getMetadataUrlEntityPK() {
        return metadataUrlEntityPK;
    }

    public void setMetadataUrlEntityPK(MetadataUrlEntityPK metadataUrlEntityPK) {
        this.metadataUrlEntityPK = metadataUrlEntityPK;
    }

    public String getValue() {
        return metaValue;
    }

    public void setMetaValue(String metaValue) {
        this.metaValue = metaValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (metadataUrlEntityPK != null ? metadataUrlEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetadataUrlEntity)) {
            return false;
        }
        MetadataUrlEntity other = (MetadataUrlEntity) object;
        if ((this.metadataUrlEntityPK == null && other.metadataUrlEntityPK != null) || (this.metadataUrlEntityPK != null && !this.metadataUrlEntityPK.equals(other.metadataUrlEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.MetadataUrlEntity[ metadataUrlEntityPK=" + metadataUrlEntityPK + " ]";
    }

    @Override
    public ServiceEntity getService() {
        return serviceEntity;
    }

    @Override
    public String getData() {
        return this.metaValue;
    }

    @Override
    public void setData(String newData) {
        metaValue = newData;
    }

    @Override
    public String getName() {
        return this.metadataUrlEntityPK.getMetaKey();
    }

    @Override
    public METADATA_TYPE getType() {
        return METADATA_TYPE.URL;
    }
}
