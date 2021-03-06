/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import net.patho234.entities.filter.ClientObjectAndFilter;
import net.patho234.entities.filter.ClientObjectFilterBase;
import net.patho234.entities.filter.ClientObjectSearchManager;
import net.patho234.interfaces.client.IFilterUpdatedListener;

/**
 *
 * @author rehkind
 */
public class Case2DFilterPane extends AnchorPane implements IFilterUpdatedListener{
    List<FilterPane> filterPanes;
    List<ClientObjectFilterBase> filters;
    private VBox box;
    
    
    
    public Case2DFilterPane(){
        filterPanes = CaseFilterFactory.create2DFilterPanes();
        filters = new ArrayList<>();
        List<ClientObjectFilterBase> andFilters2D = new ArrayList<>();
        box = new VBox();
        box.setSpacing(5.);
        AnchorPane.setBottomAnchor(box, 10.);
        AnchorPane.setTopAnchor(box, 10.);
        AnchorPane.setLeftAnchor(box, 10.);
        AnchorPane.setRightAnchor(box, 10.);
        
        for (FilterPane fp : filterPanes){
            this.box.getChildren().add(fp);
            filters.add(fp.getFilter());
            fp.getFilter().addFilterUpdatedListener(this);
        }
        ClientObjectAndFilter service2DFilter=new ClientObjectAndFilter(filters);
        andFilters2D.add(service2DFilter);
        ClientObjectSearchManager.create().getSearch("global_cases").setFilterItems("2D", filters);
        ClientObjectSearchManager.create().getSearch("global_2D").setFilterItems("2D", andFilters2D);
        this.getChildren().add(box);
    }
    
    @Override
    public void filterUpdatedEvent( Object srcFilter ){
        ClientObjectSearchManager.create().getSearch("global_cases").updateSearchResult();
    }

    public List<ClientObjectFilterBase> getFilter(){
        return filters;
    }
}
