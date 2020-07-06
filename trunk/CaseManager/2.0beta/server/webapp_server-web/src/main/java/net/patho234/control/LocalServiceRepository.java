/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.control;

import java.util.List;
import javax.ejb.Local;
import net.patho234.interfaces.IService;
import net.patho234.interfaces.IServiceDefinition;

/**
 *
 * @author HS
 */
@Local
public interface LocalServiceRepository {
    public IService getService(int serviceId);
    public List<IService> getServicesForCase(int caseId);
    public List<IService> getServices();
    public List<IService> getServicesByServiceDef(IServiceDefinition serviceDefId);

    public void updateService(IService serviceToUpdate);
    public boolean createService(IService serviceToUpdate);
    public boolean deleteService(IService serviceToUpdate);
}
