/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities.pool;

import javax.json.Json;
import javax.json.JsonObject;
import net.rehkind_mag.entities.ClientClinic;
import net.rehkind_mag.entities.ClientSubmitter;

/**
 *
 * @author rehkind
 */
public class Mockups {
    static public ClientClinic getClinicMockup(int id){
        JsonObject mockupClinicJson = Json.createObjectBuilder()
            .add("id", id)
            .add("name", "Mockup clinic")
            .add("zipCode", "XXX-XXX")
            .add("city", "Mockup city")
            .add("street", "Mockup street 1")
        .build();
        ClientClinic clinic = new ClientClinic(mockupClinicJson);
        
        return clinic;
    }
    
    static public ClientSubmitter getSubmitterMockup(int id){
        JsonObject mockupSubmitterJson = Json.createObjectBuilder()
            .add("id", id)
            .add("login", "guest")
            .add("password", "123456")
            .add("title", "Herr")
            .add("forename", "Max")
            .add("surname", "Mustermann")
        .build();
        ClientSubmitter submitter = new ClientSubmitter(mockupSubmitterJson);
        
        return submitter;
    }
}
