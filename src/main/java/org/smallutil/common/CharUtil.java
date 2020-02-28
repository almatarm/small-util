/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smallutil.common;

/**
 *
 * @author MUFEED
 */
public class CharUtil {
    public static final int CHARACTER = 01;
    public static final int NUMBER    = 02;
    public static final int SYMBOL    = 03;
    public static final int SPACE     = 04;

    private static String symbol = "~!@#$%^&*()_+`-=,./<>?{}|[]\\";
    private static String other_symbol = "ـ";


    public static int getType(String ch) {
        if(isNumber(ch))
            return NUMBER;
        if (symbol.indexOf(ch) != -1 || other_symbol.indexOf(ch) != -1)
            return SYMBOL;
        if (ch.equals(" "))
            return SPACE;
        return CHARACTER;
    }

    public static int getType(char ch) {
        return getType(ch + "");
    }
    
    public static boolean isNumber(String x) {
        try {
            Integer.parseInt(x);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    public static boolean isNumber(char x) {
        return isNumber(x + "");
    }

    public static boolean isCharacter(String ch) {
        return getType(ch) == CHARACTER;
    }

    public static boolean isCharacter(char ch) {
        return isCharacter(ch + "");
    }

    public static String repeat(String str, int count) {
        String r = "";
        for (int i = 0; i < count; i++) {
            r += str;
        }
        return r;
    }

    public static int parseInt(String str, boolean force) {
        String rVal = "";
        for(int i = 0; i < str.length(); i++)
            if (isNumber(str.charAt(i)))
                rVal += str.charAt(i);
        if(force)
            if (rVal.length() == 0) return 0;
        int rValInt = 0;
        try {
            rValInt = Integer.parseInt(rVal);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
            return rValInt;
    }

    public static int parseInt(String str) {
        return parseInt(str, false);
    }
    public static void main(String[] args) {
        System.out.println(CharUtil.getType("s"));
        System.out.println(CharUtil.getType("4"));
        System.out.println(CharUtil.getType("."));
        System.out.println(repeat("x", 2));
    }

}
