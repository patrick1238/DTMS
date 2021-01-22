/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements.validator;

import java.util.HashMap;
import java.util.Map.Entry;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class ValidationResult {
    public static final int CODE_VALID=0;
    public static final int CODE_MINOR_CONCERNS=1;
    public static final int CODE_MAJOR_CONCERNS=2;
    public static final int CODE_INVALID=9999;
    
    
    private int validationCode=0;
    HashMap<String,String> validChecks=new HashMap<>();
    HashMap<String,String> minorConcernChecks=new HashMap<>();
    HashMap<String,String> majorConcernChecks=new HashMap<>();
    HashMap<String,String> invalidChecks=new HashMap<>();
    
    public int getValidationCode(){ return validationCode; }
    
    public void addValidationCheck(int validationCode, String key, String message){
        switch( validationCode ){
            case CODE_VALID:
                validChecks.put(key, message);
                break;
            case CODE_MINOR_CONCERNS:
                minorConcernChecks.put(key, message);
                break;
            case CODE_MAJOR_CONCERNS:
                majorConcernChecks.put(key, message);
                break;
            case CODE_INVALID:
                invalidChecks.put(key, message);
                break;
            default:
                Logger.getLogger(getClass()).error("Validation code '"+validationCode+"' is unknown and will be ignored...");
                return;
        }
        
        if(this.validationCode<validationCode){ this.validationCode = validationCode; }
    }
    
    public String toString(int minCode){
        StringBuilder sb = new StringBuilder();
        if( minCode <= CODE_VALID ){
            sb.append("---  valid checks  ---\n");
            this.validChecks.entrySet().forEach((e) -> {
                sb.append("  ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
            });
        }
        if( minCode <= CODE_MINOR_CONCERNS ){
            if(sb.length()>0){ sb.append("\n"); }
            sb.append("--- minor concerns ---\n");
            this.minorConcernChecks.entrySet().forEach((e) -> {
                sb.append("  ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
            });
        }
        if( minCode <= CODE_MAJOR_CONCERNS ){
            if(sb.length()>0){ sb.append("\n"); }
            sb.append("--- major concerns ---\n");
            this.majorConcernChecks.entrySet().forEach((e) -> {
                sb.append("  ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
            });
        }
        if(sb.length()>0){ sb.append("\n"); }
        sb.append("--- invalid checks ---\n");
        this.invalidChecks.entrySet().forEach((e) -> {
            sb.append("  ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
        });
        
        return sb.toString();
    }
    
    @Override
    public String toString(){
        return toString(0);
    }
}
