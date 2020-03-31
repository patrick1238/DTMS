/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.interfaces;

/**
 *
 * @author rehkind
 */
public interface IHttpConnector {
    public void setServerAddress(String address);
    public void setConnectionTimeOut(Integer timeOut);    
    public void setReadTimeOut(Integer timeOut);
    
    public String getServerAddress();
    public Integer getConnectionTimeOut();
    public Integer getReadTimeOut();
}
