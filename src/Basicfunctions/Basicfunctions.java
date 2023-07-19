/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basicfunctions;

import BackgroundHandler.Config;
import BackgroundHandler.DataCache;
import Database.Database;
import Windowcontroller.ObjectWindowController;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author patri
 */
public class Basicfunctions {

    public static ArrayList<String> splitString(String line){
        ArrayList<String> list = new ArrayList<>();
        int index = 0;
        boolean comma = false;
        while(line.contains(",")){
            if(line.startsWith("\"")){
                index = line.indexOf("\"",1)+1;
                comma = true;
            }else{
                index = line.indexOf(",");
                comma = false;
            }
            if(comma){
                list.add(line.substring(1, index-1));
                line = line.substring(Math.min(line.length(), index+1));
            }else{
                list.add(line.substring(0, index));
                line = line.substring(Math.min(line.length(), index+1));
            }                    
        }
        list.add(line);
        return list;
    }

    public static void fillGridPane(ObjectWindowController controller, String type) {
        Database database = DataCache.getDataCache().getDatabase(type);
        Config config = Config.getConfig();
        for (String key : database.getEntryHeader()) {
            switch (config.getControllitem(type,key)) {
                case "Field":
                    controller.addRowWithField(new Label(key + ": "), new TextField());
                    break;
                case "Box":
                    ComboBox box = new ComboBox();
                    box.setEditable(true);
                    if (key.equals("FileType")) {
                        box.setItems(FXCollections.observableArrayList(config.get("PossibleTypes").split(",")));
                    } else {
                        box.setItems(database.getPossibleEntries(key));
                    }
                    controller.addRowWithComboBox(new Label(key + ": "), box);
                    break;
                case "Button":
                    controller.addRowWithButton(new Label(key + ": "), new Button(), controller);
                    break;
                case "Label":
                    controller.addRowWithLabel(new Label(key + ": "), new Label());
                default:
                    break;
            }
        }
    }





    
}
