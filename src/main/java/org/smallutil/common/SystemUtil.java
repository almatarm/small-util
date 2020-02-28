package org.smallutil.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * SystemUtil.java
 *
 * Created on December 19, 2006, 1:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



/**
 * @author matamh0a
 */
public class SystemUtil {

    public static OS getOperationSystem() {
        String os = System.getProperty("os.name");
        if (os.startsWith("Sun")) return OS.SUN;
        else if (os.startsWith("Linux")) return OS.LINUX;
        else if (os.startsWith("Windows")) return OS.WINDOWS;
        else if (os.startsWith("Mac")) return OS.MAC;
        return OS.UNKNOWN;
    }

    public static OS getOperationSystem(int os_id) {
        if (os_id == 1) return OS.SUN;
        if (os_id == 2) return OS.LINUX;
        if (os_id == 4) return OS.WINDOWS;
        if (os_id == 8) return OS.MAC;
        return OS.UNKNOWN;
    }
    
    public static String getTmpDirectory() {
        return System.getProperty("java.io.tmpdir");
    }
    
    public static String getHostName() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
            return hostname;
        } catch (UnknownHostException ex) {
            Logger.getLogger(SystemUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
