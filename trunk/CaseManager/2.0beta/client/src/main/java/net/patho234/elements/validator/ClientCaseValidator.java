/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements.validator;

import net.patho234.entities.ClientCase;
import net.patho234.entities.pool.CasePool;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author rehkind
 */
public class ClientCaseValidator {
    
    public static ValidationResult validateCreate( ClientCase c ){
        ValidationResult isValid = new ValidationResult();
        // check for duplicate CaseNumber:
        if(c.getCaseNumber()==null || c.getCaseNumber()=="" ){ isValid.addValidationCheck(ValidationResult.CODE_INVALID, "No case number", "Case number is required"); }else{
            System.out.println("caseNumber = '"+c.getCaseNumber()+"'");
        }
        
        ClientCase duplicatedCase = CasePool.createPool().getEntityByCaseNumber(c.getCaseNumber(), false);
        if( duplicatedCase.getId() != -1 ){
            isValid.addValidationCheck(ValidationResult.CODE_INVALID, "Duplicated case number", "Case number needs to be unique. Case number equals following case in database: "+duplicatedCase.getId());
        }
        
        if(c.getCaseNumber()==null){ 
            if( c.getCaseNumber().length() <= 4 ){
                isValid.addValidationCheck(ValidationResult.CODE_MAJOR_CONCERNS, "Short case number", "Case number seems to be to short...check if value is correct: '"+c.getCaseNumber()+"'");
            }
            if( c.getCaseNumber().contains("-") || c.getCaseNumber().contains("_") ){
                isValid.addValidationCheck(ValidationResult.CODE_MINOR_CONCERNS, "Case number year missing", "Case number normally ends with _<year> or -<year>");
            }
        }
        // clinic is set
        if(c.getClinic()==null ){ isValid.addValidationCheck(ValidationResult.CODE_INVALID, "No clinic", "Clinic field is required"); }
        
        // diagnose exists in database ( show warning to avoid misspelled diagnoses )
        if(c.getDiagnose()==null || c.getDiagnose()=="" ){ isValid.addValidationCheck(ValidationResult.CODE_MAJOR_CONCERNS, "No diagnosis", "Setting a diagnose is recommended. Set to 'unknown' if diagnosis is not available for patient."); }
        
        return isValid;
    }
    
    public static ValidationResult validateUpdate( ClientCase c ){
        throw new NotImplementedException();
    }
}
