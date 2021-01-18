/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.io.importer;

import java.util.HashMap;

/**
 *
 * @author patri
 */
public class ImportCase {
    
    String caseNumber;
    Integer caseID;
    String diagnosis;
    String entryDate;
    Integer clinicID;
    Integer submitterID;
    HashMap<String,ImportTwoDim> twoDim;
    HashMap<String,ImportThreeDim> threeDim;
    HashMap<String,ImportFourDim> fourDim;
    
    public ImportCase(String caseNumber, Integer caseID, String diagnosis, String entryDate, Integer clinicID, Integer submitterID){
        this.caseNumber = caseNumber;
        this.caseID = caseID;
        this.diagnosis = diagnosis;
        this.entryDate = entryDate;
        this.clinicID = clinicID;
        this.submitterID = submitterID;
        this.twoDim = new HashMap<>();
        this.threeDim = new HashMap<>();
        this.fourDim = new HashMap<>();
    }
    
    public ImportCase(String caseNumber, String diagnosis, String entryDate, Integer clinicID, Integer submitterID){
        this.caseNumber = caseNumber;
        this.caseID = caseID;
        this.diagnosis = diagnosis;
        this.entryDate = entryDate;
        this.clinicID = clinicID;
        this.submitterID = submitterID;
        this.twoDim = new HashMap<>();
        this.threeDim = new HashMap<>();
        this.fourDim = new HashMap<>();
    }
    
    public void add_two_Dim(ImportTwoDim image){
        this.twoDim.put(image.get_imageID(),image);
    }
    
    public void add_three_Dim(ImportThreeDim image){
        this.threeDim.put(image.get_imageID(),image);
    }
    
    public void add_four_Dim(ImportFourDim image){
        this.fourDim.put(image.get_imageID(),image);
    }
    
    public String get_caseNumber(){
        return this.caseNumber;
    }
    
    public Integer get_caseID(){
        return this.caseID;
    }
    
    public String get_diagnosis(){
        return this.diagnosis;
    }
    
    public String get_entryDate(){
        return this.entryDate;
    }
    
    public Integer get_clinicID(){
        return this.clinicID;
    }
    
    public Integer get_submitterID(){
        return this.submitterID;
    }
    
    public void update_twodims(HashMap<String,ImportTwoDim> twoDims){
        this.twoDim = twoDims;
    }
    
    public void update_threedims(HashMap<String,ImportThreeDim> threeDims){
        this.threeDim = threeDims;
    }
    
    public void update_fourdims(HashMap<String,ImportFourDim> fourDims){
        this.fourDim = fourDims;
    }
    
    public HashMap<String,ImportTwoDim> get_twodim_images(){
        return this.twoDim;
    }
    
    public HashMap<String,ImportThreeDim> get_threedim_images(){
        return this.threeDim;
    }
    
    public HashMap<String,ImportFourDim> get_fourdim_images(){
        return this.fourDim;
    }
}
