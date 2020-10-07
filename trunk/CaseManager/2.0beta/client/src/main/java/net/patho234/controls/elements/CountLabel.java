/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls.elements;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/**
 *
 * @author rehkind
 */
public class CountLabel extends TextFlow{
    Text labelCount=new Text();
    Text labelSpacer=new Text(": ");
    Text labelCaption=new Text();
    public CountLabel(String caption, Integer count){
        setCaption(caption);
        setCount(count);
        
        getChildren().add(labelCaption);
        getChildren().add(labelSpacer);
        getChildren().add(labelCount);
        
        Font countFont = new Font(15.);
        
        labelCount.setFont(countFont);
        labelCount.setStyle("-fx-font-weight: bold");
        updateColor();
        
        this.prefWidth(500);
        this.prefHeight(500);
        this.setTextAlignment(TextAlignment.CENTER);
    }
    
    final public void setCount( Integer count ){
        labelCount.setText(""+count);
        updateColor();
    }
    
    final public void setSpacer( String spacer ){
        labelSpacer.setText(spacer);
    }
    
    final public void setCaption( String caption ){
        labelCaption.setText(caption);
    }
    
    private void updateColor(){
        if( labelCount.getText().equals("0") ){
            labelCount.setFill(new Color(0.8, 0.4, 0.5, 1));
        }else{
            labelCount.setFill(new Color(0.4, 0.8, 0.5, 1));
        }
    }
}
