/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smallutil.text;

/**
 *
 * @author MUFEED
 */
public class TextUtil {

    /**
     * Removes the blank lines from <code>text</code>
     * @param text source text
     * @return <code>text</code> without the blank lines.
     */
    public static String removeBlankLines(String text) {
        StringBuilder buff = new StringBuilder();

        String[] lines = text.split("\n");
        for (int i= 0; i < lines.length; i++) {
            if (lines[i].trim().length() > 0) {
                buff.append(lines[i]).append("\n");
            }
        }
        return buff.toString();
    }
}
