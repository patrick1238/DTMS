/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.http;

import net.rehkind_mag.http.IBufferedEndpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.rehkind_mag.interfaces.IHttpConnector;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.interfaces.IHttpResponseReceiver;
import net.rehkind_mag.utils.HTTP_STATUS;
import net.rehkind_mag.utils.HttpAccessRequest;
import net.rehkind_mag.utils.RegisteredHttpAccessRequest;
import net.rehkind_mag.utils.ServerSettings;

/**
 *
 * @author rehkind
 */
public class HttpRequestManager implements IHttpResponseReceiver, IHttpConnector{
    private static int UNIQUE_ID=0;
    private static HashMap<HttpRequestManagerName, HttpRequestManager> singletonManagerMap = new HashMap<>();
    
    private HashMap<String, IBufferedEndpoint> bufferedEndpoints = new HashMap<>();
    
    private Set<Integer> pendingHttpRequests=new HashSet<>();
    
    ServerSettings settings=ServerSettings.getDefaultServerSettings();
    
    static public final String CONTANT_TYPE_JSON = "application/json";
    static public final String CONTANT_TYPE_XML = "application/xml";
    
    static public HttpRequestManager createHttpRequestManager(String serverAddress, String contentType){
        HttpRequestManagerName key = new HttpRequestManagerName(serverAddress, contentType);
        if( singletonManagerMap.get( key )== null ){
            singletonManagerMap.put(key, new HttpRequestManager(serverAddress, contentType));
        }
        return singletonManagerMap.get( key );
    }

    private HttpRequestManager( String serverAddress, String contentType ){
        settings.serverAddress = serverAddress;
        settings.contentType = contentType;
    }
    
    @Override
    public void setServerAddress(String address){
        settings.serverAddress=address;
    }
    
    @Override
    public void setConnectionTimeOut(Integer timeOut){
        settings.connectionTimeOut=timeOut;
    }
        
    @Override
    public void setReadTimeOut(Integer timeOut){
        settings.readTimeOut=timeOut;
    }
    
    @Override
    public String getServerAddress(){
        return settings.serverAddress;
    }
    
    @Override
    public Integer getConnectionTimeOut(){
        return settings.connectionTimeOut;
    }
        
    @Override
    public Integer getReadTimeOut(){
        return settings.readTimeOut;
    }
    
    /**
     * Method fires a http request via IBufferedEndpoints. Immediatly returns cached IHttpResponse if possible.
     * In addition IBufferedEndpoints performs the actual HTTPRequest and propagates the result to the
     * IHttpResponseReceiver (TODO: check if the response is altered -> receiver can ignore update)
     * @param request the actual http request: login, uuid, body
     * @param receiver client side object that consumes the HTTPResponse
     * @return returns the cached IHttpResponse immediatly (null if nothing is cached yet)
     */
    synchronized public IHttpResponse fireJsonHttpRequest(HttpAccessRequest request, IHttpResponseReceiver receiver){
        ArrayList<IHttpResponseReceiver> receivers=new ArrayList<>();
        receivers.add(receiver);
        receivers.add(this);
        
        int assignedID = UNIQUE_ID;
        UNIQUE_ID++;
        
        String endpointIdent=request.getEndpoint();
        IBufferedEndpoint endpoint = BufferedEndpointFactory.getBufferedEndpoint(endpointIdent, this.settings);
        
        request.setServerSettings(settings.clone());
        
        IHttpResponse cachedResponse = endpoint.getCachedAndFireHttpRequest(new RegisteredHttpAccessRequest(request, assignedID), receivers);
        if(cachedResponse!=null){
            cachedResponse=cachedResponse.clone(assignedID, HTTP_STATUS.CACHED);
        }else{
            return new NotCachedHttpResponse(assignedID);
        }
        return cachedResponse;
    }

    @Override
    public void receiveHttpResponse( IHttpResponse response) {
        System.out.println("http response for request "+response.getRequestId()+" received.");
        this.pendingHttpRequests.remove(response.getRequestId());
        System.out.println(this.pendingHttpRequests.size()+" pending requests remaining");
    }
    
    

    
    static private class HttpRequestManagerName {
        final private String address;
        final private String contantType;
        
        private HttpRequestManagerName(String address, String contentType){
            this.address=address;
            this.contantType=contentType;
        }
        
        @Override
        public boolean equals(Object o){
            HttpRequestManagerName o2;
            if( o.getClass() == this.getClass() ){ return false; }
            return Objects.equals(this.address, ((HttpRequestManagerName)o).address) && Objects.equals(this.contantType, ((HttpRequestManagerName)o).contantType);
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 29 * hash + Objects.hashCode(this.address);
            return hash;
        }

    }
}
