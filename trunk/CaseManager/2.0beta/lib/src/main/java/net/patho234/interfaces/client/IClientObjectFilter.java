/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.interfaces.client;

import java.util.List;
import net.patho234.entities.ClientObjectBase;

/**
 *
 * @author rehkind
 */
public interface IClientObjectFilter<T extends ClientObjectBase> {
    public List<T> filterClientObjectList(ReadOnlyClientObjectList<T> originalList);
    public boolean isClientObjectInScope( T clientObject);
}
