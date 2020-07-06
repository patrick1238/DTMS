/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.interfaces;

import java.util.Date;

/**
 *
 * @author HS
 */
public interface ILogEntry {
    public int getId();
    public Date getTimestamp();
    public String getAffectedTable();
    public String getMessage();
    public ISubmitter getSubmitter();
    
    public void setTimestamp(Date timestamp);
    public void setAffectedTable(String table);
    public void setMessage(String message);
    public void setSubmitter(ISubmitter submitter);
}
