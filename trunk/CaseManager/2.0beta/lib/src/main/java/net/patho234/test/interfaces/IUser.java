/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.test.interfaces;

/**
 *
 * @author rehkind
 */
public interface IUser {
    public Integer getUserID();
    public String getAuthName();
    public IUserData getUserData();
}
