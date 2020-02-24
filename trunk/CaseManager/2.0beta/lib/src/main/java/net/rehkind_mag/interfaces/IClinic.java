/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.interfaces;

import java.util.List;

/**
 *
 * @author HS
 */
public interface IClinic {
    public int getId();
    public String getName();
    public String getZipCode();    
    public String getCity();
    public String getStreet();
    public void setId(int id);
    public void setName(String name);
    public void setZipCode(String zipcode);    
    public void setCity(String city);
    public void setStreet(String street);
    public List<IContactForClinic> getContactsForClinicList();
}
