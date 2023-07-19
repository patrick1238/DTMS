/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import BackgroundHandler.Config;
import BackgroundHandler.DataCache;
import BackgroundHandler.ErrorLog;
import Interfaces.ImageObject;
import ImageObjects.ImageObjectExport;
import ImageObjects.ImageObjectGeneral;
import Observer.ObserveConnectedFilesClass;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author patri
 * @param <T>
 */
public class ExporterDatabase<T extends ImageObject> {

    ObservableList<ImageObjectExport> export = FXCollections.observableArrayList();
    Config config;
    DataCache cache;
    ErrorLog log;
    HashMap<String, ImageObjectExport> idConnector;

    public ExporterDatabase() {
        this.idConnector = new HashMap<>();
        this.cache = DataCache.getDataCache();
        this.config = Config.getConfig();
        this.log = ErrorLog.getErrorLog();
    }

    public ObservableList getExporterList() {
        return this.export;
    }

    public void addImage(T imgCur) {
        ImageObjectGeneral i = (ImageObjectGeneral) imgCur;
        if (!this.idConnector.containsKey(i.getImageID())) {
            ImageObjectExport img = new ImageObjectExport(i.getImageID(), i.getCaseID(), i.getCaseNumber(), i.getPrimaryStaining(), i.getFileType(), i.getFilePath());
            this.export.add(img);
            this.idConnector.put(img.getImageID(), img);
        }
    }

    public void removeImage(T i) {
        if (this.idConnector.containsKey(i.getImageID())) {
            this.export.remove(this.idConnector.get(i.getImageID()));
            this.idConnector.remove(i.getImageID());
        }
    }

    public void export_DTMS_folder(String outputPath, String title) {
        String outPath = outputPath + File.separator + title + File.separator;
        HashMap<String, String> textualOutput = this.initiateTextOutput();
        for (ImageObjectExport img : this.export) {
            textualOutput = this.appendToTextOutput(img, textualOutput);
            this.exportConnectedFiles(this.cache.getDatabase("General").get(img.getImageID()), outPath);
            this.exportConnectedFiles(this.cache.getDatabase(img.getFileType()).get(img.getImageID()), outPath);
        }
        this.exportTextualOutput(outPath, textualOutput);
        this.log.createLogEntry(0, title + " exported");
        this.export.clear();
        this.idConnector = new HashMap<>();
    }

