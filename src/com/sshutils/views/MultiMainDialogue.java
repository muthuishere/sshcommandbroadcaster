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
package com.sshutils.views;

import com.sshutils.beans.CommandInfo;
import com.sshutils.beans.ConnectionInfo;
import com.sshutils.beans.ProfileStyle;
import com.sshutils.beans.Settings;
import com.sshutils.controller.ConsoleController;
import com.sshutils.controller.SettingsController;
import com.sshutils.controller.SshController;
import com.sshutils.exception.GenericLoggerException;
import com.sshutils.listeners.MultiMessageBroadcaster;
import com.sshutils.listeners.SshMessageListener;
import com.sshutils.utils.StringHelper;
import com.sshutils.views.components.CheckComboBox;
import com.sshutils.views.settings.AppSettings;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 *
 */
public final class MultiMainDialogue extends javax.swing.JDialog implements SshMessageListener, MultiMessageBroadcaster {

    //private  ConsoleController consoleController = null;
    private ConsoleController consoleController = null;
    ArrayList<SshController> lstSshController = null;
    ArrayList<CommandInfo> lstCommandInfo = null;
    Set<String> set = new HashSet<String>();
    Settings settings = null;
    ArrayList<Color> lstcolors = null;

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MultiMainDialogue.class);

    public void resetSettings(boolean flgreset) {
        SettingsController settingsController = new SettingsController();
        settings = settingsController.retrieveSettingsFromFile();

        set = new HashSet<String>();

        if (null != settings && null != settings.getConnectionInfo()) {

            for (ConnectionInfo connectionInfo : settings.getConnectionInfo()) {

                String profileName = connectionInfo.getOnlyProfileName();
                /*
                 if (set.contains(profileName)) {
                 profileName = modifyProfileName(set, profileName);
                 connectionInfo.setProfileName(profileName);
                 }
                
                 */
                set.add(profileName);

            }

        }

        if (flgreset) {
            cmbServerBox.resetObjs(set, false);
        }

    }

    /**
     * Creates new form MultiMainDialogue
     */
    public MultiMainDialogue(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setTitle("SSH-Command Broadcaster");

        resetSettings(false);
        
        
        cmbServerBox = new CheckComboBox(set);
        cmbServerBox.setVisible(true);

        initComponents();

        javax.swing.GroupLayout pnlServerLayout = (javax.swing.GroupLayout) pnlServer.getLayout();

        pnlServerLayout.setHorizontalGroup(
                pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlServerLayout.createSequentialGroup()
                .addComponent(cmbServerBox)
                .addGap(0, 250, Short.MAX_VALUE)));
        pnlServerLayout.setVerticalGroup(
                pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlServerLayout.createSequentialGroup()
                .addComponent(cmbServerBox)
                .addContainerGap()));

        lstcolors = new ArrayList<Color>();
        lstcolors.add(Color.BLUE);
        lstcolors.add(Color.GRAY);
        lstcolors.add(Color.MAGENTA);
        lstcolors.add(Color.CYAN);

        URL iconURL = getClass().getResource("/com/sshutils/resources/red/16x16/app.png");
        // iconURL is null when not found
        ImageIcon icon = new ImageIcon(iconURL);
        this.setIconImage(icon.getImage());

        if(set.size() == 0){
        
            ActionEvent e = new ActionEvent(btnSettings ,1234,"click");
            this.btnSettingsActionPerformed(e);
        
        }
    }

    public void showWarning(String msg, String title) {

        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);

    }

    public boolean confirmMsg(String msg, String title) {

        int dialogresult = JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_OPTION);
        if (dialogresult == JOptionPane.YES_OPTION) {
            return true;
        }
        return false;
    }

    public void showError(String msg, String title) {

        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);

    }

    public ConnectionInfo getConnectionInfo(String profileName) {

        for (ConnectionInfo connectionInfo : settings.getConnectionInfo()) {

            if (profileName.equals(connectionInfo.getOnlyProfileName())) {
                return connectionInfo;
            }
        }

        return null;
    }

    public String modifyProfileName(Set<String> profileSet, String profileName) {

        String newProfileName = "";
        int i = 1;
        do {

            newProfileName = profileName + "~" + i;
            i++;

        } while (profileSet.contains(newProfileName));

        return newProfileName;

    }

    public void broadcastCommand(final String cmd) {

        if (null == lstSshController || lstSshController.size() == 0) {
            return;
        }

        lstCommandInfo = new ArrayList<CommandInfo>();
        for (final SshController sshController : lstSshController) {

            new Thread() {
                public void run() {

                    CommandInfo commandInfo = new CommandInfo();
                    try {

                        commandInfo.setCmd(cmd);
                        //commandInfo.setConnectionInfo(sshController.getProfileName());

                        if (sshController.isExecuting()) {
                            sshController.terminateCommand();
                        } else {
                            sshController.execute(commandInfo);
                        }
                    } catch (GenericLoggerException ex) {
                        commandInfo.setError(true);
                        commandInfo.setErrMsg(ex.toString());
                        StringBuilder formattedResponse = new StringBuilder();

                        formattedResponse.append("[").append(sshController.getConnectionInfo().getServerInfo().getHost()).append("]");
                        formattedResponse.append(ex.toString()).append(StringHelper.NEW_LINE);
                        onReceiveExecuteMessage(commandInfo, true);

                    }

                }
            }.start();

        }

    }

    @Override
    public void onReceiveExecuteMessage(CommandInfo commandInfo, boolean flgComplete) {

       // log.info(commandInfo.toString());

        boolean flgCanEnd=false;
        
         if (flgComplete) {
            flgCanEnd=handleCommandComplete(commandInfo);
        }
        if (commandInfo.isBufferedOutput() && flgComplete) {
            //DOnt send as the out put is already sent as buffer
            commandInfo.setResponse(null);
              consoleController.sendResponse(commandInfo,flgCanEnd);
        } else {
            consoleController.sendResponse(commandInfo,flgCanEnd);
        }

       
    }

    public boolean handleCommandComplete(CommandInfo commandInfo) {

        lstCommandInfo.add(commandInfo);

        return (lstCommandInfo.size() == lstSshController.size()) ;

    }

    @Override
    public void onReceiveConnectionMessage(ConnectionInfo connectionInfo, final String msg) {

        new Thread() {
            public void run() {

              //  txtConnectConsole.append(msg + StringHelper.NEW_LINE);

            }
        }.start();

    }

    public void getColor(int index) {
    }

    
    public void connectAllServers() {

        if (cmbServerBox.getSelectedItems().length <= 0) {

            showWarning("Select a server to connect !", "No Servers Selected");
            return;
        }

      
//        ArrayList<ProfileStyle> lstProfileStyles = new ArrayList<ProfileStyle>();
        
      final  ArrayList<ConnectionInfo>  lstConnectionInfo = new ArrayList<ConnectionInfo>();

      
      final MultiMessageBroadcaster broadcaster=this;
      final SshMessageListener sshMessageListener=this;
      
        for (Object profileObj : cmbServerBox.getSelectedItems()) {

            String profileName = (String) profileObj;
            
                lstConnectionInfo.add(getConnectionInfo(profileName));
                


        }
        
          new Thread() {
            public void run() {

              //  txtConnectConsole.append(msg + StringHelper.NEW_LINE);
                
                  String errMsg = "";
        String bashName = "";

        int errorCount = 0;
        lstSshController = new ArrayList<SshController>();
ConnectServer connectServer=new ConnectServer(lstConnectionInfo,sshMessageListener,new javax.swing.JFrame(), true);


 connectServer.setVisible(true);
       //connectServer.connectAllServers();
       log.info("Starting again");
                  errMsg = connectServer.getErrMsg();
         bashName =connectServer.getBashName();

         errorCount = connectServer.getErrorCount();
         lstSshController=connectServer.getLstSshController();
         connectServer.setVisible(false);
         log.info("connectServer closed");
        
        if (cmbServerBox.getSelectedItems().length == errorCount || null == lstSshController || lstSshController.size() ==0 ) {

            showError("Unable to connect to any Servers, Modify through server settings and try again", "Error");
            return;
        }

        if (errorCount > 0) {

            String confMsg = errMsg + "Do you still want to continue with successfull connections?";

            if (!confirmMsg(confMsg, "Confirm")) {
                return;
            }

        }

        
        ArrayList<ProfileStyle> lstProfileStyles = new ArrayList<ProfileStyle>();
        int k=0;
        for(SshController sshController: lstSshController)
		 lstProfileStyles.add(new ProfileStyle(sshController.getConnectionInfo().getServerInfo().getHost(), lstcolors.get(k++)));
                         


        // Create a console Shell
        setModal(false);
        setVisible(false);

        consoleController = new ConsoleController(broadcaster, lstProfileStyles);

            }
        }.start();

          
    }
    @Override
    public void showParent() {
     
    
    }
    public void appendTxt(final String msg) {

        new Thread() {
            public void run() {

             //   txtConectConsole.append(msg + StringHelper.NEW_LINE);

            }
        }.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        pnlServer = new javax.swing.JPanel();
        btnConnect = new javax.swing.JButton();
        btnSettings = new javax.swing.JButton();
        btnSettings1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(java.awt.Color.white);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Servers");

        btnConnect.setText("Connect");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });

        btnSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sshutils/resources/red/16x16/network.png"))); // NOI18N
        btnSettings.setAutoscrolls(true);
        btnSettings.setFocusable(false);
        btnSettings.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettings.setMaximumSize(new java.awt.Dimension(51, 23));
        btnSettings.setMinimumSize(new java.awt.Dimension(51, 23));
        btnSettings.setName(""); // NOI18N
        btnSettings.setOpaque(false);
        btnSettings.setPreferredSize(new java.awt.Dimension(51, 23));
        btnSettings.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSettingsActionPerformed(evt);
            }
        });

        btnSettings1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sshutils/resources/red/16x16/question_mark.png"))); // NOI18N
        btnSettings1.setAutoscrolls(true);
        btnSettings1.setFocusable(false);
        btnSettings1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSettings1.setMaximumSize(new java.awt.Dimension(51, 23));
        btnSettings1.setMinimumSize(new java.awt.Dimension(51, 23));
        btnSettings1.setName(""); // NOI18N
        btnSettings1.setOpaque(false);
        btnSettings1.setPreferredSize(new java.awt.Dimension(51, 23));
        btnSettings1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSettings1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSettings1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlServerLayout = new javax.swing.GroupLayout(pnlServer);
        pnlServer.setLayout(pnlServerLayout);
        pnlServerLayout.setHorizontalGroup(
            pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlServerLayout.createSequentialGroup()
                .addContainerGap(245, Short.MAX_VALUE)
                .addComponent(btnConnect)
                .addGap(18, 18, 18)
                .addComponent(btnSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(btnSettings1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlServerLayout.setVerticalGroup(
            pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlServerLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(pnlServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSettings1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConnect))
                .addGap(0, 19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlServer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(pnlServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettingsActionPerformed
        // TODO add your handling code here:

        new Thread() {
            public void run() {

                AppSettings dialog = new AppSettings(new javax.swing.JFrame(), true);

                dialog.setVisible(true);

                log.info("Settings changed");
                resetSettings(true);
            }
        }.start();
    }//GEN-LAST:event_btnSettingsActionPerformed

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        // TODO add your handling code here:
        new Thread() {
            public void run() {

                connectAllServers();

            }
        }.start();
    }//GEN-LAST:event_btnConnectActionPerformed

    private void btnSettings1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettings1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSettings1ActionPerformed

    /**
     * @param args the command line arguments
     */
    
    public static void main(String args[]) {
        start();        
    }
    public static void start() {
        /* Set the Nimbus look and feel */

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {

              //  System.out.println("info.getName()" + info.getName());
                if ("Nowork".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MultiMainDialogue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MultiMainDialogue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MultiMainDialogue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MultiMainDialogue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MultiMainDialogue dialog = new MultiMainDialogue(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnSettings;
    private javax.swing.JButton btnSettings1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel pnlServer;
    // End of variables declaration//GEN-END:variables
 private CheckComboBox cmbServerBox;



}
