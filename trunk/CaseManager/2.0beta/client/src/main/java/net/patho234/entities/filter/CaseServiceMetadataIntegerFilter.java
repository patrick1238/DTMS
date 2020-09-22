/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.entities.filter;

import java.util.List;
import java.util.Objects;
import javafx.beans.property.SimpleIntegerProperty;
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
public class CaseServiceMetadataIntegerFilter  extends ClientObjectFilterBase{
    static public final int MODE_EQUALS=0;
    static public final int MODE_MIN=1;
    static public final int MODE_MAX=2;
    static public final int MODE_MIN_MAX=3;
    
    String metadataFieldName=null;
    
    Integer minValue=null;
    Integer maxValue=null;
    Integer eqValue=null;
    
    String serviceType=null;
    SimpleIntegerProperty filterMode = new SimpleIntegerProperty( MODE_MIN );

    public CaseServiceMetadataIntegerFilter(String metadataFieldName, String serviceType){
        this(metadataFieldName, serviceType, 0);
    }
    
    /**
     *
     * @param metadataFieldName
     * @param serviceType
     * @param filterMode
     */
    public CaseServiceMetadataIntegerFilter(String metadataFieldName, String serviceType, Integer filterMode){
        this(metadataFieldName, serviceType, filterMode, new Integer[]{0, Integer.MAX_VALUE, null});
    }
    
    public CaseServiceMetadataIntegerFilter(String metadataFieldName, String serviceType, Integer filterMode, Integer[] filterValues){
        this.metadataFieldName = metadataFieldName;
        
        this.minValue = filterValues[0];
        this.maxValue = filterValues[1];
        this.eqValue = filterValues[2];
        System.out.println("@INIT_FILTER: min="+minValue+" max="+maxValue+" eq="+eqValue);
        this.serviceType = serviceType;
        
        filterMode = (filterMode==null) ? MODE_MIN : filterMode;
        setSearchMode( filterMode );
    }
    
    public void setMinValue(Integer newMin){
        if( Objects.equals( this.minValue, newMin) ){ return; }
        this.minValue = newMin;
        System.out.println("@NEW_VALUE MIN= "+newMin);
        notifyAllListeners();
    }
    
    public void setMaxValue(Integer newMax){
        if( Objects.equals( this.maxValue, newMax) ){ return; }
        this.maxValue = newMax;
        System.out.println("@NEW_VALUE MAX= "+newMax);
        notifyAllListeners();
    }
    
    public void setEqualValue(Integer newEq){
        if( Objects.equals( this.eqValue, newEq) ){ return; }
        this.eqValue = newEq;
        System.out.println("@NEW_VALUE EQ= "+newEq);
        notifyAllListeners();
    }
    
