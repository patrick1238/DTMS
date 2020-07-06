/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.control;

import java.util.List;
import javax.ejb.Local;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import net.patho234.interfaces.ISubmitter;

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
    
    public Response createNoPermissionResponse(String url, String login, String operation);
}
