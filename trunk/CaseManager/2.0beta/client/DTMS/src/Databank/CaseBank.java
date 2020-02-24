/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Databank;

import CorporateObjects.Case;
import LocalInterfaces.IDatabank;
import TestPackages.TestData;
import java.util.HashMap;

/**
 *
 * @author patri
 */
public class CaseBank implements IDatabank{
    
    static CaseBank databank;
    HashMap<String,Case> cases;
    
    private CaseBank(){
        loadData();
    }
    
    public static CaseBank get() {
        if (databank == null) {
            databank = new CaseBank();
        }
        return databank;
    }

    @Override
    public void loadData() {
        TestData.createTestClinics();
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    
}
