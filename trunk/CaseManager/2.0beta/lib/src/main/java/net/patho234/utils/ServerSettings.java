/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.utils;

import com.sun.scenario.Settings;

/**
 *
 * @author rehkind
 */
public class ServerSettings {
    public String serverAddress;
    public Integer connectionTimeOut;
    public Integer readTimeOut;
    public String contentType;
    
    private ServerSettings(){
        
    }
    
    static public ServerSettings getDefaultServerSettings(){
        ServerSettings settings=new ServerSettings();
        settings.serverAddress=Settings.get("server.address");
        settings.contentType="application/json";
        settings.connectionTimeOut = 5000;
        settings.readTimeOut = 15000;
        return settings;
    }
    
    public ServerSettings clone(){
        ServerSettings clonedSettings=new ServerSettings();
        clonedSettings.serverAddress=this.serverAddress;
        clonedSettings.connectionTimeOut=this.connectionTimeOut;
        clonedSettings.readTimeOut=this.readTimeOut;
        clonedSettings.contentType=this.contentType;
        
        return clonedSettings;
    }
}
