/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls.elements;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class ClientPopupController implements Initializable {
    @FXML
    public Label lblClose;
    @FXML
    public Label lblTitle;
    @FXML
    public TextFlow tfText;
    @FXML
    public AnchorPane mainPane;
    
    Text message;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfText.setTextAlignment(TextAlignment.JUSTIFY);
    }    
    
    public void setTitle( String title ){
        lblTitle.setText(title);
    }
    
    public void setMessage( String msg ){
        message = new Text(msg);
        message.setFont(new Font("Arial", 24.));
        message.setFill(new Color(1., 1., 1., 1.));
        //message.setStroke(new Color(.95, .95, .95, 1.));
        tfText.getChildren().add(message);
    }
    
    @FXML
    public void onClose(){
        this.lblTitle.getScene().getWindow().hide();
    }
    @FXML
    public void onCloseEntered(){
        lblClose.setBackground(new Background(new BackgroundFill(new Color(1.,1.,1.,0.75), new CornerRadii(17), Insets.EMPTY)));
    }
    @FXML
    public void onCloseExited(){
        lblClose.setBackground(new Background(new BackgroundFill(new Color(1.,1.,1.,0.55), new CornerRadii(17), Insets.EMPTY)));
    }

    public double getPrefHeight() {
        // only works with current font size...made by testing no clue about the math
        return (message.getText().length()/40.)*30+190;
    }

    public void setHeight(double prefHeight) {
        mainPane.setPrefHeight(prefHeight);
    }
}
