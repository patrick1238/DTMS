/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.controls;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import net.patho234.entities.ClientCase;
import net.patho234.entities.UserLogin;
import net.patho234.entities.filter.ClientObjectSearchManager;
import net.patho234.interfaces.client.ClientObjectList;
import net.patho234.interfaces.client.IDtmsSearchListener;
import net.patho234.webapp_client.FxmlManager;
import org.jboss.logging.Logger;

/**
 * FXML Controller class
 *
 * @author patri
 */
public class TableViewerController implements Initializable, IDtmsSearchListener {

    @FXML
    private MenuItem addCaseMenuItem;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private MenuItem preferencesMenuItem;
    @FXML
    private MenuItem profileMenuItem;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private Menu userMenu;
    @FXML
    private MenuItem changeUserMenuItem;
    @FXML
    private MenuItem logoutMenuItem;
    @FXML
    private StackPane tableStack;
    @FXML
    private AnchorPane casePane;
    @FXML
    private AnchorPane twoDimPane;
    @FXML
    private AnchorPane threeDimPane;
    @FXML
    private AnchorPane fourDimPane;
    @FXML
    private AnchorPane genomicsPane;
    @FXML
    private AnchorPane methPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        userMenu.setText("Logged in as "+UserLogin.getUserName());
        
        Logger.getLogger(getClass()).info("Setting up menu items");
        //initializeMenuEventHandler();

        
        Logger.getLogger(getClass()).info("Loading cases panel.");
        
        Logger.getLogger(getClass()).info("Loading 2D images panel.");
        
        Logger.getLogger(getClass()).info("Loading 3D images panel.");
        
        Logger.getLogger(getClass()).info("Loading 4D images panel.");
        bindTableViewToSearchManger();
    }
    
    
    private void initializeMenuEventHandler(){
        // Exit event handler for Menu->Exit:
        closeMenuItem.setOnAction( new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
            }
        } );
        
        
        
        // TODO: implement  missing MenuItem events
        Logger.getLogger(getClass()).warn("Not yet all MenuItem handler implemented.");
    }
    
    private void bindTableViewToSearchManger(){
        ClientObjectSearchManager.create().getSearch("global").addDtmsSearchResultListener(this);
    }

    @Override
    public void receiveSearchResults(ClientObjectList newResults) {
        for( ClientCase resultCase : (ClientObjectList<ClientCase>)newResults ){
            System.out.println("todo: display case "+resultCase);
        }
    }
}
