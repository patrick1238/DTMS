/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.interfaces;

/**
 *
 * @author HS
 * @param <T> type of the contained metadata
 */
public interface IMetadata<T extends Object> {
    public enum METADATA_TYPE{ INTEGER, DOUBLE, STRING, TEXT, URL, UNDEFINED};
    
    public IService getService();
    public T getData();
    public void setData(T newData);
    public String getName();
    public METADATA_TYPE getType();
}
