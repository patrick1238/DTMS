/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.views;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.patho234.controls.elements.HomeStatisticsController;
import net.patho234.interfaces.IDataDisplay;
import net.patho234.webapp_client.FxmlManager;

/**
 *
 * @author patri
 */
public class HomePane extends AnchorPane {

    IDataDisplay display;
    HomeStatisticsController controller;
    FXMLLoader fxmlLoader;

    public HomePane(IDataDisplay display) {
        this.display = display;
        fxmlLoader = new FXMLLoader(this.getClass().getResource("/fxml/elements/fx_home_statistics_pane.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(RegistrationWindow.class.getName()).log(Level.SEVERE, "Could not load FXML file for main window...exiting.", ex);
            FxmlManager.EXIT_APPLICATION_HANDLER.handle(null);
        }
        controller = fxmlLoader.getController();
        //System.out.println(((AnchorPane)root).getMaxWidth());
        //AnchorPane.setTopAnchor(n, 0.0);
        //AnchorPane.setRightAnchor(n, 0.0);
        //AnchorPane.setLeftAnchor(n, 0.0);
        //AnchorPane.setBottomAnchor(n, 0.0);
        this.getChildren().add(root);
        //this.setBackground(new Background(new BackgroundFill(new Paint(Color.CYAN), null, null));
    }
}
