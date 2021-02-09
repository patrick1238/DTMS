/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements.validator;

import javafx.util.StringConverter;
import net.patho234.interfaces.IMetadata;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class MetadataStringConverter extends StringConverter{
    IMetadata meta;
    public MetadataStringConverter(IMetadata meta){
        this.meta=meta;
    }
    @Override
    public String toString(Object object) {
        if(meta.getName().toLowerCase().contains("date")){ 
            Logger.getLogger(getClass()).warn("MetadataStringConverter.toString() treats dates currently as String - TODO: handle casting downstream or here '@DATE_HANDLE_POINT'");
        }
        if(object == null){ return ""; }
        return object.toString();
    }

    @Override
    public Object fromString(String string) {
        if(string==null){ return null; }
        if(string.equals("")){ return null; }
        
        switch ( meta.getType() ) {
            case INTEGER:
                return Integer.valueOf(string);
            case DOUBLE:
                return Double.valueOf(string);
            case STRING:
            case TEXT:
            case URL:
                return string;
            case UNDEFINED:
            default:
                Logger.getLogger(getClass()).warn("MetadataStringConverter.fromString() called for UNDEFINED or UNKNOWN metadata type...returning NULL");
                Logger.getLogger(getClass()).warn("MetadataStringConverter.fromString() meta is: "+meta);
                Logger.getLogger(getClass()).warn("MetadataStringConverter.fromString() string is: "+string);
                return null;
        }
    }
    
}
