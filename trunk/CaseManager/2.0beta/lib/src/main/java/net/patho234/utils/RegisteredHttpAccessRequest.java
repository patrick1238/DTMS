/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.utils;

/**
 *
 * @author rehkind
 */
public class RegisteredHttpAccessRequest extends HttpAccessRequest{
    final Integer id;
    public RegisteredHttpAccessRequest(HttpAccessRequest request, int ID){
        this.body=request.body;
        this.compiledEndpoint=request.compiledEndpoint;
        this.endpoint=request.endpoint;
        this.submitter=request.submitter;
        this.uuid=request.uuid;
        this.settings=request.settings;
        this.id=ID;
    }
    
    public int getId(){ return id; }
}
