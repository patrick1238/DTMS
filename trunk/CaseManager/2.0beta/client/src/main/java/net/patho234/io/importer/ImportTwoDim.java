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
public class ImportTwoDim {
    String imageID = "";
    Integer blockID = -1;
    String red = "";
    String brown = "";
    String blue = "";
    String tissue = "";
    String captureDate = "";
    String cellgraphPath = "";
    String rating = "rating";
    String comment = "";
    String filepath = "";
    String project = "";
    
    public ImportTwoDim(Integer blockID, String red, String brown, String blue, String tissue, String filepath){
        this.blockID = blockID;
        this.red = red;
        this.brown = brown;
        this.blue = blue;
        this.tissue = tissue;
        this.filepath = filepath;
    }
    
    public ImportTwoDim(String imageID, String red, String brown, String blue, String tissue, String captureDate, String cellgraphPath, String comment, String filepath){
        this.imageID = imageID;
        this.red = red;
        this.brown = brown;
        this.blue = blue;
        this.tissue = tissue;
        this.captureDate = captureDate;
        this.cellgraphPath = cellgraphPath;
        this.comment = comment;
        HashMap<String,String> info = DtmsImportParser.parse_comments(this.comment);
        this.filepath = filepath;
    }
    
    public String get_imageID(){
        return this.imageID;
    }
    
    public Integer get_blockID(){
        return this.blockID;
    }
    
    public String get_red(){
        return this.red;
    }
    
    public String get_brown(){
        return this.brown;
    }
    
    public String get_blue(){
        return this.blue;
    }
    
    public String get_tissue(){
        return this.tissue;
    }
    
    public String get_captureDate(){
        return this.captureDate;
    }
    
    public String get_cellgraphPath(){
        return this.cellgraphPath;
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
    
    public void set_cellgraph_path(String path){
        this.cellgraphPath = path;
    }
}
