/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smallutil.http;

/**
 *
 * @author matamh0a
 */
public class ProxyManager {
    private static ProxyManager instance;
    private ProxyConfig config;
    
    public static ProxyManager getInstance() {
        if (instance == null) {
            instance = new ProxyManager();
        }
        return instance;
    }

    public ProxyConfig getConfig() {
        return config;
    }

    public void setConfig(ProxyConfig config) {
        this.config = config;
    }
    
    public boolean hasConfig() {
        return config != null;
    }
    
    public boolean userProxy() {
        return config != null;
    }
}
