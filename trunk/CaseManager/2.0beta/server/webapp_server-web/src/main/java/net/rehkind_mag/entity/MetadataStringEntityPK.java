/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author HS
 */
@Embeddable
public class MetadataStringEntityPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "fkey_service")
    private int fkeyService;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column( name = "meta_key" )
    private String metaKey;

    public MetadataStringEntityPK() {
    }

    public MetadataStringEntityPK(int fkeyService, String key) {
        this.fkeyService = fkeyService;
        this.metaKey = key;
    }

    public int getFkeyService() {
        return fkeyService;
    }

    public void setFkeyService(int fkeyService) {
        this.fkeyService = fkeyService;
    }

    public String getKey() {
        return metaKey;
    }

    public void setKey(String key) {
        this.metaKey = key;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) fkeyService;
        hash += (metaKey != null ? metaKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetadataStringEntityPK)) {
            return false;
        }
        MetadataStringEntityPK other = (MetadataStringEntityPK) object;
        if (this.fkeyService != other.fkeyService) {
            return false;
        }
        if ((this.metaKey == null && other.metaKey != null) || (this.metaKey != null && !this.metaKey.equals(other.metaKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.MetadataStringEntityPK[ fkeyService=" + fkeyService + ", key=" + metaKey + " ]";
    }
    
}
