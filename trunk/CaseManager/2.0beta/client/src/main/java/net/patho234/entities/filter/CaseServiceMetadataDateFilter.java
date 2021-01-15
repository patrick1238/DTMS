/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import net.patho234.entities.ClientCase;
import net.patho234.entities.ClientObjectBase;
import net.patho234.entities.ClientService;
import net.patho234.interfaces.IMetadata;
import net.patho234.interfaces.IService;
import net.patho234.webapp_client.APPLICATION_DEFAULTS;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class CaseServiceMetadataDateFilter  extends ClientObjectFilterBase{
    String metadataFieldName=null;
    String searchTerm="";
    Date startDate;
    Date endDate;
    String serviceType=null;
    String filterMode="from";
    public final static String MODE_FROM="from";
    public final static String MODE_TO="to";
    public final static String MODE_FROM_TO="from_to";
    public final static String[] MODES=new String[]{MODE_FROM,MODE_TO,MODE_FROM_TO};
    public CaseServiceMetadataDateFilter(String metadataFieldName){
        this(metadataFieldName, "");
    }
    
    /**
     *
     * @param metadataFieldName
     * @param searchTerm
     */
    public CaseServiceMetadataDateFilter(String metadataFieldName, String searchTerm){
        this(metadataFieldName, searchTerm, MODE_FROM);
    }
    
    public CaseServiceMetadataDateFilter(String metadataFieldName, String searchTerm, String filterMode){
        this.metadataFieldName = metadataFieldName;
        searchTerm = (searchTerm==null) ? "" : searchTerm;
        this.searchTerm = searchTerm;
        filterMode = (filterMode==null) ? "" : filterMode;
        this.filterMode = filterMode;
        parseSearchTerm();
    }
    public void setSearch(String newSearchTerm){
        newSearchTerm = (newSearchTerm==null) ? "" : newSearchTerm;
        this.searchTerm = newSearchTerm;
        parseSearchTerm();
        notifyAllListeners();
    }
    
    @Override
    public boolean isClientObjectInScope(ClientObjectBase clientObject) {
        if(searchTerm.equals("")){ return true; }
        
        if( clientObject.getClass().equals(ClientCase.class) ){
            List<IService> services = ((ClientCase)clientObject).getServices();
            for(IService service:services){
                for(IMetadata m : service.getMetadata()){
                    if( m.getName().equals(metadataFieldName) ){
                        if (checkConstrained(m)){ return true; }
                    }
                }
            }
            return false;
        } else if ( clientObject.getClass().equals(ClientService.class) ){
            IService service = ((ClientService)clientObject);
            for(IMetadata m : service.getMetadata()){
                if( m.getName().equals(metadataFieldName) ){
                    return checkConstrained(m);
                }
            }
            return false;
        } else {
            throw new ClassCastException("CaseServiceMetadataDateFilter only excepts ClientCase and ClientService objects. Filter item class: "+clientObject.getClass().getName());
        }
        
    }
    
    public void setSearchMode(String mode){ this.filterMode = mode; notifyAllListeners(); }
    public void setSearchMode(Integer mode){
        setSearchMode(CaseServiceMetadataDateFilter.MODES[mode%3] );
    }
    public Integer getSearchMode(){
        switch( filterMode ){
            case MODE_FROM:
                return 0;
            case MODE_TO:
                return 1;
            case MODE_FROM_TO:
                return 2;
            default:
                return -1;
        }
    }

    public void setServiceType(String serviceType){ this.serviceType = serviceType; }
    
    private boolean checkConstrained(IMetadata m) {
//        Logger.getLogger(getClass()).info("+++ Checking contraint on "+m.getName()+" - values are: '"+searchTerm+"' & '"+m.getData().toString()+"'");
        if( serviceType != null ){
            if( !m.getService().getServiceDefinition().getName().equals(this.serviceType)){
//                Logger.getLogger(getClass()).info("+++ ClientObject is rejected - wrong Service TYPE (found: "+m.getService().getServiceDefinition().getName()+" expected: "+this.serviceType+")");
                return false;
            }
        }
        String tmpValueToCheck=null;
        try {
            tmpValueToCheck=(String)m.getData();
        } catch (ClassCastException e) {
            // can not fullfil constrains cause is no String / should actually not happen
            return false;
        }
        tmpValueToCheck=(this.filterMode.endsWith("_cs"))?tmpValueToCheck:tmpValueToCheck.toLowerCase();
        
        switch( filterMode ){
            case MODE_FROM:
                // TODO: validate and return bool

            case MODE_TO:
                // TODO: validate and return bool
                
            case MODE_FROM_TO:
                // TODO: validate and return bool
            default:
                Logger.getLogger(getClass()).warn("+++ filterMode of CaseServiceMetadataStringFilter is set to an unknown value ('"+filterMode+"'), will return false by default for all items.");
                return false;
        }
    }
    
    @Override
    public String toString(){
        return "MetadataDateFilter@"+filterMode+"?"+searchTerm;
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
}
