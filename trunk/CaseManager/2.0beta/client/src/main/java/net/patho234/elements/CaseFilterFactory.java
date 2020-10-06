/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements;

import com.sun.scenario.Settings;
import java.util.ArrayList;
import java.util.List;
import net.patho234.entities.filter.CaseServiceMetadataFloatFilter;
import net.patho234.entities.filter.CaseServiceMetadataIntegerFilter;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class CaseFilterFactory {
    
    static List<FilterPane> create2DFilterPanes(){
//        ArrayList<FilterPane> createdFilters = new ArrayList<>();
//        Logger.getLogger(CaseFilterFactory.class).info("create2DFilterPanes() - [STRING]");
//        String allStringFilterAsString = Settings.get("dtms.string_filters");
//        if (allStringFilterAsString != "" && allStringFilterAsString != null){
//            Logger.getLogger(CaseFilterFactory.class).info("Loading 2D string filter GUI elements from settings.");
//            String[] splitFilter = allStringFilterAsString.split(";");
//            
//            for(String filter : splitFilter){
//                String[] filterValues=filter.split(":");
//                if(filterValues.length>2){
//                    if(filterValues[1].equals("2D")){
//                        Logger.getLogger(CaseFilterFactory.class).info("Adding new 2D filter GUI element. (loading from config entry: '"+filter+"')");
//                        CaseStringMetadataFilterPane newFilter = new CaseStringMetadataFilterPane(filterValues[0], filterValues[1], filterValues[2]);
//                        createdFilters.add(newFilter);
//                    }
//                }else{
//                    Logger.getLogger(CaseFilterFactory.class).info("Strange filter value found: '"+filter+"' will be ignored.");
//                }
//            }
//        }
//        Logger.getLogger(CaseFilterFactory.class).info("create2DFilterPanes() - [INTEGER]");
//        String allIntegerFilterAsString = Settings.get("dtms.integer_filters");
//        if (allIntegerFilterAsString != "" && allIntegerFilterAsString != null){
//            Logger.getLogger(CaseFilterFactory.class).info("Loading 2D integer filter GUI elements from settings.");
//            String[] splitFilter = allIntegerFilterAsString.split(";");
//            
//            for(String filter : splitFilter){
//                String[] filterValues=filter.split(":");
//                if(filterValues.length>2){
//                    if(filterValues[1].equals("2D")){
//                        Logger.getLogger(CaseFilterFactory.class).info("Adding new 2D filter GUI element. (loading from config entry: '"+filter+"')");
//                        CaseIntegerMetadataFilterPane newFilter = new CaseIntegerMetadataFilterPane(filterValues[0], filterValues[1], CaseServiceMetadataIntegerFilter.getModeFromString( filterValues[2] ));
//                        createdFilters.add(newFilter);
//                    }
//                }else{
//                    Logger.getLogger(CaseFilterFactory.class).info("Strange filter value found: '"+filter+"' will be ignored.");
//                }
//            }
//        }
//        Logger.getLogger(CaseFilterFactory.class).info("create2DFilterPanes() - [FLOAT]");
//        
//        String allFloatFilterAsString = Settings.get("dtms.float_filters");
//        if (allFloatFilterAsString != "" && allFloatFilterAsString != null){
//            Logger.getLogger(CaseFilterFactory.class).info("Loading 2D float filter GUI elements from settings.");
//            String[] splitFilter = allFloatFilterAsString.split(";");
//            
//            for(String filter : splitFilter){
//                String[] filterValues=filter.split(":");
//                if(filterValues.length>2){
//                    if(filterValues[1].equals("2D")){
//                        Logger.getLogger(CaseFilterFactory.class).info("Adding new 2D filter GUI element. (loading from config entry: '"+filter+"')");
//                        CaseFloatMetadataFilterPane newFilter = new CaseFloatMetadataFilterPane(filterValues[0], filterValues[1], CaseServiceMetadataFloatFilter.getModeFromString( filterValues[2] ));
//                        createdFilters.add(newFilter);
//                    }
//                }else{
//                    Logger.getLogger(CaseFilterFactory.class).info("Strange filter value found: '"+filter+"' will be ignored.");
//                }
//            }
//        }
        return createFilterPanes("2D");
    }
    
    static List<FilterPane> create3DFilterPanes(){
        // TODO parse correct service def and create all required filters
//        ArrayList<FilterPane> createdFilters = new ArrayList<>();
//        Logger.getLogger(CaseFilterFactory.class).info("create3DFilterPanes()");
//        String allStringFilterAsString = Settings.get("dtms.string_filters");
//        if (allStringFilterAsString != "" && allStringFilterAsString != null){
//            Logger.getLogger(CaseFilterFactory.class).info("Loading 3D filter GUI elements from settings.");
//            String[] splitFilter = allStringFilterAsString.split(";");
//            
//            for(String filter : splitFilter){
//                String[] filterValues=filter.split(":");
//                if(filterValues.length>2){
//                    if(filterValues[1].equals("3D")){
//                        Logger.getLogger(CaseFilterFactory.class).info("Adding new 3D filter GUI element. (loading from config entry: '"+filter+"')");
//                        CaseStringMetadataFilterPane newFilter = new CaseStringMetadataFilterPane(filterValues[0], filterValues[1], filterValues[2]);
//                        createdFilters.add(newFilter);
//                    }
//                }else{
//                    Logger.getLogger(CaseFilterFactory.class).info("Strange filter value found: '"+filter+"' will be ignored.");
//                }
//            }
//        }
//        
//        String allIntegerFilterAsString = Settings.get("dtms.integer_filters");
//        if (allStringFilterAsString != "" && allIntegerFilterAsString != null){
//            Logger.getLogger(CaseFilterFactory.class).info("Loading 3D filter GUI elements from settings.");
//            String[] splitFilter = allIntegerFilterAsString.split(";");
//            
//            for(String filter : splitFilter){
//                String[] filterValues=filter.split(":");
//                if(filterValues.length>2){
//                    if(filterValues[1].equals("3D")){
//                        Logger.getLogger(CaseFilterFactory.class).info("Adding new 3D filter GUI element. (loading from config entry: '"+filter+"')");
//                        CaseIntegerMetadataFilterPane newFilter = new CaseIntegerMetadataFilterPane(filterValues[0], filterValues[1], CaseServiceMetadataIntegerFilter.getModeFromString( filterValues[2] ));
//                        createdFilters.add(newFilter);
//                    }
//                }else{
//                    Logger.getLogger(CaseFilterFactory.class).info("Strange filter value found: '"+filter+"' will be ignored.");
//                }
//            }
//        }
        
        return createFilterPanes("3D");
    }
    
    static List<FilterPane> create4DFilterPanes(){
        // TODO parse correct service def and create all required filters
//        ArrayList<FilterPane> createdFilters = new ArrayList<>();
//        Logger.getLogger(CaseFilterFactory.class).info("create4DFilterPanes()");
//        String allStringFilterAsString = Settings.get("dtms.string_filters");
//        if (allStringFilterAsString != "" && allStringFilterAsString != null){
//            Logger.getLogger(CaseFilterFactory.class).info("Loading 4D filter GUI elements from settings.");
//            String[] splitFilter = allStringFilterAsString.split(";");
//            
//            for(String filter : splitFilter){
//                String[] filterValues=filter.split(":");
//                if(filterValues.length>2){
//                    if(filterValues[1].equals("4D")){
//                        Logger.getLogger(CaseFilterFactory.class).info("Adding new 4D filter GUI element. (loading from config entry: '"+filter+"')");
//                        CaseStringMetadataFilterPane newFilter = new CaseStringMetadataFilterPane(filterValues[0], filterValues[1], filterValues[2]);
//                        createdFilters.add(newFilter);
//                    }
//                }else{
//                    Logger.getLogger(CaseFilterFactory.class).info("Strange filter value found: '"+filter+"' will be ignored.");
//                }
//            }
//        }
//        
//        String allIntegerFilterAsString = Settings.get("dtms.integer_filters");
//        if (allStringFilterAsString != "" && allIntegerFilterAsString != null){
//            Logger.getLogger(CaseFilterFactory.class).info("Loading 4D filter GUI elements from settings.");
//            String[] splitFilter = allIntegerFilterAsString.split(";");
//            
//            for(String filter : splitFilter){
//                String[] filterValues=filter.split(":");
//                if(filterValues.length>2){
//                    if(filterValues[1].equals("4D")){
//                        Logger.getLogger(CaseFilterFactory.class).info("Adding new 4D filter GUI element. (loading from config entry: '"+filter+"')");
//                        CaseIntegerMetadataFilterPane newFilter = new CaseIntegerMetadataFilterPane(filterValues[0], filterValues[1], CaseServiceMetadataIntegerFilter.getModeFromString( filterValues[2] ));
//                        createdFilters.add(newFilter);
//                    }
//                }else{
//                    Logger.getLogger(CaseFilterFactory.class).info("Strange filter value found: '"+filter+"' will be ignored.");
//                }
//            }
//        }
        
        return createFilterPanes("4D");
    }
    
    private static List<FilterPane> createFilterPanes(String type){
        ArrayList<FilterPane> createdFilters = new ArrayList<>();
        Logger.getLogger(CaseFilterFactory.class).info("create"+type+"FilterPanes()");
        String allStringFilterAsString = Settings.get("dtms.string_filters");
        if (allStringFilterAsString != "" && allStringFilterAsString != null){
            Logger.getLogger(CaseFilterFactory.class).info("Loading "+type+" filter GUI elements from settings.");
            String[] splitFilter = allStringFilterAsString.split(";");
            
            for(String filter : splitFilter){
                String[] filterValues=filter.split(":");
                if(filterValues.length>2){
                    if(filterValues[1].equals(type)){
                        Logger.getLogger(CaseFilterFactory.class).info("Adding new "+type+" filter GUI element [STRING]. (loading from config entry: '"+filter+"')");
                        CaseStringMetadataFilterPane newFilter = new CaseStringMetadataFilterPane(filterValues[0], filterValues[1], filterValues[2]);
                        createdFilters.add(newFilter);
                    }
                }else{
                    Logger.getLogger(CaseFilterFactory.class).info("Strange filter value found: '"+filter+"' will be ignored.");
                }
            }
        }
        
        String allIntegerFilterAsString = Settings.get("dtms.integer_filters");
        if (allStringFilterAsString != "" && allIntegerFilterAsString != null){
            Logger.getLogger(CaseFilterFactory.class).info("Loading "+type+" filter GUI elements from settings.");
            String[] splitFilter = allIntegerFilterAsString.split(";");
            
            for(String filter : splitFilter){
                String[] filterValues=filter.split(":");
                if(filterValues.length>2){
                    if(filterValues[1].equals(type)){
                        Logger.getLogger(CaseFilterFactory.class).info("Adding new "+type+" filter GUI element [INTEGER]. (loading from config entry: '"+filter+"')");
                        CaseIntegerMetadataFilterPane newFilter = new CaseIntegerMetadataFilterPane(filterValues[0], filterValues[1], CaseServiceMetadataIntegerFilter.getModeFromString( filterValues[2] ));
                        createdFilters.add(newFilter);
                    }
                }else{
                    Logger.getLogger(CaseFilterFactory.class).info("Strange filter value found: '"+filter+"' will be ignored.");
                }
            }
        }
                Logger.getLogger(CaseFilterFactory.class).info("create"+type+"FilterPanes() - [FLOAT]");
        
        String allFloatFilterAsString = Settings.get("dtms.float_filters");
        if (allFloatFilterAsString != "" && allFloatFilterAsString != null){
            Logger.getLogger(CaseFilterFactory.class).info("Loading "+type+" float filter GUI elements from settings.");
            String[] splitFilter = allFloatFilterAsString.split(";");
            
            for(String filter : splitFilter){
                String[] filterValues=filter.split(":");
                if(filterValues.length>2){
                    if(filterValues[1].equals(type)){
                        Logger.getLogger(CaseFilterFactory.class).info("Adding new "+type+" filter GUI element [FLOAT]. (loading from config entry: '"+filter+"')");
                        CaseFloatMetadataFilterPane newFilter = new CaseFloatMetadataFilterPane(filterValues[0], filterValues[1], CaseServiceMetadataFloatFilter.getModeFromString( filterValues[2] ));
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
