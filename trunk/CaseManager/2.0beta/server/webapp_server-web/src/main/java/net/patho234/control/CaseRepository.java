/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.control;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.validation.ValidationException;
import net.patho234.entity.CaseEntity;
import net.patho234.interfaces.ICase;
import net.patho234.interfaces.IClinic;

/**
 *
 * @author HS
 */
@Stateless
public class CaseRepository extends AbstractFacade<CaseEntity> implements LocalCaseRepository {
    private EntityManager em;
    private String persistenceContext = "net.rehkind-mag_linfo_0.1";
    
    public CaseRepository(){
        super(CaseEntity.class);
        em=Persistence.createEntityManagerFactory(persistenceContext).createEntityManager();
    }
    
    @Override
    public ICase getCase(int caseId){
        return (ICase)find(caseId);
    }
    
    @Override
    public List<ICase> getCases(){
        ArrayList<ICase> returnList = new ArrayList<>();
        for( CaseEntity ce : findAll() ){
            returnList.add((ICase)ce);
        }
        return returnList;
    }
    
    @Override
    public List<ICase> getCasesByServiceType(int serviceDefId){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<ICase> getCasesForClinic(int id) {
        IClinic clinic=new ClinicRepository().getClinic(id);
        Query caseForClinicQuery = em.createNamedQuery("CaseEntity.findByClinicId", ICase.class);
        caseForClinicQuery.setParameter("clinicId", clinic);
        ArrayList<ICase> caseList = new ArrayList<>();
        for( Object ce : caseForClinicQuery.getResultList() ){
            caseList.add((ICase)ce);
        }
        return caseList;
    }

    @Override
    public void updateCase(ICase caseToUpdate) throws ValidationException{
        validate((CaseEntity)caseToUpdate);
        
        em.persist((CaseEntity)caseToUpdate);
    }

    @Override
    public boolean createCase(ICase caseToCreate) {
        Query qry=em.createNamedQuery("CaseEntity.maxId");
        Integer maxId = (Integer)qry.getSingleResult();
        maxId+=1;
        ((CaseEntity)caseToCreate).setId(maxId);
        validate((CaseEntity)caseToCreate);
        
        try{
            em.persist((CaseEntity)caseToCreate);
        }catch(Exception ex){
            System.out.println("createCase() ERROR: ");
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }

    @Override
    public boolean deleteCase(ICase caseToDelete) {
        try{
            em.remove((CaseEntity)caseToDelete);
        }catch(Exception ex){
            System.out.println("deleteCase() ERROR: ");
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }

    @Override
    public ICase getCaseByCaseNumber(String caseNumber) {
        Query caseForClinicQuery = em.createNamedQuery("CaseEntity.findByCaseNumber", ICase.class);
        caseForClinicQuery.setParameter("caseNumber", caseNumber);
        ICase foundCase = null;
        try{ foundCase = (ICase)caseForClinicQuery.getSingleResult(); }catch(NullPointerException nullEx){ /* not found so return null */ }
        return foundCase;
    }
    
}
