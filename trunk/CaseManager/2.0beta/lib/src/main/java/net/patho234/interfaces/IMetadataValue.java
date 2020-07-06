/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.interfaces;

/**
 *
 * @author HS
 */
public interface IMetadataValue {
    public Integer getId();
    public String getValueType();
    public String getKey();
    public Boolean isDepricated();
    public String getUnit();
   
    public void setValueType(String valueType);
    public void setKey(String key);
    public void setDepricated(Boolean depricated);
    public void setUnit(String unit);
}
