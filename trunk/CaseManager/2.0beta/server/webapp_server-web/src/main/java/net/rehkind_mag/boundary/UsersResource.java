/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.boundary;
import java.net.URI;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import net.rehkind_mag.control.UserRepository;
import net.rehkind_mag.test.interfaces.IUser;
                        
/**
 *
 * @author HS
 */
@Path("/userpool")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {
    
    UserRepository userRepo = new UserRepository();
    
    @Context
    UriInfo uriInfo;
    
    @GET
    @Path("/users")
    public JsonArray getUsers() {
        JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
        
        for(IUser u:userRepo.getUsers()){
            arrayBuilder.add( getUserBuilderJson(u) );
        }
        return arrayBuilder.build();
    }
    
    @GET
    @Path("/user/{id}")
    public JsonObject getUser(@PathParam("id") Integer id) {
        IUser user = userRepo.getUser(id);
        if(user==null){
            System.out.println("user is NULL");
        }
        
        return buildUserJson(user);
    }
    
    private JsonObject buildUserJson(IUser user){
        JsonObjectBuilder jsonUserBuilder=getUserBuilderJson(user);
        return jsonUserBuilder.build();
    }
    
    private JsonObjectBuilder getUserBuilderJson(IUser user){
        URI selfUri = uriInfo.getBaseUriBuilder()
            .path(UsersResource.class)
            .path(UsersResource.class, "getUser")
            .build(user.getUserID());
        
        JsonObjectBuilder jsonUserDataBuilder = Json.createObjectBuilder();
        jsonUserDataBuilder.add("forename", user.getUserData().getForeName())
                .add("name", user.getUserData().getName())
                .add("birthdate", user.getUserData().getBirthDate().toGMTString())
                .add("url", selfUri.getRawPath());
                
        
        JsonObjectBuilder jsonUserBuilder = Json.createObjectBuilder();
        jsonUserBuilder.add("userId", user.getUserID())
                .add("authName", user.getAuthName())
                .add("userData", jsonUserDataBuilder);
        return jsonUserBuilder;
    }
}
