/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import net.rehkind_mag.interfaces.IClinic;
import net.rehkind_mag.interfaces.IContactForClinic;
/**
 *
 * @author HS
 */
@Entity
@Table(name = "clinic")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClinicEntity.findAll", query = "SELECT c FROM ClinicEntity c")
    , @NamedQuery(name = "ClinicEntity.findByIdclinic", query = "SELECT c FROM ClinicEntity c WHERE c.idclinic = :idclinic")
    , @NamedQuery(name = "ClinicEntity.findByName", query = "SELECT c FROM ClinicEntity c WHERE c.name = :name")
    , @NamedQuery(name = "ClinicEntity.findByCity", query = "SELECT c FROM ClinicEntity c WHERE c.city = :city")
    , @NamedQuery(name = "ClinicEntity.findByStreet", query = "SELECT c FROM ClinicEntity c WHERE c.street = :street")
    , @NamedQuery(name = "ClinicEntity.findByZipcode", query = "SELECT c FROM ClinicEntity c WHERE c.zipcode = :zipcode")
    , @NamedQuery(name = "ClinicEntity.maxId", query = "SELECT MAX(c.idclinic) FROM ClinicEntity c")
})
public class ClinicEntity implements Serializable, IClinic {

    @Id
    @Basic(optional = false)
    @NotNull
    private Integer idclinic;
    @Size(max = 100)
    @NotNull
    private String name;
    @Size(max = 25)
    private String city;
    @Size(max = 45)
    private String street;
    @Size(max = 10)
    private String zipcode;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clinicEntity")
    private List<ContactForClinicEntity> contactForClinicEntityList;
    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkeyCaseClinic")
    private List<CaseEntity> caseEntityList;

    public ClinicEntity() {
    }

    public ClinicEntity(Integer idclinic) {
        this.idclinic = idclinic;
    }

    @Override
    public int getId() {
        return idclinic;
    }

    @Override
    public void setId(int idclinic) {
        this.idclinic = idclinic;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String getZipCode() {
        return zipcode;
    }

    @Override
    public void setZipCode(String zipcode) {
        this.zipcode = zipcode;
    }

    @XmlTransient
    public List<CaseEntity> getCaseEntityList() {
        return caseEntityList;
    }
    
    @XmlTransient
    @Override
    public List<IContactForClinic> getContactsForClinicList() {
        ArrayList<IContactForClinic> resList = new ArrayList<>();
        if(contactForClinicEntityList==null){
            return resList;
        }
        for ( ContactForClinicEntity cfce : contactForClinicEntityList){
            resList.add( (IContactForClinic)cfce );
        }
        return resList;
    }

    public void setCaseEntityList(List<CaseEntity> caseEntityList) {
        this.caseEntityList = caseEntityList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idclinic != null ? idclinic.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClinicEntity)) {
            return false;
        }
        ClinicEntity other = (ClinicEntity) object;
        if ((this.idclinic == null && other.idclinic != null) || (this.idclinic != null && !this.idclinic.equals(other.idclinic))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.ClinicEntity[ idclinic=" + idclinic + " ]";
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
