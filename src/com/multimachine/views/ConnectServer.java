/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.multimachine.views;

import com.multimachine.beans.ConnectionInfo;
import com.multimachine.controller.SshController;
import com.multimachine.exception.GenericLoggerException;
import com.multimachine.listeners.SshMessageListener;
import com.multimachine.utils.SshCommandCode;
import com.multimachine.utils.StringHelper;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author hutchuk
 */
public class ConnectServer extends javax.swing.JDialog {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ConnectServer.class);
    ArrayList<ConnectionInfo> lstConnections;
    ArrayList<SshController> lstSshController;
    SshMessageListener sshMessageListener;
    static boolean flgInputgiven = false;
    static boolean flgbreakInput = false;
    static final int MAX_RETRIES = 3;
 String errMsg = "";
        String bashName = "";
         int errorCount = 0;

         Thread pwdThread=null;
    public String getErrMsg() {
        return errMsg;
    }

  

    public String getBashName() {
        return bashName;
    }

  
    public int getErrorCount() {
        return errorCount;
    }

    public SshMessageListener getSshMessageListener() {
        return sshMessageListener;
    }

    public void setSshMessageListener(SshMessageListener sshMessageListener) {
        this.sshMessageListener = sshMessageListener;
    }

    public ArrayList<SshController> getLstSshController() {
        return lstSshController;
    }

    public ArrayList<ConnectionInfo> getLstConnections() {
        return lstConnections;
    }

    public void setLstConnections(ArrayList<ConnectionInfo> lstConnections) {
        this.lstConnections = lstConnections;
    }

 

    /**
     * Creates new form ConnectServer
     */
    public ConnectServer(ArrayList<ConnectionInfo> lstConnections, SshMessageListener sshMessageListener,java.awt.Frame parent, boolean modal) {
        super(parent, modal);
         setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.lstConnections=lstConnections;
        this.sshMessageListener=sshMessageListener;
        initComponents();
         txtPassword.setRequestFocusEnabled(true);
        hideInput();
        lstSshController = new ArrayList<SshController>();
log.info("Opened");
connectAllServers();

    }

    public void showInput() {
        txtPassword.setText("");
        txtPassword.setVisible(true);
        btnOk.setVisible(true);
        btnCancel.setVisible(true);
        
       
txtPassword.requestFocus();
    }

    public void hideInput() {
      txtPassword.setVisible(false);
        btnOk.setVisible(false);
        btnCancel.setVisible(false);
        

    }

    public void appendText(final String txt) {
        
        
         new Thread() {
            public void run() {

                log.info(txt);

        txtConnectLog.append(txt + StringHelper.NEW_LINE);

            }
        }.start();
         
        

    }

    public String getPassword() {

        flgInputgiven = false;
        flgbreakInput = false;
     new Thread() {
            public void run() {

                showInput();

            }
        }.start();

     
        while (flgInputgiven == false && flgbreakInput == false) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                log.info(ex);
            }
        }

        if (flgInputgiven) {
            hideInput();
            return (txtPassword.getText());
        }
        return null;
    }

    public void connectAllServers(){
        
        final ConnectServer connectServer=this;
     new Thread() {
            public void run() {
log.info("Started threads");
                connectAllServersThreaded();
log.info("completed threads");
connectServer.setVisible(false);
log.info("visible  false");
//closeWindow();
            }
        }.start();
    
    }
    
    public void closeWindow() {
                WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
        }
    public void connectAllServersThreaded() {

     

         errMsg = "";
         bashName = "[";
errorCount=0;
        
        lstSshController = new ArrayList<SshController>();
        
 
        for (ConnectionInfo connectionInfo : lstConnections) {

            
            log.info("Starting connection" + connectionInfo.getProfileName() );

                SshController sshController = connectServer(connectionInfo);
                
               if(null == sshController){
                if (errMsg.equals("")) {
                    errMsg = "Errors in connecting to Servers ";
                }

                errMsg = errMsg + "[" + connectionInfo.getProfileName() + "]";
                
               errorCount++;
               }else{
                lstSshController.add(sshController);
                 bashName = bashName + " " + sshController.getConnectionInfo().getServerInfo().getHost() + " ";
               }
            
               

         

        }

       

        bashName = bashName + "] $";

       
    }
    public SshController connectServer(ConnectionInfo connectInfo) {

        int count = 0;

        appendText("Attempting to connect " + connectInfo.getServerInfo().getHost());

        while (count < MAX_RETRIES) {

            try {

                appendText("Using username " + connectInfo.getServerInfo().getUser());

                if (StringHelper.isEmpty(connectInfo.getServerInfo().getPassword())) {
                    appendText("Enter  password for " + connectInfo.getServerInfo().getUser() + "@" + connectInfo.getServerInfo().getHost());
                    String pwd = getPassword();
                    if (null == pwd) {
                        return null;
                    }
                    connectInfo.getServerInfo().setPassword(pwd);

                }

                  if (!StringHelper.isEmpty(connectInfo.getGatewayInfo().getHost())) {
                      
                      
                  
                appendText("Through Tunnel Gateway   " + connectInfo.getGatewayInfo().getHost() + " with tunnel user " + connectInfo.getGatewayInfo().getUser());
                if (StringHelper.isEmpty(connectInfo.getGatewayInfo().getPassword())) {
                    appendText("Enter  password for Tunnel " + connectInfo.getGatewayInfo().getUser() + "@" + connectInfo.getGatewayInfo().getHost());
                    String pwd = getPassword();
                    if (null == pwd) {
                        return null;
                    }
                    connectInfo.getGatewayInfo().setPassword(pwd);

                }
                }
                SshController sshController = new SshController(connectInfo, sshMessageListener);
                sshController.connect();

                return sshController;
                //bashName = bashName + " " + sshController.getConnectionInfo().getServerInfo().getHost() + " ";

            } catch (GenericLoggerException ex) {

                log.error("Exception while connecting to host " + connectInfo.getServerInfo().getHost(), ex);
                if (StringHelper.isEmpty(ex.getStatusCode()) == false) {

                    if (ex.getStatusCode().equals(SshCommandCode.CREDENTIAL_SERVER_ERROR)) {
                        appendText("Invalid credentials while connecting to Server " + connectInfo.getServerInfo().getHost());
                        connectInfo.getServerInfo().setPassword(null);
                    }

                    if (ex.getStatusCode().equals(SshCommandCode.CREDENTIAL_GATEWAY_ERROR)) {

                        appendText("Invalid credentials while connecting to Gateway " + connectInfo.getGatewayInfo().getHost());
                        connectInfo.getGatewayInfo().setPassword(null);
                    }

                } else {
                    return null;
                }

            }
            count++;
        }

        return null;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        txtPassword = new javax.swing.JPasswordField();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtConnectLog = new javax.swing.JTextArea();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        txtPassword.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtPassword.setToolTipText("");
        txtPassword.setDragEnabled(true);

        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        txtConnectLog.setEditable(false);
        txtConnectLog.setColumns(20);
        txtConnectLog.setLineWrap(true);
        txtConnectLog.setRows(5);
        jScrollPane1.setViewportView(txtConnectLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtPassword, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk)
                    .addComponent(btnCancel)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        // startConnections();
    }//GEN-LAST:event_formWindowOpened

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
    
        flgInputgiven = true;
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
    

        flgbreakInput = true;
    }//GEN-LAST:event_btnCancelActionPerformed

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
            java.util.logging.Logger.getLogger(ConnectServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConnectServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConnectServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConnectServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ConnectServer dialog = new ConnectServer(null,null,new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtConnectLog;
    private javax.swing.JPasswordField txtPassword;
    // End of variables declaration//GEN-END:variables
}
