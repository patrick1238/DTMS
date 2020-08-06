/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.VBox;

/**
 *
 * @author rehkind
 */
public class Case2DFilterFactory {
    
    static List<FilterPane> create2DFilterPanes(){
        // TODO parse correct service def and create all required filters
        ArrayList<FilterPane> createdFilters = new ArrayList<>();
        CaseStringMetadataFilterPane redFilter = new CaseStringMetadataFilterPane("Red", "2D", "contains");
        createdFilters.add(redFilter);
        return createdFilters;
    }
}
