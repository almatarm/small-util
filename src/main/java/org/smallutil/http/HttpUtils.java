/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smallutil.http;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.smallutil.dialog.ProxyDialog;

/**
 *
 * @author Sakeel
 */
public class HttpUtils {
    static {
        //read proxy configuration
        File settingsFile = new File(
                System.getProperty("user.home")  + File.separator 
                + "proxy_settings.properties");
        if(settingsFile.exists()) {
            try {
                Properties settings = new Properties();
                settings.load(new FileInputStream(settingsFile));
                ProxyConfig pc = new ProxyConfig("", 0);
                pc.setHost(settings.getProperty("host", ""));
                pc.setPort(Integer.parseInt(settings.getProperty("port", 0 + "")));
                pc.setUser(settings.getProperty("username", ""));                
                pc.setPasswd(settings.getProperty("password", ""));
                
                if(pc.getHost().length() == 0 || pc.getPort() == 0 
                        || pc.getUser().length() == 0 
                        || pc.getPasswd().length() == 0) {
                    ProxyDialog pd = new ProxyDialog(null, true, pc);
                    pd.setVisible(true);
                    
                    pc.setHost(pd.getProxyHost());
                    pc.setPort(pd.getProxyPort());
                    pc.setUser(pc.getUser());
                    pc.setPasswd(pc.getPasswd());
                    pd.dispose();
                }
                ProxyManager.getInstance().setConfig(pc);
                
            } catch (IOException ex) {
                Logger.getLogger(HttpUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void httpSave(String link, String dest) throws IOException {
        byte[] data;
        HttpEntity entity = httpGetEntity(link);
        data = EntityUtils.toByteArray(entity);
        EntityUtils.consume(entity);
        
        ReadableByteChannel rbc = Channels.newChannel(new ByteArrayInputStream(data));
        FileOutputStream fos = new FileOutputStream(dest);
        fos.getChannel().transferFrom(rbc, 0, 1 << 24);
    }
    
    public static String httpGet(String link) throws IOException {
        String rVal;
        HttpEntity entity = httpGetEntity(link);
        rVal = EntityUtils.toString(entity);
        EntityUtils.consume(entity);
        return rVal;
    }
    
    /**
     * Retrives web page, specified by <code>link</code> url.
     * @param link the url to the page you want to download
     * @return the html contents
     * @throws IOException 
     */
    public static HttpEntity httpGetEntity(String link) throws IOException {
//        String rVal = null;
        HttpEntity entity;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            
            HttpHost proxy;
            HttpGet httpget;
            HttpResponse response;
            if(ProxyManager.getInstance().userProxy()) {
                httpclient.getCredentialsProvider().setCredentials(
                    new AuthScope(
                        ProxyManager.getInstance().getConfig().getHost(), 
                        ProxyManager.getInstance().getConfig().getPort()),
                    new UsernamePasswordCredentials(
                        ProxyManager.getInstance().getConfig().getUser(), 
                        ProxyManager.getInstance().getConfig().getPasswd()));
                
                proxy = new HttpHost(
                        ProxyManager.getInstance().getConfig().getHost(), 
                        ProxyManager.getInstance().getConfig().getPort());
                httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                
                int hostSptIdx = link.indexOf("/", "http://".length());
                HttpHost targetHost = new HttpHost(link.substring(0, hostSptIdx).
                        replace("http://", "").replace("https://", ""));
                httpget = new HttpGet(
                        link.substring(hostSptIdx));
                response = httpclient.execute(targetHost, httpget);
            } else {
                httpget = new HttpGet(link);
                response = httpclient.execute(httpget);
            }
            
            entity = response.getEntity();
            if (entity != null) {
                entity = new BufferedHttpEntity(entity);
            }
//            rVal = EntityUtils.toString(entity);
//            EntityUtils.consume(entity);
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
//        return rVal;
        return entity;
    }
    
    public static void main(String[] args) throws Exception {
//        System.out.println(
//                HttpUtils.httpGet(
//                "http://www.amazon.com/Best-Sellers-Kindle-Store/zgbs/digital-text/"));
        
        HttpUtils.httpSave("http://www.springsource.org/files/uploads/all/images/icons/Spring_ICO_48x48.png", 
                "/Users/MUFEED/Desktop/t.png");
    }
}
