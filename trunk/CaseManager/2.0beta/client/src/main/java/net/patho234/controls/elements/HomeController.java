/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls.elements;

import com.sun.xml.internal.ws.api.server.ServiceDefinition;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import net.patho234.entities.filter.ClientObjectSearchManager;
import net.patho234.entities.pool.ServiceDefinitionPool;
import net.patho234.interfaces.IDataDisplay;
import net.patho234.interfaces.IMetadataValue;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class HomeController implements Initializable {

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
    }    

    @FXML
    private void caseButtonClicked(ActionEvent event) {
        final Node source = (Node) event.getSource();
        String id = source.getId();
        this.display.setVisible(viewIDs.get(id));
        // TODO: remove update, currently just for testing the SearchManager function
        ClientObjectSearchManager.create().getSearch("global").updateSearchResult();
        for(IMetadataValue mv : ServiceDefinitionPool.createPool().getEntity(3).getFields() ){
            System.out.println("oooOOO000 "+mv.getKey()+" TYPE="+mv.getValueType());
        }
    }

    @FXML
    private void twoDimButtonClicked(ActionEvent event) {
        final Node source = (Node) event.getSource();
        String id = source.getId();
        this.display.setVisible(viewIDs.get(id));
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
                this.caseButton.setText(key + ": " + Integer.toString(this.display.getVisibleDataCount(this.viewIDs.get(key))));
                this.caseButton.setId(key);
            }else if(key.equals("2D")){
                this.twoDimButton.setText(key + ": " + Integer.toString(this.display.getVisibleDataCount(this.viewIDs.get(key))));
                this.twoDimButton.setId(key);
            }else if(key.equals("3D")){
                this.threeDimButton.setText(key + ": " + Integer.toString(this.display.getVisibleDataCount(this.viewIDs.get(key))));
                this.threeDimButton.setId(key);
            }else if(key.equals("4D")){
                this.fourDimButton.setText(key + ": " + Integer.toString(this.display.getVisibleDataCount(this.viewIDs.get(key))));
                this.fourDimButton.setId(key);
            }else if(key.equals("Genomics")){
                this.genomicsButton.setText(key + ": " + Integer.toString(this.display.getVisibleDataCount(this.viewIDs.get(key))));
                this.genomicsButton.setId(key);
            }else if(key.equals("Methylation")){
                this.methButton.setText(key + ": " + Integer.toString(this.display.getVisibleDataCount(this.viewIDs.get(key))));
                this.methButton.setId(key);
            }else{
                System.out.println(key);
            }
        }
    }
}
