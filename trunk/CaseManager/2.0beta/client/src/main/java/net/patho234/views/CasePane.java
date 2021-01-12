/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.views;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.patho234.controls.CaseController;
import net.patho234.entities.ClientCase;
import net.patho234.entities.pool.CasePool;

/**
 *
 * @author rehkind
 */
public class CasePane extends AnchorPane {
    FXMLLoader loader;
    CaseController controller;
    ClientCase myCase;
    
    @FXML
    AnchorPane caseMainAnchor;
    
    public CasePane () throws IOException {
        this( ClientCase.getCaseTemplate() );
    }
    
    public CasePane (ClientCase theCase) throws IOException {
        myCase = theCase;
        initView();
    }
    
    
    private void initView() throws IOException {
        loader = new FXMLLoader( getClass().getResource( "/fxml/fx_case_pane.fxml") );
        
        Pane caseView =  loader.load();
        controller = loader.getController();
        
        this.getChildren().add(caseView);
        
        controller.loadCase(myCase);
    }
}
