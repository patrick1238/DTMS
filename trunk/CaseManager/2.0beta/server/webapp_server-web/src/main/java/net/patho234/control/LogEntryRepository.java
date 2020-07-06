/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.validation.ValidationException;
import net.patho234.entity.LogEntryEntity;
import net.patho234.interfaces.ILogEntry;
import net.patho234.interfaces.ISubmitter;

/**
 *
 * @author HS
 */
@Stateless
public class LogEntryRepository extends AbstractFacade<LogEntryEntity> implements LocalLogRepository {
    private EntityManager em;
    private String persistenceContext = "net.rehkind-mag_linfo_0.1";
    
    @EJB
    LocalSubmitterRepository submitterRepo;
    
    public LogEntryRepository(){
        super(LogEntryEntity.class);
        em=Persistence.createEntityManagerFactory(persistenceContext).createEntityManager();
        
    }
    
    @Override
    public ILogEntry getLogEntry(int logId){
        return (ILogEntry)find(logId);
    }
    
    @Override
    public List<ILogEntry> getLogEntries(){
        ArrayList<ILogEntry> returnList = new ArrayList<>();
        for( LogEntryEntity lee : findAll() ){
            returnList.add((ILogEntry)lee);
        }
        return returnList;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<ILogEntry> getLogEntriesForSubmitter(int submitterId) {
        Query qry=em.createNamedQuery("LogEntryEntity.findBySubmitter");
        ISubmitter subm = submitterRepo.getSubmitter(submitterId);
        
        qry.setParameter("submitter", subm);
        
        List<ILogEntry> res =  qry.getResultList();
        
        return res;
    }

    @Override
    public List<ILogEntry> getLogEntriesForTimeinterval(Date start, Date end) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateLogEntry(ILogEntry logEntryToUpdate) throws ValidationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean createLogEntry(ILogEntry logEntryToUpdate) throws ValidationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean deleteLogEntry(ILogEntry logEntryToDelete) throws ValidationException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
