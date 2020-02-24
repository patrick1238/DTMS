/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windowcontroller;

import Databank.SubmitterBank;
import EventHandler.AutoCompleteHandler;
import dtms_2_beta_wc.dtms_2_beta_wc;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class LoginController implements Initializable {
    
    SubmitterBank databank;
    @FXML
    private Button LoginButton;
    @FXML
    private Button RegisterButton;
    @FXML
    private ComboBox<String> RegisterBox;
    @FXML
    private PasswordField PasswordField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databank = SubmitterBank.get();
        ObservableList<String> usernames = FXCollections.observableArrayList(databank.getUserNames());
        System.out.println(usernames);
        RegisterBox.setItems(usernames);
        new AutoCompleteHandler(this.RegisterBox);
    }    

    @FXML
    private void LoginButtonPressed(ActionEvent event) {
        if(databank.verifyUser(RegisterBox.getEditor().getText(), PasswordField.getText())){
            Stage curstage = (Stage) this.LoginButton.getScene().getWindow();
            curstage.close();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SplashScreen.fxml"));
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
            final SplashScreenController controller = fxmlLoader.getController();
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        }else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("DTMS");
            alert.setHeaderText("Login failed");
            alert.setContentText("Username or password wrong!");
            alert.showAndWait();
        }
    }

    @FXML
    private void RegisterButtonPressed(ActionEvent event) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/RegisterUser.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(dtms_2_beta_wc.class.getName()).log(Level.SEVERE, null, ex);
        }
        final RegisterUserController controller = fxmlLoader.getController();
        Scene scene = new Scene(root);
        stage.setTitle("Register user");
        stage.setScene(scene);
        stage.show();
    }
    
}
