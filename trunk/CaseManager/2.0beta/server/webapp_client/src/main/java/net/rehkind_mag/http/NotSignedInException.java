/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.http;

/**
 *
 * @author rehkind
 */
public class NotSignedInException extends Exception{
    String message;
    public NotSignedInException(String msg){
        this.message = msg;
    }
    
    @Override public String getMessage(){ return message; }
}
