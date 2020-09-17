/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import net.patho234.controls.elements.CaseFilterPaneController;

/**
 *
 * @author rehkind
 */
public class CaseFilterPane extends AnchorPane {
    FXMLLoader loader;
    CaseFilterPaneController controller;
    AnchorPane caseFilterPane;
    
    public CaseFilterPane() throws IOException{
        super();
        
        FXMLLoader loader = new FXMLLoader( getClass().getResource("/fxml/fx_case_filter_pane.fxml") );
        
        caseFilterPane = loader.load();
        controller = loader.getController();
        
        getChildren().add(caseFilterPane);
        AnchorPane.setBottomAnchor(caseFilterPane, 0.);
        AnchorPane.setLeftAnchor(caseFilterPane, 0.);
        AnchorPane.setTopAnchor(caseFilterPane, 0.);
        AnchorPane.setRightAnchor(caseFilterPane, 0.);
        
        //setDisable(false);
    }
}
