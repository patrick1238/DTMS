/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import net.patho234.entities.ClientMetadata;
import net.patho234.interfaces.ICase;
import net.patho234.interfaces.IService;


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
