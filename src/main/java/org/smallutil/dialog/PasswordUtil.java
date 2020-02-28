/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smallutil.dialog;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

/**
 *
 * @author matamh0a
 */
public class PasswordUtil {
    public static char[] showPasswordDialog(){
        final JPasswordField jpf = new JPasswordField();
        JOptionPane jop = new JOptionPane(jpf,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = jop.createDialog("Password:");
        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        jpf.requestFocusInWindow();
                    }
                });
            }
        });
        
        dialog.setVisible(true);
        jpf.requestFocus();
        int result = (Integer) jop.getValue();
        dialog.dispose();
        char[] password = null;
        if (result == JOptionPane.OK_OPTION) {
            password = jpf.getPassword();
        }
        return password;
    }
}
