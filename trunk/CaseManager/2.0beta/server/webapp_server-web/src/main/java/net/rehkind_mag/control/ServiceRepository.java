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
import net.rehkind_mag.entity.ServiceDefinitionEntity;
import net.rehkind_mag.entity.ServiceEntity;
import net.rehkind_mag.interfaces.IService;
import net.rehkind_mag.interfaces.IServiceDefinition;

/**
 *
 * @author HS
 */
@Stateless
public class ServiceRepository extends AbstractFacade<ServiceEntity> implements LocalServiceRepository{
    private EntityManager em;
    private String persistenceContext = "net.rehkind-mag_linfo_0.1";
    
    public ServiceRepository(){
        super(ServiceEntity.class);
        em=Persistence.createEntityManagerFactory(persistenceContext).createEntityManager();
    }
    
    @Override
    public IService getService(int serviceId){
        return (IService)find(serviceId);
    }
    
    @Override
    public List<IService> getServicesForCase(int caseId){
        Query forCaseQuery = em.createNamedQuery("ServiceEntity.findAllForCase", ServiceEntity.class);
        forCaseQuery.setParameter("idcase", new CaseRepository().getCase(caseId));
        List<ServiceEntity> qResult = forCaseQuery.getResultList();
        List<IService> returnList = new ArrayList<>();
        for( ServiceEntity se : qResult){
            returnList.add( (IService)se );
        }
        return returnList;
    }
    
    @Override
    public List<IService> getServices(){
        ArrayList<IService> returnList=new ArrayList<>();
        for(ServiceEntity se : findAll()){
            returnList.add((IService)se);
        }
        
        return returnList;
    }
    
    @Override
    public List<IService> getServicesByServiceDef(IServiceDefinition serviceDef){
        Query qry = em.createNamedQuery("ServiceEntity.findAllForDefinition");
        qry.setParameter("iddefinition", (ServiceDefinitionEntity)serviceDef);
        List<IService> resList = new ArrayList<>();
        for(Object se : qry.getResultList()){
            resList.add((IService)se);
        }
        return resList;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void updateService(IService serviceToUpdate) {
        em.persist(serviceToUpdate);
    }

    @Override
    public boolean createService(IService serviceToCreate) {
        Query qry=em.createNamedQuery("ServiceEntity.maxId");
        Integer maxId = (Integer)qry.getSingleResult();
        maxId+=1;
        ((ServiceEntity)serviceToCreate).setId(maxId);
        validate((ServiceEntity)serviceToCreate);
        
        try{
            em.persist((ServiceEntity)serviceToCreate);
        }catch(Exception ex){
            System.out.println("createService() ERROR: ");
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }

    @Override
    public boolean deleteService(IService serviceToDelete) {
        try{
            em.remove((ServiceEntity)serviceToDelete);
        }catch(Exception ex){
            System.out.println("deleteService() ERROR: ");
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }
}
