/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.io.importer;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author patri
 */
public class Importer {
    
    public static HashMap<String,File> select_Folders(List<String> identifier){
        HashMap<String,File> folders = new HashMap<>();
        JFileChooser chooser = new JFileChooser();
        for(String id : identifier){
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("Select " + id + " folder");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
              folders.put(id, chooser.getSelectedFile());
            } else {
              System.out.println("No Selection ");
            }
        }
        return folders;
    }
    
    public static HashMap<Integer,ImportCase> DTMS_1_Importer(){
        List<String> identifier = Arrays.asList("database","storage");
        HashMap<String,File>folders = select_Folders(identifier);
        HashMap<Integer,ImportCase> cases = DtmsImportParser.parse_dtmsfiles_to_case_map(folders.get("database"));
        for(ImportCase c:cases.values()){
            Logger.getLogger("Importer").fine( c.get_caseNumber() + ":" );
            System.out.println( c.get_caseNumber() + ":" );
            String output = "2D:\n";
            for(ImportTwoDim t:c.get_twodim_images().values()){
                output = output + t.get_imageID() + " , ";
            }
            output = output + "\n" + "3D:\n";
            for(ImportThreeDim t:c.get_threedim_images().values()){
                output = output + t.get_imageID() + " , ";
            }
            output = output + "\n" + "4D:\n";
            for(ImportFourDim t:c.get_fourdim_images().values()){
                output = output + t.get_imageID() + " , ";
            }
            Logger.getLogger("Importer").fine( output + "\n" );
            System.out.println( output + "\n" );
        }
        return cases;
    }
    
    public static HashMap<Integer,ImportCase> hamamatsu_importer(){
        HashMap<Integer,ImportCase> cases = DtmsImportParser.parse_dtmsfiles_to_case_map(new File(""));
        return cases;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Logger.getLogger("Importer").setLevel(Level.FINEST);
        HashMap<Integer,ImportCase> cases = DTMS_1_Importer();
        System.out.println(cases.keySet().size());
        
    }
    
}
