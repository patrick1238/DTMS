/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements;

import com.sun.scenario.Settings;
import java.util.ArrayList;
import java.util.List;
import net.patho234.entities.filter.CaseServiceMetadataIntegerFilter;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class CaseFilterFactory {
    
    static List<FilterPane> create2DFilterPanes(){
        ArrayList<FilterPane> createdFilters = new ArrayList<>();
        Logger.getLogger(CaseFilterFactory.class).info("create2DFilterPanes() - [STRING]");
        String allStringFilterAsString = Settings.get("dtms.string_filters");
        if (allStringFilterAsString != "" && allStringFilterAsString != null){
            Logger.getLogger(CaseFilterFactory.class).info("Loading 2D string filter GUI elements from settings.");
            String[] splitFilter = allStringFilterAsString.split(";");
            
            for(String filter : splitFilter){
                String[] filterValues=filter.split(":");
                if(filterValues.length>2){
                    if(filterValues[1].equals("2D")){
                        Logger.getLogger(CaseFilterFactory.class).info("Adding new 2D filter GUI element. (loading from config entry: '"+filter+"')");
                        CaseStringMetadataFilterPane newFilter = new CaseStringMetadataFilterPane(filterValues[0], filterValues[1], filterValues[2]);
                        createdFilters.add(newFilter);
                    }
                }else{
                    Logger.getLogger(CaseFilterFactory.class).info("Strange filter value found: '"+filter+"' will be ignored.");
                }
            }
        }
        Logger.getLogger(CaseFilterFactory.class).info("create2DFilterPanes() - [STRING]");
        String allIntegerFilterAsString = Settings.get("dtms.integer_filters");
        if (allIntegerFilterAsString != "" && allIntegerFilterAsString != null){
            Logger.getLogger(CaseFilterFactory.class).info("Loading 2D integer filter GUI elements from settings.");
            String[] splitFilter = allIntegerFilterAsString.split(";");
            
            for(String filter : splitFilter){
                String[] filterValues=filter.split(":");
                if(filterValues.length>2){
                    if(filterValues[1].equals("2D")){
                        Logger.getLogger(CaseFilterFactory.class).info("Adding new 2D filter GUI element. (loading from config entry: '"+filter+"')");
                        CaseIntegerMetadataFilterPane newFilter = new CaseIntegerMetadataFilterPane(filterValues[0], filterValues[1], CaseServiceMetadataIntegerFilter.getModeFromString( filterValues[2] ));
                        createdFilters.add(newFilter);
                    }
                }else{
                    Logger.getLogger(CaseFilterFactory.class).info("Strange filter value found: '"+filter+"' will be ignored.");
                }
            }
        }
        return createdFilters;
    }
    
    static List<FilterPane> create3DFilterPanes(){
        // TODO parse correct service def and create all required filters
        ArrayList<FilterPane> createdFilters = new ArrayList<>();
        Logger.getLogger(CaseFilterFactory.class).info("create3DFilterPanes()");
        String allStringFilterAsString = Settings.get("dtms.string_filters");
        if (allStringFilterAsString != "" && allStringFilterAsString != null){
            Logger.getLogger(CaseFilterFactory.class).info("Loading 3D filter GUI elements from settings.");
            String[] splitFilter = allStringFilterAsString.split(";");
            
            for(String filter : splitFilter){
                String[] filterValues=filter.split(":");
                if(filterValues.length>2){
                    if(filterValues[1].equals("3D")){
                        Logger.getLogger(CaseFilterFactory.class).info("Adding new 3D filter GUI element. (loading from config entry: '"+filter+"')");
                        CaseStringMetadataFilterPane newFilter = new CaseStringMetadataFilterPane(filterValues[0], filterValues[1], filterValues[2]);
                        createdFilters.add(newFilter);
                    }
                }else{
                    Logger.getLogger(CaseFilterFactory.class).info("Strange filter value found: '"+filter+"' will be ignored.");
                }
            }
        }
        
        return createdFilters;
    }
}
