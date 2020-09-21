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
    public final int MODE_EQUALS=0;
    public final int MODE_MIN=1;
    public final int MODE_MAX=2;
    public final int MODE_MIN_MAX=3;
    
    String metadataFieldName=null;
    
    Integer minValue=0;
    Integer maxValue=Integer.MAX_VALUE;
    Integer eqValue=null;
    
    String serviceType=null;
    SimpleIntegerProperty filterMode = new SimpleIntegerProperty( MODE_MIN );

    public CaseServiceMetadataIntegerFilter(String metadataFieldName){
        this(metadataFieldName, 0);
    }
    
    /**
     *
     * @param metadataFieldName
     * @param filterMode
     */
    public CaseServiceMetadataIntegerFilter(String metadataFieldName, Integer filterMode){
        this(metadataFieldName, filterMode, new Integer[]{0, Integer.MAX_VALUE, null});
    }
    
    public CaseServiceMetadataIntegerFilter(String metadataFieldName, Integer filterMode, Integer[] filterValues){
        this.metadataFieldName = metadataFieldName;
        
        this.minValue = filterValues[0];
        this.maxValue = filterValues[1];
        this.eqValue = filterValues[2];
        
        filterMode = (filterMode==null) ? MODE_MIN : filterMode;
        this.filterMode.setValue( filterMode );
    }
    
    public void setMinValue(Integer newMin){
        this.minValue = newMin;
        notifyAllListeners();
    }
    
    @Override
    public boolean isClientObjectInScope(ClientObjectBase clientObject) {
        if( filterMode.equals(MODE_MIN) && minValue==null ){ return true; }
        if( filterMode.equals(MODE_MAX) && maxValue==null ){ return true; }
        if( filterMode.equals(MODE_EQUALS) && eqValue==null ){ return true; }
        
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
            throw new ClassCastException("CaseServiceMetadataIntegerFilter only excepts ClientCase and ClientService objects. Filter item class: "+clientObject.getClass().getName());
        }
        
    }
    
    public void setSearchMode(Integer mode){ this.filterMode.setValue( mode != null ? mode : MODE_MIN ); }

    public void setServiceType(String serviceType){ this.serviceType = serviceType; }
    
    private boolean checkConstrained(IMetadata m) {
//        Logger.getLogger(getClass()).info("+++ Checking contraint on "+m.getName()+" - values are: '"+searchTerm+"' & '"+m.getData().toString()+"'");
        if( serviceType != null ){
            if( !m.getService().getServiceDefinition().getName().equals(this.serviceType)){
//                Logger.getLogger(getClass()).info("+++ ClientObject is rejected - wrong Service TYPE (found: "+m.getService().getServiceDefinition().getName()+" expected: "+this.serviceType+")");
                return false;
            }
        }
        Integer tmpValueToCheck=null;
        try {
            tmpValueToCheck=(Integer)m.getData();
        } catch (ClassCastException e) {
            // can not fullfil constrains cause is no Integer / should actually not happen
            return false;
        }
        boolean minCheck;
        boolean maxCheck;
        switch( filterMode.getValue() ){
            case MODE_MIN:
                return ( minValue==null ) ? true : tmpValueToCheck >= minValue;
            case MODE_MAX:
                return ( maxValue==null ) ? true : tmpValueToCheck <= maxValue;
            case MODE_MIN_MAX:
                minCheck = ( minValue==null ) ? true : tmpValueToCheck >= minValue;
                maxCheck = ( maxValue==null ) ? true : tmpValueToCheck <= maxValue;
                return minCheck && maxCheck;
            case MODE_EQUALS:
                return ( eqValue==null ) ? true : tmpValueToCheck == eqValue;
            default:
                Logger.getLogger(getClass()).warn("+++ filterMode of CaseServiceMetadataIntegerFilter is set to an unknown value ('"+filterMode+"'), will return false by default for all items.");
                return false;
        }
    }
}
