/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smallutil.text;


import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 *
 * @author almatarm
 */
public class HtmlUtils {
    
    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }
    
    public static String html2textRelaxed(String html) {
        return Jsoup.clean(html, Whitelist.relaxed());
    }
}
