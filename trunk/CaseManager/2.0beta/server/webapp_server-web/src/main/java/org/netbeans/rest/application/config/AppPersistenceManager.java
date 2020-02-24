/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author HS
 */
public class AppPersistenceManager {
    static HashMap<String, EntityManager> entityManagers=new HashMap<>();
    
    public static EntityManager requestEntityManager(String persistenceContext){
        if( entityManagers.get(persistenceContext)==null ){
            EntityManager newManager=Persistence.createEntityManagerFactory(persistenceContext).createEntityManager();
            entityManagers.put(persistenceContext, newManager);
        }
        
        return entityManagers.get(persistenceContext);
    }
}
