/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CorporateObjects;

import CorporateInterfaces.ISubmitter;
import LocalInterfaces.IJSONObject;
import javafx.beans.property.SimpleStringProperty;
import org.json.simple.JSONObject;

/**
 *
 * @author patri
 */
public class Submitter implements ISubmitter,IJSONObject{
    
    JSONObject object;
    SimpleStringProperty surname;
    SimpleStringProperty forename;
    SimpleStringProperty title;
    SimpleStringProperty login;
    String password;
    
    public Submitter(JSONObject object){
        this.object = object;
        surname = new SimpleStringProperty((String) this.object.get("surname"));
        forename = new SimpleStringProperty((String) this.object.get("forename"));
        title = new SimpleStringProperty((String) this.object.get("title"));
        login = new SimpleStringProperty((String) this.object.get("login"));
        password = (String) this.object.get("password");
    }

    @Override
    public int getId() {
        return (int)this.object.get("id");
    }

    @Override
    public String getSurname() {
        return this.surname.get();
    }

    @Override
    public String getForename() {
        return this.forename.get();
    }

    @Override
    public String getTitle() {
        return this.title.get();
    }

    @Override
    public String getLogin() {
        return this.login.get();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setSurname(String surname) {
        this.surname.set(surname);
        this.object.put("surname", surname);
        this.post();
    }

    @Override
    public void setForename(String forename) {
        this.forename.set(forename);
        this.object.put("forename", forename);
        this.post();
    }

    @Override
    public void setTitle(String title) {
        this.title.set(title);
        this.object.put("title", title);
        this.post();
    }

    @Override
    public void setLogin(String loginName) {
        this.login.set(loginName);
        this.object.put("login", loginName);
        this.post();
    }

    @Override
    public void setPassword(String pwd) {
        this.password = pwd;
        this.object.put("password", pwd);
        this.post();
    }

    @Override
    public void post() {
        System.out.println(object.toJSONString());
    }
    
}
