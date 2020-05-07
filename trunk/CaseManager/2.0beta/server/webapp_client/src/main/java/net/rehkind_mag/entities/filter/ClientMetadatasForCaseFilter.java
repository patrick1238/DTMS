/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.filter;

import net.rehkind_mag.entities.ClientMetadata;
import net.rehkind_mag.interfaces.ICase;
import net.rehkind_mag.interfaces.IService;


/**
 *
 * @author rehkind
 */
public class ClientMetadatasForCaseFilter  extends ClientObjectFilterBase<ClientMetadata>{
    ICase filterCase;
    public ClientMetadatasForCaseFilter( ICase targetService  ){
        filterCase = targetService;
    }
    @Override
    public boolean isClientObjectInScope(ClientMetadata clientObject) {
        return clientObject.getService().getCase().getId() == filterCase.getId();
    }
}
