/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls.elements;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import net.patho234.interfaces.IDataDisplay;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.interfaces.client.IDtmsSearchListener;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class HomeController implements Initializable, IDtmsSearchListener {

    @FXML
    private Button caseButton;
    @FXML
    private Button twoDimButton;
    @FXML
    private Button threeDimButton;
    @FXML
    private Button fourDimButton;
    @FXML
    private Button genomicsButton;
    @FXML
    private Button methButton;
    @FXML
    private AnchorPane anchor;
    
    IDataDisplay display;
    HashMap<String,Integer> viewIDs;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        caseButton.setGraphic(new CountLabel("Cases",0));
        caseButton.setText("");
        twoDimButton.setGraphic(new CountLabel("2D images",0));
        twoDimButton.setText("");
        threeDimButton.setGraphic(new CountLabel("3D images",0));
        threeDimButton.setText("");
        fourDimButton.setGraphic(new CountLabel("4D images",0));
        fourDimButton.setText("");
        genomicsButton.setGraphic(new CountLabel("Genomic data",0));
        genomicsButton.setText("");
        methButton.setGraphic(new CountLabel("Methylation data",0));
        methButton.setText("");
    }    

    @FXML
    private void caseButtonClicked(ActionEvent event) {
        final Node source = (Node) event.getSource();
        String id = source.getId();
        this.display.setVisible(viewIDs.get(id));
        // TODO: remove update, currently just for testing the SearchManager function
        //ClientObjectSearchManager.create().getSearch("global_cases").updateSearchResult();
    }

    @FXML
    private void twoDimButtonClicked(ActionEvent event) {
        final Node source = (Node) event.getSource();
        String id = source.getId();
        this.display.setVisible(viewIDs.get(id));
        
        // TODO: remove update, currently just for testing the SearchManager function
        //ClientObjectSearchManager.create().getSearch("global_2D").updateSearchResult();
    }

    @FXML
    private void threeDimButtonClicked(ActionEvent event) {
        final Node source = (Node) event.getSource();
        String id = source.getId();
        this.display.setVisible(viewIDs.get(id));
    }

    @FXML
    private void fourDimButtonClicked(ActionEvent event) {
        final Node source = (Node) event.getSource();
        String id = source.getId();
        this.display.setVisible(viewIDs.get(id));
    }

    @FXML
    private void genomicsButtonClicked(ActionEvent event) {
        final Node source = (Node) event.getSource();
        String id = source.getId();
        this.display.setVisible(viewIDs.get(id));
    }

    @FXML
    private void methButtonClicked(ActionEvent event) {
        final Node source = (Node) event.getSource();
        String id = source.getId();
        this.display.setVisible(viewIDs.get(id));
    }

    @FXML
    private void inspectCaseGeneral(ActionEvent event) {
    }

    @FXML
    private void inspectTwoDimDiagnose(ActionEvent event) {
    }

    @FXML
    private void inspectTwoDimStaining(ActionEvent event) {
    }

    @FXML
    private void inspectThreeDimDiagnose(ActionEvent event) {
    }

    @FXML
    private void inspectThreeDimStaining(ActionEvent event) {
    }

    @FXML
    private void inspectFourDimDiagnose(ActionEvent event) {
    }

    @FXML
    private void inspectFourDimStaining(ActionEvent event) {
    }

    @FXML
    private void inspectGenomicsDiagnose(ActionEvent event) {
    }

    @FXML
    private void inspectMethDiagnose(ActionEvent event) {
    }
    
    public void setDisplay(IDataDisplay display){
        this.display = display;
        this.viewIDs = this.display.getViews();
        for(String key:this.viewIDs.keySet()){
            if(key.equals("Case")){
                ((CountLabel)this.caseButton.getGraphic()).setCount( this.display.getVisibleDataCount(this.viewIDs.get(key)) );
                this.caseButton.setId(key);
            }else if(key.equals("2D")){
                ((CountLabel)this.twoDimButton.getGraphic()).setCount( this.display.getVisibleDataCount(this.viewIDs.get(key)) );
                this.twoDimButton.setId(key);
            }else if(key.equals("3D")){
                ((CountLabel)this.threeDimButton.getGraphic()).setCount( this.display.getVisibleDataCount(this.viewIDs.get(key)) );
                this.threeDimButton.setId(key);
            }else if(key.equals("4D")){
                ((CountLabel)this.fourDimButton.getGraphic()).setCount( this.display.getVisibleDataCount(this.viewIDs.get(key)) );
                this.fourDimButton.setId(key);
            }else if(key.equals("Genomics")){
                ((CountLabel)this.genomicsButton.getGraphic()).setCount( this.display.getVisibleDataCount(this.viewIDs.get(key)) );
                this.genomicsButton.setId(key);
            }else if(key.equals("Methylation")){
                ((CountLabel)this.methButton.getGraphic()).setCount( this.display.getVisibleDataCount(this.viewIDs.get(key)) );
                this.methButton.setId(key);
            }else{
                System.out.println(key);
            }
        }
    }

    @Override
    public void receiveSearchResults(ClientObjectList newResults, String searchIdentifier) {
        if(caseButton==null){ 
            Logger.getLogger(getClass().getName()).warning("HomePane received SearchResult before finished loading...skipping");
            return;
        }
        switch( searchIdentifier ){
            case "global_cases":
                Platform.runLater(() -> { ((CountLabel)this.caseButton.getGraphic()).setCount( newResults.size() );  });
                break;
            case "global_2D":
                Platform.runLater(() -> { ((CountLabel)this.twoDimButton.getGraphic()).setCount( newResults.size() );  });
                break;
            case "global_3D":
                Platform.runLater(() -> { ((CountLabel)this.threeDimButton.getGraphic()).setCount( newResults.size() );  });
                break;
            case "global_4D":
                Platform.runLater(() -> { ((CountLabel)this.fourDimButton.getGraphic()).setCount( newResults.size() );  });
                break;
            case "global_Genome":
                Platform.runLater(() -> { ((CountLabel)this.genomicsButton.getGraphic()).setCount( newResults.size() );  });
                break;
            case "global_Methylation":
                Platform.runLater(() -> { ((CountLabel)this.methButton.getGraphic()).setCount( newResults.size() );  });
                break;
            default:
                Logger.getLogger("TableViewerWindow").warning("TableViewerWindow.receiveSearchResults() for unknown search result ("+searchIdentifier+")");
        }
    }
}