/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.io;

import java.net.URL;
import java.util.HashMap;

/**
 *
 * @author patri
 */
public class FilenameParser {
    
    public static HashMap<String,String> twoDimFrankfurtParser(String filename){
        HashMap<String,String> info = new HashMap<>();
        if (filename.indexOf(".") > 0) {
            filename = filename.substring(0, filename.lastIndexOf("."));
        }
        String[] blank_splitted = filename.split(" ");
        String[] main_info = blank_splitted[0].split("_");
        info.put("CaseID", main_info[0]+"-"+main_info[1]);
        info.put("Block", main_info[2]);
        info.put("Diagnosis", main_info[3]);
        info.put("Red", main_info[4]);
        info.put("CaptureDate", blank_splitted[2]);
        for(String key:info.keySet()){
            System.out.println(key);
            System.out.println(info.get(key));
        }
        return info;
    }
    
    public static HashMap<String,String> threeDimFrankfurtParser(String filename){
        HashMap<String,String> info = new HashMap<>();
        if (filename.indexOf(".") > 0) {
            filename = filename.substring(0, filename.lastIndexOf("."));
        }
        String test = "L518-18_MZ_CD20_594_Actin_488_63x_001.lif";
        String[] main_info = filename.split("_");
        info.put("CaseID", main_info[0]);
        info.put("Diagnosis", main_info[1]);
        info.put("Red", main_info[4]);
        return info;
    }
    
    private HashMap<String,String> identifyStain(String filename){
        HashMap<String,String> stains = new HashMap<>();
        
        return stains;
    }
}
