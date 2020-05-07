/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.filter;

import net.rehkind_mag.entities.ClientMetadata;
import net.rehkind_mag.interfaces.IService;


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
    public boolean isClientObjectInScope(ClientMetadata clientObject) {
        return clientObject.getService().getId() == service.getId();
    }
}
