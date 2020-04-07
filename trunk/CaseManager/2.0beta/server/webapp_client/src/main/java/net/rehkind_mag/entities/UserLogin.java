/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author rehkind
 */
public class UserLogin {
    static String login="guest";
    static String pwd="123456";
    static JsonObject jsonLogin;
    public static void setLogin(String login, String pwd){
        UserLogin.login=login;
        UserLogin.pwd=pwd;
    }
    
    public static JsonObject getLoginAsJson(){
        if( jsonLogin==null ){
           updateJsonLogin();
        }
        
        return jsonLogin;
    }
    
    private static void updateJsonLogin(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(login, UserLogin.login);
        builder.add(pwd, UserLogin.pwd);
    }
}
