/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.test.interfaces;

import java.util.List;


/**
 *
 * @author rehkind
 */

public interface IUserRepository {
    public IUser getUser(Integer id);
    public List<IUser> getUsers();
    public boolean storeUser(IUser userToStore);
}
