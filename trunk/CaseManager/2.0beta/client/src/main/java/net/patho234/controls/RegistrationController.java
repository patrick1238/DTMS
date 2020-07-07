package net.patho234.controls;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import net.patho234.entities.ClientSubmitter;
import net.patho234.gui.ClientPopup;
import net.patho234.interfaces.ISubmitter;
import org.jboss.logging.Logger;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class RegistrationController implements Initializable {

    @FXML
    private ChoiceBox<String> titleField;
    @FXML
    private TextField forenameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button registerButton;
    @FXML
    private PasswordField passwordConfirmField;

    ISubmitter registeredSubmitter = null;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleField.getItems().add("Frau");
        titleField.getItems().add("Herr");
        titleField.getItems().add("Doktor");
        titleField.getSelectionModel().select(0);
    }    

    @FXML
    private void cancelPressed(ActionEvent event) {
        registeredSubmitter=null;
        this.cancelButton.getScene().getWindow().hide();
    }

    @FXML
    private void registerPressed(ActionEvent event) {
        registeredSubmitter= ClientSubmitter.getSubmitterTemplate();
        
        registeredSubmitter.setForename( forenameField.getText() );
        registeredSubmitter.setSurname( surnameField.getText() );
        registeredSubmitter.setTitle( (String)titleField.getSelectionModel().getSelectedItem() );
        registeredSubmitter.setLogin( loginField.getText() );
        registeredSubmitter.setPassword( passwordField.getText() );
        
        Logger.getLogger(getClass()).warn("Local submitter created. TODO: check if login is available and persist to SubmitterPool");
        Boolean invalidData=false;
        
        invalidData = (forenameField.getText().equals("")) ? true : invalidData;
        invalidData = (surnameField.getText().equals("")) ? true : invalidData;
        invalidData = (titleField.getSelectionModel().getSelectedItem() == "" || titleField.getSelectionModel().getSelectedItem() == null ) ? true : invalidData;
        invalidData = (loginField.getText().equals("")) ? true : invalidData;
        invalidData = (passwordField.getText().equals("")) ? true : invalidData;
        
        
        if( invalidData ){
            Logger.getLogger(getClass()).warn("Data for new account is invalid.");
            try {
                new ClientPopup("Login failed", "All values need to be filled in...aborting").show( forenameField.getScene().getWindow() );
            }catch(IOException ioEx){
                Logger.getLogger(getClass()).error("FXML template for ClientPopup window not found...something is wrong with the JAR.");
            }finally{
                return;
            }
        }
        Window regWnd = this.cancelButton.getScene().getWindow();
        regWnd.fireEvent( new WindowEvent(regWnd, WindowEvent.WINDOW_CLOSE_REQUEST) );
        this.cancelButton.getScene().getWindow().hide();
    }
    
    
    public ISubmitter getSubmitter(){ return this.registeredSubmitter; }
}
