/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.http;

/**
 *
 * @author rehkind
 */
public class HTTP_ENDPOINT_TEMPLATES {
    static final public String GET_CASE="casepool/case/{ID}";
    static final public String GET_CASES="casepool/cases";
    static final public String CREATE_CASE="casepool/case/create";
    static final public String UPDATE_CASE="casepool/case/update";
    static final public String DELETE_CASE="casepool/case/delete";
    
    static final public String GET_CASE_BY_CASE_NUMBER="casepool/case/casenumber/{CASE_NUMBER}";
    
    static final public String GET_CLINIC="clinicpool/clinic/{ID}";
    static final public String GET_CLINICS="clinicpool/clinics";
    static final public String CREATE_CLINIC="clinicpool/clinic/create";
    static final public String UPDATE_CLINIC="clinicpool/clinic/update";
    static final public String DELETE_CLINIC="clinicpool/clinic/delete";
    
    static final public String GET_SERVICE="servicepool/service/{ID}";
    static final public String GET_SERVICES="servicepool/services";
    static final public String CREATE_SERVICE="servicepool/service/create";
    static final public String UPDATE_SERVICE="servicepool/service/update";
    static final public String DELETE_SERVICE="servicepool/service/delete";
    static final public String GET_SERVICES_FOR_CASE="servicepool/services/forcase/{CASEID}";
    static final public String GET_SERVICES_FOR_DEFINITION="servicepool/services/fordef/{DEFINITIONID}";
    static final public String UPDATE_SERVICE_METADATA="servicepool/service/{SERVICEID}/metadata/update";
    
    static final public String GET_SERVICE_DEFINITION="servicedefinitionpool/servicedefinition/{ID}";
    static final public String GET_SERVICE_DEFINITIONS="servicedefinitionpool/servicedefinitions";
    
    static final public String GET_SUBMITTER="submitterpool/submitter/{SUBMITTERID}";
    static final public String GET_SUBMITTERS="submitterpool/submitters";
    static final public String CREATE_SUBMITTER="submitterpool/submitter/create";
    
    static final public String GET_METADATA="metadatapool/metadata/{SERVICEID}/{NAME}";
    static final public String GET_ALL_METADATA="metadatapool/metadata";
    static final public String GET_METADATA_FOR_SERVICE="metadatapool/metadata/forservice/{SERVICEID}";
    static final public String GET_METADATA_FOR_CASE="metadatapool/metadata/forcase/{CASEID}";
    
    

    static public String get_HTTP_METHOD_FOR_ENDPOINT(String endpoint) throws Exception{
        switch(endpoint){
            case GET_CASE:
            case GET_CASE_BY_CASE_NUMBER:
            case GET_CASES:
            case GET_CLINIC:
            case GET_CLINICS:
            case GET_SERVICE:
            case GET_SERVICES:
            case GET_SUBMITTER:
            case GET_SUBMITTERS:
            case GET_SERVICE_DEFINITION:
            case GET_SERVICE_DEFINITIONS:
            case GET_METADATA:
            case GET_ALL_METADATA:
            case GET_METADATA_FOR_SERVICE:
            case GET_METADATA_FOR_CASE:
                return "GET";
            case CREATE_CASE:
            case UPDATE_CASE:
            case CREATE_CLINIC:
            case UPDATE_CLINIC:
            case CREATE_SERVICE:
            case UPDATE_SERVICE:
            case CREATE_SUBMITTER:
            case UPDATE_SERVICE_METADATA:
                return "PUT";
            case DELETE_CASE:
            case DELETE_CLINIC:
            case DELETE_SERVICE:
                return "DELETE";
            default:
                throw new Exception("get_HTTP_METHOD_FOR_ENDPOINT: unknown endpoint '"+endpoint+"'");
                
        }
    }
}
