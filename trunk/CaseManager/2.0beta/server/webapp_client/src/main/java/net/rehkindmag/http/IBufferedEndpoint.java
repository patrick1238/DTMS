/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkindmag.http;

import java.util.ArrayList;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.interfaces.IHttpResponseReceiver;
import net.rehkind_mag.utils.RegisteredHttpAccessRequest;

/**
 *
 * @author rehkind
 */
public interface IBufferedEndpoint {
    public ArrayList<IHttpResponseReceiver> receivers=new ArrayList<>();
    public IHttpResponse getCachedAndFireHttpRequest(RegisteredHttpAccessRequest registeredHttpAccessRequest, ArrayList<IHttpResponseReceiver> receivers);
    
    public void notifyHTTPResponseReceivers();
}
