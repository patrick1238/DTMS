/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class DateParser {
    static final private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ssZ");
    static final private SimpleDateFormat df_short = new SimpleDateFormat("dd.MM.yyyy");
    static final private SimpleDateFormat df_EN = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
    static final private SimpleDateFormat df_short_EN = new SimpleDateFormat("yyyy-MM-dd");
    
    static final public Date parse(String dateAsString){
        Date returnDate = null;
        // date+time
        if( dateAsString.contains(" ") ){
            if(dateAsString.contains(".")){ // german
                try{
                    returnDate=df.parse(dateAsString);
                }catch(ParseException ex){
                    Logger.getLogger(DateParser.class).warn("Date '"+dateAsString+"' could not be parsed (long+DE)");
                }
            }else{ // english
                try{
                    returnDate=df_EN.parse(dateAsString);
                }catch(ParseException ex){
                    Logger.getLogger(DateParser.class).warn("Date '"+dateAsString+"' could not be parsed (long+EN)");
                }
            }
        } else { // date only:
            if(dateAsString.contains(".")){ // german
                try{
                    returnDate=df_short.parse(dateAsString);
                }catch(ParseException ex){
                    Logger.getLogger(DateParser.class).warn("Date '"+dateAsString+"' could not be parsed (short+DE)");
                }
            }else{ // english
                try{
                    returnDate=df_short_EN.parse(dateAsString);
                }catch(ParseException ex){
                    Logger.getLogger(DateParser.class).warn("Date '"+dateAsString+"' could not be parsed (short+EN)");
                }
            }
        }
        return returnDate;
    }
    
    public final static String format(Date d){
        return df.format(d);
    }
}
