/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.http;

import java.util.ArrayList;
import net.patho234.interfaces.IHttpResponse;
import net.patho234.interfaces.IHttpResponseReceiver;

/**
 *
 * @author rehkind
 */
abstract public class BufferedEndpoint implements IBufferedEndpoint, IHttpResponseReceiver {
    ArrayList<IHttpResponseReceiver> responseReceivers=new ArrayList<>();
    IHttpResponse response;
    
    @Override
    public void notifyHTTPResponseReceivers() {
        for( IHttpResponseReceiver receiver : responseReceivers){
            receiver.receiveHttpResponse(response);
        }
    }
}
