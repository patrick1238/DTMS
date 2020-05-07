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
import javax.xml.bind.annotation.XmlRootElement;
import net.rehkind_mag.interfaces.IMetadataInt;
import net.rehkind_mag.interfaces.IService;

/**
 *
 * @author HS
 */
@Entity
@Table(name = "metadata_int")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MetadataIntEntity.findAll", query = "SELECT m FROM MetadataIntEntity m")
    , @NamedQuery(name = "MetadataIntEntity.findByFkeyService", query = "SELECT m FROM MetadataIntEntity m WHERE m.metadataIntEntityPK.fkeyService = :fkeyService")
    , @NamedQuery(name = "MetadataIntEntity.findByKey", query = "SELECT m FROM MetadataIntEntity m WHERE m.metadataIntEntityPK.metaKey = :metaKey")
    , @NamedQuery(name = "MetadataIntEntity.findByValue", query = "SELECT m FROM MetadataIntEntity m WHERE m.metaValue = :metaValue")})
public class MetadataIntEntity implements Serializable, IMetadataInt {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MetadataIntEntityPK metadataIntEntityPK;
    @Column( name ="meta_value" )
    private Integer metaValue;
    @JoinColumn(name = "fkey_service", referencedColumnName = "idservice", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ServiceEntity serviceEntity;

    public MetadataIntEntity() {
    }

    public MetadataIntEntity(MetadataIntEntityPK metadataIntEntityPK) {
        this.metadataIntEntityPK = metadataIntEntityPK;
    }

    public MetadataIntEntity(int fkeyService, String key) {
        this.metadataIntEntityPK = new MetadataIntEntityPK(fkeyService, key);
    }

    public MetadataIntEntityPK getMetadataIntEntityPK() {
        return metadataIntEntityPK;
    }

    public void setMetadataIntEntityPK(MetadataIntEntityPK metadataIntEntityPK) {
        this.metadataIntEntityPK = metadataIntEntityPK;
    }

    @Override
    public Integer getData() {
        return metaValue;
    }

    @Override
    public void setData(Integer value) {
        this.metaValue = value;
    }
    
    @Override
    public String getName() {
        return metadataIntEntityPK.getKey();
    }
    
    @Override
    public IService getService() {
        return (IService)serviceEntity;
    }    

    public void setServiceEntity(ServiceEntity serviceEntity) {
        this.serviceEntity = serviceEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (metadataIntEntityPK != null ? metadataIntEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetadataIntEntity)) {
            return false;
        }
        MetadataIntEntity other = (MetadataIntEntity) object;
        if ((this.metadataIntEntityPK == null && other.metadataIntEntityPK != null) || (this.metadataIntEntityPK != null && !this.metadataIntEntityPK.equals(other.metadataIntEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.MetadataIntEntity[ metadataIntEntityPK=" + metadataIntEntityPK + " ]";
    }
    

    @Override
    public METADATA_TYPE getType() {
        return METADATA_TYPE.INTEGER;
    }
    
}
