/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.http;

import java.util.ArrayList;
import java.util.List;
import net.patho234.interfaces.IHttpResponse;
import net.patho234.utils.HTTP_STATUS;

/**
 *
 * @author rehkind
 */
public class NotCachedHttpResponse implements IHttpResponse<Object>{
    List<String> headerFields=new ArrayList<>();
    int status = HTTP_STATUS.NOT_CACHED;
    int requestId;
    String message;
    
    public NotCachedHttpResponse(int requestId){ this.requestId=requestId; }
    
    @Override
    public List<String> getHeaderFields() {
        return headerFields;
    }

    @Override
    public int getResponseStatus() {
        return status;
    }

    @Override
    public Object getContent() {
        return null;
    }

    @Override
    public String getMessage() {
        return "No cached object found for request. [requestId="+requestId+"]";
    }

    @Override
    public int getRequestId() {
        return requestId;
    }

    @Override
    public IHttpResponse clone(int cloneId, int status) {
        return this;
    }

    @Override
    public boolean responseSucceeded() {
        return false;
    }
    
}
