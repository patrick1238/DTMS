/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.interfaces;

import java.util.HashMap;

/**
 *
 * @author patri
 */
public interface IDataDisplay {
    public HashMap<String,Integer> getViews();
    public void setVisible(Integer id);
    public Integer getVisibleDataCount(Integer id);
}
