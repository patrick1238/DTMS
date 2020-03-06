/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.rehkindmag.webapp_client;

import com.formdev.flatlaf.FlatDarkLaf;
import com.sun.scenario.Settings;
import javax.swing.JFrame;
import javax.swing.UIManager;
import net.rehkindmag.views.MainWindow;

/**
 *
 * @author rehkind
 * sample application for accessing webapp database by http requests
 */
public class WebappClient {
    
    
    static public void main(String[] args){
        Settings.set("server.address", "http://192.168.31.1:8585/webapp/resources/");
        
        FlatDarkLaf.install();
        try {
            FlatDarkLaf fdf = new FlatDarkLaf();
            System.out.println("Setting new look and feel: "+fdf.getName()+" - "+fdf.getDescription());
            UIManager.setLookAndFeel( fdf );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        
        MainWindow mainWnd = new MainWindow();
        mainWnd.setTitle("Sample client - HTTP requests for linfoDB");
        mainWnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWnd.show();
    }
}
