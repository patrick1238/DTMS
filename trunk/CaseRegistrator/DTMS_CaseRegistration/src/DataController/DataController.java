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
public class DataController {
    
    private static DataController controller;
    private HashMap<String, ClinicalCase> case_handler;
    
    private DataController(){
        this.case_handler = new HashMap<String, ClinicalCase>();
    }
    
    public static DataController getController() {
        if(controller==null){
            controller = new DataController();
        }
        return controller;
    }
    
    public ClinicalCase get_case(String id){
        return this.case_handler.get(id);
    }
    
    public void add_case(ClinicalCase cur_case){
        this.case_handler.put("test", cur_case);
    }
    
    public void delete_case(String id){
        this.case_handler.remove(id);
    }
    
    
    
}
