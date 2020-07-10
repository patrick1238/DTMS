/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.patho234.views;

import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.patho234.entities.ClientCase;

/**
 *
 * @author rehkind
 */
public class CaseWindow extends Stage {
    CasePane myPane;
    
    public CaseWindow(ClientCase theCase) throws IOException{
        myPane = new CasePane(theCase);
        Scene sc = new Scene(myPane);
        this.setScene(sc);
    }
    
}
