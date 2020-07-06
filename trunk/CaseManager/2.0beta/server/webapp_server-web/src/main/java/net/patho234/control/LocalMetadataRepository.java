/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.control;

import java.util.List;
import javax.ejb.Local;
import net.patho234.interfaces.ICase;
import net.patho234.interfaces.IMetadata;
import net.patho234.interfaces.IMetadataDouble;
import net.patho234.interfaces.IMetadataInt;
import net.patho234.interfaces.IMetadataString;
import net.patho234.interfaces.IMetadataText;
import net.patho234.interfaces.IMetadataUrl;
import net.patho234.interfaces.IService;

/**
 *
 * @author HS
 */
@Local
public interface LocalMetadataRepository {
    public List<IMetadata> getAllEntities();
    
    public IMetadataDouble getDoubleMetadata(IService service, String key);
    public IMetadataInt getIntegerMetadata(IService service, String key);
    public IMetadataString getStringMetadata(IService service, String key);
    public IMetadataText getTextMetadata(IService service, String key);
    public IMetadataUrl getUrlMetadata(IService service, String key);
    
    public IMetadataDouble setDoubleMetadata(IService service, String key, Double value);
    public IMetadataInt setIntegerMetadata(IService service, String key, Integer value);
    public IMetadataString setStringMetadata(IService service, String key, String value);
    public IMetadataText setTextMetadata(IService service, String key, String value);
    public IMetadataUrl setUrlMetadata(IService service, String key, String value);
    
    public List<IMetadata> getMetadataForService(IService service);
    public List<IMetadata> getMetadataForCase(ICase requestCase);
}
