/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.control;


import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import net.patho234.entity.validation.DefaultValidationException;

/**
 *
 * @author HS
 */
public abstract class AbstractFacade<T extends Serializable> {

    private Class<T> entityClass;
    private final Validator validator;
    
    public AbstractFacade(Class<T> entityClass) {
        try {
            entityClass.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, "InstantiationException", ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, "IllegalAccessException", ex);
        }
        this.entityClass = entityClass;
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator  = vf.getValidator();
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        Logger.getLogger("persistance").log(Level.INFO, "Creating new entity {0}", entity);
        getEntityManager().persist(entity);
    }

    public T edit(T entity) {
        Logger.getLogger("persistance").log(Level.INFO, "Editing existing entity {0}", entity);
        return getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        Logger.getLogger("persistance").log(Level.INFO, "Removing existing entity {0}", entity);
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        T entity = getEntityManager().find(entityClass, id);
        if(entity!=null){
            Logger.getLogger("persistance").log(Level.INFO, "Entity {0} was found.", entity);
        }else{ 
            try{
                Logger.getLogger("persistance").log(Level.INFO, "Entity {0} was not found.", entity);
            }catch(Exception ex){
                Logger.getGlobal().log(Level.SEVERE, "ClassInfo ERROR");
            }
        }
        return entity;
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public void validate(T entity) throws ValidationException{
        Set<ConstraintViolation<Object>> violations=validator.validate((Object)entity);
        if( !violations.isEmpty() ){
            ValidationException ex= new DefaultValidationException((Set<ConstraintViolation<Object>>)violations);
            throw ex;
        }
    }
}
