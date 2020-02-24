/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Windowcontroller.CaseScreenController;
import java.util.HashMap;

/**
 *
 * @author patri
 */
public interface View {
    public void set_file_id(int id);
    public void set_parentcontroller(CaseScreenController parentController);
    public void load_file(HashMap<String, String> file);
}
