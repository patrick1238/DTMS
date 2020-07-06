/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entity;

import java.util.Date;
import net.patho234.test.interfaces.IUserData;

/**
 *
 * @author rehkind
 */
public class UserDataEntity implements IUserData {
    private String foreName;
    private String name;
    private Date birthDate; 
    
    public UserDataEntity(String fName, String sName, Date birth){
        foreName=fName; name=sName; birthDate=birth;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getForeName() {
        return foreName;
    }

    @Override
    public Date getBirthDate() {
        return birthDate;
    }
    
}
