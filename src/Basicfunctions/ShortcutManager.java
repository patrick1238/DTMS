/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basicfunctions;

import javafx.scene.control.MenuBar;
import BackgroundHandler.Config;
import java.util.HashMap;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

/**
 *
 * @author patri
 */
public class ShortcutManager {

    public static void initializeShortcuts(MenuBar bar) {
        Config config = Config.getConfig();
        HashMap<String, MenuItem> hashedItems = identifyItems(bar);
        String[] shortcuts = config.get("shortcuts").split(",");
        for (String shortcut : shortcuts) {
            if (!shortcut.equals("empty")) {
                String[] splitted = shortcut.split("-");
                String key = splitted[0];
                String value = splitted[1];
                MenuItem item = hashedItems.get(key);
                item.setAccelerator(KeyCombination.keyCombination(value));
            }
        }
    }

    public static HashMap<String, MenuItem> identifyItems(MenuBar bar) {
        HashMap<String, MenuItem> items = new HashMap<>();
        for (Menu menu : bar.getMenus()) {
            addItem("", menu, items);
        }
        return items;
    }

    private static void addItem(String identifier, MenuItem item, HashMap<String, MenuItem> items) {
        if (item.getStyleClass().contains("menu")) {
            Menu menu = (Menu) item;
            for (MenuItem curItem : menu.getItems()) {
                addItem(identifier + ((identifier.isEmpty()) ? "" : ">") + menu.getText(), curItem, items);
            }
        } else {
            items.put(identifier + ((identifier.isEmpty()) ? "" : ">") + item.getText(), item);
        }
    }
}
