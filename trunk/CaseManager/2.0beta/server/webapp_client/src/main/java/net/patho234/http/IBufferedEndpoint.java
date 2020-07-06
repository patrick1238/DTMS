/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.http;

import java.util.ArrayList;
import net.patho234.interfaces.IHttpResponse;
import net.patho234.interfaces.IHttpResponseReceiver;
import net.patho234.utils.RegisteredHttpAccessRequest;

/**
 *
 * @author rehkind
 */
public interface IBufferedEndpoint {
    public ArrayList<IHttpResponseReceiver> receivers=new ArrayList<>();
    public IHttpResponse getCachedAndFireHttpRequest(RegisteredHttpAccessRequest registeredHttpAccessRequest, ArrayList<IHttpResponseReceiver> receivers);
    
    public void notifyHTTPResponseReceivers();
}
