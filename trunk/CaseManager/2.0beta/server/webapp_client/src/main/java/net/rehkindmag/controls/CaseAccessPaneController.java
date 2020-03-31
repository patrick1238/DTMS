/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkindmag.controls;

import net.rehkindmag.entities.ClientCase;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.scenario.Settings;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javax.json.JsonArray;
import javax.json.JsonObject;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.interfaces.client.ReadOnlyClientObjectList;
import net.rehkind_mag.utils.HTTP_REQUEST_TYPE;
import net.rehkind_mag.utils.HTTP_STATUS;
import net.rehkindmag.entities.pool.CasePool;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class CaseAccessPaneController extends AccessPaneController implements Initializable {
    @FXML HBox bResponseCases;
    @FXML CheckBox cbEditable;
    
    ArrayList<String> methods;
    ReadOnlyClientObjectList<ClientCase> observeCaseList;
    ArrayList<ClientCase> showList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        methods=new ArrayList<>();
        
        methods.add(HTTP_REQUEST_TYPE.GET);
        methods.add(HTTP_REQUEST_TYPE.GET_ALL);
        methods.add(HTTP_REQUEST_TYPE.CREATE);
        methods.add(HTTP_REQUEST_TYPE.UPDATE);
        methods.add(HTTP_REQUEST_TYPE.DELETE);
        
        ObservableList<String> methodList = new ObservableListWrapper<String>( methods );
        
        cbMethod.setItems(methodList);
        cbMethod.getSelectionModel().select(HTTP_REQUEST_TYPE.GET_ALL);
        onMethodSelected();
        
    }
    
    @FXML
    @Override
    public void fireRequest(){
        switch( selectedMethod ){
            case HTTP_REQUEST_TYPE.GET:
                
            case HTTP_REQUEST_TYPE.GET_ALL:
                this.observeCaseList=CasePool.createPool().getAllEntities();
                detachView();
                this.showList=new ArrayList<>();
                buildView();
                break;
            case HTTP_REQUEST_TYPE.CREATE:
                
            case HTTP_REQUEST_TYPE.UPDATE:
                
            case HTTP_REQUEST_TYPE.DELETE:
                
            default:
                org.jboss.logging.Logger.getLogger( getClass() ).warn( "{0} does not support http method {1} yet.", new String[]{getClass().getName(), selectedMethod});
        }
//        if( cbEndpointTemplate.getSelectionModel().getSelectedItem().startsWith("@DELETE")
//            || cbEndpointTemplate.getSelectionModel().getSelectedItem().startsWith("@CREATE")
//            || cbEndpointTemplate.getSelectionModel().getSelectedItem().startsWith("@UPDATE"))
//        {
//            String asJson=txtCaseJson.getText();
//            if(asJson.equals("")){ 
//                Alert alert = new Alert(AlertType.INFORMATION);
//                alert.setTitle("Case not set as Json object...");
//                alert.setHeaderText("DELETE/CREATE/UPDATE HTTPRequests need an input object...");
//                alert.setContentText("Insert a valid json object representation of the object you would like to create/delete/update...");
//
//                alert.showAndWait();
//                return;
//            }
//        }
        
    }
    
    public void updateView(IHttpResponse response) {
        Logger.getLogger(getClass()).log(Level.INFO, "HttpResponse received: id={0} status: {1} message: {2}", new Object[]{response.getRequestId(), response.getResponseStatus(), response.getMessage()});
        long timeMS=-1;
        Boolean isCached=false;
        if( response.getResponseStatus()==HTTP_STATUS.CACHED ){
            Logger.getLogger(getClass()).log(Level.INFO, "[CACHED] HttpResponse {0} ms.", new Object[]{timeMS});
            isCached=true;
        }else{
            Logger.getLogger(getClass().getName()).log(Level.INFO, "[HTTP] HttpResponse {0} ms.", new Object[]{timeMS});
        }
        final boolean finalIsCached=isCached;
        if( response.getResponseStatus()!=HTTP_STATUS.OK && response.getResponseStatus()!=HTTP_STATUS.CACHED ){
            handleHttpResponseError(response.getRequestId(), response);
            return;
        }
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                
                bResponseCases.setVisible(true);
                String request = pendingHttpRequests.get(response.getRequestId());
                bResponseCases.getChildren().clear();
                
                if( request.startsWith("@GET cases") ){ // response is json array containing cases as JSON objects
                    viewControls.clear();
                    Logger.getLogger(getClass().getName()).info("Received JsonArray for cases, creating views...");
                    JsonArray casesAsJsonArray = (JsonArray)response.getContent();
                    casesAsJsonArray.forEach((curCase) -> {
                        JsonObject caseAsJson = (JsonObject)curCase;
                        ClientCase responseCase = new ClientCase(caseAsJson);

                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fx_case_pane.fxml"));
                            Pane casePane=loader.load();
                            CasePaneController controller = loader.getController();
                            viewControls.add(controller);
                            controller.setCase(responseCase);
                            controller.setCachedView(finalIsCached);
                            controller.setEditable(cbEditable.isSelected());
                            bResponseCases.getChildren().add(casePane);
                        } catch (IOException ex) {
                            Logger.getLogger(CaseAccessPaneController.class.getName()).log(Level.FATAL, null, ex);
                        }
                    });
                }else if(request.startsWith("@DELETE cases")){ // no case as response (deleted)
                    Logger.getLogger(getClass().getName()).info("Case deleted successfully.");
                }
                else{ // all other request result in a single case as JSON object
                    Logger.getLogger(getClass().getName()).info("Received JsonObject for single case, creating view...");
                    JsonObject caseAsJson = (JsonObject)response.getContent();
                    ClientCase responseCase = new ClientCase(caseAsJson);

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fx_case_pane.fxml"));
                        Pane casePane=loader.load();
                        CasePaneController controller = loader.getController();
                        viewControls.add(controller);
                        controller.setCachedView(finalIsCached);
                        controller.setCase(responseCase);
                        controller.setEditable(cbEditable.isSelected());
                        bResponseCases.getChildren().add(casePane);
                    } catch (IOException ex) {
                        Logger.getLogger(CaseAccessPaneController.class.getName()).log(Level.FATAL, null, ex);
                    }
                }
            }
        });
        hbHTTPResponseStatus.setVisible(true);
    }
    
    private void handleHttpResponseError(Integer requestID, IHttpResponse response){
        switch( response.getResponseStatus() ){
            
            default:
                Logger.getLogger(getClass().getName()).log(Level.INFO, "HttpResponse with status ''{0}'' received. TODO: handle error", new Object[]{response.getResponseStatus()});
        }
        
    }
    
    @FXML public void onEditableChanged(){
        for (ClientObjectController cpc : viewControls){
            cpc.setEditable(cbEditable.isSelected());
        }
    }

    @FXML public void onLoginChanged(){
        String user = txtUser.getText();
        String pwd = pfPassword.getText();
        if( user.isEmpty() || pwd.isEmpty() ){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Either user or password not set...");
            alert.setHeaderText("Insert correct Login credentials");
            alert.setContentText("Insert a valid user name and password.");

            alert.showAndWait();
            return;
        }
        
        Settings.set("login.user", user);
        Settings.set("login.password", pwd);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Login credentials changed");
            alert.setHeaderText("Login changed");
            alert.setContentText("Using new entered login values for next HTTPRequest");

            alert.showAndWait();
    }
    
    @Override
    protected void buildView() {
        bResponseCases.setVisible(true);
        Logger.getLogger(getClass()).info("Creating views for {0} ClientCases.", new Object[]{observeCaseList.size()});
        this.observeCaseList.forEach((theCase) -> {
            try {
                ClientCase clientCase = (ClientCase)theCase;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fx_case_pane.fxml"));
                Pane casePane=loader.load();
                CasePaneController controller = loader.getController();
                viewControls.add(controller);
                controller.setCase(clientCase);
                controller.setCachedView(false);
                controller.setEditable(cbEditable.isSelected());
                bResponseCases.getChildren().add(casePane);
            } catch (IOException ex) {
                Logger.getLogger(CaseAccessPaneController.class.getName()).log(Level.FATAL, null, ex);
            }
        });
        hbHTTPResponseStatus.setVisible(true);
    }
}
