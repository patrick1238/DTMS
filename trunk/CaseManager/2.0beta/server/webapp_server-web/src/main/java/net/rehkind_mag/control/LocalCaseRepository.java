/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.control;

import java.util.List;
import javax.ejb.Local;
import javax.validation.ValidationException;
import net.rehkind_mag.interfaces.ICase;

/**
 *
 * @author HS
 */
@Local
public interface LocalCaseRepository {
    public ICase getCase(int caseId);
    public ICase getCaseByCaseNumber(String caseNumber);
    public List<ICase> getCases();
    public List<ICase> getCasesByServiceType(int serviceDefId);

    public List<ICase> getCasesForClinic(int id);

    public void updateCase(ICase caseToUpdate) throws ValidationException;

    public boolean createCase(ICase caseToCreate);
    
    public boolean deleteCase(ICase caseToDelete);
}
