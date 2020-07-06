/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entity;

import net.patho234.test.interfaces.IUser;
import net.patho234.test.interfaces.IUserData;

/**
 *
 * @author rehkind
 */
public class UserEntity implements IUser{
    private static Integer MAX_ID=0;
    
    private Integer id;
    private String authName;
    
    private IUserData userData;
    
    public UserEntity( Integer id, String auth, IUserData data ){
        this.authName=auth;
        this.userData=data;
        UserEntity.MAX_ID++;
        this.id=id;
    }
    
    @Override
    public Integer getUserID() {
        return id;
    }

    @Override
    public String getAuthName() {
        return authName;
    }

    @Override
    public IUserData getUserData() {
        return userData;
    }
}
