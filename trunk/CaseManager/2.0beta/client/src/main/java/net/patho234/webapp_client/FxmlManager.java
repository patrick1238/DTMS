/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.webapp_client;

import javafx.scene.Parent;
import javafx.scene.Scene;
import jfxtras.styles.jmetro8.JMetro;

/**
 *
 * @author rehkind
 */
public class FxmlManager {
    public static JMetro.Style DEFAULT_STYLE = JMetro.Style.LIGHT;
    
    public static void applyDefaultStyle(Parent fxComponent){
        new JMetro( DEFAULT_STYLE ).applyTheme( fxComponent );
    }
    
    public static void applyDefaultStyle( Scene fxComponent ){
        new JMetro( DEFAULT_STYLE ).applyTheme( fxComponent );
    }
}
