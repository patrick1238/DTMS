/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BackgroundHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author patri
 */
public final class BackUpHandler {
    
    private static BackUpHandler handler;
    
    Config config;
    Date lastBackup;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private BackUpHandler() {
        this.config = Config.getConfig();
        if(observerLastBackUp()){
            createBackup();
        }
    }
    
    public static BackUpHandler getBackupHandler() {
        if(handler==null){
            handler = new BackUpHandler();
        }
        return handler;
    }
    
    public void createBackup(){
        try {
            File server = new File(this.config.get("ImageServer")+"Backup"+File.separator+LocalDate.now()+File.separator);
            server.mkdirs();
            String[] files = this.config.get("backupFiles").split(",");
            for(String file:files){
                File cur = new File(this.config.get(file));
                Files.copy(cur.toPath(), Paths.get(server.getAbsolutePath()+File.separator+cur.getName()), REPLACE_EXISTING);
            }
            this.config.replace("BackUpDate", String.valueOf(LocalDate.now()));
            ErrorLog.getErrorLog().createLogEntry(0, "Backup saved");
        } catch (IOException ex) {
            ErrorLog.getErrorLog().createLogEntry(2, "Backup creation failed");
            
        }
    }
    
    private boolean observerLastBackUp(){
        LocalDate now = LocalDate.now();
        LocalDate lastBackUp = LocalDate.parse(this.config.get("BackUpDate"), dtf);
        long period = Math.abs(ChronoUnit.DAYS.between(lastBackUp,now));
        return (period>30);
    }
}
