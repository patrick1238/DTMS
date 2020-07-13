/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.interfaces;

import java.util.List;

/**
 *
 * @author HS
 */
public interface ISubmitter {
    public Integer getId();
    
    
    public String getSurname();
    public String getForename();
    public String getTitle();
    public String getLogin();
    public String getPassword();
    
    public void setSurname(String surname);
    public void setForename(String forename);
    public void setTitle(String title);
    public void setLogin(String loginName);
    public void setPassword(String pwd);
}
