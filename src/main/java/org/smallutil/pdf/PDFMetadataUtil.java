/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smallutil.pdf;


import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author almatarm
 */
public class PDFMetadataUtil {
    public static boolean write(String pdfPath, String title, String  author, 
            String[] keywords) throws IOException {
        return write(pdfPath, pdfPath + "2.pdf", title, author, keywords);
    }
    
    public static boolean write(String pdfPath, String outPath,  
            String title, String  author, 
            String[] keywords) throws IOException {
        HashMap<String, String> info = new HashMap<String, String>();
        info.put("Title", title);
        info.put("Author", author);
        if(keywords.length > 0) {
            String tags = keywords[0];
            for(int i = 1; i <  keywords.length; i++) {
                tags += ',' +  keywords[i];
            }
            info.put("Keywords", tags);    
        }
        
        return write(pdfPath, outPath, info);
    }
    
    public static boolean write(String pdfPath, Map<String, String>  metadata) 
            throws IOException {
        return write(pdfPath, pdfPath + "2.pdf", metadata);
    }
    
    public static boolean write(String pdfPath, String outPath, 
           Map<String, String>  metadata) 
            throws IOException {
    
        try {
            File inFile = new File(pdfPath);
            File outFile = new File(outPath);
            
            PdfReader.unethicalreading = true; 
            PdfReader reader = new PdfReader(inFile.getAbsolutePath());
            reader.getInfo().putAll(metadata);  
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                    outFile.getAbsolutePath()));
            stamper.setMoreInfo(metadata);
            stamper.close();
            return true;
        } catch (DocumentException ex) {
            Logger.getLogger(PDFMetadataUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getCause());
        }
    }
    
    public static Map<String, String> read(String pdfPath) throws IOException {
         File inFile = new File(pdfPath);
         PdfReader reader = new PdfReader(inFile.getAbsolutePath());
         HashMap<String, String> info = reader.getInfo();    
         reader.close();
         return info;
    }

    public static void main(String[] args) throws IOException {
       
        write("/Users/almatarm/Downloads/hkm.pdf", 
              "/Users/almatarm/Downloads/hkm2.pdf", 
              "شرح حكم نهج البلاغة", 
              "الشيخ عباس القمي", 
              new String[]{"مصور", "scanned"});
        
    }
}
