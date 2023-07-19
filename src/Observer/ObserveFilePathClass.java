/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Observer;

import BackgroundHandler.ErrorLog;
import java.io.File;

/**
 *
 * @author patri
 */
public class ObserveFilePathClass {
    
    public static boolean observer2DFilePath(File file){
        String filepath = file.getAbsolutePath();
        String[] splitted = filepath.split(" - ");
        boolean observer = true;
        if (splitted[0].split("_").length < 5){
            ErrorLog.getErrorLog().createLogEntry(2, "Fileinformation missing, " + file.getName() + " skipped!");
            observer = false;
        }else if(!filepath.contains(".ndpi") && !filepath.contains(".svs") && !filepath.contains(".tif") && !filepath.contains(".czi")){
            ErrorLog.getErrorLog().createLogEntry(2, "Wrong filetype, " + file.getName() + " skipped!");
            observer = false;
        }
        return observer;
    }
    
    public static boolean observer2DFilePathPatho(File file){
        String filepath = file.getAbsolutePath();
        String[] splitted = filepath.split(" - ");
        boolean observer = true;
        if(!file.exists()){
            ErrorLog.getErrorLog().createLogEntry(2, "File does not exists, " + file.getName() + " skipped!");
            observer = false;
        }
        if(!filepath.contains(".ndpi") && !filepath.contains(".svs") && !filepath.contains(".tif") && !filepath.contains(".czi")){
            ErrorLog.getErrorLog().createLogEntry(2, "Wrong filetype, " + file.getName() + " skipped!");
            observer = false;
        }
        return observer;
    }
    
    public static String observeDiagnosis(String diagnosis){
        String observed = diagnosis;
        if (observed.contains("DLBL")){
            observed = observed.replace("DLBL", "DLBCL");
        }
        if (observed.contains("Marginalzonenly")){
            observed = observed.replace("Marginalzonenly", "MZ");
        }
        if (observed.contains("MZLy")){
            observed = observed.replace("MZLy", "MZ");
        }
        if (observed.contains("Mantelzellly")){
            observed = observed.replace("Mantelzellly", "MC");
        }
        if (observed.contains("nonGCBC")){
            observed = observed.replace("nonGCBC", "nonGCB");
        }
        if (observed.contains(" Rezidiv")){
            observed = observed.replace(" Rezidiv", "");
        }
        if (observed.contains("Rezidiv ")){
            observed = observed.replace("Rezidiv ", "");
        }
        if (observed.contains("(Rezidiv)")){
            observed = observed.replace("(Rezidiv)", "");
        }
        if (observed.contains("(schwierig)")){
            observed = observed.replace("(schwierig)", "");
        }
        return observed;
    }    
}
