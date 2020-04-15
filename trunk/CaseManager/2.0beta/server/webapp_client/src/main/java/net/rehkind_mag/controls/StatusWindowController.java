/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.controls;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class StatusWindowController implements Initializable {
    Integer maxProgress=100;
    
    @FXML ProgressBar pbLoading;
    @FXML Label lblCurrent;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pbLoading.setProgress(0);
    }    
    
    public void setStatus(String currentJob, Integer progress){
        pbLoading.setProgress((double)progress/(double)maxProgress);
        lblCurrent.setText(currentJob);
        
        if(progress==maxProgress){
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ex) {
                Logger.getLogger(StatusWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
            pbLoading.getScene().getWindow().hide();
        }
        
    }
}
