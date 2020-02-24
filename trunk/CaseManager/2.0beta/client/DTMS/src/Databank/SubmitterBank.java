/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Databank;

import CorporateObjects.Submitter;
import LocalInterfaces.IDatabank;
import TestPackages.TestData;
import java.util.HashMap;
import java.util.Set;
import org.json.simple.JSONObject;

/**
 *
 * @author patri
 */
public class SubmitterBank implements IDatabank{
    
    static SubmitterBank databank;
    HashMap<String,Submitter> user;
    
    private SubmitterBank(){
        loadData();
    }

    public static SubmitterBank get() {
        if (databank == null) {
            databank = new SubmitterBank();
        }
        return databank;
    }

    @Override
    public void loadData() {
        this.user = TestData.createTestSubmitter();
    }
    
    public Set<String> getUserNames(){
        return user.keySet();
    }
    
    public boolean verifyUser(String username, String password){
        if(user.containsKey(username) && user.get(username).getPassword().equals(password)){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean containsUser(String username){
        return this.user.containsKey(username);
    }
    
    public void addUser(String surname, String forename, String title, String username, String password){
        JSONObject newUser = new JSONObject();
        newUser.put("surname", surname);
        newUser.put("forename", forename);
        newUser.put("title", title);
        newUser.put("username", username);
        newUser.put("password", password);
        this.user.put(username, new Submitter(newUser));
        this.createInDatabase(newUser);
    }
    
    private void createInDatabase(JSONObject newUser){
        System.out.println("Created"+newUser.toJSONString());
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
