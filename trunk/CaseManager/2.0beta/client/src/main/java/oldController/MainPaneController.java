/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oldController;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class MainPaneController implements Initializable {
    // FXML attributes:
    @FXML Button bttCases;
    @FXML Button bttServices;
    @FXML Button bttClinics;
    
    @FXML StackPane spSubpaneview;
    
    // other attributes
    HashMap<String,Pane> subPanes;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            createSubPanes();
        } catch (IOException ex) {
            Logger.getLogger(MainPaneController.class.getName()).log(Level.SEVERE, "Error while loading sub-panes", ex);
            System.exit(100);
        }
    }    
    
    @FXML
    public void onSubpaneSelected(ActionEvent e){
        switch( ( (Button)e.getSource() ).getText() ){
            case "Cases":
                selectSubpane("casesPane");
                break;
            case "Services":
                selectSubpane("servicesPane");
                break;
            case "Clinics":
                
                break;
            default:
               Logger.getLogger(MainPaneController.class.getName()).log(Level.WARNING, "onSubpaneSelected was called by unknown source: {0}", ( (Button)e.getSource() ).getId()); 
        }
    }
    
    private void selectSubpane(String name){
        for( String testName : subPanes.keySet() ){
            if( testName.equals(name) ){
                subPanes.get(testName).setVisible(true);
                subPanes.get(testName).toFront();
                Logger.getLogger(MainPaneController.class.getName()).log(Level.INFO, "Briging sub-pane {0} to front.", testName);
            }else{
                subPanes.get(testName).setVisible(false);
                Logger.getLogger(MainPaneController.class.getName()).log(Level.INFO, "Hiding sub-pane {0}.", testName);
            }
        }
    }
    
    private void createSubPanes() throws IOException{
        subPanes=new HashMap<>();
        
        Pane p=FXMLLoader.load(getClass().getResource("/fxml/fx_case_access_pane.fxml"));
        subPanes.put("casesPane", p);
        spSubpaneview.getChildren().add(p);
        // TODO: replace with real services pane
        p=FXMLLoader.load(getClass().getResource("/fxml/fx_service_access_pane.fxml"));
        spSubpaneview.getChildren().add(p);
        subPanes.put("servicesPane", p);
        
        selectSubpane("casesPane");
    }
}
