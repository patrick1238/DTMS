/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basicfunctions;

import BackgroundHandler.ErrorLog;
import ImageObjects.ImageObject2D;
import ImageObjects.ImageObject3D;
import ImageObjects.ImageObject4D;
import ImageObjects.ImageObjectGeneral;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author patri
 */
public class Loader {

    public static ObservableList loadFiles(String csvPath, String type) {
        ObservableList files = FXCollections.observableArrayList();
        if (new File(csvPath).exists()) {
            try (final BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
                String line = br.readLine();
                line = br.readLine();
                while (line != null) {
                    ArrayList<String> imageProps = Basicfunctions.splitString(line);
                    switch (type) {
                        case "General":
                            files.add(new ImageObjectGeneral(imageProps));
                            break;
                        case "2D":
                            files.add(new ImageObject2D(imageProps));
                            break;
                        case "3D":
                            files.add(new ImageObject3D(imageProps));
                            break;
                        case "4D":
                            files.add(new ImageObject4D(imageProps));
                            break;
                    }
                    line = br.readLine();
                }
            } catch (FileNotFoundException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "Csv file of "+type+"images not found");
            } catch (IOException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "Can't read the "+type+" csv file");
            }
        }
        return files;
    }    
}
