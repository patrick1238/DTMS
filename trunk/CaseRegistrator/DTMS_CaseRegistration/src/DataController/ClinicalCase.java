/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataController;

import java.util.HashMap;

/**
 *
 * @author patri
 */
public class ClinicalCase {
    
    private HashMap<String,String> general_information;
    private HashMap<String,HashMap<String,String>> images;
    private HashMap<String,HashMap<String,String>> mol;
    private int last_file_id = -1;
    
    public ClinicalCase(){
        this.general_information = new HashMap<String, String>();
        this.images = new HashMap<String,HashMap<String,String>>();
        this.mol = new HashMap<String,HashMap<String,String>>();
    }
    
    public void set(String id,String value){
        this.general_information.put(id, value);
    }
    
    public void add_file(String id,String type,HashMap<String,String> file){
        if(type.equals("image")){
            this.images.put(id, file);
        }else{
            this.mol.put(id, file);
        }
    }
    
    public void remove_file(String id){
        if(this.images.containsKey(id)){
            this.images.remove(id);
        }else{
            this.mol.remove(id);
        }
    }
    
    public int get_new_id(){
        last_file_id = last_file_id + 1;
        return last_file_id;
    }
    
    public HashMap<String,String> out(){
        HashMap<String,String> map = new HashMap<>();
        String image_string = "";
        String mol_string = "";
        for(HashMap<String,String> file:this.images.values()){
            image_string = image_string + file_to_string(file) + "\n";
        }
        for(HashMap<String,String> file:this.images.values()){
            mol_string = mol_string + file_to_string(file) + "\n";
        }
        map.put("mols", mol_string);
        map.put("images", image_string);
        return map;
    }
    
    private String file_to_string(HashMap<String,String> file){
        String out = "";
        for(String key:file.keySet()){
            if(file.get(key).contains(",")){
                out = out + "\"" + file.get(key) + "\"" + ",";
            }else{
                out = out + file.get(key) + ",";
            }
        }
        return out.substring(-1);
    }
    
}
