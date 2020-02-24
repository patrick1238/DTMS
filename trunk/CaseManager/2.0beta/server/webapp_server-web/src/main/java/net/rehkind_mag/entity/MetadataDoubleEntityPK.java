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
public class MetadataDoubleEntityPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "fkey_service")
    private int fkeyService;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    private String meta_key;

    public MetadataDoubleEntityPK() {
    }

    public MetadataDoubleEntityPK(int fkeyService, String key) {
        this.fkeyService = fkeyService;
        this.meta_key = key;
    }

    public int getFkeyService() {
        return fkeyService;
    }

    public void setFkeyService(int fkeyService) {
        this.fkeyService = fkeyService;
    }

    public String getKey() {
        return meta_key;
    }

    public void setKey(String key) {
        this.meta_key = key;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) fkeyService;
        hash += (meta_key != null ? meta_key.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetadataDoubleEntityPK)) {
            return false;
        }
        MetadataDoubleEntityPK other = (MetadataDoubleEntityPK) object;
        if (this.fkeyService != other.fkeyService) {
            return false;
        }
        if ((this.meta_key == null && other.meta_key != null) || (this.meta_key != null && !this.meta_key.equals(other.meta_key))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.MetadataDoubleEntityPK[ fkeyService=" + fkeyService + ", key=" + meta_key + " ]";
    }
    
}
