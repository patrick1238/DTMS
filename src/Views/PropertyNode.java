/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import BackgroundHandler.Config;
import BackgroundHandler.DataCache;
import BackgroundHandler.ViewController;
import BackgroundHandler.ErrorLog;
import Basicfunctions.Basicfunctions;
import Database.Database;
import Interfaces.ImageObject;
import Observer.ObservePropertyClass;
import ImageObjects.ImageObjectGeneral;
import Listener.SelectionChangedListener;
import Windowcontroller.ObjectWindowController;
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
public final class PropertyNode implements DTMSView {

    Node node;
    DataCache cache;
    ErrorLog log;
    Pane parent;
    Config config;
    ImageObject editableImage;
    ViewController viewcontroller;
    SelectionChangedListener selectionChangedListener;

    public PropertyNode(ImageObjectGeneral img, Pane parent) {
        this.cache = DataCache.getDataCache();
        this.log = ErrorLog.getErrorLog();
        this.parent = parent;
        this.config = Config.getConfig();
        this.viewcontroller = ViewController.getViewController();
        initPropertyView(img);
    }

    public void initPropertyView(ImageObjectGeneral img) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ObjectWindow.fxml"));
        try {
            this.node = (Node) fxmlLoader.load();
            ObjectWindowController controller = (ObjectWindowController) fxmlLoader.getController();
            controller.addTitle("PropertiesView");
            this.editableImage = this.loadProperty(controller, img);
            controller.initializeListenerForAddingAndEditing();
            this.prepareView(controller, this.editableImage);
            this.selectionChangedListener = new SelectionChangedListener(this.viewcontroller.getActiveTableWindowController());
            this.viewcontroller.getActiveTableWindowController().getAutoFillTable().getTableView().getSelectionModel().selectedItemProperty().addListener(selectionChangedListener);
            this.parent.getChildren().add(0, node);
        } catch (IOException ex) {
            ErrorLog.getErrorLog().createLogEntry(2, getIdentifier() + ": Failed loading the fxml");
        }
    }

    public ImageObject loadProperty(ObjectWindowController controller, ImageObjectGeneral img) {
        String fileType = img.getFileType();
        Database database = cache.getDatabase(fileType);
        ImageObject property = null;
        if (database.contains(img.getImageID())) {
            property = (ImageObject) database.get(img.getImageID());
        } else {
            ObservePropertyClass.createProperty(img);
            property = (ImageObject) database.get(img.getImageID());
        }
        controller.initImageObject(fileType);
        controller.setImageObject(property);
        return controller.getImageObject();
    }

    public void prepareView(ObjectWindowController controller, ImageObject property) {
        String fileType = property.getType();
        Basicfunctions.fillGridPane(controller, fileType);
        controller.setWindow(property);
        controller.setSize();
        controller.initializeListenerForAddingAndEditing();
        AnchorPane.setBottomAnchor(this.node, 0.d);
        AnchorPane.setTopAnchor(this.node, 0.d);
        AnchorPane.setLeftAnchor(this.node, 0.d);
        AnchorPane.setRightAnchor(this.node, 0.d);
    }

    public Node getNode() {
        return this.node;
    }

    @Override
    public String getIdentifier() {
        return "PropertyNode";
    }

    @Override
    public String getProperty() {
        return "Node";
    }

    @Override
    public void close() {
        String type = this.editableImage.getType();
        ImageObject old = this.cache.getDatabase(type).get(this.editableImage.getImageID());
        this.viewcontroller.getActiveTableWindowController().getAutoFillTable().getTableView().getSelectionModel().selectedItemProperty().removeListener(this.selectionChangedListener);
        this.cache.getDatabase(type).replaceFile(old, this.editableImage);
        this.parent.getChildren().remove(this.node);
    }
}
