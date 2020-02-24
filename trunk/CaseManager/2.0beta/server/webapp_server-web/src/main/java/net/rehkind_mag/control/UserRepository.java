/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.rehkind_mag.entity.UserDataEntity;
import net.rehkind_mag.entity.UserEntity;
import net.rehkind_mag.test.interfaces.IUser;
import net.rehkind_mag.test.interfaces.IUserRepository;
//

/**
 *
 * @author rehkind
 */
public class UserRepository implements IUserRepository {
    HashMap<Integer, UserEntity> users = new HashMap<>();
    
    public UserRepository(){
        Logger.getGlobal().log(Level.INFO, "Building user repository for testing.");
        UserEntity newUser;
        newUser=new UserEntity(1, "logen", new UserDataEntity("Leonard", "Gruper", new Date(1978, 6, 17)));
        
        storeUser(newUser);
        newUser=new UserEntity(2, "peggie", new UserDataEntity("Petra", "Giers", new Date(1978, 11, 17)));
        storeUser(newUser);
        newUser=new UserEntity(3, "lorenzo", new UserDataEntity("Lorenzo", "Italiano", new Date(1992, 1, 2)));
        storeUser(newUser);
        newUser=new UserEntity(4, "kati87", new UserDataEntity("Katarina", "Müller", new Date(1981, 12, 20)));
        storeUser(newUser);
        newUser=new UserEntity(5, "pegasus", new UserDataEntity("Peter", "Peenuts", new Date(1979, 6, 26)));
        storeUser(newUser);
        newUser=new UserEntity(6, "gruffel08", new UserDataEntity("Georg", "Rufeld", new Date(2008, 6, 7)));
        storeUser(newUser);
        newUser=new UserEntity(7, "lolliePub", new UserDataEntity("Pia", "Lolita", new Date(1993, 9, 13)));
        storeUser(newUser);
        
    }
    
    @Override
    public UserEntity getUser(Integer id){
        return users.get(id);
    }
    
    @Override
    public final boolean storeUser(IUser userToStore){
        if(userToStore.getUserID()==null || userToStore.getUserID()<0){
            return false;
        }
        users.put(userToStore.getUserID(), (UserEntity)userToStore);
        return true;
    }
    
    public List<IUser> getUsers(){
        List<IUser> usersAsList = new ArrayList<>();
        for(UserEntity u : users.values()){
            usersAsList.add(u);
        }
        return (List<IUser>)usersAsList;
    }
}
