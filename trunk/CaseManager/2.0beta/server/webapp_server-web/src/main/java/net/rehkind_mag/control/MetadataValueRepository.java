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
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.validation.ValidationException;
import net.rehkind_mag.entity.MetadataValueEntity;
import net.rehkind_mag.interfaces.IMetadataValue;
import org.netbeans.rest.application.config.AppPersistenceManager;

/**
 *
 * @author rehkind
 */
@Stateless
public class MetadataValueRepository extends AbstractFacade<MetadataValueEntity> implements LocalMetadataValueRepository {
    private EntityManager em;
    private String persistenceContext = "net.rehkind-mag_linfo_0.1";
    
    public MetadataValueRepository(){
        super(MetadataValueEntity.class);
        em=Persistence.createEntityManagerFactory(persistenceContext).createEntityManager();
    }    
    
    @Override
    public IMetadataValue getMetadataValue(int metadataValueId) {
        return find(metadataValueId);
    }

    @Override
    public List<IMetadataValue> getMetadataValues() {
        ArrayList<IMetadataValue> allMetadataValues = new ArrayList<>();
        findAll().forEach( entity -> { allMetadataValues.add(entity); } );
        return allMetadataValues;
    }

    @Override
    public void updateMetadataValue(IMetadataValue metadataValueToUpdate) throws ValidationException {
        validate( (MetadataValueEntity)metadataValueToUpdate );
        
        em.persist(metadataValueToUpdate);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public boolean createMetadataValue(IMetadataValue metadataValueToCreate) throws ValidationException {
        Query qry=em.createNamedQuery("MetadataValueEntity.maxId");
        Integer maxId = (Integer)qry.getSingleResult();
        maxId+=1;
        //((MetadataValueEntity)metadataValueToCreate).setId(maxId);
        validate((MetadataValueEntity)metadataValueToCreate);
        this.create((MetadataValueEntity)metadataValueToCreate);
        try{
            em.persist((MetadataValueEntity)metadataValueToCreate);
        }catch(Exception ex){
            System.out.println("createMetadataValue() ERROR: ");
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }

    @Override
    public boolean deleteMetadataValue(IMetadataValue metadataValueToDelete) throws ValidationException {
        try{
            em.remove((MetadataValueEntity)metadataValueToDelete);
        }catch(Exception ex){
            System.out.println("deleteMetadataValue() ERROR: ");
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }
    
}
