/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.control;

import java.util.List;
import javax.ejb.Local;
import javax.validation.ValidationException;
import net.rehkind_mag.interfaces.ISubmitter;

/**
 *
 * @author rehkind
 */
@Local
public interface LocalSubmitterRepository {
    public ISubmitter getSubmitter(int submitterId);
    
    public List<ISubmitter> getSubmitters();

    public void updateSubmitter(ISubmitter submitterToUpdate) throws ValidationException ;
    
    public boolean submitterHasAccess(String login, String pwd);
}
