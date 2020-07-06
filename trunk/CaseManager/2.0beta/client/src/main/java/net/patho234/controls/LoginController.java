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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import net.patho234.entities.ClientSubmitter;
import net.patho234.entities.pool.SubmitterPool;
import net.patho234.gui.ClientPopup;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;
import org.jboss.logging.Logger;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button registerButton;
    @FXML
    private Button loginButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void registerPressed(ActionEvent event) {

    }

    @FXML
    private void loginPressed(ActionEvent event) {
        String user = usernameField.getText();
        String pwd = passwordField.getText();
        System.out.println("PIMPANELLA");
        boolean allowLogin=true;
        String errorMsg="";
        if( "".equals(user) || user == null ){
            allowLogin=false;
            errorMsg="Required login field is empty.\n";
        }
        
        if( "".equals(pwd) || pwd == null ){
            allowLogin=false;
            errorMsg+="Required password field is empty.\n";
        }
        
        if( !allowLogin ){
            try {
                new ClientPopup("Login failed", errorMsg).show( usernameField.getScene().getWindow() );
            }catch(IOException ioEx){
                Logger.getLogger(getClass()).error("FXML template for ClientPopup window not found...something is wrong with the JAR.");
            }
            return;
        }
        
        // TODO:
        // start MainWindow here
    }
    
    private boolean isValidLogin(String login, String password){
        ReadOnlyClientObjectList<ClientSubmitter> submitters = SubmitterPool.createPool().getAllEntities();
        boolean isValid=false;
        boolean loginExists=false;
        for( ClientSubmitter cs : submitters ){
            if(cs.getLogin().equals(login)){
                
                isValid = isValid || cs.getPassword().equals(password);
            }
        }
        
        if( !isValid ){
            String errorMsg = ( !loginExists )?"Account with login '"+login+"' was not found, check for possible typos or register an account first.":"Password was incorrect.";
            try {
                new ClientPopup("Login failed", errorMsg).show(usernameField.getScene().getWindow());
            }catch(IOException ioEx){
                Logger.getLogger(getClass()).error("FXML template for ClientPopup window not found...something is wrong with the JAR.");
            }
        }
        return isValid;
    }
}
