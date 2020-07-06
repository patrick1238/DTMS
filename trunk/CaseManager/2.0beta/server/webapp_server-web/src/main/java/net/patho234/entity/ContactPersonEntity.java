/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import net.patho234.interfaces.IContactPerson;

/**
 *
 * @author HS
 */
@Entity
@Table(name = "contact_person")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContactPersonEntity.findAll", query = "SELECT c FROM ContactPersonEntity c")
    , @NamedQuery(name = "ContactPersonEntity.findByIdcontactPerson", query = "SELECT c FROM ContactPersonEntity c WHERE c.idcontactPerson = :idcontactPerson")
    , @NamedQuery(name = "ContactPersonEntity.findByTitle", query = "SELECT c FROM ContactPersonEntity c WHERE c.title = :title")
    , @NamedQuery(name = "ContactPersonEntity.findByForename", query = "SELECT c FROM ContactPersonEntity c WHERE c.forename = :forename")
    , @NamedQuery(name = "ContactPersonEntity.findBySurname", query = "SELECT c FROM ContactPersonEntity c WHERE c.surname = :surname")
    , @NamedQuery(name = "ContactPersonEntity.findByEmail", query = "SELECT c FROM ContactPersonEntity c WHERE c.email = :email")
    , @NamedQuery(name = "ContactPersonEntity.findByPhone", query = "SELECT c FROM ContactPersonEntity c WHERE c.phone = :phone")
    , @NamedQuery(name = "ContactPersonEntity.maxId", query = "SELECT MAX(c.idcontactPerson) FROM ContactPersonEntity c")
})
public class ContactPersonEntity implements Serializable, IContactPerson {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idcontact_person")
    private Integer idcontactPerson;
    @Size(max = 45)
    private String title;
    @Size(max = 45)
    private String forename;
    @Size(max = 45)
    private String surname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 45)
    private String email;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 45)
    private String phone;
    
    public ContactPersonEntity() {
    }

    public ContactPersonEntity(Integer idcontactPerson) {
        this.idcontactPerson = idcontactPerson;
    }

    @Override
    public Integer getId() {
        return idcontactPerson;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getForename() {
        return forename;
    }

    public void setId(Integer id){
        this.idcontactPerson=id;
    }
    
    @Override
    public void setForename(String forename) {
        this.forename = forename;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcontactPerson != null ? idcontactPerson.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContactPersonEntity)) {
            return false;
        }
        ContactPersonEntity other = (ContactPersonEntity) object;
        if ((this.idcontactPerson == null && other.idcontactPerson != null) || (this.idcontactPerson != null && !this.idcontactPerson.equals(other.idcontactPerson))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.ContactPersonEntity[ idcontactPerson=" + idcontactPerson + " ]";
    }
    
}
