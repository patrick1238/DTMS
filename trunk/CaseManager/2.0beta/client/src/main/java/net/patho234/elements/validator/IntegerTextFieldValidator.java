/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements.validator;

import java.util.HashSet;
import javafx.scene.control.TextField;
import net.patho234.elements.MetadataPane;

/**
 *
 * @author rehkind
 */
public class IntegerTextFieldValidator {
    TextField myTextField;
    public static HashSet<String> NUMBER_CHARACTERS;
    
    public IntegerTextFieldValidator( TextField textField){
        this.myTextField = textField;
        if(IntegerTextFieldValidator.NUMBER_CHARACTERS == null){
            IntegerTextFieldValidator.NUMBER_CHARACTERS = new HashSet<>();
            IntegerTextFieldValidator.NUMBER_CHARACTERS.add("1"); IntegerTextFieldValidator.NUMBER_CHARACTERS.add("2"); IntegerTextFieldValidator.NUMBER_CHARACTERS.add("3");
            IntegerTextFieldValidator.NUMBER_CHARACTERS.add("3"); IntegerTextFieldValidator.NUMBER_CHARACTERS.add("4"); IntegerTextFieldValidator.NUMBER_CHARACTERS.add("5");
            IntegerTextFieldValidator.NUMBER_CHARACTERS.add("6"); IntegerTextFieldValidator.NUMBER_CHARACTERS.add("7"); IntegerTextFieldValidator.NUMBER_CHARACTERS.add("8");
            IntegerTextFieldValidator.NUMBER_CHARACTERS.add("9"); IntegerTextFieldValidator.NUMBER_CHARACTERS.add("0");
        }
        // TODO: add listener and validate user input
        textField.setOnKeyTyped((event) -> {
            if( !(IntegerTextFieldValidator.NUMBER_CHARACTERS.contains( event.getCharacter() ))){
                event.consume();
            }
        });
    }
    
    
}
