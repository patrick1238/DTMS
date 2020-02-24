/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windowcontroller;

import Databank.SubmitterBank;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class RegisterUserController implements Initializable {

    @FXML
    private Button RegisterButton;
    @FXML
    private TextField SurnameField;
    @FXML
    private TextField ForenameField;
    @FXML
    private TextField TitleField;
    @FXML
    private TextField UserField;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private PasswordField RepeatPasswordField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void RegisterButtonPressed(ActionEvent event) {
        SubmitterBank databank = SubmitterBank.get();        
        if(SurnameField.getText().isEmpty() || ForenameField.getText().isEmpty() || UserField.getText().isEmpty() || PasswordField.getText().isEmpty() || RepeatPasswordField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("DTMS");
            alert.setHeaderText("Registration failed");
            alert.setContentText("Information incomplete!");
            alert.showAndWait();            
        }else if(databank.containsUser(this.UserField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("DTMS");
            alert.setHeaderText("Registration failed");
            alert.setContentText("Username still exists!");
            alert.showAndWait();
        }else if(!this.PasswordField.getText().equals(this.RepeatPasswordField.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("DTMS");
            alert.setHeaderText("Registration failed");
            alert.setContentText("Password and its repetition are unequal!");
            alert.showAndWait();
        }else{
            databank.addUser(this.SurnameField.getText(), this.ForenameField.getText(), this.TitleField.getText(), this.UserField.getText(), this.PasswordField.getText());
            Stage stage = (Stage)RegisterButton.getScene().getWindow();
            stage.close();
        }
    }
    
}
