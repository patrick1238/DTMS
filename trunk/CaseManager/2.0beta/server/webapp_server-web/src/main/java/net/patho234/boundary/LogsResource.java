/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.boundary;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import net.patho234.boundary.utils.DefaultResponse;
import net.patho234.control.LocalLogRepository;
import net.patho234.interfaces.ILogEntry;

/**
 *
 * @author rehkind
 */
@Path("/logpool")
@Produces(MediaType.APPLICATION_JSON)
public class LogsResource {
    final private String LOGS_URL="/logentries";
    final private String LOGS_FOR_SUBMITTER_URL="/logentries/submitterid/{SUBMITTER_ID}";
    final private String LOGS_FOR_TIME_URL="/logentries/timeinterval/{START_DATE}/{END_DATE}";
    final private String LOG_URL="/logentry/{LOG_ENTRY_ID}";
    
    @EJB
    LocalLogRepository logsRepo;
    
    @Context
    UriInfo uriInfo;
    
    
    @GET
    @Path(LOGS_URL)
    public Response getLogs() {
        List<ILogEntry> entries = logsRepo.getLogEntries();
        JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
        if( entries != null ){ // if there are any logs, convert them to jsonObj
            for(ILogEntry e : entries){
                arrayBuilder.add( getLogEntryBuilderJson(e) );
            }
        }
        return DefaultResponse.createOKResponse( arrayBuilder.build() );
    }
    
    
    public JsonObjectBuilder getLogEntryBuilderJson(ILogEntry logToBuild){
        String url = LogsURLResource.getURL(logToBuild, uriInfo);
        DateFormat format = DateFormat.getInstance();
        
        Date d = logToBuild.getTimestamp();
        String dateAsString=format.format(d);
        
        JsonObjectBuilder jsonLogEntryBuilder = Json.createObjectBuilder();
        jsonLogEntryBuilder.add("id", logToBuild.getId())
                .add("affectedTable", logToBuild.getAffectedTable())
                .add("submitter", SubmittersURLResource.getURL(logToBuild.getSubmitter(), uriInfo))
                .add("timestamp", dateAsString)
                .add("message", logToBuild.getMessage())
                .add("url", url);
        return jsonLogEntryBuilder;
    }
}
