/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basicfunctions;

import BackgroundHandler.Config;
import BackgroundHandler.DataCache;
import BackgroundHandler.ErrorLog;
import BackgroundHandler.ViewController;
import Database.Database;
import ImageObjects.ImageObjectGeneral;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author patri
 */
public class Exporter {

    public static void exportOverview() {
        ViewController controller = ViewController.getViewController();
        DataCache cache = DataCache.getDataCache();
        Config config = Config.getConfig();
        ErrorLog log = ErrorLog.getErrorLog();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(controller.getMainWindowController().getWindow());
        if (selectedDirectory == null) {
            log.createLogEntry(1, "No outputpath selected");
        } else {
            String outputPath = selectedDirectory.getAbsolutePath();
            Database database = cache.getDatabase("General");
            HashMap<String, Integer> diagnose = new HashMap<>();
            HashMap<String, Integer> filetype = new HashMap<>();
            HashMap<String, Integer> primaryStaining = new HashMap<>();
            for (Object img : database.getDatabaseList()) {
                ImageObjectGeneral image = (ImageObjectGeneral) img;
                if (diagnose.containsKey(image.getDiagnose())) {
                    diagnose.replace(image.getDiagnose(), diagnose.get(image.getDiagnose()) + 1);
                } else {
                    diagnose.put(image.getDiagnose(), 1);
                }
                if (filetype.containsKey(image.getFileType())) {
                    filetype.replace(image.getFileType(), filetype.get(image.getFileType()) + 1);
                } else {
                    filetype.put(image.getFileType(), 1);
                }
                if (primaryStaining.containsKey(image.getPrimaryStaining())) {
                    primaryStaining.replace(image.getPrimaryStaining(), primaryStaining.get(image.getPrimaryStaining()) + 1);
                } else {
                    primaryStaining.put(image.getPrimaryStaining(), 1);
                }
            }
            String diagnosePart = "";
            for (String key : diagnose.keySet()) {
                if (key.equalsIgnoreCase("")) {
                    diagnosePart = diagnosePart + "Unknown: " + diagnose.get(key) + "\n";
                } else {
                    diagnosePart = diagnosePart + key + ": " + diagnose.get(key) + "\n";
                }
            }
            String filetypePart = "";
            for (String key : filetype.keySet()) {
                if (key.equalsIgnoreCase("")) {
                    filetypePart = filetypePart + "Unknown: " + filetype.get(key) + "\n";
                } else {
                    filetypePart = filetypePart + key + ": " + filetype.get(key) + "\n";
                }
            }
            String primaryPart = "";
            for (String key : primaryStaining.keySet()) {
                if (key.equalsIgnoreCase("")) {
                    primaryPart = primaryPart + "Unknown: " + primaryStaining.get(key) + "\n";
                } else {
                    primaryPart = primaryPart + key + ": " + primaryStaining.get(key) + "\n";
                }
            }
            String output = "###DTMS Overview\n\nCurrently the Database consists of " + database.getDatabaseList().size() + " images in total.\n\n" + "Detailed information about available images per diagnose: \n" + diagnosePart + "\n" + "Detailed information about available images per filetype: \n" + filetypePart + "\n" + "Detailed information about available images per primary staining: \n" + primaryPart;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            try {
                Files.write(new File(outputPath + File.separator + "DTMSGeneralOverview_" + localDate + ".txt").toPath(), output.getBytes(), StandardOpenOption.CREATE);
            } catch (IOException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "Failed writing the exported file"
                        + "");
            }
            log.createLogEntry(0, "General overview exported");
        }
    }

    public static void exportCaseOverview() {
        ViewController controller = ViewController.getViewController();
        DataCache cache = DataCache.getDataCache();
        Config config = Config.getConfig();
        ErrorLog log = ErrorLog.getErrorLog();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(controller.getMainWindowController().getWindow());
        if (selectedDirectory == null) {
            log.createLogEntry(1, "No outputpath selected");
        } else {
            String outputPath = selectedDirectory.getAbsolutePath();
            Database database = cache.getDatabase("General");
            HashMap<String, ArrayList> caseIds = database.getCaseMap();
            String output = "CaseNumber;CaseID;Diagnose;Available images\n";
            for (ArrayList images : caseIds.values()) {
                if (!images.isEmpty()) {
                    ImageObjectGeneral i = (ImageObjectGeneral) images.get(0);
                    output = output + i.getCaseNumber() + ";" + i.getCaseID() + ";" + i.getDiagnose() + ";";
                    for (Object o : images) {
                        ImageObjectGeneral img = (ImageObjectGeneral) o;
                        if (img.getComment().toLowerCase().contains("negco") || img.getComment().toLowerCase().contains("negko")) {
                            output = output + "(negCo)";
                        } else {
                            output = output + "(";
                            if (!img.getPrimaryStaining().equals("")){
                                output = output + img.getPrimaryStaining() + ",";
                            }
                            if (!img.getSecondaryStaining().equals("")){
                                output = output + img.getSecondaryStaining() + ",";
                            }
                            if (!img.getThirdStaining().equals("")){
                                output = output + img.getThirdStaining() + ",";
                            }
                            output = output.substring(0, output.length()-1) + ")";
                        }
                        output = output + "(" + img.getFileType() + ")" + ";";
                    }
                    output = output.substring(0, output.length()-1) + "\n";
                }
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            try {
                Files.write(new File(outputPath + File.separator + "DTMSCaseOverview_" + localDate + ".csv").toPath(), output.getBytes(), StandardOpenOption.CREATE);
            } catch (IOException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "Failed writing the exported file"
                        + "");
            }
            log.createLogEntry(0, "Case overview exported");
        }
    }

}
