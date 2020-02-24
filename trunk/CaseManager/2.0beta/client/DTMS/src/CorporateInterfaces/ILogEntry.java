/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CorporateInterfaces;

import java.util.Date;

/**
 *
 * @author HS
 */
public interface ILogEntry {
    public Date getTimestamp();
    public String getAffectedTable();
    public String getMessage();
    public ISubmitter getSubmitter();
    
    public void setTimestamp(Date timestamp);
    public void setAffectedTable(String table);
    public void setMessage(String message);
    public void setSubmitter(ISubmitter submitter);
}
