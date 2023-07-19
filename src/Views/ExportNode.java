/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import AutofillTable.AutoFillTable;
import BackgroundHandler.ErrorLog;
import BackgroundHandler.ViewController;
import Windowcontroller.ExporterWindowController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import Interfaces.DTMSView;

/**
 *
 * @author patri
 */
public class ExportNode implements DTMSView{
    
    Node node;
    ViewController viewcontroller;
    Pane parent;
    
    public ExportNode(AutoFillTable table, Pane parent){
        this.parent = parent;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ExporterWindow.fxml"));
        try {
            this.node = (Node) fxmlLoader.load();
        } catch (IOException ex) {
            ErrorLog.getErrorLog().createLogEntry(2, getIdentifier() + ": Failed loading the fxml");
        }
        ExporterWindowController controller = (ExporterWindowController) fxmlLoader.getController();
        controller.init(table);
        this.prepareView();
    }
    
    private void prepareView(){
        AnchorPane.setBottomAnchor(this.node, 0.d);
        AnchorPane.setTopAnchor(this.node, 0.d);
        AnchorPane.setLeftAnchor(this.node, 0.d);
        AnchorPane.setRightAnchor(this.node, 0.d);
        this.parent.getChildren().add(0, node);
    }

    @Override
    public String getIdentifier() {
        return "ExportNode";
    }

    @Override
    public String getProperty() {
        return "Node";
    }

    @Override
    public void close() {
        this.parent.getChildren().remove(this.node);
    }   
}
