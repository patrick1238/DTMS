/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.control;

import java.util.List;
import javax.ejb.Local;
import net.patho234.interfaces.IClinic;
import net.patho234.interfaces.IContactForClinic;

/**
 *
 * @author HS
 */
@Local
public interface LocalClinicRepository {
    public IClinic getClinic(int clinicId);
    public List<IClinic> getClinics();
    public List<IContactForClinic> getContactsForClinic(IClinic clinic);

    public void updateClinic(IClinic clinicToUpdate);
    public boolean createClinic(IClinic clinicToUpdate);
    public boolean deleteClinic(IClinic clinicToUpdate);
}
