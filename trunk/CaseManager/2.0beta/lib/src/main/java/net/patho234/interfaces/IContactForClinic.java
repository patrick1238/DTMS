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
public interface IContactForClinic {
    public String getNotes();
    public IContactPerson getContact();
    public IClinic getClinic();
    
    public void setNotes(String notes);
    public void setContact(IContactPerson newContactPerson);
}
