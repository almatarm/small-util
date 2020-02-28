package org.smallutil.common;

/*
 * OS.java
 *
 * Created on December 19, 2006, 1:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



/**
 * @author matamh0a
 */
public enum OS {

    SUN(1), //2^0
    LINUX(2), //2^1
    WINDOWS(4), //2^2
    MAC(8), //2^3
    UNKNOWN(16);//2^4

    private int id;

    OS(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
