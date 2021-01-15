/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements;

import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.patho234.entities.filter.CaseServiceMetadataStringFilter;
import net.patho234.entities.filter.ClientObjectFilterBase;
import net.patho234.webapp_client.APPLICATION_DEFAULTS;

/**
 *
 * @author rehkind
 */
public class CaseDateMetadataFilterPane extends FilterPane{
    public static final String MODE_FROM="from";
    public static final String MODE_TO="to";
    public static final String MODE_FROM_TO="from_to";
    
    CaseServiceMetadataDateFilter filter;
    
    private String metadataFieldName;
    private String serviceType;
    
    // GUI elements:
    private HBox box;
    private Label lblMetadataFieldName;
    private DatePicker dpStartDate;
    private DatePicker dpEndDate;
    
    private SimpleStringProperty strStartDate = new SimpleStringProperty();
    private SimpleStringProperty strEndDate = new SimpleStringProperty();
    
    public CaseDateMetadataFilterPane(String metadataFieldName){ this(metadataFieldName, null); }
    
    public CaseDateMetadataFilterPane(String metadataFieldName, String serviceType){
        this(metadataFieldName, serviceType, "from");
    }
    
    public CaseDateMetadataFilterPane(String metadataFieldName, String serviceType, String filterMode){
        super();
        filter = new CaseServiceMetadataStringFilter(metadataFieldName,"",filterMode);
        filter.setServiceType(serviceType);
        this.metadataFieldName = metadataFieldName;
        this.serviceType = serviceType;
        initGui();
        
        ChangeListener listener = new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                
            }
        };
        
        dpStartDate.chronologyProperty().addListener(listener);
        dpEndDate.chronologyProperty().addListener(listener);
    }
    
    private void initGui(){
        box = new HBox();
        
        lblMetadataFieldName=new Label(metadataFieldName);
        lblMetadataFieldName.setPrefSize(100, 40);
        
        dpStartDate = new DatePicker();
        dpStartDate.setConverter(APPLICATION_DEFAULTS.DEFAULT_DATE_CONVERTER);
        dpStartDate.setPrefSize(200, 40);
        dpEndDate = new DatePicker();
        dpEndDate.setConverter(APPLICATION_DEFAULTS.DEFAULT_DATE_CONVERTER);
        dpEndDate.setPrefSize(200, 40);
        
        setupDatePickerListener();
        
        
        box.getChildren().add(lblMetadataFieldName);
        box.getChildren().add(dpStartDate);
        box.getChildren().add(dpEndDate);
        
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

    @Override
    public void nextFilterMode() {
        filter.setSearchMode(filter.getSearchMode()+1);
    }

    private void setupDatePickerListener() {
        // bind string property to local date of date picker (start|end):
        ChangeListener<String> stringStartListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LocalDate ld = APPLICATION_DEFAULTS.DEFAULT_DATE_CONVERTER.fromString(newValue);
                if( ld != null ){
                    dpStartDate.valueProperty().setValue(ld);
                }
            }
        };
        strStartDate.addListener(stringStartListener);
        
        ChangeListener<String> stringEndListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LocalDate ld = APPLICATION_DEFAULTS.DEFAULT_DATE_CONVERTER.fromString(newValue);
                if( ld != null ){
                    dpEndDate.valueProperty().setValue(ld);
                }
            }
        };
        strStartDate.addListener(stringEndListener);
        
        ChangeListener<LocalDate> localDateStartListener = new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                String dateAsStr = APPLICATION_DEFAULTS.DEFAULT_DATE_CONVERTER.toString(newValue);
                if( dateAsStr != null ){
                    strStartDate.setValue(dateAsStr);
                }
            }
        };
        dpStartDate.valueProperty().addListener(localDateStartListener);
        
        ChangeListener<LocalDate> localDateEndListener = new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                String dateAsStr = APPLICATION_DEFAULTS.DEFAULT_DATE_CONVERTER.toString(newValue);
                if( dateAsStr != null ){
                    strEndDate.setValue(dateAsStr);
                }
            }
        };
        dpEndDate.valueProperty().addListener(localDateEndListener);
    }
    
    
    
}
