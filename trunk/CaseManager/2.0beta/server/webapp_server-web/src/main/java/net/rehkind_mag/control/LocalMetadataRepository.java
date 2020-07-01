/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.control;

import java.util.List;
import javax.ejb.Local;
import net.rehkind_mag.interfaces.ICase;
import net.rehkind_mag.interfaces.IMetadata;
import net.rehkind_mag.interfaces.IMetadataDouble;
import net.rehkind_mag.interfaces.IMetadataInt;
import net.rehkind_mag.interfaces.IMetadataString;
import net.rehkind_mag.interfaces.IMetadataText;
import net.rehkind_mag.interfaces.IMetadataUrl;
import net.rehkind_mag.interfaces.IService;

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
