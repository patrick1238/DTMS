/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.webapp_client;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.WindowEvent;
import jfxtras.styles.jmetro8.JMetro;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class FxmlManager {
    public static JMetro.Style DEFAULT_STYLE = JMetro.Style.LIGHT;
    public static EventHandler<WindowEvent> EXIT_APPLICATION_HANDLER = new EventHandler<WindowEvent>(){
        @Override
        public void handle(WindowEvent t) {
            
            if( t==null || !t.isConsumed() ){
                Logger.getLogger(getClass()).info("[FxmlManager.EXIT_APPLICATION_HANDLER] Shutting down "+APPLICATION_DEFAULTS.APPLICATION_NAME+".");
                Platform.exit();
            } else { Logger.getLogger(getClass()).info("[FxmlManager.EXIT_APPLICATION_HANDLER] Ignoring already consumed event "+t); }
            System.exit(0);
        }
    };
    public static EventHandler<WindowEvent> DISPOSE_WINDOW_HANDLER = new EventHandler<WindowEvent>(){
        @Override
        public void handle(WindowEvent t) {
            if( !t.isConsumed() ){
                Logger.getLogger(getClass()).info("[FxmlManager.DISPOSE_WINDOW_HANDLER] Closing window "+t.getSource()+".");
            } else { Logger.getLogger(getClass()).info("[FxmlManager.DISPOSE_WINDOW_HANDLER] Ignoring already consumed event "+t); }
        }
    };    
    
    public static void applyDefaultStyle(Parent fxComponent){
        new JMetro( DEFAULT_STYLE ).applyTheme( fxComponent );
    }
    
    public static void applyDefaultStyle( Scene fxComponent ){
        new JMetro( DEFAULT_STYLE ).applyTheme( fxComponent );
    }
    
    
}
