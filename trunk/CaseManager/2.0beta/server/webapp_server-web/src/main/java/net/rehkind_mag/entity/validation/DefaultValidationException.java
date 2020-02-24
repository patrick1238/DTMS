/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.entity.validation;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;

/**
 *
 * @author rehkind
 */
public class DefaultValidationException extends ValidationException {
    private final Set<ConstraintViolation<Object>> violations;
    public DefaultValidationException(Set<ConstraintViolation<Object>> violations){
        super(violations.size()+" constraints violated");
        this.violations = violations;
    }
    
    public Set<ConstraintViolation<Object>> getViolations(){ return this.violations; }
}
