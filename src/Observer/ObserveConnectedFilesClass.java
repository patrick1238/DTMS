/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Observer;

import BackgroundHandler.Config;
import BackgroundHandler.ErrorLog;
import Interfaces.ImageObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javax.swing.JOptionPane;

/**
 *
 * @author patri
 */
public class ObserveConnectedFilesClass {

    public static void deleteConnectedFiles(ImageObject img) {
        Config config = Config.getConfig();
        for (String item : getConnectedFiles(img)) {
            if (!img.getAsHashMap().get(item).equals("")) {
                File image = new File(config.get("ImageServer") + img.getAsHashMap().get(item).get());
                if (image.exists() && image.isFile()) {
                    try {
                        Files.delete(Paths.get(image.getAbsolutePath()));
                    } catch (IOException ex) {
                        ErrorLog.getErrorLog().createLogEntry(2, "Failed deleting " + image.getName() + "");
                    }
                }
            }
        }
    }

    public static ArrayList<String> getConnectedFiles(ImageObject img) {
        Config config = Config.getConfig();
        ArrayList<String> files = new ArrayList<>();
        String[] header = config.get(img.getType() + "Header").split(",");
        String[] items = config.get(img.getType() + "ControlItems").split(",");
        int counter = 0;
        for (String item : items) {
            if (item.equals("Button")) {
                files.add(header[counter]);
            }
            counter++;
        }
        return files;
    }

    public static void observeConnectedFiles(ImageObject img, boolean newImg) {
        Config config = Config.getConfig();
        for (String item : getConnectedFiles(img)) {
            if (!img.getAsHashMap().get(item).get().equals("")) {
                File file = null;
                if (new File(img.getAsHashMap().get(item).get()).isAbsolute()) {
                    file = new File(img.getAsHashMap().get(item).get());
                } else {
                    file = new File(config.get("ImageServer") + img.getAsHashMap().get(item).get());
                }
                if (file.exists()) {
                    String destinationPath = createDestinationPath(img);
                    File destination = new File(config.get("ImageServer") + destinationPath);
                    if (!destination.exists()) {
                        destination.mkdirs();
                    }
                    destinationPath = destinationPath + file.getName();
                    File newFile = new File(config.get("ImageServer") + destinationPath.replace(" ", ""));
                    if (!file.getPath().equals(newFile.getPath())) {
                        try {
                            if (newImg == true) {
                                Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            } else {
                                Files.move(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            }
                        } catch (IOException ex) {
                            ErrorLog.getErrorLog().createLogEntry(2, "Failed moving " + file.getName() + " to the right destination");
                        }
                    }
                    img.setValue(item, new SimpleStringProperty(destinationPath));
                } else {
                    ErrorLog.getErrorLog().createLogEntry(2, "Can't find " + file.getName());
                }
            }
        }
    }

    private static String createDestinationPath(ImageObject img) {
        Config config = Config.getConfig();
        String destinationPath = "";
        for (String key : config.get("ImageServerLogic").split(",")) {
            String logicPart = "unknown";
            String value = "";
            if (key.equals("FileType") && !img.getType().equals("General")) {
                value = img.getType();
            } else {
                value = img.getAsHashMap().get(key).get();
            }
            if (!value.equals("")) {
                logicPart = value;
            }
            destinationPath = destinationPath + logicPart + File.separator;
        }
        return destinationPath;
    }

    public static ImageObject convertRelativeToAbsoluteFilePaths(String folder, ImageObject img) {
        getConnectedFiles(img).forEach((fileKey) -> {
            if (!img.getAsHashMap().get(fileKey).get().equals("")) {
                img.setValue(fileKey, new SimpleStringProperty(folder + img.getAsHashMap().get(fileKey).get()));
            }
        });
        return img;
    }

}
