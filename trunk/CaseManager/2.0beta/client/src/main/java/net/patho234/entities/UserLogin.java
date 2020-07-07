/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import net.patho234.interfaces.ISubmitter;

/**
 *
 * @author rehkind
 */
public class UserLogin {
    
    static String login="guest";
    static String pwd="123456";
    static String name="<not set>";
    static JsonObject jsonLogin;
    static ClientSubmitter submitter;
    
    public static void setLogin(String login, String pwd){
        UserLogin.login=login;
        UserLogin.pwd=pwd;
        updateJsonLogin();
    }
    
    public static void setLogin(ISubmitter s){
        UserLogin.login=s.getLogin();
        UserLogin.pwd=s.getPassword();
        UserLogin.name=s.getTitle()+" "+s.getForename() + " " + s.getSurname();
        
        UserLogin.submitter = (ClientSubmitter) s;
        updateJsonLogin();
    }
    
    public static JsonObject getLoginAsJson(){
        if( jsonLogin==null ){ updateJsonLogin(); }
        
        return jsonLogin;
    }
    
    public static ClientSubmitter getLogin(){
        return submitter;
    }
    
    private static void updateJsonLogin(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("login", UserLogin.login);
        builder.add("password", UserLogin.pwd);
        jsonLogin = builder.build();
    }
    
    public static String getUserName(){ return UserLogin.name; }
}
