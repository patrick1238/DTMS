/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls.elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import net.patho234.elements.Case2DFilterPane;
import net.patho234.elements.Case3DFilterPane;
import net.patho234.elements.Case4DFilterPane;
import net.patho234.elements.CaseFilterPane;
import net.patho234.interfaces.IDataDisplay;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.interfaces.client.IDtmsSearchListener;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class FilterController implements Initializable, IDtmsSearchListener {

    @FXML
    private StackPane filterStack;
    @FXML
    private AnchorPane casePane;
    @FXML
    private AnchorPane twoDimPane;
    @FXML
    private AnchorPane threeDimPane;
    @FXML
    private ScrollPane caseScrollPane;
    @FXML
    private ScrollPane twoDimScrollPane;
    @FXML
    private ScrollPane threeDimScrollPane;
    @FXML
    private ScrollPane fourDimScrollPane;
    @FXML
    private AnchorPane fourDimPane;
    @FXML
    private ScrollPane genomicsScrollPane;
    @FXML
    private AnchorPane genomicsPane;
    @FXML
    private ScrollPane methScrollPane;
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
    private IDataDisplay display;
    private HashMap<String,Integer> viewIDs;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CaseFilterPane caseFilterPane=null;
        
        try {
            caseFilterPane=new CaseFilterPane();
            AnchorPane.setBottomAnchor(caseFilterPane, 0.);
            AnchorPane.setLeftAnchor(caseFilterPane, 0.);
            AnchorPane.setTopAnchor(caseFilterPane, 0.);
            AnchorPane.setRightAnchor(caseFilterPane, 0.);
        } catch (IOException ex) {
            Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, "Could not load CaseFilterPane from fxml file.", ex);
        }
        
        casePane.getChildren().add( caseFilterPane );
        
        Case2DFilterPane case2DFilterPane=null;
        
        try {
            case2DFilterPane=new Case2DFilterPane();
            AnchorPane.setBottomAnchor(case2DFilterPane, 0.);
            AnchorPane.setLeftAnchor(case2DFilterPane, 0.);
            AnchorPane.setTopAnchor(case2DFilterPane, 0.);
            AnchorPane.setRightAnchor(case2DFilterPane, 0.);
        } catch (Exception ex) {
            Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, "Could not load Case2DFilterPane ERROR occured.", ex);
        }
        
        twoDimPane.getChildren().add( case2DFilterPane );
        
        Case3DFilterPane case3DFilterPane=null;
        try {
            case3DFilterPane=new Case3DFilterPane();
            AnchorPane.setBottomAnchor(case3DFilterPane, 0.);
            AnchorPane.setLeftAnchor(case3DFilterPane, 0.);
            AnchorPane.setTopAnchor(case3DFilterPane, 0.);
            AnchorPane.setRightAnchor(case3DFilterPane, 0.);
        } catch (Exception ex) {
            Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, "Could not load Case3DFilterPane ERROR occured.", ex);
        }
        threeDimPane.getChildren().add( case3DFilterPane );
        
        Case4DFilterPane case4DFilterPane=null;
        try {
            case4DFilterPane=new Case4DFilterPane();
            AnchorPane.setBottomAnchor(case4DFilterPane, 0.);
            AnchorPane.setLeftAnchor(case4DFilterPane, 0.);
            AnchorPane.setTopAnchor(case4DFilterPane, 0.);
            AnchorPane.setRightAnchor(case4DFilterPane, 0.);
        } catch (Exception ex) {
            Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, "Could not load Case4DFilterPane ERROR occured.", ex);
        }
        fourDimPane.getChildren().add( case4DFilterPane );
        
        backPane=new Pane();
        //AnchorPane.setBottomAnchor(backPane, 0.);
        //AnchorPane.setLeftAnchor(backPane, 0.);
        AnchorPane.setTopAnchor(backPane, 0.);
        AnchorPane.setRightAnchor(backPane, 0.);
        backPane.setBackground(new Background(new BackgroundFill(Color.grayRgb(245), CornerRadii.EMPTY, Insets.EMPTY)));
        filterStack.getChildren().add(backPane);
      
        caseCounter.setGraphic(new CountLabel("Cases",0));
        caseCounter.setText("");
        twoDimCounter.setGraphic(new CountLabel("2D images",0));
        twoDimCounter.setText("");
        threeDimCounter.setGraphic(new CountLabel("3D images",0));
        threeDimCounter.setText("");
        fourDimCounter.setGraphic(new CountLabel("4D images",0));
        fourDimCounter.setText("");
        genomicsCounter.setGraphic(new CountLabel("Genomic data",0));
        genomicsCounter.setText("");
        methCounter.setGraphic(new CountLabel("Methylation data",0));
        methCounter.setText("");
        casesClicked(null);
    }    

    @FXML
    private void casesClicked(ActionEvent event) {
        backPane.toFront();
        caseScrollPane.toFront();
    }

    @FXML
    private void twoDimClicked(ActionEvent event) {
        Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, "Bringing 2D filter pane to front.");
        backPane.toFront();
        twoDimScrollPane.toFront();
    }

    @FXML
    private void threeDimClicked(ActionEvent event) {
        Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, "Bringing 3D filter pane to front.");
        backPane.toFront();
        threeDimScrollPane.toFront();
    }

    @FXML
    private void fourDimClicked(ActionEvent event) {
        Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, "Bringing 4D filter pane to front.");
        backPane.toFront();
        fourDimScrollPane.toFront();
    }

    @FXML
    private void genomicsClicked(ActionEvent event) {
        Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, "Bringing genomics filter pane to front.");
        backPane.toFront();
        genomicsScrollPane.toFront();
    }

    @FXML
    private void methClicked(ActionEvent event) {
        Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, "Bringing methylation filter pane to front.");
        backPane.toFront();
        methScrollPane.toFront();
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
    
    @Override
    public void receiveSearchResults(ClientObjectList newResults, String searchIdentifier) {
        if(caseCounter==null){ 
            Logger.getLogger(getClass().getName()).warning("HomePane received SearchResult before finished loading...skipping");
            return;
        }
        switch( searchIdentifier ){
            case "global_cases":
                Platform.runLater(() -> { ((CountLabel)caseCounter.getGraphic()).setCount(newResults.size() );  });
                break;
            case "global_2D":
                Platform.runLater(() -> { ((CountLabel)twoDimCounter.getGraphic()).setCount(newResults.size() );  });
                break;
            case "global_3D":
                Platform.runLater(() -> { ((CountLabel)threeDimCounter.getGraphic()).setCount(newResults.size() );  });
                break;
            case "global_4D":
                Platform.runLater(() -> { ((CountLabel)fourDimCounter.getGraphic()).setCount(newResults.size() );  });
                break;
            case "global_Genome":
                Platform.runLater(() -> { ((CountLabel)genomicsCounter.getGraphic()).setCount(newResults.size() );  });
                break;
            case "global_Methylation":
                Platform.runLater(() -> { ((CountLabel)methCounter.getGraphic()).setCount(newResults.size() );  });
                break;
            default:
                Logger.getLogger("TableViewerWindow").warning("TableViewerWindow.receiveSearchResults() for unknown search result ("+searchIdentifier+")");
        }
    }
    
    public void setDisplay(IDataDisplay display){
        this.display = display;
        this.viewIDs = this.display.getViews();
        for(String key:this.viewIDs.keySet()){
            if(key.equals("Case")){
                ((CountLabel)this.caseCounter.getGraphic()).setCount(this.display.getVisibleDataCount(this.viewIDs.get(key)) );
                this.caseCounter.setId(key);
            }else if(key.equals("2D")){
                ((CountLabel)this.twoDimCounter.getGraphic()).setCount(this.display.getVisibleDataCount(this.viewIDs.get(key)) );
                this.twoDimCounter.setId(key);
            }else if(key.equals("3D")){
                ((CountLabel)this.threeDimCounter.getGraphic()).setCount(this.display.getVisibleDataCount(this.viewIDs.get(key)) );
                this.threeDimCounter.setId(key);
            }else if(key.equals("4D")){
                ((CountLabel)this.fourDimCounter.getGraphic()).setCount(this.display.getVisibleDataCount(this.viewIDs.get(key)) );
                this.fourDimCounter.setId(key);
            }else if(key.equals("Genomics")){
                ((CountLabel)this.genomicsCounter.getGraphic()).setCount(this.display.getVisibleDataCount(this.viewIDs.get(key)) );
                this.genomicsCounter.setId(key);
            }else if(key.equals("Methylation")){
                ((CountLabel)this.methCounter.getGraphic()).setCount(this.display.getVisibleDataCount(this.viewIDs.get(key)) );
                this.methCounter.setId(key);
            }else{
                System.out.println("No button defined for key "+key+ " in filter pane");
            }
        }
    }
}
