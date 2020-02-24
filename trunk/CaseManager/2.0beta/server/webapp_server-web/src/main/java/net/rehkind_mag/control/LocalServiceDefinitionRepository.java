/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.control;

import java.util.List;
import javax.ejb.Local;
import net.rehkind_mag.interfaces.IServiceDefinition;

/**
 *
 * @author HS
 */
@Local
public interface LocalServiceDefinitionRepository {
    public IServiceDefinition getServiceDefinition(int serviceDefId);
    public List<IServiceDefinition> getServiceDefinitions();
}
