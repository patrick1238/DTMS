/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import net.patho234.entities.filter.CaseServiceMetadataIntegerFilter;
import net.patho234.entities.filter.ClientObjectFilterBase;

/**
 *
 * @author rehkind
 */
public class CaseIntegerMetadataFilterPane extends FilterPane{
    
    CaseServiceMetadataIntegerFilter filter;
    
    private String metadataFieldName;
    private String serviceType;
    
    // GUI elements:
    private HBox boxBody;
    private HBox boxHeader;
    private Label lblMetadataFieldName;
    private Label lblHeaderSpacer;
    private Label lblMinField;
    private Label lblMaxField;
    private Label lblEqualField;
    private TextField txtMinField;
    private TextField txtMaxField;
    private TextField txtEqualField;
    
    public CaseIntegerMetadataFilterPane(String metadataFieldName){ this(metadataFieldName, null); }
    
    public CaseIntegerMetadataFilterPane(String metadataFieldName, String serviceType){
        this(metadataFieldName, serviceType, CaseServiceMetadataIntegerFilter.MODE_MIN);
    }
    
    public CaseIntegerMetadataFilterPane(String metadataFieldName, String serviceType, Integer filterMode){
        super();
        filter = new CaseServiceMetadataIntegerFilter(metadataFieldName,"",filterMode,new Integer[]{null,null,null});
        filter.setServiceType(serviceType);
        this.metadataFieldName = metadataFieldName;
        this.serviceType = serviceType;
        initGui();
        txtMinField.textProperty().addListener( new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                filter.setMinValue(castValue(t1));
                if(t1.equals("")){
                    lblMinField.setBackground(new Background(new BackgroundFill(new Color(0.96, 0.96, 0.96, 1.), CornerRadii.EMPTY, Insets.EMPTY)));
                }
                else if(castValue(t1)==null){
                    lblMinField.setBackground(new Background(new BackgroundFill(new Color(0.4, 0.1, 0.1, 0.3), CornerRadii.EMPTY, Insets.EMPTY)));
                }else{
                    lblMinField.setBackground(new Background(new BackgroundFill(new Color(0.1, 0.4, 0.1, 0.3), CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        } );
        
        txtMaxField.textProperty().addListener( new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                filter.setMaxValue(castValue(t1));
                if(t1.equals("")){
                    lblMaxField.setBackground(new Background(new BackgroundFill(new Color(0.96, 0.96, 0.96, 1.), CornerRadii.EMPTY, Insets.EMPTY)));
                }
                else if(castValue(t1)==null){
                    lblMaxField.setBackground(new Background(new BackgroundFill(new Color(0.4, 0.1, 0.1, 0.3), CornerRadii.EMPTY, Insets.EMPTY)));
                }else{
                    lblMaxField.setBackground(new Background(new BackgroundFill(new Color(0.1, 0.4, 0.1, 0.3), CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        } );
        
        txtEqualField.textProperty().addListener( new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                filter.setEqualValue(castValue(t1));
                if(t1.equals("")){
                    lblEqualField.setBackground(new Background(new BackgroundFill(new Color(0.96, 0.96, 0.96, 1.), CornerRadii.EMPTY, Insets.EMPTY)));
                }
                else if(castValue(t1)==null){
                    lblEqualField.setBackground(new Background(new BackgroundFill(new Color(0.4, 0.1, 0.1, 0.3), CornerRadii.EMPTY, Insets.EMPTY)));
                }else{
                    lblEqualField.setBackground(new Background(new BackgroundFill(new Color(0.1, 0.4, 0.1, 0.3), CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        } );
    }
    
    private void initGui(){
        setPrefHeight(55);
        boxBody = new HBox();
        boxHeader = new HBox();
        boxBody.setSpacing(5);
        boxHeader.setSpacing(5);
        
        lblMetadataFieldName=new Label(metadataFieldName);
        lblMetadataFieldName.setPrefSize(100, 30);
        lblHeaderSpacer=new Label("");
        lblHeaderSpacer.setPrefSize(100, 25);
        
        lblMinField = new Label("min");
        lblMinField.setPrefSize(50, 25);
        txtMinField = new TextField();
        txtMinField.setPrefSize(50, 30);
        
        lblMaxField = new Label("max");
        lblMaxField.setPrefSize(50, 25);
        txtMaxField = new TextField();
        txtMaxField.setPrefSize(50, 30);
        
        lblEqualField = new Label(" = ");
        lblEqualField.setPrefSize(50, 25);
        txtEqualField = new TextField();
        txtEqualField.setPrefSize(50, 30);
        
        boxHeader.getChildren().add(lblHeaderSpacer);
        boxHeader.getChildren().add(lblMinField);
        boxHeader.getChildren().add(lblMaxField);
        boxHeader.getChildren().add(lblEqualField);
        
        boxBody.getChildren().add(lblMetadataFieldName);
        boxBody.getChildren().add(txtMinField);
        boxBody.getChildren().add(txtMaxField);
        boxBody.getChildren().add(txtEqualField);
        
        this.getChildren().add(boxHeader);
        this.getChildren().add(boxBody);
        AnchorPane.setBottomAnchor(boxBody, 0.);
        AnchorPane.setTopAnchor(boxBody, 25.);
        AnchorPane.setLeftAnchor(boxBody, 0.);
        AnchorPane.setRightAnchor(boxBody, 0.);
        AnchorPane.setTopAnchor(boxHeader, 0.);
        AnchorPane.setLeftAnchor(boxHeader, 0.);
        AnchorPane.setRightAnchor(boxHeader, 0.);
    }

    @Override
    public ClientObjectFilterBase getFilter() {
        return filter;
    }
    
    public Integer castValue(String strValue){
        Integer castedValue = null;
        if(strValue==""){ return null; }
        try{
            castedValue = Integer.valueOf(strValue);
        }catch( NumberFormatException ccEx ){ /*ignored we just return null*/ }
        
        return castedValue;
    }
    
}
