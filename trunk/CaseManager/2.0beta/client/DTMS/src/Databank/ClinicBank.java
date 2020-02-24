/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Databank;

import CorporateObjects.Clinic;
import LocalInterfaces.IDatabank;
import TestPackages.TestData;
import java.util.HashMap;

/**
 *
 * @author patri
 */
public class ClinicBank implements IDatabank{
    
    static ClinicBank databank;
    HashMap<String,Clinic> clinics;
    
    private ClinicBank(){
        loadData();
    }

    public static ClinicBank get() {
        if (databank == null) {
            databank = new ClinicBank();
        }
        return databank;
    }

    @Override
    public void loadData() {
        TestData.createTestCases();
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
