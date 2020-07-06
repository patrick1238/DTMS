/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author MLH
 */
@javax.ws.rs.ApplicationPath("webapp")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(net.patho234.boundary.CasesResource.class);
        resources.add(net.patho234.boundary.ClinicsResource.class);
        resources.add(net.patho234.boundary.ContactPersonResource.class);
        resources.add(net.patho234.boundary.LogsResource.class);
        resources.add(net.patho234.boundary.MetadataResource.class);
        resources.add(net.patho234.boundary.MetadataValuesResource.class);
        resources.add(net.patho234.boundary.ServiceDefinitionsResource.class);
        resources.add(net.patho234.boundary.ServicesResource.class);
        resources.add(net.patho234.boundary.SubmittersResource.class);
        resources.add(net.patho234.boundary.UsersResource.class);
    }
    
}
