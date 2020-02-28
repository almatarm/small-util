/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smallutil.http;

/**
 *
 * @author matamh0a
 */
public class ProxyConfig {
    String host;
    int port;
    String user;
    String passwd;

    public ProxyConfig(String host, int prot) {
        this.host = host;
        this.port = prot;
    }

    public ProxyConfig(String host, int prot, String user, String passwd) {
        this.host = host;
        this.port = prot;
        this.user = user;
        this.passwd = passwd;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
