/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements;

import java.util.EventListener;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 *
 * @author rehkind
 */
public class FilterModePicker extends Label implements EventHandler{
    FilterPane parent;
    public FilterModePicker(String txt, FilterPane parent){
        super("o");
        
        this.parent=parent;
        setBackground(new Background(new BackgroundFill(Color.KHAKI, new CornerRadii(10.), Insets.EMPTY)));
        setTextFill(new Color(0, 0, 0, 0));
        setPrefSize(30, 30);
        setMinSize(30, 30);
        setMaxSize(30, 30);
        
        setOnMouseClicked( this );
        setVisible(true);
    }
    
    @Override
    public void handle(Event event) {
        parent.nextFilterMode();
    }
}
