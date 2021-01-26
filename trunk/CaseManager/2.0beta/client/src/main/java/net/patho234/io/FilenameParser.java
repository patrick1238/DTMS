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
    public final static String CASE_ID="CaseID";
    public final static String BLOCK="Block";
    public final static String DIAGNOSIS="Diagnosis";
    public final static String RED="Red";
    public final static String BLUE="Blue";
    public final static String LOCATION="Location";
    
    public static HashMap<String,String> twoDimFrankfurtParser(String filename){
        HashMap<String,String> info = new HashMap<>();
        if (filename.indexOf(".") > 0) {
            filename = filename.substring(0, filename.lastIndexOf("."));
        }
        String[] captureDateSplitted = filename.split(" - ");
        String[] mainInfo = captureDateSplitted[0].split("_");
        info.put(CASE_ID, mainInfo[0]+"-"+mainInfo[1]);
        info.put(BLOCK, mainInfo[2]);
        info.put(DIAGNOSIS, mainInfo[3]);
        info.put(RED, mainInfo[4]);
        try{
            info.put("CaptureDate", captureDateSplitted[1]);
        }catch(Exception ex){
            info.put("CaptureDate", null);
            // could not be parsed: if value is required will result in a NULL pointer exception
        }
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
        info.put(CASE_ID, main_info[0]);
        info.put(DIAGNOSIS, main_info[1]);
        info.put(RED, main_info[4]);
        if(main_info.length>6){
            info.put(BLUE, main_info[6]);
            System.out.println("TODO: CHECK IF BLUE IS CORRECT");
        }
        return info;
    }
    
    private HashMap<String,String> identifyStain(String filename){
        HashMap<String,String> stains = new HashMap<>();
        
        return stains;
    }
}
