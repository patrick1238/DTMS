/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import net.rehkind_mag.interfaces.ICase;
import net.rehkind_mag.interfaces.IClinic;
import net.rehkind_mag.interfaces.IService;
import net.rehkind_mag.interfaces.ISubmitter;

/**
 *
 * @author HS
 */
@Entity
@Table(name = "clinic_case")
@NamedQueries({
    @NamedQuery(name = "CaseEntity.findAll", query = "SELECT c FROM CaseEntity c")
    , @NamedQuery(name = "CaseEntity.findByIdcase", query = "SELECT c FROM CaseEntity c WHERE c.idcase = :idcase")
    , @NamedQuery(name = "CaseEntity.findByCaseNumber", query = "SELECT c FROM CaseEntity c WHERE c.caseNumber = :caseNumber")
    , @NamedQuery(name = "CaseEntity.findByClinicId", query = "SELECT c FROM CaseEntity c WHERE c.fkeyCaseClinic = :clinicId")
    , @NamedQuery(name = "CaseEntity.maxId", query = "SELECT MAX(c.idcase) FROM CaseEntity c")
})
public class CaseEntity implements Serializable, ICase {
    @Id
    @Basic(optional = false)
    @NotNull(message="id for case can not be NULL.")
    private Integer idcase;
    @JoinColumn(name = "fkey_case_clinic", referencedColumnName = "idclinic")
    @ManyToOne(optional = false)
    @NotNull(message="clinic for case can not be NULL.")
    private ClinicEntity fkeyCaseClinic;
    
    @JoinColumn(name = "fkey_case_submitter", referencedColumnName = "idsubmitter")
    @ManyToOne(optional = false)
    @NotNull(message="submitter for case can not be NULL.")
    private SubmitterEntity fkeySubmitter;
    
    
    @Column(name = "case_entry_date")
    @Temporal(TemporalType.DATE)
    private Date caseEntryDate;
    @Size(max = 100)
    @Column(name = "case_diagnosis")
    private String caseDiagnosis;
    
    @OneToMany(mappedBy = "fkeyCase")
    private List<ServiceEntity> serviceEntityList;

    private static final long serialVersionUID = 1L;
    @NotNull
    @Column(name = "case_number")
    @Size(min=5, max = 35, message="Case number string is restricted to a length of 5 to 35 characters.")
    private String caseNumber;

    public CaseEntity() {
    }

    public CaseEntity(Integer idcase) {
        this.idcase = idcase;
    }

    @Override
    public int getId() {
        return idcase;
    }

    public void setId(Integer idcase) {
        this.idcase = idcase;
    }

    @Override
    public String getCaseNumber() {
        return caseNumber;
    }

    @Override
    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcase != null ? idcase.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CaseEntity)) {
            return false;
        }
        CaseEntity other = (CaseEntity) object;
        if ((this.idcase == null && other.idcase != null) || (this.idcase != null && !this.idcase.equals(other.idcase))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.rehkind_mag.entity.CaseEntity[ idcase=" + idcase + " ]";
    }
    
    @Override
    public List<IService> getServices() {
        List<IService> serviceList = new ArrayList<>();
        for (ServiceEntity se : serviceEntityList){
            serviceList.add((IService) se);
        }
        return serviceList;
    }

    public List<ServiceEntity> getServiceEntityList() {
        return serviceEntityList;
    }

    public void setServiceEntityList(List<ServiceEntity> serviceEntityList) {
        this.serviceEntityList = serviceEntityList;
    }

    @Override
    public IClinic getClinic() {
        return (IClinic) fkeyCaseClinic;
    }

    @Override
    public void setClinic(IClinic fkeyCaseClinic) {
        this.fkeyCaseClinic = (ClinicEntity)fkeyCaseClinic;
    }

    @Override
    public String getDiagnose() {
        return this.caseDiagnosis;
    }

    @Override
    public Date getEntryDate() {
        return caseEntryDate;
    }

    @Override
    public ISubmitter getSubmitter() {
        return fkeySubmitter;
    }

    @Override
    public void setDiagnose(String diagnose) {
        this.caseDiagnosis=diagnose;
    }

    @Override
    public void setEntryDate(Date date) {
        this.caseEntryDate=date;
    }


    public Date getCaseEntryDate() {
        return caseEntryDate;
    }

    public void setCaseEntryDate(Date caseEntryDate) {
        this.caseEntryDate = caseEntryDate;
    }

    public String getCaseDiagnosis() {
        return caseDiagnosis;
    }

    public void setCaseDiagnosis(String caseDiagnosis) {
        this.caseDiagnosis = caseDiagnosis;
    }

    @Override
    public void setSubmitter(ISubmitter submitter) {
        this.fkeySubmitter = (SubmitterEntity)submitter;
    }

}
