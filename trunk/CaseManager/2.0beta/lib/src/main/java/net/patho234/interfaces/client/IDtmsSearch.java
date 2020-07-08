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
public interface IDtmsSearch<T extends ClientObjectBase> {
    public void setOriniginalList( ClientObjectList<T> newOriginalItems);
    public void setFilterItems( List<IClientObjectFilter<? super ClientObjectBase>> newFilterItems);
    public List<IClientObjectFilter<? super ClientObjectBase>> getFilterItems();
    public ClientObjectList getSearchResult();
    public void updateSearchResult();
    public void addDtmsSearchResultListener(IDtmsSearchListener l);
    public void removeDtmsSearchResultListener(IDtmsSearchListener l);
}
