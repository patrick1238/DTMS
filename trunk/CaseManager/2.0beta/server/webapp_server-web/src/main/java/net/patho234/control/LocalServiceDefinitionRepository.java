/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.control;

import java.util.List;
import javax.ejb.Local;
import net.patho234.interfaces.IServiceDefinition;

/**
 *
 * @author HS
 */
@Local
public interface LocalServiceDefinitionRepository {
    public IServiceDefinition getServiceDefinition(int serviceDefId);
    public List<IServiceDefinition> getServiceDefinitions();
}
