/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.interfaces;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author rehkind
 * @param <T> type of the responses content (e.g. JsonObject)
 */
public interface IHttpResponse<T> {
    public boolean responseSucceeded();
    public List<String> getHeaderFields();
    public int getResponseStatus();
    public T getContent();
    public String getMessage();
    public int getRequestId();
    public IHttpResponse clone(int cloneId, int statusOverride);
    
    static public List<String> parseHeader( HttpURLConnection con ){
        List<String> headerFieldList = new ArrayList<>();
        con.getHeaderFields().entrySet().stream()
            .filter(entry -> entry.getKey() != null)
            .forEach(entry -> {
                String newField = entry.getKey()+":";
                
            List headerValues = entry.getValue();
            Iterator it = headerValues.iterator();
            if (it.hasNext()) {
                newField+=it.next();
                while (it.hasNext()) {
                    newField+=",";
                }
            }
            
            headerFieldList.add( newField );
            });
        return headerFieldList;
    }
    
}
