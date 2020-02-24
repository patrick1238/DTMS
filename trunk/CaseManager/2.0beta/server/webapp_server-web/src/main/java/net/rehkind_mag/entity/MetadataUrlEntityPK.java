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
 * @author MLH
 */
@Embeddable
public class MetadataUrlEntityPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "fkey_service")
    private int fkeyService;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "meta_key")
    private String metaKey;

    public MetadataUrlEntityPK() {
    }

    public MetadataUrlEntityPK(int fkeyService, String metaKey) {
        this.fkeyService = fkeyService;
        this.metaKey = metaKey;
    }

    public int getFkeyService() {
        return fkeyService;
    }

    public void setFkeyService(int fkeyService) {
        this.fkeyService = fkeyService;
    }

    public String getMetaKey() {
        return metaKey;
    }

    public void setMetaKey(String metaKey) {
        this.metaKey = metaKey;
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
        if (!(object instanceof MetadataUrlEntityPK)) {
            return false;
        }
        MetadataUrlEntityPK other = (MetadataUrlEntityPK) object;
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
        return "net.rehkind_mag.entity.MetadataUrlEntityPK[ fkeyService=" + fkeyService + ", metaKey=" + metaKey + " ]";
    }
    
}
