/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.interfaces;

import java.util.List;

/**
 *
 * @author HS
 */
public interface IService {
    public Integer getId();
    public IServiceDefinition getServiceDefinition();
    public ICase getCase();
    public void setCase(ICase caseValue);
    public void setServiceDefinition(IServiceDefinition serviceDef);
    
    public List<IMetadata> getMetadata();
}
