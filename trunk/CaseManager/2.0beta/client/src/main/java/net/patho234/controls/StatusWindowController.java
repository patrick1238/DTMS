/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
    
    synchronized public void setStatus(String currentJob, Integer progress){
        pbLoading.setProgress((double)progress/(double)maxProgress);
        lblCurrent.setText(currentJob);
    }
    
    public void terminate(){
        try {
            for(int i=0; i<100; i++){
                Thread.sleep(15);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(StatusWindowController.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            pbLoading.getScene().getWindow().hide();
        }
    }
}
