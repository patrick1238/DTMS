/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import java.text.ParseException;
import java.util.Date;
import net.patho234.entities.ClientCase;
import net.patho234.webapp_client.APPLICATION_DEFAULTS;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class CaseEntryDateFilter  extends ClientObjectFilterBase<ClientCase>{
    String searchTerm="";
    Date startDate;
    Date endDate;
    /**
     * defines a new search term
     * @param newSearchTerm String of form: "" | null | <DATE> | <DATE>;<DATE> (see parseSearchTerm)
     */
    public void setSearch(String newSearchTerm){
        newSearchTerm = (newSearchTerm==null) ? "" : newSearchTerm;
        this.searchTerm = newSearchTerm.toLowerCase();
        parseSearchTerm();
        notifyAllListeners();
    }
    
    private void parseSearchTerm(){
        // no boundaries for date set:
        if( searchTerm == "" || searchTerm == null ){
            startDate=null;
            endDate=null;
            return;
        }
        // single boundary set (interpreted as start date)
        if(!searchTerm.contains(";")){
            try {
                startDate = APPLICATION_DEFAULTS.DEFAULT_DATE_SHORT_FORMATTER.parse(searchTerm);
            }catch(ParseException pEx){
                startDate=null;
                Logger.getLogger(getClass()).warn("Could not parse searchTerm "+searchTerm+" setting startDate to NULL.");
            }
            endDate = null;
        }
            
        // upper and lower boundary set
        try {
            startDate = APPLICATION_DEFAULTS.DEFAULT_DATE_SHORT_FORMATTER.parse(searchTerm.split(";")[0]);
        }catch(ParseException pEx){
            startDate=null;
            Logger.getLogger(getClass()).warn("Could not parse searchTerm "+searchTerm+" setting startDate to NULL.");
        }
        try {
            endDate = APPLICATION_DEFAULTS.DEFAULT_DATE_SHORT_FORMATTER.parse(searchTerm.split(";")[1]);
        }catch(ParseException pEx){
            endDate=null;
            Logger.getLogger(getClass()).warn("Could not parse searchTerm "+searchTerm+" setting endDate to NULL.");
        }
    }
    
    @Override
    public boolean isClientObjectInScope(ClientCase clientObject) {
        Boolean isInScope=true;
        Date entryDate = clientObject.getEntryDate();
        isInScope = (startDate != null) ? isInScope && entryDate.getTime() >= startDate.getTime() : isInScope;
        isInScope = (endDate != null) ? isInScope && entryDate.getTime() <= endDate.getTime() : isInScope;
        return isInScope;
    }
    
}
