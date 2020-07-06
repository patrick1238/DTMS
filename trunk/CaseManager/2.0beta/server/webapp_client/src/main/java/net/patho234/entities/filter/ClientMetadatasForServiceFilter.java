/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import net.patho234.entities.ClientMetadata;
import net.patho234.interfaces.IService;


/**
 *
 * @author rehkind
 */
public class ClientMetadatasForServiceFilter  extends ClientObjectFilterBase<ClientMetadata>{
    IService service;
    public ClientMetadatasForServiceFilter( IService targetService  ){
        service = targetService;
    }
    @Override
    public synchronized boolean isClientObjectInScope(ClientMetadata clientObject) {
        return clientObject.getService().getId() == service.getId();
    }
}
