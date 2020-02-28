/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smallutil.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author <a href="mailto:almatarm@gmail.com">Mufeed H. AlMatar</a>
 * @version 1.0
 */
public class Utopia {

    private static final String UTOPIA_FILE_PATH = System.getProperty("user.home")  
            + File.separator + ".utopia";
    
    public enum Key {
        RESOURCES_PATH,
        DROPBOX_PATH,
        ;
    }
    
    Properties prop;
    
    private Utopia() {
        readConfig();
    }

    public static Utopia getInstance() {
        return UtopiaHolder.INSTANCE;
    }

    private static class UtopiaHolder {
        private static final Utopia INSTANCE = new Utopia();
    }
    
    private void readConfig() {
        try {
            prop = new Properties();
            prop.load(new FileInputStream(UTOPIA_FILE_PATH));
        } catch (IOException ex) {
            Logger.getLogger(Utopia.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }
    
    public String getProperty(Key key) {
        return prop.getProperty(key.name()).trim();
    }
 }
