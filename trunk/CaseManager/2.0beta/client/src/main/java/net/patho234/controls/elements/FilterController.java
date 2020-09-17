/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls.elements;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import net.patho234.elements.Case2DFilterPane;
import net.patho234.elements.Case3DFilterPane;
import net.patho234.elements.CaseFilterPane;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class FilterController implements Initializable {

    @FXML
    private StackPane filterStack;
    @FXML
    private AnchorPane casePane;
    @FXML
    private AnchorPane twoDimPane;
    @FXML
    private AnchorPane threeDimPane;
    @FXML
    private AnchorPane fourDimPane;
    @FXML
    private AnchorPane genomicsPane;
    @FXML
    private AnchorPane methPane;
    @FXML
    private Button caseCounter;
    @FXML
    private Button twoDimCounter;
    @FXML
    private Button threeDimCounter;
    @FXML
    private Button fourDimCounter;
    @FXML
    private Button genomicsCounter;
    @FXML
    private Button methCounter;
    
    private Pane backPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CaseFilterPane caseFilterPane=null;
        
        try {
            caseFilterPane=new CaseFilterPane();
            //AnchorPane.setBottomAnchor(caseFilterPane, 0.);
            AnchorPane.setLeftAnchor(caseFilterPane, 0.);
            AnchorPane.setTopAnchor(caseFilterPane, 0.);
            //AnchorPane.setRightAnchor(caseFilterPane, 0.);
        } catch (IOException ex) {
            Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, "Could not load CaseFilterPane from fxml file.", ex);
        }
        
        casePane.getChildren().add( caseFilterPane );
        
        Case2DFilterPane case2DFilterPane=null;
        
        try {
            case2DFilterPane=new Case2DFilterPane();
            //AnchorPane.setBottomAnchor(case2DFilterPane, 0.);
            AnchorPane.setLeftAnchor(case2DFilterPane, 0.);
            AnchorPane.setTopAnchor(case2DFilterPane, 0.);
            //AnchorPane.setRightAnchor(case2DFilterPane, 0.);
        } catch (Exception ex) {
            Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, "Could not load Case2DFilterPane ERROR occured.", ex);
        }
        
        twoDimPane.getChildren().add( case2DFilterPane );
        
        Case3DFilterPane case3DFilterPane=null;
        try {
            case3DFilterPane=new Case3DFilterPane();
            //AnchorPane.setBottomAnchor(case3DFilterPane, 0.);
            AnchorPane.setLeftAnchor(case3DFilterPane, 0.);
            AnchorPane.setTopAnchor(case3DFilterPane, 0.);
            //AnchorPane.setRightAnchor(case3DFilterPane, 0.);
        } catch (Exception ex) {
            Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, "Could not load Case2DFilterPane ERROR occured.", ex);
        }
        threeDimPane.getChildren().add( case3DFilterPane );
        
        backPane=new Pane();
        //AnchorPane.setBottomAnchor(backPane, 0.);
        //AnchorPane.setLeftAnchor(backPane, 0.);
        AnchorPane.setTopAnchor(backPane, 0.);
        AnchorPane.setRightAnchor(backPane, 0.);
        backPane.setBackground(new Background(new BackgroundFill(Color.grayRgb(245), CornerRadii.EMPTY, Insets.EMPTY)));
        filterStack.getChildren().add(backPane);
        
        casesClicked(null);
    }    

    @FXML
    private void casesClicked(ActionEvent event) {
        backPane.toFront();
        casePane.toFront();
    }

    @FXML
    private void twoDimClicked(ActionEvent event) {
        Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, "Bringing 2D filter pane to front.");
        backPane.toFront();
        twoDimPane.toFront();
    }

    @FXML
    private void threeDimClicked(ActionEvent event) {
        Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, "Bringing 3D filter pane to front.");
        backPane.toFront();
        threeDimPane.toFront();
    }

    @FXML
    private void fourDimClicked(ActionEvent event) {
    }

    @FXML
    private void genomicsClicked(ActionEvent event) {
    }

    @FXML
    private void methClicked(ActionEvent event) {
    }

    @FXML
    private void caseCounterClicked(ActionEvent event) {
    }

    @FXML
    private void twoDimCounterClicked(ActionEvent event) {
    }

    @FXML
    private void threeDimCounterClicked(ActionEvent event) {
    }

    @FXML
    private void fourDimCounterClicked(ActionEvent event) {
    }

    @FXML
    private void genomicsCounterClicked(ActionEvent event) {
    }

    @FXML
    private void methCounterClicked(ActionEvent event) {
    }
    
}
