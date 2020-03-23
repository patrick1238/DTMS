/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkindmag.controls;

import net.rehkindmag.entities.ClientCase;
import com.sun.javafx.collections.ObservableListWrapper;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javax.json.JsonArray;
import javax.json.JsonObject;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.utils.HTTP_STATUS;

/**
 * FXML Controller class
 *
 * @author rehkind
 */
public class CaseAccessPaneController extends AccessPaneController implements Initializable {
    @FXML HBox bResponseCases;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        endpoints=new HashMap<>();
        
        endpoints.put("@GET case", "casepool/case/{ID}");
        endpoints.put("@GET cases", "casepool/cases");
        endpoints.put("@UPDATE case", "casepool/case/update");
        endpoints.put("@CREATE case", "casepool/case/create");
        endpoints.put("@DELETE case", "casepool/case/delete");
        
        ArrayList<String> asList = new ArrayList<String>();
        asList.addAll(endpoints.keySet());
        ObservableList<String> endpointList = new ObservableListWrapper<String>( asList );
        
        cbEndpointTemplate.setItems(endpointList);
        cbEndpointTemplate.getSelectionModel().select("@GET cases");
        onEndpointTemplateSelected();
        
    }

    @Override
    public void receiveHttpResponse(IHttpResponse response) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "HttpResponse received: id={0} status: {1} message: {2}", new Object[]{response.getRequestId(), response.getResponseStatus(), response.getMessage()});
        long timeMS=-1;
        Boolean isCached=false;
        if( response.getResponseStatus()==HTTP_STATUS.CACHED ){
            Logger.getLogger(getClass().getName()).log(Level.INFO, "[CACHED] HttpResponse {0} ms.", timeMS);
            isCached=true;
        }else{
            Logger.getLogger(getClass().getName()).log(Level.INFO, "[HTTP] HttpResponse {0} ms.", timeMS);
        }
        final boolean finalIsCached=isCached;
        if( response.getResponseStatus()!=HTTP_STATUS.OK && response.getResponseStatus()!=HTTP_STATUS.CACHED ){
            handleHttpResponseError(response.getRequestId(), response);
            return;
        }
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                

                String request = pendingHttpRequests.get(response.getRequestId());
                bResponseCases.getChildren().clear();
                
                if( request.startsWith("@GET cases") ){ // response is json array containing cases as JSON objects
                    Logger.getLogger(getClass().getName()).info("Received JsonArray for cases, creating views...");
                    JsonArray casesAsJsonArray = (JsonArray)response.getContent();
                    casesAsJsonArray.forEach((curCase) -> {
                        JsonObject caseAsJson = (JsonObject)curCase;
                        ClientCase responseCase = new ClientCase(caseAsJson);

                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fx_case_pane.fxml"));
                            Pane casePane=loader.load();
                            CasePaneController controller = loader.getController();
                            controller.setCase(responseCase);
                            controller.setCachedView(finalIsCached);
                            bResponseCases.getChildren().add(casePane);
                        } catch (IOException ex) {
                            Logger.getLogger(CaseAccessPaneController.class.getName()).log(Level.SEVERE, null, ex);
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
                        controller.setCachedView(finalIsCached);
                        controller.setCase(responseCase);
                        bResponseCases.getChildren().add(casePane);
                    } catch (IOException ex) {
                        Logger.getLogger(CaseAccessPaneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        hbHTTPResponseStatus.setVisible(true);
    }
    
    private void handleHttpResponseError(Integer requestID, IHttpResponse response){
        switch( response.getResponseStatus() ){
            
            default:
                Logger.getLogger(getClass().getName()).info("HttpResponse with status '"+response.getResponseStatus()+"' received. TODO: handle error");
        }
        
    }
}
