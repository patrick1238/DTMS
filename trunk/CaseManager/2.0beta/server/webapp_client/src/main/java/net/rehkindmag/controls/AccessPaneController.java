/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkindmag.controls;

import net.rehkindmag.http.HttpRequestManager;
import net.rehkindmag.entities.UserLogin;
import com.sun.scenario.Settings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javax.json.Json;
import net.rehkind_mag.interfaces.IHttpResponse;
import net.rehkind_mag.interfaces.IHttpResponseReceiver;
import net.rehkind_mag.utils.HttpAccessRequest;
import net.rehkind_mag.utils.UUIDGenerator;

/**
 *
 * @author rehkind
 */
public abstract class AccessPaneController implements IHttpResponseReceiver {
    HashMap<String,String> endpoints;
    protected String epTemplateName="";
    protected String epTemplate;
    protected String epResult;
    
    static UUIDGenerator uuidGen = new UUIDGenerator();
    HashMap<Integer, String> pendingHttpRequests=new HashMap<>();
    
    //FXML attributes:
    @FXML ComboBox<String> cbEndpointTemplate;
    @FXML HBox hbEndpointVariables;
    @FXML Label lblEndpoint;
    @FXML Label lblUUID;
    @FXML TextField txtCaseJson;
    //   -- HttpResponseStatus
    @FXML HBox hbHTTPResponseStatus;
    @FXML Label lblResponseStatus;
    @FXML Label lblResponseTime;
    
    public void onEndpointTemplateSelected(){
        String newEndpointName=cbEndpointTemplate.getSelectionModel().getSelectedItem();
        if( newEndpointName != this.epTemplateName ){
            Logger.getLogger(getClass().getName()).log(Level.INFO, "New endpoint selected: {0}", newEndpointName);
            this.epTemplate=endpoints.get(newEndpointName);
            // TODO: update GUI / listener
            this.epTemplateName=newEndpointName;
        }
        hbHTTPResponseStatus.setVisible(false);
        updateEndpointGUI();
        updateEndpointResult();
    }
    
    private void updateEndpointGUI(){
        hbEndpointVariables.getChildren().clear();
        // check which variables need to be set:
        ArrayList<String> variables=new ArrayList<>();
        for( String var : epTemplate.split("\\{") ){
            if( !var.contains("}") ){ continue; }
            String tmpVarName=var.split("\\}")[0];
            Label tmpLabel=new Label(tmpVarName+":");
            TextField tmpTextField=new TextField();
            tmpTextField.setId(tmpVarName);
            
            tmpTextField.textProperty().addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        if( !oldValue.equals(newValue)){
                            updateEndpointResult();
                        }
                        
                    }
                }
            );
            hbEndpointVariables.getChildren().add(tmpLabel);
            hbEndpointVariables.getChildren().add(tmpTextField);
        }
        
    }
    
    private void updateEndpointResult(){
        epResult=epTemplate;
        for(Node n : hbEndpointVariables.getChildren()){
            try{
                TextField curField = (TextField)n;
                String toReplace="{"+n.getId()+"}";
                String toInsert=curField.getText();
                Logger.getLogger(getClass().getName()).info("Replacing template variable '"+toReplace+"' with value '"+toInsert+"'");
                epResult=epResult.replace(toReplace, toInsert);
            }catch(ClassCastException ex){
                // ignored...happens for Labels we only want to handle the TextFields
            }
        }
        lblEndpoint.setText(epResult);
    }
    
    @FXML
    public void fireHTTPRequest(){
        HttpRequestManager manager = HttpRequestManager.createHttpRequestManager(Settings.get("server.address"), HttpRequestManager.CONTANT_TYPE_JSON);
        String uuid = lblUUID.getText();
        
        if( cbEndpointTemplate.getSelectionModel().getSelectedItem().startsWith("@DELETE")
            || cbEndpointTemplate.getSelectionModel().getSelectedItem().startsWith("@CREATE")
            || cbEndpointTemplate.getSelectionModel().getSelectedItem().startsWith("@UPDATE"))
        {
            String asJson=txtCaseJson.getText();
            if(asJson.equals("")){ 
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Case not set as Json object...");
                alert.setHeaderText("DELETE/CREATE/UPDATE HTTPRequests need an input object...");
                alert.setContentText("Insert a valid json object representation of the object you would like to create/delete/update...");

                alert.showAndWait();
                return;
            }
        }
        
        //JsonParser parser=Json.createParser(new ByteArrayInputStream(txtaRequestBody.getText().getBytes(Charset.forName("UTF-8"))));
        HttpAccessRequest request = new HttpAccessRequest( epTemplate, epResult, uuid, UserLogin.getLoginAsJson(), Json.createObjectBuilder().build() );
        IHttpResponse cachedResponse = manager.fireJsonHttpRequest(request, this);
        
        pendingHttpRequests.put(cachedResponse.getRequestId(), cbEndpointTemplate.getSelectionModel().getSelectedItem()+" "+lblEndpoint.getText()+" uuid="+uuid);
        if( cachedResponse.responseSucceeded() ){ this.receiveHttpResponse(cachedResponse); }
    }
    
    @FXML
    public void generateUUID(){
        lblUUID.setText( uuidGen.getRandomUUIDString() );
    }
}
