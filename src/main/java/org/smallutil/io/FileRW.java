/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smallutil.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author MUFEED
 */
public class FileRW {

    public static void save(String path, String data) throws Exception{
        save(path, data, null);
    }

    public static void save(String path, String data, String encoding)
            throws IOException{
        BufferedWriter out;
        if(encoding == null) {
            out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(new File(path))));
        } else {
                out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(new File(path)),encoding));
        }
        out.write(data);
        out.close();
    }

    public static BufferedReader getReader(String path, String encoding) 
            throws FileNotFoundException, UnsupportedEncodingException {
        BufferedReader in;
        File inFile = new File(path);
        
        if(encoding == null) {
            in = new BufferedReader(new InputStreamReader(
                new FileInputStream(inFile)));
        } else {
            in = new BufferedReader(new InputStreamReader(
                new FileInputStream(inFile), encoding));            
        }
        return in;
    }
    public static String load(String path) throws IOException {
        return load(path, null);
    }

    public static String load(String path, String encoding) throws IOException {
        BufferedReader in = getReader(path, encoding);

        StringBuffer buff = new StringBuffer();
        String line;
        while((line = in.readLine()) != null) {
            buff.append(line + "\n");
        }

        return buff.toString();

    }
}