    @Override
    public boolean isClientObjectInScope(ClientObjectBase clientObject) {
        if( filterMode.getValue().equals(MODE_MIN) && minValue==null ){ return true; }
        if( filterMode.getValue().equals(MODE_MAX) && maxValue==null ){ return true; }
        if( filterMode.getValue().equals(MODE_EQUALS) && eqValue==null ){ return true; }
        if( filterMode.getValue().equals(MODE_MIN_MAX) && (minValue==null && maxValue==null) ){ return true; }
        
        Logger.getLogger(getClass()).info("isClientObjectInScope() - performing check: "+toString());
        
        
        if( clientObject.getClass().equals(ClientCase.class) ){
            List<IService> services = ((ClientCase)clientObject).getServices();
            for(IService service:services){
                for(IMetadata m : service.getMetadata()){
                    if( m.getName().equals(metadataFieldName) ){
                        if (checkConstrained(m)){
                            Logger.getLogger(getClass()).info("@ACCEPTED:");
                            Logger.getLogger(getClass()).info("          Case: "+service.getCase().getCaseNumber()+" Service: "+service.getServiceDefinition().getName()+" Meta: "+m.getName()+"="+m.getData());
                            return true;
                        }else{
                            Logger.getLogger(getClass()).info("@REJECTED:");
                            Logger.getLogger(getClass()).info("          Case: "+service.getCase().getCaseNumber()+" Service: "+service.getServiceDefinition().getName()+" Meta: "+m.getName()+"="+m.getData());
                        }
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
            throw new ClassCastException("CaseServiceMetadataIntegerFilter only excepts ClientCase and ClientService objects. Filter item class: "+clientObject.getClass().getName());
        }
        
    }
    
    public void setSearchMode(Integer mode){
        this.filterMode.setValue( mode != null ? ((mode<4)? mode : 0) : MODE_MIN );
        notifyAllListeners();
    }
    public Integer getSearchMode(){ return this.filterMode.getValue(); }
    
    public void setServiceType(String serviceType){ this.serviceType = serviceType; }
    
    private boolean checkConstrained(IMetadata m) {
//        Logger.getLogger(getClass()).info("+++ Checking contraint on "+m.getName()+" - values are: '"+searchTerm+"' & '"+m.getData().toString()+"'");
        if( serviceType != null ){
            if( !m.getService().getServiceDefinition().getName().equals(this.serviceType)){
                Logger.getLogger(getClass()).info("+++ ClientObject is rejected - wrong Service TYPE (found: "+m.getService().getServiceDefinition().getName()+" expected: "+this.serviceType+")");
                return false;
            }
        }
        Integer tmpValueToCheck=null;
        try {
            tmpValueToCheck=(Integer)m.getData();
        } catch (ClassCastException e) {
            // can not fullfil constrains cause is no Integer / should actually not happen
            Logger.getLogger(getClass()).info("+++ ClientObject is rejected - cannot be casted to Integer ( "+m.getData()+" )");
            return false;
        }
        boolean minCheck;
        boolean maxCheck;
        switch( filterMode.getValue() ){
            case MODE_MIN:
                String checkResult = ( minValue==null ) ? "minValue="+minValue + " check: -":"minValue="+minValue + " check: "+(tmpValueToCheck >= minValue);
                Logger.getLogger(getClass()).info("+++ Performing min-check: "+checkResult+" ");
                return ( minValue==null ) ? true : tmpValueToCheck >= minValue;
            case MODE_MAX:
                return ( maxValue==null ) ? true : tmpValueToCheck <= maxValue;
            case MODE_MIN_MAX:
                minCheck = ( minValue==null ) ? true : tmpValueToCheck >= minValue;
                maxCheck = ( maxValue==null ) ? true : tmpValueToCheck <= maxValue;
                return minCheck && maxCheck;
            case MODE_EQUALS:
                String checkResult2 = ( eqValue==null ) ? "minValue="+eqValue + " check: -":"eqValue="+eqValue + " check: "+(tmpValueToCheck == eqValue);
                Logger.getLogger(getClass()).info("+++ Performing min-check: "+checkResult2+" ");
                return ( eqValue==null ) ? true : tmpValueToCheck == eqValue;
            default:
                Logger.getLogger(getClass()).warn("+++ filterMode of CaseServiceMetadataIntegerFilter is set to an unknown value ('"+filterMode+"'), will return false by default for all items.");
                return false;
        }
    }
    
    static public Integer getModeFromString(String modeAsString){
        switch ( modeAsString ){
            case "min":
                return MODE_MIN;
            case "max":
                return MODE_MAX;
            case "min_max":
                return MODE_MIN_MAX;
            case "equals":
                return MODE_EQUALS;
            default:
                Logger.getLogger(CaseServiceMetadataIntegerFilter.class).warn("String '"+modeAsString+"' cannot be converted into a proper filter mode -> returning NULL");
                return null;
        }
    }
    public String getModeAsString(){
        switch ( filterMode.getValue() ){
            case MODE_MIN:
                return "min";
            case MODE_MAX:
                return "max";
            case MODE_MIN_MAX:
                return "min_max";
            case MODE_EQUALS:
                return "equals";
            default:
                Logger.getLogger(CaseServiceMetadataIntegerFilter.class).warn("Mode '"+filterMode.getValue()+"' cannot be converted into a proper filter mode string -> returning NULL");
                return null;
        }
    }
    
    @Override
    public String toString(){
        return "MetadataIntegerFilter@"+getModeAsString()+"?"+minValue+":"+maxValue+":"+eqValue;
    }
}
