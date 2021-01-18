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
public class ImportThreeDim {
    
    String imageID = "";
    String captureDate = "";
    String red = "";
    String magenta = "";
    String green = "";
    String blue = "";
    String rating = "unrated";
    String comment = "";
    String tissue = "nod";
    String filepath = "";
    Integer tiles = -1;
    String imarisfile = "";
    String overviewfile = "";
    Double magnification = 63.;
    
    public ImportThreeDim(String imageID, String captureDate, String red, String magenta, String green, String blue, String comment, String filepath, Integer tiles, String imarisfile, String overviewfile){
        this.imageID = imageID;
        this.captureDate = captureDate;
        this.red = red;
        this.magenta = magenta;
        this.green = green;
        this.blue = blue;
        this.comment = comment;
        HashMap<String,String> comment_info = DtmsImportParser.parse_comments(this.comment);
        if(comment_info.containsKey("Bewertung")){
            this.rating = comment_info.get("Bewertung");
            this.comment = comment_info.get("fixedcomment");
        }
        this.filepath = filepath;
        this.tiles = tiles;
        this.imarisfile = imarisfile;
        this.overviewfile = overviewfile;
        HashMap<String,String> filepath_info = DtmsImportParser.parse_filepath(this.filepath);
    }
    
    public String get_imageID(){
        return this.imageID;
    }
    
    public String get_captureDate(){
        return this.captureDate;
    }
    
    public String get_red(){
        return this.red;
    }
    
    public String get_magenta(){
        return this.magenta;
    }
    
    public String get_green(){
        return this.green;
    }
    
    public String get_blue(){
        return this.blue;
    }
    
    public String get_tissue(){
        return this.tissue;
    }
    
    public String get_rating(){
        return this.rating;
    }
    
    public String get_comment(){
        return this.comment;
    }
    
    public String get_filepath(){
        return this.filepath;
    }
    
    public Integer get_tiles(){
        return this.tiles;
    }
    
    public String get_imarisfile(){
        return this.imarisfile;
    }
    
    public String get_overviewfile(){
        return this.overviewfile;
    }
    
    public void set_tiles(Integer tiles){
        this.tiles = tiles;
    }
    
    public void set_imarisfile(String path){
        this.imarisfile = path;
    }
    
    public void set_overviewfile(String path){
        this.overviewfile = path;
    }
}
