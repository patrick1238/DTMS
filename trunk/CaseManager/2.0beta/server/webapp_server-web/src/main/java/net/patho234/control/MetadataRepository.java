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
import net.patho234.entity.MetadataDoubleEntity;
import net.patho234.entity.MetadataDoubleEntityPK;
import net.patho234.entity.MetadataIntEntity;
import net.patho234.entity.MetadataIntEntityPK;
import net.patho234.entity.MetadataStringEntity;
import net.patho234.entity.MetadataStringEntityPK;
import net.patho234.entity.MetadataTextEntity;
import net.patho234.entity.MetadataTextEntityPK;
import net.patho234.entity.MetadataUrlEntity;
import net.patho234.entity.MetadataUrlEntityPK;
import net.patho234.interfaces.ICase;
import net.patho234.interfaces.IMetadata;
import net.patho234.interfaces.IMetadataDouble;
import net.patho234.interfaces.IMetadataInt;
import net.patho234.interfaces.IMetadataString;
import net.patho234.interfaces.IMetadataText;
import net.patho234.interfaces.IMetadataUrl;
import net.patho234.interfaces.IService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author HS
 */
@Stateless
public class MetadataRepository implements LocalMetadataRepository {
    private EntityManager em;
    private String persistenceContext = "net.rehkind-mag_linfo_0.1";
    
    private final IntRepo intRepo;
    private final DoubleRepo doubleRepo;
    private final StringRepo stringRepo;
    private final TextRepo textRepo;
    private final UrlRepo urlRepo;
    
    public MetadataRepository(){
        em=Persistence.createEntityManagerFactory(persistenceContext).createEntityManager();
        intRepo = new IntRepo(em);
        doubleRepo = new DoubleRepo(em);
        stringRepo = new StringRepo(em);
        textRepo = new TextRepo(em);
        urlRepo = new UrlRepo(em);
    }
    
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<IMetadata> getAllEntities(){
        ArrayList<IMetadata> combinedEntities=new ArrayList<>();
        combinedEntities.addAll( doubleRepo.findAll() );
        combinedEntities.addAll( intRepo.findAll() );
        combinedEntities.addAll( textRepo.findAll() );
        combinedEntities.addAll( stringRepo.findAll() );
        combinedEntities.addAll( urlRepo.findAll() );
        
        return combinedEntities;
    }
    
    @Override
    public List<IMetadata> getMetadataForService(IService service){
        ArrayList<IMetadata> data = new ArrayList<>();
        Query qry = em.createNamedQuery("MetadataIntEntity.findByFkeyService");
        qry.setParameter("fkeyService", service.getId());
        data.addAll(qry.getResultList());
        
        qry = em.createNamedQuery("MetadataDoubleEntity.findByFkeyService");
        qry.setParameter("fkeyService", service.getId());
        data.addAll(qry.getResultList());
        
        qry = em.createNamedQuery("MetadataStringEntity.findByFkeyService");
        qry.setParameter("fkeyService", service.getId());
        data.addAll(qry.getResultList());
        
        qry = em.createNamedQuery("MetadataTextEntity.findByFkeyService");
        qry.setParameter("fkeyService", service.getId());
        data.addAll(qry.getResultList());
        
        qry = em.createNamedQuery("MetadataUrlEntity.findByFkeyService");
        qry.setParameter("fkeyService", service.getId());
        data.addAll(qry.getResultList());
        
        return data;
    } 
    
    @Override
    public IMetadataDouble getDoubleMetadata(IService service, String key) {
        return doubleRepo.find(new MetadataDoubleEntityPK(service.getId(), key));
    }

    @Override
    public IMetadataInt getIntegerMetadata(IService service, String key) {
        return intRepo.find(new MetadataIntEntityPK(service.getId(), key));
    }

    @Override
    public IMetadataString getStringMetadata(IService service, String key) {
        return stringRepo.find(new MetadataStringEntityPK(service.getId(), key));
    }

    @Override
    public IMetadataText getTextMetadata(IService service, String key) {
        return textRepo.find(new MetadataTextEntityPK(service.getId(), key));
    }

