/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import java.util.List;
import net.patho234.entities.ClientCase;
import net.patho234.entities.ClientObjectBase;
import net.patho234.entities.ClientService;
import net.patho234.interfaces.IMetadata;
import net.patho234.interfaces.IService;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class CaseServiceMetadataStringFilter  extends ClientObjectFilterBase{
    String metadataFieldName=null;
    String searchTerm="";
    String serviceType=null;
    String filterMode="contains";
    public final String MODE_EQUALS="equals";
    public final String MODE_EQUALS_CASE_SENSITIVE="equals_cs";
    public final String MODE_STARTS_WITH="starts_with";
    public final String MODE_STARTS_WITH_CASE_SENSITIVE="starts_with_cs";
    public final String MODE_CONTAINS="contains";
    public final String MODE_CONTAINS_CASE_SENSITIVE="contains_cs";
    
    public CaseServiceMetadataStringFilter(String metadataFieldName){
        this(metadataFieldName, "");
    }
    
    /**
     *
     * @param metadataFieldName
     * @param searchTerm
     */
    public CaseServiceMetadataStringFilter(String metadataFieldName, String searchTerm){
        this(metadataFieldName, searchTerm, "contains");
    }
    
    public CaseServiceMetadataStringFilter(String metadataFieldName, String searchTerm, String filterMode){
        this.metadataFieldName = metadataFieldName;
        searchTerm = (searchTerm==null) ? "" : searchTerm;
        this.searchTerm = searchTerm;
        filterMode = (filterMode==null) ? "" : filterMode;
        this.filterMode = filterMode;
    }
    public void setSearch(String newSearchTerm){
        newSearchTerm = (newSearchTerm==null) ? "" : newSearchTerm;
        this.searchTerm = newSearchTerm;
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
            throw new ClassCastException("CaseServiceMetadataStringFilter only excepts ClientCase and ClientService objects. Filter item class: "+clientObject.getClass().getName());
        }
        
    }
    
    public void setSearchMode(String mode){ this.filterMode = mode; }

    public void setServiceType(String serviceType){ this.serviceType = serviceType; }
    
    private boolean checkConstrained(IMetadata m) {
//        Logger.getLogger(getClass()).info("+++ Checking contraint on "+m.getName()+" - values are: '"+searchTerm+"' & '"+m.getData().toString()+"'");
        if( serviceType != null ){
            if( !m.getService().getServiceDefinition().getName().equals(this.serviceType)){
//                Logger.getLogger(getClass()).info("+++ ClientObject is rejected - wrong Service TYPE (found: "+m.getService().getServiceDefinition().getName()+" expected: "+this.serviceType+")");
                return false;
            }
        }
        String tmpSearchTerm=(this.filterMode.endsWith("_cs"))?this.searchTerm:this.searchTerm.toLowerCase();
        String tmpValueToCheck=null;
        try {
            tmpValueToCheck=(String)m.getData();
        } catch (ClassCastException e) {
            // can not fullfil constrains cause is no String / should actually not happen
            return false;
        }
        tmpValueToCheck=(this.filterMode.endsWith("_cs"))?tmpValueToCheck:tmpValueToCheck.toLowerCase();
        
        String[] tmpSplitSearchTerm = tmpSearchTerm.split(";");
        while(tmpSearchTerm.endsWith(";")){ tmpSearchTerm=tmpSearchTerm.substring(0, tmpSearchTerm.length()-1); }
        switch( filterMode ){
            case MODE_CONTAINS:
            case MODE_CONTAINS_CASE_SENSITIVE:
                if(tmpSplitSearchTerm.length==1){
//                    Logger.getLogger(getClass()).info("+++ CONTAINS: ["+tmpValueToCheck.contains(tmpSearchTerm)+"]");
                    return tmpValueToCheck.contains(tmpSearchTerm);
                }else{
                    for( String oneOfMultipleSearchTerms : tmpSplitSearchTerm ){
                        if( oneOfMultipleSearchTerms.equals("") ){ continue; }
                        if( tmpValueToCheck.contains(oneOfMultipleSearchTerms) ){ return true; }
                    }
                    return false;
                }
            case MODE_STARTS_WITH:
            case MODE_STARTS_WITH_CASE_SENSITIVE:
                if(tmpSplitSearchTerm.length==1){
//                    Logger.getLogger(getClass()).info("+++ STARTS_WITH: ["+tmpValueToCheck.startsWith(tmpSearchTerm)+"]");
                    return tmpValueToCheck.startsWith(tmpSearchTerm);
                }else{
                    for( String oneOfMultipleSearchTerms : tmpSplitSearchTerm ){
                        if( oneOfMultipleSearchTerms.equals("") ){ continue; }
                        if( tmpValueToCheck.startsWith(oneOfMultipleSearchTerms) ){ return true; }
                    }
                    return false;
                }
            case MODE_EQUALS:
            case MODE_EQUALS_CASE_SENSITIVE:
                if(tmpSplitSearchTerm.length==1){
//                    Logger.getLogger(getClass()).info("+++ EQUALS: ["+tmpValueToCheck.equals(tmpSearchTerm)+"]");
                    return tmpValueToCheck.equals(tmpSearchTerm);
                }else{
                    for( String oneOfMultipleSearchTerms : tmpSplitSearchTerm ){
                        if( oneOfMultipleSearchTerms.equals("") ){ continue; }
                        if( tmpValueToCheck.startsWith(oneOfMultipleSearchTerms) ){ return true; }
                    }
                    return false;
                }
            default:
                Logger.getLogger(getClass()).warn("+++ filterMode of CaseServiceMetadataStringFilter is set to an unknown value ('"+filterMode+"'), will return false by default for all items.");
                return false;
        }
    }
    
    @Override
    public String toString(){
        return "MetadataStringFilter@"+filterMode+"?"+searchTerm;
    }
}
