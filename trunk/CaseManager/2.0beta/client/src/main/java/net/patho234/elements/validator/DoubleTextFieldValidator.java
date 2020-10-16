/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements.validator;

import java.util.HashSet;
import javafx.scene.control.TextField;

/**
 *
 * @author rehkind
 */
public class DoubleTextFieldValidator {
    TextField myTextField;
    public static HashSet<String> NUMBER_CHARACTERS;
    
    public DoubleTextFieldValidator( TextField textField){
        this.myTextField = textField;
        if(DoubleTextFieldValidator.NUMBER_CHARACTERS == null){
            DoubleTextFieldValidator.NUMBER_CHARACTERS = new HashSet<>();
            DoubleTextFieldValidator.NUMBER_CHARACTERS.add("1"); DoubleTextFieldValidator.NUMBER_CHARACTERS.add("2"); DoubleTextFieldValidator.NUMBER_CHARACTERS.add("3");
            DoubleTextFieldValidator.NUMBER_CHARACTERS.add("3"); DoubleTextFieldValidator.NUMBER_CHARACTERS.add("4"); DoubleTextFieldValidator.NUMBER_CHARACTERS.add("5");
            DoubleTextFieldValidator.NUMBER_CHARACTERS.add("6"); DoubleTextFieldValidator.NUMBER_CHARACTERS.add("7"); DoubleTextFieldValidator.NUMBER_CHARACTERS.add("8");
            DoubleTextFieldValidator.NUMBER_CHARACTERS.add("9"); DoubleTextFieldValidator.NUMBER_CHARACTERS.add("0");  DoubleTextFieldValidator.NUMBER_CHARACTERS.add(".");
        }
        // TODO: add listener and validate user input
        textField.setOnKeyTyped((event) -> {
            if( !(DoubleTextFieldValidator.NUMBER_CHARACTERS.contains( event.getCharacter() ) ) ){
                event.consume();
            }
        });
    }
    
    
}
