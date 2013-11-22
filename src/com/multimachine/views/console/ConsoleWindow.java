/* 
 * Copyright 2013 Muthukumaran (https://github.com/muthuishere/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.multimachine.views.console;

import com.multimachine.beans.ProfileStyle;
import com.multimachine.controller.ConsoleController;
import com.multimachine.listeners.MultiMessageBroadcaster;
import com.multimachine.utils.StringHelper;
import com.multimachine.views.MultiMainDialogue;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.apache.log4j.Logger;

/**
 *
 * @author hutchuk
 */
public class ConsoleWindow extends javax.swing.JFrame {

    private MultiMessageBroadcaster consoleCallBack;

    private boolean executing = false;

    public boolean isExecuting() {
        return executing;
    }

    public void setExecuting(boolean executing) {
        this.executing = executing;
    }
    private int lastKey = 0;
    String prompt;
    int commandIndex = 0;
    private ArrayList<ProfileStyle> lstProfileStyles = null;

    private static final Logger log = Logger.getLogger(ConsoleWindow.class);

    public ConsoleWindow(MultiMessageBroadcaster consoleCallBack, ArrayList<ProfileStyle> lstProfileStyles) {
        this("", consoleCallBack, lstProfileStyles);

    }

    /**
     * Creates new form SwingConsoleWindow
     */
    public ConsoleWindow(String initmessage, MultiMessageBroadcaster consoleCallBack, ArrayList<ProfileStyle> lstProfileStyles) {
        initComponents();
        URL iconURL = getClass().getResource("/com/multimachine/resources/red/16x16/app.png");
        // iconURL is null when not found
        ImageIcon icon = new ImageIcon(iconURL);
        this.setIconImage(icon.getImage());
        this.setAlwaysOnTop(true);
        
        this.consoleCallBack = consoleCallBack;
        // 
        // this.prompt = shellName + ">";

        if (!StringHelper.isEmpty(initmessage)) {
            appendText(initmessage);
        }
        this.lstProfileStyles = lstProfileStyles;

        String tmptitle = "[";
        int i = 0;
        for (ProfileStyle profileStyle : lstProfileStyles) {

            if (i != 0) {
                tmptitle = tmptitle + ",";
            }

            tmptitle = tmptitle + profileStyle.getHostName();
            i++;
            StyledDocument doc = txtAreaConsole.getStyledDocument();
            Style style = txtAreaConsole.addStyle(profileStyle.getHostName(), null);
            StyleConstants.setForeground(style, profileStyle.getColor());

        }

        tmptitle = tmptitle + "]";
        this.setTitle(tmptitle);

        showPrompt(false);

        setDocumentFilter(true);
    }

    @ConsoleCommand("clear")
    public void clearTxt() {
        log.info("This method is written to clear");

        new Thread() {
            public void run() {

                setDocumentFilter(false);
                txtAreaConsole.setText("");
                showPrompt(false);

                setDocumentFilter(true);
            }
        }.start();
    }

    @ConsoleCommand("exit")
    public void exitWindow() {
        log.info("This method is written to exit");

        //this.consoleCallBack.showParent();
        this.processWindowEvent(
                new WindowEvent(
                this, WindowEvent.WINDOW_CLOSING));
        
            //MultiMainDialogue.start();

    }

    public void sendCommand(final String cmd) {

        new Thread() {
            public void run() {
                setExecuting(true);
                consoleCallBack.broadcastCommand(cmd);

            }
        }.start();

    }

    public void setDocumentFilter(boolean flgEnable) {

        if (flgEnable) {
            ((AbstractDocument) txtAreaConsole.getDocument()).setDocumentFilter(
                    new CommandFilter(this));
        } else {
            ((AbstractDocument) txtAreaConsole.getDocument()).setDocumentFilter(
                    null);
        }
    }

   

    public void onResponse(final String msg, final String hostname,final boolean flgComplete) {

        new Thread() {
            public void run() {

                

                if(!StringHelper.isEmpty(msg)){
                
                    String curMsg=msg;
                    if(!flgComplete)
                      curMsg=curMsg+StringHelper.NEW_LINE;
                    
                            appendText(curMsg, hostname);
                }
                
                
                if(flgComplete){
                        setDocumentFilter(false);
                        txtAreaConsole.setEditable(true);
                        showPrompt(true);

                        setDocumentFilter(true);
                }
                log.info(msg + " displayed in window");
            }
        }.start();

    }

    public synchronized void appendText(String txt) {
        try {
            txtAreaConsole.getStyledDocument().insertString(txtAreaConsole.getDocument().getLength(), txt, null);
        } catch (BadLocationException ex) {
            java.util.logging.Logger.getLogger(ConsoleWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void appendText(String txt, String hostname) {
        try {

            setDocumentFilter(false);
            txtAreaConsole.setEditable(false);
            Style hostStyle = null;

            if (null != hostname) {
                hostStyle = txtAreaConsole.getStyle(hostname);
            }

            txtAreaConsole.getStyledDocument().insertString(txtAreaConsole.getDocument().getLength(), txt, hostStyle);

        } catch (BadLocationException ex) {
            //
        }
        txtAreaConsole.setCaretPosition(txtAreaConsole.getDocument().getLength());

        setDocumentFilter(true);

        // Define a default background color attribute
    }

    public void showPrompt(boolean flgNewLine) {

        setExecuting(false);
        String initmsg = "";
        if (flgNewLine) {
            initmsg = StringHelper.NEW_LINE;
        }

        appendText(initmsg + "[");
        prompt = "[";
        int i = 0;
        for (ProfileStyle profileStyle : lstProfileStyles) {

            if (i != 0) {
                appendText("," + profileStyle.getHostName(), profileStyle.getHostName());
                prompt = prompt + "," + profileStyle.getHostName();
            } else {
                appendText(profileStyle.getHostName(), profileStyle.getHostName());
                prompt = prompt + profileStyle.getHostName();
            }

            i++;
        }

        appendText("] $>");
        prompt = prompt + "] $>";
        // txtAreaConsole.getDocument().insertString(txtAreaConsole.getDocument().getLength(), txt, null);
        txtAreaConsole.setCaretPosition(txtAreaConsole.getDocument().getLength());
        txtAreaConsole.setEditable(true);
    }

    public MultiMessageBroadcaster getConsoleCallBack() {
        return consoleCallBack;
    }

    public void setConsoleCallBack(MultiMessageBroadcaster consoleCallBack) {
        this.consoleCallBack = consoleCallBack;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaConsole = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtAreaConsole.setBackground(new java.awt.Color(0, 0, 0));
        txtAreaConsole.setFont(new java.awt.Font("Arial Unicode MS", 1, 12)); // NOI18N
        txtAreaConsole.setForeground(new java.awt.Color(0, 204, 51));
        txtAreaConsole.setCaretColor(new java.awt.Color(255, 255, 255));
        txtAreaConsole.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAreaConsoleKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(txtAreaConsole);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtAreaConsoleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaConsoleKeyPressed
        // TODO add your handling code here:

        final int currentcode = evt.getKeyCode();

        if (lastKey == KeyEvent.VK_CONTROL && currentcode == KeyEvent.VK_B && isExecuting()) {
            log.info("Ctrl +b pressed , sending terminate signal");

            new Thread() {
                public void run() {

                    consoleCallBack.broadcastCommand(prompt);

                }
            }.start();

        }

        lastKey = currentcode;

    }//GEN-LAST:event_txtAreaConsoleKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ConsoleWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConsoleWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConsoleWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConsoleWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new ConsoleWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane txtAreaConsole;
    // End of variables declaration//GEN-END:variables
}
