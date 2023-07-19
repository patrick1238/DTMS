/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import BackgroundHandler.ErrorLog;
import BackgroundHandler.ViewController;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import Interfaces.DTMSView;

/**
 *
 * @author patri
 */
public class HelpStage implements DTMSView {

    Stage stage;
    final String helpFilePath = "D:\\Sonja\\Digital_tissue_management_suite\\tag\\DTMS_version_1.0\\Resources\\AdditionalFiles\\help.txt";

    public HelpStage() {
        Alert alert = new Alert(AlertType.NONE);
        stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest((final WindowEvent event) -> {
                ViewController.getViewController().closeView(this.getIdentifier());
            });
        alert.setTitle("Help");
        alert.setHeaderText("For any additional questions or remarks please contact via\n"
            + "          p.wurzel@bioinformatik.uni-frankfurt.de");
        alert.setContentText(readHelpFile());
        alert.initModality(Modality.NONE);
        alert.show();
    }

    private String readHelpFile() {
        String output = "";
        try {
            output = new String(Files.readAllBytes(Paths.get(helpFilePath)), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ErrorLog.getErrorLog().createLogEntry(2, getIdentifier() + ": Failed loading the help file");
        }
        return output;
    }

    @Override
    public String getIdentifier() {
        return "HelpStage";
    }

    @Override
    public String getProperty() {
        return "Stage";
    }

    @Override
    public void close() {
        this.stage.close();
    }

}