    @Override
    public IMetadataUrl getUrlMetadata(IService service, String key) {
        return urlRepo.find(new MetadataUrlEntityPK(service.getId(), key));
    }

    @Override
    public IMetadataDouble setDoubleMetadata(IService service, String key, Double value) {
        MetadataDoubleEntity updateEntity=doubleRepo.find(new MetadataDoubleEntityPK(service.getId(), key));
        if( updateEntity!=null ){
            if(value!=null){
                updateEntity.setData(value);
                em.merge( updateEntity );
            }else{
                em.remove(updateEntity);
            }
        }else{
            if( value != null ){
                updateEntity = new MetadataDoubleEntity(service.getId(), key);
                updateEntity.setData(value);
                em.persist( updateEntity );
            }
        }
        return updateEntity;
    }

    @Override
    public IMetadataInt setIntegerMetadata(IService service, String key, Integer value) {
        MetadataIntEntity updateEntity=intRepo.find(new MetadataIntEntityPK(service.getId(), key));
        if( updateEntity!=null ){
            if(value!=null){
                updateEntity.setData(value);
                em.merge( updateEntity );
            }else{
                em.remove(updateEntity);
            }
        }else{
            if( value != null ){
                updateEntity = new MetadataIntEntity(service.getId(), key);
                updateEntity.setData(value);
                em.persist( updateEntity );
            }
        }
        return updateEntity;
    }

    @Override
    public IMetadataString setStringMetadata(IService service, String key, String value) {
        MetadataStringEntity updateEntity=stringRepo.find(new MetadataStringEntityPK(service.getId(), key));
        if( value == null ){ value=""; }
        if( updateEntity!=null ){
            updateEntity.setData(value);
            em.merge( updateEntity );
        }else{
            updateEntity = new MetadataStringEntity(service.getId(), key);
            updateEntity.setData(value);
            em.persist( updateEntity );
        }
        return updateEntity;
    }

    @Override
    public IMetadataText setTextMetadata(IService service, String key, String value) {
        MetadataTextEntity updateEntity=textRepo.find(new MetadataTextEntityPK(service.getId(), key));
        if( value == null ){ value=""; }
        if( updateEntity!=null ){
            updateEntity.setData(value);
            em.merge( updateEntity );
        }else{
            updateEntity = new MetadataTextEntity(service.getId(), key);
            updateEntity.setData(value);
            em.persist( updateEntity );
        }
        return updateEntity;
    }

    @Override
    public IMetadataUrl setUrlMetadata(IService service, String key, String value) {
        MetadataUrlEntity updateEntity=urlRepo.find(new MetadataUrlEntityPK(service.getId(), key));
        if( value == null ){ value=""; }
        if( updateEntity!=null ){
            updateEntity.setData(value);
            em.merge( updateEntity );
        }else{
            updateEntity = new MetadataUrlEntity(service.getId(), key);
            updateEntity.setData(value);
            em.persist( updateEntity );
        }
        return updateEntity;
    }

    @Override
    public List<IMetadata> getMetadataForCase(ICase requestCase) {
        throw new NotImplementedException();
    }
    
    
}

class IntRepo extends AbstractFacade<MetadataIntEntity>{
    EntityManager em;
    public IntRepo(EntityManager em){
        super(MetadataIntEntity.class);
        this.em=em;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
class DoubleRepo extends AbstractFacade<MetadataDoubleEntity>{
    EntityManager em;
    public DoubleRepo(EntityManager em){
        super(MetadataDoubleEntity.class);
        this.em=em;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
class StringRepo extends AbstractFacade<MetadataStringEntity>{
    EntityManager em;
    public StringRepo(EntityManager em){
        super(MetadataStringEntity.class);
        this.em=em;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
class TextRepo extends AbstractFacade<MetadataTextEntity>{
    EntityManager em;
    public TextRepo(EntityManager em){
        super(MetadataTextEntity.class);
        this.em=em;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
class UrlRepo extends AbstractFacade<MetadataUrlEntity>{
    EntityManager em;
    public UrlRepo(EntityManager em){
        super(MetadataUrlEntity.class);
        this.em=em;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}