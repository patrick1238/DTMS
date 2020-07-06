/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.control;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.validation.ValidationException;
import net.patho234.interfaces.ILogEntry;

/**
 *
 * @author HS
 */
@Local
public interface LocalLogRepository {
    public ILogEntry getLogEntry(int logId);
    public List<ILogEntry> getLogEntries();
    public List<ILogEntry> getLogEntriesForSubmitter(int submitterId);

    public List<ILogEntry> getLogEntriesForTimeinterval(Date start, Date end);

    public void updateLogEntry(ILogEntry logEntryToUpdate) throws ValidationException;
    public boolean createLogEntry(ILogEntry logEntryToUpdate) throws ValidationException;
    public boolean deleteLogEntry(ILogEntry logEntryToUpdate) throws ValidationException;
    
}
