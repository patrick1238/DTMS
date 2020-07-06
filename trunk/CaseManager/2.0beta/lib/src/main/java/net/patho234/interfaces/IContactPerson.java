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
public interface IContactPerson {
    public Integer getId();
    public String getTitle();
    public String getForename();
    public String getSurname();
    
    public String getEmail();
    public String getPhone();
    
    public void setTitle(String title);
    public void setForename(String forename);
    public void setSurname(String surname);
    
    public void setEmail(String email);
    public void setPhone(String phone);
}
