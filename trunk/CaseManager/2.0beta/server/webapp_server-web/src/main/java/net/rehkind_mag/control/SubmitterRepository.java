/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.control;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.validation.ValidationException;
import net.rehkind_mag.entity.SubmitterEntity;
import org.netbeans.rest.application.config.AppPersistenceManager;
import net.rehkind_mag.interfaces.ISubmitter;


/**
 *
 * @author HS
 */
@Stateless
public class SubmitterRepository extends AbstractFacade<SubmitterEntity> implements LocalSubmitterRepository {
    private EntityManager em;
    private String persistenceContext = "net.rehkind-mag_linfo_0.1";
    public SubmitterRepository(){
        super(SubmitterEntity.class);
        em=AppPersistenceManager.requestEntityManager(persistenceContext);
    }
    
    @Override
    public ISubmitter getSubmitter(int submitterId){
        return (ISubmitter)find(submitterId);
    }
    
    @Override
    public List<ISubmitter> getSubmitters(){
        ArrayList<ISubmitter> returnList = new ArrayList<>();
        for( SubmitterEntity ce : findAll() ){
            returnList.add((ISubmitter)ce);
        }
        return returnList;
    }
    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void updateSubmitter(ISubmitter submitterToUpdate) throws ValidationException {
        validate((SubmitterEntity)submitterToUpdate);
        
        em.persist((SubmitterEntity)submitterToUpdate);
    }
    
    @Override
    public boolean submitterHasAccess(String login, String pwd){
        if( login==null || pwd==null ){ return false; }
        
        for( ISubmitter s : getSubmitters() ){
            if( s.getLogin().equals(login) && s.getPassword().equals(pwd) ){ return true; }
        }
        return false;
    }
}
