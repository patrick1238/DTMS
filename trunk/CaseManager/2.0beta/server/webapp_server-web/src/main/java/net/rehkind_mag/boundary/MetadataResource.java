/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.boundary;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import net.rehkind_mag.boundary.utils.DefaultResponse;
import net.rehkind_mag.control.LocalMetadataRepository;
import net.rehkind_mag.interfaces.IMetadata;
import net.rehkind_mag.interfaces.IMetadataValue;
import net.rehkind_mag.interfaces.IServiceDefinition;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
@Path("/metadatapool")
@Produces(MediaType.APPLICATION_JSON)
public class MetadataResource {
    final private String METADATA_URL="/metadata";
    final private String METADATA_FOR_SERVICE="/metadata/forservice/{SERVICE_ID}";
    final private String METADATA_FOR_CASE="/metadata/forcase/{CASE_ID}";
    
    @EJB
    LocalMetadataRepository metaRepo;
    
    @Context
    UriInfo uriInfo;
    
    @GET
    @Path(METADATA_URL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllMetadata() {
        List<IMetadata> entries = metaRepo.getAllEntities();
        JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
        
        if( entries != null ){ // if there are any logs, convert them to jsonObj
            for(IMetadata m : entries){
                arrayBuilder.add( getMetadataBuilderJson(m) );
            }
        }
        return DefaultResponse.createOKResponse( arrayBuilder.build() );
    }

    private JsonObjectBuilder getMetadataBuilderJson(IMetadata m) {
            JsonObjectBuilder metadataObjectBuilder = Json.createObjectBuilder();
            IMetadataValue metaValue = getMetadataValueForMetadata( m );
            
            if(metaValue==null){
                Logger.getLogger(getClass()).warn("MetaValue for Metadata object is NULL.");
            }
            
            String type="UNDEFINED";
            switch( m.getType() ){
                case INTEGER:
                    type="integer";
                    break;
                case DOUBLE:
                    type="double";
                    break;
                case STRING:
                    type="string";
                    break;
                case TEXT:
                    type="text";
                    break;
                case URL:
                    type="url";
                    break;
                default:
                    type="UNDEFINED";
            }
                    
            metadataObjectBuilder.add("name", m.getName())
                .add("value", ""+m.getData())
                .add("type", type)
                .add("unit", (metaValue.getUnit()==null)?"NO_UNIT":metaValue.getUnit())
                .add("serviceId", m.getService().getId())
                .add("category", m.getService().getServiceDefinition().getName());
            
            return metadataObjectBuilder;
    }
    
    private IMetadataValue getMetadataValueForMetadata( IMetadata meta ){
        HashMap<IServiceDefinition, List<IMetadataValue>> fieldsPerDef = meta.getService().getServiceDefinition().getMetadataValues();
        StringBuilder sb=new StringBuilder();
        sb.append("\nTrying to find MetadataValue for Metadata: '"+meta.getName()+"'");
        for(IServiceDefinition def : fieldsPerDef.keySet()){
            sb.append("\nchecking definition: "+def.getName());
            List<IMetadataValue> values = fieldsPerDef.get(def);
            for( IMetadataValue v : values ){
                sb.append("\n---- metadata field: "+v.getKey());
                if( v.getKey().equals(meta.getName()) ){
                    return v;
                }
            }
        }
        
        Logger.getLogger(getClass()).warn("No MetadataValue found for Metadata "+meta.getName());
        Logger.getLogger(getClass()).warn("Search results: \n___________________________\n"+sb.toString());
        return null;
    }
}
