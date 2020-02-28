/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smallutil.http;

import java.awt.GridLayout;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author MUFEED
 */
public class WebUtil {

    public static String getHttp(String page_url, String encoding) throws IOException {
        try {
            org.htmlparser.Parser parser = new org.htmlparser.Parser(page_url);
            parser.setEncoding(encoding);
            NodeList nodes = parser.parse(null);
            return nodes.toHtml();
        } catch (ParserException ex) {
            Logger.getLogger(WebUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String getHttp(String page_url, String encoding, int nTry) throws IOException {
        NodeList nodes = null;
        for (int i = 0; i < nTry; i++) {
            try {
                org.htmlparser.Parser parser = new org.htmlparser.Parser(page_url);
                parser.setEncoding(encoding);
                nodes = parser.parse(null);
                break;
            } catch (ParserException ex) {
                try { Thread.sleep(5000);}
                catch (InterruptedException ex1) { Logger.getLogger(WebUtil.class.getName()).log(Level.SEVERE, null, ex1);}
                continue;
            }
        }
        return nodes.toHtml();
    }

    
    public static int saveWebFile(String webFile, String localFile) throws IOException{
        final JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        final JDialog dialog = new JDialog();
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(2,1));
        pane.add(new JLabel("Downloading: " + webFile));
        pane.add(progressBar);
        dialog.setContentPane(pane);
        dialog.pack();
        dialog.setVisible(true);
        dialog.setResizable(false);

        URL url = new URL(webFile);
        final int fileSize = url.openConnection().getContentLength();
        
        BufferedInputStream in = new BufferedInputStream(url.openStream());
        FileOutputStream fos = new java.io.FileOutputStream(localFile + ".part");
        BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
        if(fileSize > 0) {
            progressBar.setMaximum(fileSize);
        } else {
            //progressBar.setIndeterminate(true);
            progressBar.setStringPainted(true);
            progressBar.setString("0 KB" );
        }
        
        byte[] data = new byte[1024];

        int x=0;
        //int kb  = 0;
        int prog = 0;
        while((x=in.read(data,0,1024))>=0) {
            bout.write(data,0,x);
            prog += x;
            final int total = prog;
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    if(fileSize > 0) {
                        progressBar.setValue(total);
                    } else {
                        double kb = Math.floor(total/1024);
                        progressBar.setString(kb <= 1024?kb + " KB":
                            Math.floor(kb/1024*100)/100 + " MB");
                    }
                }
            });
            
            //kb++;
        }
        bout.close();
        in.close();
        if(fileSize > 0)
             progressBar.setString(progressBar.getString() + " - Done");
        //Rename the file
        (new File(localFile + ".part")).renameTo(new File(localFile));
        dialog.setVisible(false);

        return prog/1024;
    }

    public static int saveWebFileCmd(String webFile, String localFile) throws IOException {
        return saveWebFileCmd(webFile, localFile, null);
    }
    public static int saveWebFileCmd(String webFile, String localFile, String post) throws IOException {
        if ((new File(localFile)).exists()) {
            System.out.println("File already exists!");
            return 0;
        }

        URL url = new URL(webFile);
        URLConnection conn = url.openConnection();
        OutputStreamWriter wr = null;
        if (post != null) {
            conn.setDoOutput(true);
            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(post);
            wr.flush();
        }
        
        int fileSize = conn.getContentLength();

        File localDir = new File(localFile.substring(0, localFile.lastIndexOf(File.separator)));
        if(!localDir.exists())
            localDir.mkdirs();

        BufferedInputStream in;
        in = (post == null)?  new BufferedInputStream(url.openStream()):
                new BufferedInputStream(conn.getInputStream());
                
        FileOutputStream fos = new java.io.FileOutputStream(localFile + ".part");
        BufferedOutputStream bout = new BufferedOutputStream(fos,1024);

        byte[] data = new byte[1024 * 16];

        int x = 0;
        int prog = 0;
        while((x=in.read(data,0,1024 * 16))>=0) {
            bout.write(data,0,x);
            prog += x;
            printDownloadStatus(prog, fileSize);
        }
        double kb = Math.floor(prog/1024);
        System.out.println("\r" + (kb <= 1024?kb + " KB":
                Math.floor(kb/1024*100)/100 + " MB") + " - Done.      ");
        bout.close();
        in.close();
        //Rename the file
        (new File(localFile + ".part")).renameTo(new File(localFile));
        return prog/1024;
    }

    private static void printDownloadStatus(int prog, int max) {
        int dashedCount = 50;
        double kb = Math.floor(prog/1024);

        if(max > 0) {
            System.out.print("\r " + (kb <= 1024?kb + " KB":
                Math.floor(kb/1024*100)/100 + " MB") + " - %" + Math.floor(prog * 1000.0/max)/10 + "    ");
            
        } else {
            System.out.print("\r " + (kb <= 1024?kb + " KB":
                Math.floor(kb/1024*100)/100 + " MB"));
       }
    }
    
    public static Map<String, String> getLinks(String page, String baseUrl) {
        Map links = new HashMap();
        Document doc = Jsoup.parse(page, baseUrl);
        Elements elements = doc.select("a"); 
        Iterator<Element> it = elements.iterator();
        while(it.hasNext()) {
            Element elm = it.next();
            String text = elm.text();
            String url = elm.absUrl("href");
            links.put(text, url);
        }
        return links;
    }
    
    public static Set<String> getURLsSet(String page, String baseUrl) {
        Set links = new HashSet();

        Document doc = Jsoup.parse(page, baseUrl);
        Elements elements = doc.select("a[href]"); 
        Iterator<Element> it = elements.iterator();
        while(it.hasNext()) { 
            String url = it.next().absUrl("href");
            links.add(url);
        }
        return links;
    }
    
    public static List<String> getURLs(String page, String baseUrl) {
        List links = new ArrayList();

        Document doc = Jsoup.parse(page, baseUrl);
        Elements elements = doc.select("a[href]"); 
        Iterator<Element> it = elements.iterator();
        while(it.hasNext()) { 
            String url = it.next().absUrl("href");
            links.add(url);
        }
        return links;
    }
    
    public static Set<String> getImagesSet(String page, String baseUrl) {
        Set links = new HashSet();
        
        Document doc = Jsoup.parse(page, baseUrl);
        Elements elements = doc.select("img[src]"); 
        Iterator<Element> it = elements.iterator();
        while(it.hasNext()) {
            String url = it.next().absUrl("src");
            links.add(url);
        }
        return links;
    }
    
    public static List<String> getImages(String page, String baseUrl) {
        List links = new ArrayList();
        
        Document doc = Jsoup.parse(page, baseUrl);
        Elements elements = doc.select("img[src]"); 
        Iterator<Element> it = elements.iterator();
        while(it.hasNext()) {
            String url = it.next().absUrl("src");
            links.add(url);
        }
        return links;
    }

    public static void main(String args[]) throws IOException {
//        System.out.println(saveWebFileCmd("http://live1.islamweb.net/quran/Mh-ref3at/jalsah/073.mp3",
//                "/Users/MUFEED/Desktop/Test.mp3"));    
//        getLinks(getHttp("http://alfeker.net/", "utf-8"), "http://alfeker.net");
    
        
        saveWebFileCmd("http://shiavoice.com/save-H3hOg", "/home/almatarm/test.mp3",
                "");
    }

}
