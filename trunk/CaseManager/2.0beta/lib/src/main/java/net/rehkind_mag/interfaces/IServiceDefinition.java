/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.interfaces;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author HS
 */
public interface IServiceDefinition {
    public int getId();
    public IServiceDefinition getParentDefinition();
    public String getName();
    public String getDescription();
    public List<IService> getServices();
    public HashMap<IServiceDefinition, List<IMetadataValue>> getMetadataValues();
            
    public void setName(String name);
    public void setDescription(String description);
    public void setParentDefinition(IServiceDefinition parentDef);
}
