/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Background_handler;

import Databank.CaseBank;
import Databank.ClinicBank;

/**
 *
 * @author patri
 */
public class PreLoader {
    
    public static void load_backgroundinformation(){
        Config.getConfig();
        CaseBank.get();
        ClinicBank.get();
    }  
    
}
