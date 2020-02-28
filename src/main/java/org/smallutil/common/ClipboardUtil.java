/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smallutil.common;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MUFEED
 */
public class ClipboardUtil
{
    public static void copy(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection data = new StringSelection(text);
        clipboard.setContents(data, data);
    }


    public static String paste() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            return (String) clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException ex) {
            Logger.getLogger(ClipboardUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClipboardUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args) {
        ClipboardUtil.copy("Hello, World!");
        System.out.println(ClipboardUtil.paste());
    }
}
