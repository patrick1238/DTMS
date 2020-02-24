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
public interface IHttpResponseReceiver {
    public void receiveHttpResponse(Integer requestID, IHttpResponse response);
}
