/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.interfaces.client;

import javax.json.JsonObject;

/**
 *
 * @author rehkind
 */
public interface IClientObject<T extends IClientObject> {
    public final static int STATE_PERSISTENT=0;
    public final static int STATE_CACHED=1;
    
    public boolean hasLocalChanges();
    public int getId();
    public T getLocalClone();
    public JsonObject toJson();
    public void merge(T toMergeWith);
}
