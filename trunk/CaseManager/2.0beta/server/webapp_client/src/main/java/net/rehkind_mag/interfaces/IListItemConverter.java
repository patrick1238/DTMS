/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkind_mag.interfaces;

import javafx.scene.control.ListCell;

/**
 *
 * @author rehkind
 */
public interface IListItemConverter<T> {
    public ListCell<T> convert();
}
