/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entity;

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
import net.patho234.interfaces.IContactForClinic;
import net.patho234.interfaces.IContactPerson;

/**
 *
 * @author HS
 */
@Entity
@Table(name = "contact_for_clinic")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContactForClinicEntity.findAll", query = "SELECT c FROM ContactForClinicEntity c")
    , @NamedQuery(name = "ContactForClinicEntity.findByFkeyCfcClinic", query = "SELECT c FROM ContactForClinicEntity c WHERE c.contactForClinicEntityPK.fkeyCfcClinic = :fkeyCfcClinic")
    , @NamedQuery(name = "ContactForClinicEntity.findByFkeyCfcContact", query = "SELECT c FROM ContactForClinicEntity c WHERE c.contactForClinicEntityPK.fkeyCfcContact = :fkeyCfcContact")
    , @NamedQuery(name = "ContactForClinicEntity.findByNotes", query = "SELECT c FROM ContactForClinicEntity c WHERE c.notes = :notes")
    , @NamedQuery(name = "ContactForClinicEntity.insert", query = "SELECT c FROM ContactForClinicEntity c WHERE c.notes = :notes")
})
public class ContactForClinicEntity implements Serializable, IContactForClinic {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ContactForClinicEntityPK contactForClinicEntityPK;
    @Size(max = 400)
    private String notes;
    @JoinColumn(name = "fkey_cfc_clinic", referencedColumnName = "idclinic", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ClinicEntity clinicEntity;
    @JoinColumn(name = "fkey_cfc_contact", referencedColumnName = "idcontact_person", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ContactPersonEntity contactPersonEntity;

    public ContactForClinicEntity() {
    }

    public ContactForClinicEntity(ContactForClinicEntityPK contactForClinicEntityPK) {
        this.contactForClinicEntityPK = contactForClinicEntityPK;
    }

    public ContactForClinicEntity(int fkeyCfcClinic, int fkeyCfcContact) {
        this.contactForClinicEntityPK = new ContactForClinicEntityPK(fkeyCfcClinic, fkeyCfcContact);
    }

    public ContactForClinicEntityPK getContactForClinicEntityPK() {
        return contactForClinicEntityPK;
    }

    public void setContactForClinicEntityPK(ContactForClinicEntityPK contactForClinicEntityPK) {
        this.contactForClinicEntityPK = contactForClinicEntityPK;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public ClinicEntity getClinic() {
        return clinicEntity;
    }

    /**
     *
     * @param clinicEntity
     */
    public void setClinic(ClinicEntity clinicEntity) {
        this.clinicEntity = clinicEntity;
    }

    @Override
    public IContactPerson getContact() {
        return (IContactPerson)contactPersonEntity;
    }

    @Override
    public void setContact(IContactPerson contactPersonEntity) {
        this.contactPersonEntity = (ContactPersonEntity)contactPersonEntity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contactForClinicEntityPK != null ? contactForClinicEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContactForClinicEntity)) {
            return false;
        }
        ContactForClinicEntity other = (ContactForClinicEntity) object;
        if ((this.contactForClinicEntityPK == null && other.contactForClinicEntityPK != null) || (this.contactForClinicEntityPK != null && !this.contactForClinicEntityPK.equals(other.contactForClinicEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.ContactForClinicEntity[ contactForClinicEntityPK=" + contactForClinicEntityPK + " ]";
    }
    
}
