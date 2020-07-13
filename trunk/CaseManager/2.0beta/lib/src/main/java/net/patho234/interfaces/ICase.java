/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.interfaces;

import java.util.Date;
import java.util.List;

/**
 *
 * @author HS
 */
public interface ICase extends IClinicReceiver{
    public Integer getId();
    
    public String getCaseNumber();
    public IClinic getClinic();
    public List<IService> getServices();
    
    public String getDiagnose();
    public Date getEntryDate();
    public ISubmitter getSubmitter();
    
    public void setCaseNumber(String caseNumber);
    @Override
    public void setClinic(IClinic clinic);
    
    public void setDiagnose(String diagnose);
    public void setEntryDate(Date date);
    public void setSubmitter(ISubmitter submitter);
}
