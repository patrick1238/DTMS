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
import net.rehkind_mag.entity.ServiceDefinitionEntity;
import net.rehkind_mag.interfaces.IServiceDefinition;

/**
 *
 * @author HS
 */
@Stateless
public class ServiceDefinitionRepository extends AbstractFacade<ServiceDefinitionEntity> implements LocalServiceDefinitionRepository{
    private EntityManager em;
    private String persistenceContext = "net.rehkind-mag_linfo_0.1";
    
    public ServiceDefinitionRepository(){
        super(ServiceDefinitionEntity.class);
        em=Persistence.createEntityManagerFactory(persistenceContext).createEntityManager();
    }
    
    
    @Override
    public List<IServiceDefinition> getServiceDefinitions(){
        ArrayList<IServiceDefinition> returnList=new ArrayList<>();
        for(ServiceDefinitionEntity se : findAll()){
            returnList.add((IServiceDefinition)se);
        }
        
        return returnList;
    }
    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public IServiceDefinition getServiceDefinition(int serviceDefId) {
        return (IServiceDefinition)find(serviceDefId);
    }
}
