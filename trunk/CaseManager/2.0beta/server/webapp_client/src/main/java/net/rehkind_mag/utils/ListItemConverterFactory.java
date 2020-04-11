/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.utils;

import java.util.HashMap;
import javafx.scene.control.ListCell;
import net.rehkind_mag.interfaces.ICase;
import net.rehkind_mag.interfaces.IClinic;
import net.rehkind_mag.interfaces.IListItemConverter;
import org.jboss.logging.Logger;

/**
 *
 * @author rehkind
 */
public class ListItemConverterFactory {
    HashMap<String, IListItemConverter> converter = new HashMap<>();
    
    static public IListItemConverter getConverter(String className){
        switch ( className ) {
            case "net.rehkind_mag.entities.ClientClinic":
            case "net.rehkind_mag.interfaces.IClinic":
                return new IClinicConverter();
            case "net.rehkind_mag.entities.ClientCase":
            case "net.rehkind_mag.interfaces.ICase":
                return new ICaseConverter();
            
            
            default:
                Logger.getLogger(ListItemConverterFactory.class).warn("No ListItemConverter for class: '{0}'", new Object[]{className});
                return null;
        }
    }
        
    
    static private class IClinicConverter<T> implements IListItemConverter<IClinic>{

        @Override
        public ListCell convert() {
            return new ListCell<IClinic>() {
                @Override
                protected void updateItem(IClinic item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null || item.getName() == null) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        }
    }
    static private class ICaseConverter<T> implements IListItemConverter<ICase>{

        @Override
        public ListCell convert() {
            return new ListCell<ICase>() {
                @Override
                protected void updateItem(ICase item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null || item.getCaseNumber() == null) {
                        setText(null);
                    } else {
                        setText(item.getCaseNumber());
                    }
                }
            };
        }
    }
}
