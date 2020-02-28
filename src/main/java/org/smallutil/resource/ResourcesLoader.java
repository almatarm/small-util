/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smallutil.resource;

import org.smallutil.common.SystemUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matamh0a
 */
public class ResourcesLoader {
    private String defaultResourcePath;
    private String separator = File.separator;
    private String wrongSeparator = 
            ("/".equals(separator)? "\\":"/");
    
    private static ResourcesLoader instance;
    
    public static ResourcesLoader getInstance() {
        if(instance == null)
            instance = new ResourcesLoader();
        return instance;
    }

    private ResourcesLoader() {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(
                    System.getProperty("user.home")  + separator + ".utopia"));
//            System.out.println(System.getProperty("user.home")  
//                    + separator + ".utopia");
            defaultResourcePath = prop.getProperty("RESOURCES_PATH");
            defaultResourcePath = defaultResourcePath.
                    replace(wrongSeparator, separator);
            if(!defaultResourcePath.endsWith(separator)) {
                defaultResourcePath += separator;
            }
//            System.out.println(defaultResourcePath);
        } catch (IOException ex) {
            Logger.getLogger(ResourcesLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getDefaultResourcePath() {
        return defaultResourcePath;
    }
    
    public String getResource(String path) {
        path = path.replace(wrongSeparator, separator);
//        System.out.println(defaultResourcePath + path);
        return defaultResourcePath + path;
    }
    
    public String getHostResource(String path) {
        String hostname = SystemUtil.getHostName();
        return getResource(path + (hostname == null? "":"." + SystemUtil.getHostName()));
    }
    
    public String getResourcePathForWeb(String path) {
        return ("file:///" + defaultResourcePath + path).replace("\\", "/");
    }
    
    public static void main(String[] args) {
        System.out.println(ResourcesLoader.getInstance().getResource("/grap/m1112"));
        System.out.println(ResourcesLoader.getInstance().getHostResource("/grap/m1112"));
    }
}
