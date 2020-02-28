/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smallutil.text;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.FractionConversionException;

/**
 *
 * @author almatarm
 */
public class FractionFormat {
    public static String toFraction(double q) {
        double epsi = .125;
        boolean frac = Math.abs(q - ((int) q)) > epsi;


        try {

            if (new Fraction(q - ((int) q), epsi, 10).toString().
                    replace(" ", "").equals("0")) {
                q = (int) q;
                frac = false;
            } else if (new Fraction(q - ((int) q), epsi, 10).toString().
                    replace(" ", "").equals("1")) {
                q = (int) q + 1;
                frac = false;
            }


            return !frac ? (int) q + ""
                    : ((int) q == 0 ? "" : (int) q + " ")
                    + new Fraction(q - ((int) q), .125, 10).toString().
                    replace(" ", "");
        } catch (FractionConversionException ex) {
            Logger.getLogger(FractionFormat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return q + "";
    }
 
    public static double toDecimal(String frac) {
        
        double r = 0.0;
        
        try {
            String[] n = frac.split(" ");

            //One part only
            if(n.length == 1) {
                if(frac.contains("/")) {
                    n = frac.split("/");
                    r = Double.parseDouble(n[0])/ Double.parseDouble(n[1]);
                } else {
                    r = Double.parseDouble(n[0]);
                }
                return r;
            }

            //two parts
            r = Double.parseDouble(n[0]);

            if(n.length == 2) {
                n = n[1].split("/");
                r += Double.parseDouble(n[0])/ Double.parseDouble(n[1]);
            }
        } catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException: " + frac);
        }
        
        return r;
    }
}
