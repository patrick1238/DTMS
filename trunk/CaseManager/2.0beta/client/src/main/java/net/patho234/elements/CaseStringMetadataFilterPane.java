/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.patho234.entities.filter.CaseServiceMetadataStringFilter;
import net.patho234.entities.filter.ClientObjectFilterBase;

/**
 *
 * @author rehkind
 */
public class CaseStringMetadataFilterPane extends FilterPane{
    
    CaseServiceMetadataStringFilter filter;
    
    private String metadataFieldName;
    private String serviceType;
    
    // GUI elements:
    private HBox box;
    private Label lblMetadataFieldName;
    private TextField txtSearchTerm;
    
    public CaseStringMetadataFilterPane(String metadataFieldName){ this(metadataFieldName, null); }
    
    public CaseStringMetadataFilterPane(String metadataFieldName, String serviceType){
        this(metadataFieldName, serviceType, "contains");
    }
    
    public CaseStringMetadataFilterPane(String metadataFieldName, String serviceType, String filterMode){
        super();
        filter = new CaseServiceMetadataStringFilter(metadataFieldName,"",filterMode);
        filter.setServiceType(serviceType);
        this.metadataFieldName = metadataFieldName;
        this.serviceType = serviceType;
        initGui();
        
        txtSearchTerm.textProperty().addListener( new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                filter.setSearch(t1);
            }
        
        } );
        //setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    
    private void initGui(){
        box = new HBox();
        
        lblMetadataFieldName=new Label(metadataFieldName);
        lblMetadataFieldName.setPrefSize(100, 40);
        
        txtSearchTerm = new TextField();
        txtSearchTerm.setPrefSize(200, 40);
        
        box.getChildren().add(lblMetadataFieldName);
        box.getChildren().add(txtSearchTerm);
        
        AnchorPane.setBottomAnchor(box, 0.);
        AnchorPane.setTopAnchor(box, 0.);
        AnchorPane.setLeftAnchor(box, 0.);
        AnchorPane.setRightAnchor(box, 0.);
        
        this.getChildren().add(box);
        
    }

    @Override
    public ClientObjectFilterBase getFilter() {
        return filter;
    }
    
}
