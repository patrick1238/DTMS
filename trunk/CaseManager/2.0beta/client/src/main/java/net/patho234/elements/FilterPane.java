/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.elements;

import javafx.scene.layout.AnchorPane;
import net.patho234.entities.filter.ClientObjectFilterBase;

/**
 *
 * @author rehkind
 */
abstract public class FilterPane extends AnchorPane {
    abstract public ClientObjectFilterBase getFilter();
}
