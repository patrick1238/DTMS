/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import BackgroundHandler.DataCache;
import Database.Database;
import Interfaces.ImageObject;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 *
 * @author patri
 * @param <T>
 */
public class AutoFillTextFieldListener<T extends ImageObject> implements EventHandler<KeyEvent> {

    /**
     *
     */
    Database database;

    /**
     *
     * @param type
     */
    public AutoFillTextFieldListener(String type) {
        this.database = DataCache.getDataCache().getDatabase(type);
    }

    /**
     *
     * @param field
     */
    public void bind(TextField field) {
        field.setOnKeyReleased(this);
    }

    private void filterList(ArrayList<TextField> keywords) {
        ObservableList filtered = FXCollections.observableArrayList();
        for (Object object : this.database.getDatabaseList()) {
            ImageObject image = (ImageObject) object;
            filtered.add(image);
            for(TextField field:keywords){
                String cur = image.getAsHashMap().get(field.getId()).get();
                if (cur != null) {
                    if (!cur.toLowerCase().contains(field.getText().toLowerCase())) {
                        filtered.remove(image);
                    }
                }
            }
        }
        this.database.setTableList(filtered);
    }

    @Override
    public void handle(KeyEvent event) {
        TextField field = (TextField) event.getSource();
        GridPane pane = (GridPane)field.getParent();
        ObservableList<Node> children = pane.getChildren();
        ArrayList<TextField> keywords = new ArrayList<>();
        for(Node node:children){
            if(!node.getId().equals("descriptor")){
                keywords.add((TextField) node);
            }
        }
        filterList(keywords);
    }
}
