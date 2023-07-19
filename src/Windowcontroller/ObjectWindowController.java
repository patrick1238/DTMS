/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windowcontroller;

import Listener.AutoFillImageListener;
import BackgroundHandler.Config;
import EventHandler.FileChooserButtonClickedHandler;
import EventHandler.SubmitButtonClickedHandler;
import EventHandler.UpdateButtonClickedHandler;
import Interfaces.ImageObject;
import Observer.ObserveConnectedFilesClass;
import ImageObjects.ImageObject2D;
import ImageObjects.ImageObject3D;
import ImageObjects.ImageObject4D;
import ImageObjects.ImageObjectGeneral;
import Listener.AutoCompleteListener;
import Listener.AutoFillTextFieldListener;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * FXML DTMSController class
 *
 * @author patri
 */
public class ObjectWindowController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private GridPane gpane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TitledPane titledPane;

    private ImageObject img;
    private String title;
    private Stage stage;

    int index = 0;
    int height = 55;
    
    public void addTitle(String title){
        this.title = title;
        this.titledPane.setText(this.title);
    }

    /**
     *
     * @param label
     * @param field
     */
    public void addRowWithField(Label label, TextField field) {
        label.setId("descriptor");
        field.setPrefWidth(202);
        field.setId(label.getText().replace(": ", ""));
        gpane.addRow(index, label, field);
        index++;
    }

    /**
     *
     * @param label
     * @param button
     * @param controller
     */
    public void addRowWithButton(Label label, Button button, ObjectWindowController controller) {
        label.setId("descriptor");
        button.setOnMouseClicked(new FileChooserButtonClickedHandler(this.img));
        button.setId(label.getText().replace(": ", ""));
        button.setPrefWidth(202);
        button.setText("Select " + label.getText().replace("Path: ", ""));
        gpane.addRow(index, label, button);
        index++;
    }

    /**
     *
     * @param label
     * @param label2
     */
    public void addRowWithLabel(Label label, Label label2) {
        label.setId("descriptor");
        label2.setPrefWidth(202);
        label2.setId(label.getText().replace(": ", ""));
        gpane.addRow(index, label, label2);
        index++;
    }

    /**
     *
     * @param label
     * @param comboBox
     */
    public void addRowWithComboBox(Label label, ComboBox comboBox) {
        comboBox.setPrefWidth(202);
        comboBox.setId(label.getText().replace(": ", ""));
        if(label.getText().contains("FileType")){
            comboBox.setEditable(false);
            comboBox.getSelectionModel().selectFirst();
        }
        gpane.addRow(index, label, comboBox);
        index++;
    }

    /**
     *
     */
    public void addSubmitButton() {
        Button button = new Button();
        button.setText("Submit");
        button.setPrefWidth(202);
        button.setOnMouseClicked(new SubmitButtonClickedHandler((ImageObjectGeneral)this.img));
        gpane.addRow(index, new Label(), button);
        index++;
    }
    
    public void addUpdateButton(ImageObject oldImage) {
        Button button = new Button("Update");
        button.setPrefWidth(202);
        button.setOnMouseClicked(new UpdateButtonClickedHandler(oldImage,this.img));
        gpane.addRow(index, new Label(), button);
        index++;
    }

    /**
     *
     * @param type
     */
    public void initializeListenerForFiltering(String type) {
        AutoFillTextFieldListener listener = new AutoFillTextFieldListener(type);
        ObservableList list = gpane.getChildren();
        int k = 0;
        for (int i = k; i < list.size(); i += 2) {
            Label label = (Label) list.get(i);
            TextField field = (TextField) list.get(i + 1);
            field.setId(label.getText().replace(": ", ""));
            listener.bind(field);
        }
    }

    /**
     *
     */
    public void initializeListenerForAddingAndEditing() {
        AutoFillImageListener listener = new AutoFillImageListener(this.img);
        ObservableList list = gpane.getChildren();
        for (Object o : list) {
            if (!o.toString().contains("Label")) {
                if (o.toString().contains("TextField")) {
                    TextField field = (TextField) o;
                    listener.bindTextField(field);
                } else if (o.toString().contains("ComboBox")) {
                    ComboBox box = (ComboBox) o;
                    if(!box.getId().contains("FileType")){
                        TextField field = box.getEditor();
                        field.setId(box.getId());
                        listener.bindTextField(field);
                        new AutoCompleteListener(box);
                    }else{
                        listener.bindComboBox(box);
                    }                    
                }
            }
        }
    }
    
    public ImageObject getImageObject(){
        return this.img;
    }
    
    public void setImageObject(ImageObject img){
        HashMap<String,SimpleStringProperty> oldImage = img.getAsHashMap();
        for(String key:oldImage.keySet()){
            this.img.setValue(key, oldImage.get(key));
        }
    }
    
    public void initImageObject(String type){
        switch(type){
            case "General":
                this.img = new ImageObjectGeneral();
                break;
            case "2D":
                this.img = new ImageObject2D();
                break;
            case "3D":
                this.img = new ImageObject3D();
                break;
            case "4D":
                this.img = new ImageObject4D();
                break;
        }
    }

    /**
     *
     * @param o
     */
    public void setWindow(ImageObject o) {
        Config config = Config.getConfig();
        ObservableList list = gpane.getChildren();
        o.getImageID();
        for (Object t : list) {
            Node n = (Node) t;
            if (n.getId()!=null) {
                if(!n.getId().equals("descriptor")){
                    switch (config.getControllitem(o.getType(),n.getId())) {
                        case "Field":
                            TextField field = (TextField) n;
                            field.setText(this.img.getAsHashMap().get(field.getId()).get());
                            break;
                        case "Box":
                            ComboBox box = (ComboBox) n;
                            if(box.getId().contains("FileType")){
                                box.getSelectionModel().select(this.img.getAsHashMap().get(box.getId()).get());
                            }else{
                                box.getEditor().setText(this.img.getAsHashMap().get(box.getId()).get());
                            }
                            break;
                        case "Button":
                            Button button = (Button) n;
                            File file = new File(this.img.getAsHashMap().get(button.getId()).get());
                            if (file.getName().isEmpty() || file.getName().length()<=1) {
                                button.setText("Select " + button.getId().replace("Path", ""));
                            } else {
                                button.setText(file.getName());
                            }   break;
                        case "Label":
                            Label label = (Label) n;
                            label.setText(this.img.getAsHashMap().get(label.getId()).get());
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    } 
    
    public void setSize(){
        this.scrollPane.setPrefHeight(height*index);
        this.anchorPane.setPrefHeight((height*index)+10);
    }    
    
    public void setStage(Stage stage){
        this.stage = stage;
    }
}
