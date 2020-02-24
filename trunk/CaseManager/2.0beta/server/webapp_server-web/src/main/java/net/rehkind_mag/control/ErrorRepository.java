/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.control;

import java.util.Set;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.validation.ConstraintViolation;
import net.rehkind_mag.utils.LocalUUIDManager;

/**
 *
 * @author HS
 */
public class ErrorRepository {
    public static final String ERROR_DOES_NOT_EXISTS="NOT_EXISTING"; 
    public static final String ERROR_CONTRAINTS_VIOLATED="CONTRAINTS_VIOLATED";
    public static final String ERROR_NOT_DELETED="NOT_DELETED";
    public static final String ERROR_FORMAT_EXCEPTION="FORMAT_EXCEPTION";
    public static final String ERROR_NO_PERMISSION="NO_PERMISSION";
    public static final String ERROR_NOT_FOUND="NOT_FOUND";
    public static final String ERROR_DECLINED_BY_UUID="DECLINED_BY_UUID";
    
    public static JsonObject createTargetNotExisting(String url, String operation) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("targetUrl", url);
        builder.add("error", ERROR_DOES_NOT_EXISTS);
        builder.add("failedOperation", operation);
        return builder.build();
    }
    
    
    public static JsonObject createValidationContraintsViolatedError(String url, String operation, Set<ConstraintViolation<Object>> violations) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("targetUrl", url);
        builder.add("error", ERROR_CONTRAINTS_VIOLATED);
        builder.add("failedOperation", operation);
        JsonArrayBuilder builder2 = Json.createArrayBuilder();
        for(ConstraintViolation cv : violations){
            builder2.add( cv.getMessage() );
        }
        builder.add("constrainViolations", builder2.build());
        return builder.build();
    }
    
    public static JsonObject createNotDeletedError(String url, String operation){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("targetUrl", url);
        builder.add("error", ERROR_NOT_DELETED);
        builder.add("failedOperation", operation);
        return builder.build();
    }
    
    public static JsonObject createFormatException(String url, String operation, String type){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("targetUrl", url);
        builder.add("error", ERROR_FORMAT_EXCEPTION);
        builder.add("failedOperation", operation);
        builder.add("type", type);
        return builder.build();
    }

    public static JsonObject createNoPermissionError(String url, String login, String operation) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("targetUrl", url);
        builder.add("error", ERROR_NO_PERMISSION);
        builder.add("login", login);
        builder.add("failedOperation", operation);
        return builder.build();
    }
    
    
    public static JsonObject createNotFoundError(String url, String operation){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("targetUrl", url);
        builder.add("error", ERROR_NOT_FOUND);
        builder.add("failedOperation", operation);
        return builder.build();
    }

    public static JsonObject createDuplicatedRequestError(String url, String uuid, String operation, LocalUUIDManager uuidManager) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("targetUrl", url);
        builder.add("error", ERROR_DECLINED_BY_UUID);
        builder.add("failedOperation", operation);
        builder.add("uuid", uuid);
        builder.add("uuidState", uuidManager.getStateUUID(uuid));
        return builder.build();
    }
}
