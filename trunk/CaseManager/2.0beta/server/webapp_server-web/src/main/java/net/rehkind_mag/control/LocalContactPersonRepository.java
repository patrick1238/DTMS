/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.control;

import java.util.List;
import javax.ejb.Local;
import net.rehkind_mag.interfaces.IClinic;

import net.rehkind_mag.interfaces.IContactPerson;

/**
 *
 * @author HS
 */
@Local
public interface LocalContactPersonRepository {
    public IContactPerson getContactPerson(int contactId);
    public List<IContactPerson> getContactPersons();

    public void updateContactPerson(IContactPerson contactToUpdate);
    public boolean createContactPerson(IContactPerson contactToCreate);
    public boolean deleteContactPerson(IContactPerson contactToDelete);
    public boolean addContactPersonToClinic(IContactPerson contact, IClinic clinic, String notes);
}
