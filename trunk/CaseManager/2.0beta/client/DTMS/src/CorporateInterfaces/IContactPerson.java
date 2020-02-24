/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CorporateInterfaces;

/**
 *
 * @author HS
 */
public interface IContactPerson {
    public int getContactPersonId();
    public String getTitle();
    public String getForename();
    public String getSurname();
    
    public String getNotes();
    public IClinic getClinic();
    
    public String getEmail();
    public String getPhone();
    
    public void setContactPersonId(int id);
    public void setTitle(String title);
    public void setForename(String forename);
    public void setSurname(String surname);
    
    public void setNotes(String notes);
    
    public void setEmail(String email);
    public void setPhone(String phone);
}
