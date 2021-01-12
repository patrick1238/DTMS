/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.webapp_client;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.util.StringConverter;

/**
 *
 * @author rehkind
 */
public class APPLICATION_DEFAULTS {
    public static String APPLICATION_NAME="DTMS CaseManager";
    public static String VERSION_NUMBER="0.0.1 (alpha)";
    
    public static int MAIN_WINDOW_WIDTH=900;
    public static int MAIN_WINDOW_HEIGHT=700;
    
    final public static int SERVICE_DEFINITION_ID_2D=3;
    final public static int SERVICE_DEFINITION_ID_3D=4;
    final public static int SERVICE_DEFINITION_ID_4D=5;
    final public static int SERVICE_DEFINITION_ID_GENOMIC=6;
    final public static int SERVICE_DEFINITION_ID_METHYLATION=7;
    
    static final public Locale DEFAULT_LOCALE=Locale.GERMANY;
    static final public SimpleDateFormat DEFAULT_DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy HH:mm:ssZ");
    static final public SimpleDateFormat DEFAULT_DATE_SHORT_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");
    static final public DateTimeFormatter DEFAULT_DATETIME_SHORT_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    static final public StringConverter<LocalDate> DEFAULT_DATE_CONVERTER = new StringConverter<LocalDate>(){
        DateTimeFormatter f=DEFAULT_DATETIME_SHORT_FORMATTER.withLocale(DEFAULT_LOCALE);
        @Override
        public String toString(LocalDate ld) {
            try{
                if( ld==null ){ return ""; }
                String d = ld.getDayOfMonth()+".";
                d = (d.length()<2) ? "0"+d : d;
                String m = ld.getMonthValue() + ".";
                m = (m.length()<3) ? "0"+m : m;
                String y = ld.getYear()+"";
                
                return  d + m + y;
            }catch( Exception ex ){
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        public LocalDate fromString(String asString) {
            if( asString.contains(" ") ){ // if string contains Time + TimeZone information cut it off
                asString = asString.split(" ")[0];
            }
            LocalDate date = LocalDate.parse(asString, f);
            return date;
        }
    
    };
}
