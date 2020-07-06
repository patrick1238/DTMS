/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.control;

import java.util.List;
import javax.ejb.Local;
import javax.validation.ValidationException;
import net.patho234.interfaces.IMetadataValue;

/**
 *
 * @author rehkind
 */
@Local
public interface LocalMetadataValueRepository {
    public IMetadataValue getMetadataValue(int metadataValueId);
    
    public List<IMetadataValue> getMetadataValues();

    public void updateMetadataValue(IMetadataValue metadataValueToUpdate) throws ValidationException ;
    public boolean createMetadataValue(IMetadataValue metadataValueToCreate) throws ValidationException ;
    public boolean deleteMetadataValue(IMetadataValue metadataValueToDelete) throws ValidationException ;
    
}
