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
        filterPanes = Case2DFilterFactory.create2DFilterPanes();
        filters = new ArrayList<>();
        
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
        
        ClientObjectSearchManager.create().getSearch("global_cases").setFilterItems("2D", filters);
        this.getChildren().add(box);
    }
    
    @Override
    public void filterUpdatedEvent( Object srcFilter ){
        System.out.println("REQUESTING UPDATE 2D");
        ClientObjectSearchManager.create().getSearch("global_cases").updateSearchResult();
    }

    public List<ClientObjectFilterBase> getFilter(){
        return filters;
    }
}
