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
public interface IClinic {
    public Integer getId();
    public String getName();
    public String getZipCode();    
    public String getCity();
    public String getStreet();
    public void setName(String name);
    public void setZipCode(String zipcode);    
    public void setCity(String city);
    public void setStreet(String street);
    public List<IContactForClinic> getContactsForClinicList();
}
