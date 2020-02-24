/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestPackages;

import CorporateObjects.Case;
import CorporateObjects.Clinic;
import CorporateObjects.Submitter;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author patri
 */
public class TestData {

    public static List<Case> createTestCases() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Diagnose", "NScHL");
        System.out.println(jsonObject.get("Diagnose"));
        return null;

    }

    public static List<Clinic> createTestClinics() {
        return null;

    }

    public static HashMap<String,Submitter> createTestSubmitter() {
        HashMap<String,Submitter> test = new HashMap<>();
        try {
            JSONParser parser = new JSONParser();
            Submitter one = new Submitter((JSONObject) parser.parse("{\"surname\":\"Patrick\",\"forename\":\"Wurzel\",\"title\":\"Msc.\",\"login\":\"patrick1238\",\"password\":\"s0uthpar\"}"));
            Submitter two = new Submitter((JSONObject) parser.parse("{\"surname\":\"Hendrik\",\"forename\":\"Schäfer\",\"title\":\"Dr.\",\"login\":\"rehkind\",\"password\":\"H0dgk!n\"}"));
            test.put(one.getLogin(), one);
            test.put(two.getLogin(), two);
        } catch (ParseException ex) {
            Logger.getLogger(TestData.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(test);
        return test;
    }
}
