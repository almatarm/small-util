/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smallutil.www;

import org.smallutil.http.WebUtil;
import java.io.IOException;
import org.htmlparser.util.ParserException;

/**
 *
 * @author MUFEED
 */
public class NbanewsUs {
    public static String getLink(String link) throws Exception {
        //Getting the first page
//        try {
            String t1 = WebUtil.getHttp(link, "UTF-8");
            int i1 = t1.indexOf("index.php?id");
            int i2 = t1.indexOf("'", i1);

            //System.out.println(t1);
            //Getting the second page
            String id = t1.substring(i1 + "index.php".length(), i2);
            String t2 = WebUtil.getHttp(link.substring(0, link.lastIndexOf("/") +1) + id,  "UTF-8");

            //System.out.println(link.substring(0, link.lastIndexOf("/") +1) +
            //        t1.substring(i1 + "index.php".length(), i2));

            //Getting the link
            i1 = t2.indexOf("Click Here To Download");
            if(i1 == -1) {
               // System.out.println("http://www.nbanews.us/" + id);
                t2 = WebUtil.getHttp("http://www.nbanews.us/" + id,  "UTF-8");
            }
            i1 = t2.indexOf("Click Here To Download");
            i1 = t2.substring(0, i1).lastIndexOf("href") + "href='".length();
            i2 = t2.indexOf("'", i1);
            return t2.substring(i1, i2);
//        } catch (Exception ex) {
//            System.out.println("Exception parsing:" + link);
//            return "";
//        }
    }

    public static void main(String args[]) throws Exception{
        System.out.println(getLink(
                //"http://share-link.info/138062"
                //"http://share-link.info/138065"
                //"http://wwenews.us/314331"
                "http://share-link.info/116975"
                ));

        //System.out.println(WebUtil.getHttp("http://business-investment.bz/thread.php?url=aHR0cDovL3NoYXJlLWxpbmsuaW5mby8xMzgwNjU=", "UTF-8"));

    }
}
