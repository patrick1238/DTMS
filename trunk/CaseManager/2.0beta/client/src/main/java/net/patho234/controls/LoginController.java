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
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;
import net.patho234.entities.ClientSubmitter;
import net.patho234.entities.UserLogin;
import net.patho234.entities.pool.SubmitterPool;
import net.patho234.gui.ClientPopup;
import net.patho234.interfaces.ISubmitter;
import net.patho234.interfaces.client.ISubmitterReceiver;
import net.patho234.interfaces.client.ReadOnlyClientObjectList;
import net.patho234.views.MainWindow;
import net.patho234.views.TableViewerWindow;
import net.patho234.views.RegistrationWindow;
import net.patho234.webapp_client.FxmlManager;
import org.jboss.logging.Logger;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class LoginController implements Initializable, ISubmitterReceiver {

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
    }    

    @FXML
    private void registerPressed(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fx_register_pane.fxml"));
        Parent root = loader.load();
        
        RegistrationController controller = loader.getController();
        
        FxmlManager.applyDefaultStyle( root );
        
        Scene scene = new Scene(root);
        Stage registerStage = new RegistrationWindow( this );
        registerStage.show();
        
    }

    @FXML
    private void loginPressed(ActionEvent event) {
        String user = "guest";//usernameField.getText();
        String pwd = "123456";//passwordField.getText();
        
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
        usernameField.getScene().getWindow().setOnCloseRequest(FxmlManager.DISPOSE_WINDOW_HANDLER);
        usernameField.getScene().getWindow().hide();
        System.out.println("login '"+user+"/"+pwd+"' is valid TODO: now starting main window");
        MainWindow mainWnd = new MainWindow();
        mainWnd.loadTableViewerWindow();
        mainWnd.loadSubpanes();
        mainWnd.show();
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
    
    @Override
    public void setSubmitter(ISubmitter submitter){
        this.usernameField.setText(submitter.getLogin());
        this.passwordField.setText(submitter.getPassword());
        
        UserLogin.setLogin(submitter);
    }
}
