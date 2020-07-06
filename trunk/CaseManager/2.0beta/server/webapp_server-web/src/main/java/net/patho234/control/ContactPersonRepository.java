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
import net.patho234.entity.ClinicEntity;
import net.patho234.entity.ContactForClinicEntity;
import net.patho234.entity.ContactForClinicEntityPK;
import net.patho234.entity.ContactPersonEntity;
import net.patho234.interfaces.IClinic;
import net.patho234.interfaces.IContactPerson;
import org.jboss.logging.Logger;

/**
 *
 * @author HS
 */
@Stateless
public class ContactPersonRepository extends AbstractFacade<ContactPersonEntity> implements LocalContactPersonRepository{
    private EntityManager em;
    private String persistenceContext = "net.rehkind-mag_linfo_0.1";
    
    public ContactPersonRepository(){
        super(ContactPersonEntity.class);
        em=Persistence.createEntityManagerFactory(persistenceContext).createEntityManager();
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public IContactPerson getContactPerson(int contactId) {
        return find(contactId);
    }

    @Override
    public List<IContactPerson> getContactPersons() {
        List<IContactPerson> all=new ArrayList<>();
        all.addAll( findAll() );
        
        return all;
    }

    @Override
    public void updateContactPerson(IContactPerson contactToUpdate) {
        validate((ContactPersonEntity)contactToUpdate);
        
        em.persist((ContactPersonEntity)contactToUpdate);
    }

    @Override
    public boolean createContactPerson(IContactPerson contactToCreate) {
        Query qry=em.createNamedQuery("ContactPersonEntity.maxId");
        Integer maxId = (Integer)qry.getSingleResult();
        maxId+=1;
        ((ContactPersonEntity)contactToCreate).setId(maxId);
        validate((ContactPersonEntity)contactToCreate);
        
        try{
            em.persist((ContactPersonEntity)contactToCreate);
        }catch(Exception ex){
            System.out.println("createCase() ERROR: ");
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }

    @Override
    public boolean deleteContactPerson(IContactPerson contactToDelete) {
        try{
            em.remove((ContactPersonEntity)contactToDelete);
        }catch(Exception ex){
            Logger.getLogger("global").warn("deleteContactPerson() ERROR: ");
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean addContactPersonToClinic(IContactPerson contact, IClinic clinic, String notes) {
        try{
            Logger.getLogger("global").warn("adding "+contact.getForename()+" "+contact.getSurname()+" as contact for clinic "+clinic.getName());
            ContactForClinicEntityPK pk=new ContactForClinicEntityPK(clinic.getId(), contact.getId());
            ContactForClinicEntity c4ce=new ContactForClinicEntity( pk );
            c4ce.setNotes(notes);
            c4ce.setClinic((ClinicEntity)clinic);
            c4ce.setContact(contact);
            em.persist( c4ce );
        }catch(Exception ex){
            Logger.getLogger("global").warn("addContactToClinic() ERROR: ");
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }
    
}
