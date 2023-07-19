/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import BackgroundHandler.DataCache;
import BackgroundHandler.ErrorLog;
import Database.Database;
import Windowcontroller.ObjectWindowController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import Interfaces.DTMSView;

/**
 *
 * @author patri
 */
public class FilterNode implements DTMSView{
    
    Node node;
    Database database;
    Pane parent;
    
    public FilterNode(String type, Pane parent) {
        try {
            this.database = DataCache.getDataCache().getDatabase(type);
            this.parent = parent;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ObjectWindow.fxml"));
            this.node = (Node) loader.load();
            ObjectWindowController controller = loader.<ObjectWindowController>getController();
            prepareView(controller);
            this.parent.getChildren().add(node);
        } catch (IOException ex) {
            ErrorLog.getErrorLog().createLogEntry(2, "Can't load fxml of" + this.getIdentifier());
        }
    }
    
    private void prepareView(ObjectWindowController controller) {
        controller.addTitle("Filter bar");
        for (String head : database.getEntryHeader()) {
            controller.addRowWithField(new Label(head + ": "), new TextField());
        }
        controller.initializeListenerForFiltering(database.getType());
        controller.setSize();
        AnchorPane.setBottomAnchor(this.node, 0.d);
        AnchorPane.setTopAnchor(this.node, 0.d);
        AnchorPane.setLeftAnchor(this.node, 0.d);
        AnchorPane.setRightAnchor(this.node, 0.d);
    }

    @Override
    public String getIdentifier() {
        return "FilterNode";
    }

    @Override
    public String getProperty() {
        return "Node";
    }

    @Override
    public void close() {
        this.database.resetTableView();
        this.parent.getChildren().remove(this.node);
    }
}
