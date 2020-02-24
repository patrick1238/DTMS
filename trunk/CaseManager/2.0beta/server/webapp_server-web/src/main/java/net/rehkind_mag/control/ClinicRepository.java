/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.control;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import net.rehkind_mag.entity.ClinicEntity;
import net.rehkind_mag.interfaces.IClinic;
import net.rehkind_mag.interfaces.IContactForClinic;
import org.netbeans.rest.application.config.AppPersistenceManager;


/**
 *
 * @author HS
 */
@Stateless
public class ClinicRepository extends AbstractFacade<ClinicEntity> implements LocalClinicRepository{
    private EntityManager em;
    private String persistenceContext = "net.rehkind-mag_linfo_0.1";
    public ClinicRepository(){
        super(ClinicEntity.class);
        em=AppPersistenceManager.requestEntityManager(persistenceContext);
    }
    
    @Override
    public IClinic getClinic(int clinicId){
        return (IClinic)find(clinicId);
    }
    
    @Override
    public List<IClinic> getClinics(){
        ArrayList<IClinic> returnList = new ArrayList<>();
        for( ClinicEntity ce : findAll() ){
            returnList.add((IClinic)ce);
        }
        return returnList;
    }
    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<IContactForClinic> getContactsForClinic(IClinic clinic) {
        List<IContactForClinic> contacts = clinic.getContactsForClinicList();
        Query qry = em.createNamedQuery("ContactForClinicEntity.findByFkeyCfcClinic");
        qry.setParameter("fkeyCfcClinic", clinic.getId());
        for(Object cfce : qry.getResultList()){
            contacts.add((IContactForClinic)cfce);
        }
        return contacts;
    }

    @Override
    public void updateClinic(IClinic clinicToUpdate) {
        em.persist(clinicToUpdate);
    }

    @Override
    public boolean createClinic(IClinic clinicToCreate) {
        Query qry=em.createNamedQuery("ClinicEntity.maxId");
        Integer maxId = (Integer)qry.getSingleResult();
        maxId+=1;
        ((ClinicEntity)clinicToCreate).setId(maxId);
        validate((ClinicEntity)clinicToCreate);
        
        try{
            em.persist((ClinicEntity)clinicToCreate);
        }catch(Exception ex){
            System.out.println("createClinic() ERROR: ");
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }

    @Override
    public boolean deleteClinic(IClinic clinicToDelete) {
        try{
            em.remove((ClinicEntity)clinicToDelete);
        }catch(Exception ex){
            System.out.println("deleteClinic() ERROR: ");
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    
}