    public void export_single_images(String outputPath, String title, String structure) {
        String outPath = outputPath + File.separator + title + File.separator;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Export for Batch analysis");
        alert.setHeaderText("Which connected files do you want to export?");
        ButtonType editable = new ButtonType("Editable");
        ButtonType general = new ButtonType("General");
        ButtonType cancel = new ButtonType("Cancel");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().setAll(editable, general, cancel);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == editable) {
            for (ImageObjectExport img : this.export) {
                String seperator = structure;
                if (!seperator.equals("unrelated") && !seperator.equals("anonymized")) {
                    seperator = this.cache.getDatabase("General").get(img.getImageID()).getAsHashMap().get(seperator).get();
                }
                File destination = getDestination(img, outPath, seperator);
                String workingFile = this.cache.getDatabase(img.getFileType()).get(img.getImageID()).getAsHashMap().get(this.config.get(img.getFileType() + "WorkingFile")).get();
                if (workingFile.equals("")) {
                    workingFile = this.cache.getDatabase("General").get(img.getImageID()).getFilePath();
                }
                if (seperator.equals("anonymized")) {
                    ErrorLog.getErrorLog().createLogEntry(2, "Exporting anonymized working files is not yet implemented.");
                } else {
                    this.exportSingleImages(workingFile, destination);
                }
            }
        } else if (option.get() == general) {
            for (ImageObjectExport img : this.export) {
                String seperator = structure;
                if (!seperator.equals("unrelated") && !seperator.equals("anonymized")) {
                    seperator = this.cache.getDatabase("General").get(img.getImageID()).getAsHashMap().get(seperator).get();
                }
                File destination = getDestination(img, outPath, seperator);
                if (seperator.equals("anonymized")) {
                    if(img.getFilePath().contains("lif")||img.getFilePath().contains("svs")||img.getFilePath().contains("ndpi")||img.getFilePath().contains("tiff")||img.getFilePath().contains("tif")||img.getFilePath().contains("jpg")||img.getFilePath().contains("png")||img.getFilePath().contains("czi")){
                        this.exportSingleImageAnonymized(this.cache.getDatabase("General").get(img.getImageID()).getFilePath(), create_anonymized_File_path((ImageObjectGeneral) this.cache.getDatabase("General").get(img.getImageID()), destination));
                    }else{
                        ErrorLog.getErrorLog().createLogEntry(2, "Anonymized export ist just available for lif, svs, tiff, tif, jpg, png, czi or ndpi files");
                    }
                } else {
                    this.exportSingleImages(this.cache.getDatabase("General").get(img.getImageID()).getFilePath(), destination);
                }
            }
        } else if (option.get() == cancel) {
            ErrorLog.getErrorLog().createLogEntry(2, "Exporting aborted");
        }
    }

    private String create_anonymized_File_path(ImageObjectGeneral img, File destination) {
        String outpath = destination.getAbsolutePath() + File.separator + img.getCaseID() + "-" + img.getImageID();
        ArrayList<String> stainings = new ArrayList<>();
        stainings.add(img.getThirdStaining());
        stainings.add(img.getSecondaryStaining());
        stainings.add(img.getPrimaryStaining());
        Integer iterator = 0;
        for(String stain:stainings){
            if(!stain.equals("")){
                if(iterator==0){
                    outpath = outpath+"_";
                }else{
                    outpath = outpath+"-";
                }
                outpath=outpath+stain;
                iterator+=1;
            }
        }
        String extension = "";
        String fileName = img.getFilePath();
        int i = fileName.lastIndexOf('.');
        if (i >= 0) {
            extension = fileName.substring(i+1);
        }
        outpath = outpath + "_" + img.getDiagnose();
        return outpath + "." + extension;
    }

    private File getDestination(ImageObject img, String outPath, String structure) {
        File destination;
        if (structure.equals("unrelated")) {
            destination = new File(outPath);
        } else {
            structure = structure.replace(" ", "_");
            destination = new File(outPath + structure + File.separator);
        }
        if (!destination.exists()) {
            destination.mkdirs();
        }
        return destination;
    }

    private void exportSingleImages(String file, File destination) {
        if (!file.equals("")) {
            File curFile = new File(file);
            File inputImage = new File(this.config.get("ImageServer") + file);
            File exportImage = new File(destination.getAbsolutePath() + File.separator + curFile.getName());
            try {
                Files.copy(inputImage.toPath(), exportImage.toPath(), REPLACE_EXISTING);
            } catch (IOException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "Failed copying " + inputImage.getName());
            }
        }
    }

    private void exportSingleImageAnonymized(String file, String outfile) {
        if (!file.equals("")) {
            File curFile = new File(file);
            File inputImage = new File(this.config.get("ImageServer") + file);
            File exportImage = new File(outfile);
            try {
                Files.copy(inputImage.toPath(), exportImage.toPath(), REPLACE_EXISTING);
            } catch (IOException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "Failed copying " + inputImage.getName());
            }
        }

    }

    private void exportConnectedFiles(ImageObject img, String outPath) {
        ArrayList<String> connectedFiles = ObserveConnectedFilesClass.getConnectedFiles(img);
        for (String file : connectedFiles) {
            if (!img.getAsHashMap().get(file).get().equals("")) {
                File curFile = new File(img.getAsHashMap().get(file).get());
                File destination = new File(outPath + curFile.getParent() + File.separator);
                if (!destination.exists()) {
                    destination.mkdirs();
                }
                File inputImage = new File(this.config.get("ImageServer") + curFile.getParent() + File.separator + curFile.getName());
                File exportImage = new File(destination.getAbsolutePath() + File.separator + curFile.getName());
                try {
                    Files.copy(inputImage.toPath(), exportImage.toPath(), REPLACE_EXISTING);
                } catch (IOException ex) {
                    ErrorLog.getErrorLog().createLogEntry(2, "Failed copying " + inputImage.getName());
                }
            }
        }
    }

    private HashMap<String, String> initiateTextOutput() {
        HashMap<String, String> output = new HashMap<>();
        String[] headerGeneral = this.cache.getDatabase("General").getEntryHeader();
        String headerStringGeneral = "";
        for (String head : headerGeneral) {
            headerStringGeneral = headerStringGeneral + head + ",";
        }
        headerStringGeneral = headerStringGeneral.substring(0, headerStringGeneral.length() - 1) + "\n";
        output.put("General", headerStringGeneral);
        for (String type : this.config.get("PossibleTypes").split(",")) {
            String[] header = this.cache.getDatabase(type).getEntryHeader();
            String headerString = "";
            for (String head : header) {
                headerString = headerString + head + ",";
            }
            headerString = headerString.substring(0, headerString.length() - 1) + "\n";
            output.put(type, headerString);
        }
        return output;
    }

    private HashMap<String, String> appendToTextOutput(ImageObjectExport img, HashMap<String, String> output) {
        Database general = this.cache.getDatabase("General");
        ImageObjectGeneral generalImg = (ImageObjectGeneral) general.get(img.getImageID());
        String curOutputGeneral = output.get("General") + generalImg.toString(general.getEntryHeader()) + "\n";
        output.replace("General", curOutputGeneral);
        Database property = this.cache.getDatabase(generalImg.getFileType());
        ImageObject propertyImg = property.get(generalImg.getImageID());
        String curOutputProperty = output.get(generalImg.getFileType()) + propertyImg.toString(property.getEntryHeader()) + "\n";
        output.replace(generalImg.getFileType(), curOutputProperty);
        return output;
    }

    private void exportTextualOutput(String outputPath, HashMap<String, String> output) {
        File outputFolder = new File(outputPath + "DTMSDatabases" + File.separator);
        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }
        for (String key : output.keySet()) {
            File textOutput = new File(outputFolder.getAbsolutePath() + File.separator + key + ".csv");
            try {
                textOutput.createNewFile();
            } catch (IOException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "Failed creating " + textOutput.getName());
            }
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(textOutput));
            } catch (IOException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "Failed opening " + textOutput.getName() + "for writing purposes");
            }
            try {
                writer.write(output.get(key));
            } catch (IOException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "Failed writing to " + textOutput.getName());
            }
            try {
                writer.close();
            } catch (IOException ex) {
                ErrorLog.getErrorLog().createLogEntry(2, "Failed closing " + textOutput.getName());
            }
        }
    }

    public String[] getEntryHeader() {
        return this.config.get("ImageIdentifier").split(",");
    }
}
