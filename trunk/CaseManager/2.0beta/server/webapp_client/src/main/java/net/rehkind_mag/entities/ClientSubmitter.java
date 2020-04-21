/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import net.rehkind_mag.interfaces.ISubmitter;

/**
 *
 * @author rehkind
 */
public class ClientSubmitter extends ClientObjectBase<ClientSubmitter> implements ISubmitter{
    StringProperty surname=new SimpleStringProperty();
    StringProperty forename=new SimpleStringProperty();
    StringProperty title=new SimpleStringProperty();
    StringProperty login=new SimpleStringProperty();
    StringProperty password=new SimpleStringProperty();
    
    public static ClientSubmitter getSubmitterTemplate() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", -1);
        builder.add("forename", "");
        builder.add("surname", "");
        builder.add("title", "");
        builder.add("login", "");
        builder.add("password", "");
        return new ClientSubmitter( builder.build() );
    }
    
    public ClientSubmitter(Integer id){
        this.ID.setValue(id);
    }    
    
    public ClientSubmitter(JsonObject submitterAsJson){
        ID.setValue( submitterAsJson.getInt("id") );
        forename.setValue( submitterAsJson.getString("forename") );
        surname.setValue( submitterAsJson.getString("surname") );
        title.setValue( submitterAsJson.getString("title") );
        login.setValue( submitterAsJson.getString("login") );
        password.setValue( submitterAsJson.getString("password") );
        
        // TODO: create and add listener here
    }
    
    @Override
    public int getId() {
        return ID.getValue();
    }

    @Override
    public String getSurname() {
        return surname.getValue();
    }

    @Override
    public String getForename() {
        return forename.getValue();
    }

    @Override
    public String getTitle() {
        return title.getValue();
    }

    @Override
    public String getLogin() {
        return login.getValue();
    }

    @Override
    public String getPassword() {
        return password.getValue();
    }

    @Override
    public void setSurname(String surname) {
        this.surname.setValue(surname);
    }

    @Override
    public void setForename(String forename) {
        this.forename.setValue(forename);
    }

    @Override
    public void setTitle(String title) {
        this.title.setValue(title);
    }

    @Override
    public void setLogin(String loginName) {
        this.login.setValue(loginName);
    }

    @Override
    public void setPassword(String pwd) {
        this.password.setValue(pwd);
    }

    @Override
    public boolean hasLocalChanges() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ClientSubmitter getLocalClone() {
        ClientSubmitter clone = new ClientSubmitter( getId() );
        clone.setForename( getForename() );
        clone.setSurname( getSurname() );
        clone.setTitle( getTitle() );
        clone.setLogin( getLogin() );
        clone.setPassword( getPassword() );
        return clone;
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", getId())
            .add("forename", getForename())
            .add("surname", getSurname())
            .add("title", getTitle())
            .add("login", getLogin())
            .add("password", getPassword());
        return builder.build();
    }
    
}
