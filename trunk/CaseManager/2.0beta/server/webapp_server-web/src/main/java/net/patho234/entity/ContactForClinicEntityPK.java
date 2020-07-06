/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author MLH
 */
@Embeddable
public class ContactForClinicEntityPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "fkey_cfc_clinic")
    private int fkeyCfcClinic;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fkey_cfc_contact")
    private int fkeyCfcContact;

    public ContactForClinicEntityPK() {
    }

    public ContactForClinicEntityPK(int fkeyCfcClinic, int fkeyCfcContact) {
        this.fkeyCfcClinic = fkeyCfcClinic;
        this.fkeyCfcContact = fkeyCfcContact;
    }

    public int getFkeyCfcClinic() {
        return fkeyCfcClinic;
    }

    public void setFkeyCfcClinic(int fkeyCfcClinic) {
        this.fkeyCfcClinic = fkeyCfcClinic;
    }

    public int getFkeyCfcContact() {
        return fkeyCfcContact;
    }

    public void setFkeyCfcContact(int fkeyCfcContact) {
        this.fkeyCfcContact = fkeyCfcContact;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) fkeyCfcClinic;
        hash += (int) fkeyCfcContact;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContactForClinicEntityPK)) {
            return false;
        }
        ContactForClinicEntityPK other = (ContactForClinicEntityPK) object;
        if (this.fkeyCfcClinic != other.fkeyCfcClinic) {
            return false;
        }
        if (this.fkeyCfcContact != other.fkeyCfcContact) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.ContactForClinicEntityPK[ fkeyCfcClinic=" + fkeyCfcClinic + ", fkeyCfcContact=" + fkeyCfcContact + " ]";
    }
    
}
