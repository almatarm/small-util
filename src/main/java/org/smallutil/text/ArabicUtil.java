/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smallutil.text;

/**
 *
 * @author MUFEED
 */
public class ArabicUtil {
     public static String removeShortVowels(String c) {
        StringBuilder buff = new StringBuilder();
        for (int i = 0; i < c.length(); i++) {
            if (!isShortVowel(c.substring(i, i + 1))) {
                buff.append(c.substring(i, i + 1));
            }
        }
        return buff.toString();
    }

    public static boolean isShortVowel(String c) {
        return "ّ ٌ ُ ً َ ٍ ِ ْ".contains(c);
    }
    
    public static boolean isHindiDigit(String c) {
        return "٠١٢٣٤٥٦٧٨٩".contains(c);
    }
    
    public static int toArabicDigit(char ch) {
        switch(ch) {
            case '٠':
                return 0;
            case '١':
                return 1;
            case '٢':
                return 2;
            case '٣':
                return 3;
            case '٤':
                return 4;
            case '٥':
                return 5;
            case '٦':
                return 6;
            case '٧':
                return 7;
            case '٨':
                return 8;
            case '٩':
                return 9;    
            default:
                return ch;
        }
    }
}
