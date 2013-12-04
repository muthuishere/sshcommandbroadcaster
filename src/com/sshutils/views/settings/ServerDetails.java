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
package com.sshutils.views.settings;

import com.sshutils.beans.ConnectionInfo;
import com.sshutils.beans.ServerInfo;
import com.sshutils.utils.StringHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.apache.log4j.Logger;


public class ServerDetails extends javax.swing.JDialog {

    private static final Logger log = Logger.getLogger(ServerDetails.class);
    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;

    private ConnectionInfo connectionInfo = null;

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

      public ServerDetails(String title, ConnectionInfo connectionInfo, java.awt.Frame parent, boolean modal) {
          
          this(title,"",connectionInfo,parent,modal);
      }
    /**
     * Creates new form ServerDetails
     */
    public ServerDetails(String title, String profileFolder,ConnectionInfo connectionInfo, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setTitle(title);
        initComponents();
        URL iconURL = getClass().getResource("/com/sshutils/resources/red/16x16/app.png");
        // iconURL is null when not found
        ImageIcon icon = new ImageIcon(iconURL);
        this.setIconImage(icon.getImage());
        this.connectionInfo = connectionInfo;
        resetError();

        log.info("Components" + this.jTabbedPane1.getTabCount());
        this.jTabbedPane1.setEnabledAt(1, false);
        tabTunnel.setEnabled(false);

        
        if(!StringHelper.isEmpty(profileFolder)){
           txtFolder.setText(profileFolder);
        }
        if (null != connectionInfo) {

            
            
            txtOnlyProfileName.setText(connectionInfo.getOnlyProfileName());
            txtFolder.setText(connectionInfo.getFolderName());
            if (null != connectionInfo.getServerInfo()) {

                txtServerHostName.setText(connectionInfo.getServerInfo().getHost());
                txtServerPassword.setText(connectionInfo.getServerInfo().getPassword());
                txtServerUserName.setText(connectionInfo.getServerInfo().getUser());

                cmbServerPort.setText(connectionInfo.getServerInfo().getPort() + "");

            }
            if (null != connectionInfo.getGatewayInfo() && StringHelper.isEmpty(connectionInfo.getGatewayInfo().getHost()) == false) {
                //Enable tunnel

                txtTunnelHostName.setText(connectionInfo.getGatewayInfo().getHost());
                txtTunnelPassword.setText(connectionInfo.getGatewayInfo().getPassword());
                txtTunnelUserName.setText(connectionInfo.getGatewayInfo().getUser());

                cmbTunnelPort.setText(connectionInfo.getGatewayInfo().getPort() + "");

                this.jTabbedPane1.setEnabledAt(1, true);
                this.chkUseTunnel.setSelected(true);
            }

        } else {

            this.connectionInfo = new ConnectionInfo();
        }

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }

    public void resetError() {

        new Thread() {
            public void run() {

                lblError.setText("");

            }
        }.start();

    }

    public void updateProfileName() {

        new Thread() {
            public void run() {

                if (StringHelper.isEmpty(txtOnlyProfileName.getText())) {

                    if (!StringHelper.isEmpty(txtServerHostName.getText()) && !StringHelper.isEmpty(txtServerUserName.getText())) {

                        txtOnlyProfileName.setText(txtServerUserName.getText() + "@" + txtServerHostName.getText());
                    }

                }

            }
        }.start();

    }

    public void setError(String txt) {

        lblError.setText("* " + txt);
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        tabHost = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtServerHostName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtServerUserName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtServerPassword = new javax.swing.JPasswordField();
        chkUseTunnel = new javax.swing.JCheckBox();
        cmbServerPort = new javax.swing.JTextField();
        tabTunnel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtTunnelHostName = new javax.swing.JTextField();
        txtTunnelUserName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtTunnelPassword = new javax.swing.JPasswordField();
        jLabel8 = new javax.swing.JLabel();
        cmbTunnelPort = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtOnlyProfileName = new javax.swing.JTextField();
        lblError = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtFolder = new javax.swing.JTextField();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        okButton.setLabel("Save");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jLabel1.setText("Host Name");

        jLabel2.setText("Port");

        txtServerHostName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtServerHostNameFocusLost(evt);
            }
        });
        txtServerHostName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtServerHostNameKeyPressed(evt);
            }
        });

        jLabel3.setText("UserName");

        txtServerUserName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtServerUserNameFocusLost(evt);
            }
        });
        txtServerUserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtServerUserNameKeyPressed(evt);
            }
        });

        jLabel4.setText("Password");

        txtServerPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtServerPasswordKeyPressed(evt);
            }
        });

        chkUseTunnel.setText("Use Tunnel");
        chkUseTunnel.setToolTipText("");
        chkUseTunnel.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkUseTunnelItemStateChanged(evt);
            }
        });
        chkUseTunnel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkUseTunnelActionPerformed(evt);
            }
        });

        cmbServerPort.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbServerPort.setText("22");
        cmbServerPort.setToolTipText("");

        javax.swing.GroupLayout tabHostLayout = new javax.swing.GroupLayout(tabHost);
        tabHost.setLayout(tabHostLayout);
        tabHostLayout.setHorizontalGroup(
            tabHostLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabHostLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(tabHostLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabHostLayout.createSequentialGroup()
                        .addGroup(tabHostLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txtServerUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                        .addGroup(tabHostLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(tabHostLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(128, 128, 128))
                            .addComponent(txtServerPassword))
                        .addGap(39, 39, 39))
                    .addGroup(tabHostLayout.createSequentialGroup()
                        .addGroup(tabHostLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabHostLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtServerHostName))
                        .addGap(18, 18, 18)
                        .addGroup(tabHostLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(cmbServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31))
                    .addGroup(tabHostLayout.createSequentialGroup()
                        .addComponent(chkUseTunnel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        tabHostLayout.setVerticalGroup(
            tabHostLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabHostLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabHostLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabHostLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtServerHostName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(chkUseTunnel)
                .addGap(18, 18, 18)
                .addGroup(tabHostLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(tabHostLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtServerUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabHostLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtServerPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("App Server", tabHost);

        tabTunnel.setVerifyInputWhenFocusTarget(false);

        jLabel5.setText("Host Name");

        txtTunnelHostName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTunnelHostNameKeyPressed(evt);
            }
        });

        txtTunnelUserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTunnelUserNameKeyPressed(evt);
            }
        });

        jLabel6.setText("UserName");

        jLabel7.setText("Password");

        txtTunnelPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTunnelPasswordKeyPressed(evt);
            }
        });

        jLabel8.setText("Port");

        cmbTunnelPort.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbTunnelPort.setText("22");
        cmbTunnelPort.setToolTipText("");

        javax.swing.GroupLayout tabTunnelLayout = new javax.swing.GroupLayout(tabTunnel);
        tabTunnel.setLayout(tabTunnelLayout);
        tabTunnelLayout.setHorizontalGroup(
            tabTunnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabTunnelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabTunnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tabTunnelLayout.createSequentialGroup()
                        .addGroup(tabTunnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(txtTunnelUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                        .addGroup(tabTunnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7)
                            .addComponent(txtTunnelPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(tabTunnelLayout.createSequentialGroup()
                        .addGroup(tabTunnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabTunnelLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtTunnelHostName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tabTunnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(cmbTunnelPort, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)))
                .addGap(49, 49, 49))
        );
        tabTunnelLayout.setVerticalGroup(
            tabTunnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabTunnelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabTunnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tabTunnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTunnelHostName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTunnelPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(71, 71, 71)
                .addGroup(tabTunnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(tabTunnelLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTunnelUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabTunnelLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTunnelPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Tunnel", tabTunnel);

        jLabel9.setText("ProfileName");

        txtOnlyProfileName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOnlyProfileNameKeyPressed(evt);
            }
        });

        lblError.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lblError.setForeground(new java.awt.Color(255, 0, 0));
        lblError.setText("jLabel10");

        jLabel10.setText("Folder");

        txtFolder.setToolTipText("Use / character for each folder");
        txtFolder.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFolderKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(txtFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(txtOnlyProfileName, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(lblError, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOnlyProfileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton)
                    .addComponent(lblError))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed

        //        
        new Thread() {
            public void run() {

                ServerInfo serverInfo = new ServerInfo();
                ServerInfo tunnelInfo = new ServerInfo();
                String errText = "";

                if (StringHelper.isEmpty(txtServerHostName.getText())) {
                    errText = "Server HostName cannot be Empty !";
                } else {
                    serverInfo.setHost(txtServerHostName.getText());
                }

                if (StringHelper.isEmpty(new String(txtServerPassword.getPassword()))) {
                    errText = "Server Password cannot be Empty !";
                } else {
                    serverInfo.setPassword(new String(txtServerPassword.getPassword()));
                }

                if (StringHelper.isEmpty(txtServerUserName.getText())) {
                    errText = "Server Username cannot be Empty !";
                } else {
                    serverInfo.setUser(txtServerUserName.getText());
                }

                if (StringHelper.isEmpty(txtOnlyProfileName.getText())) {
                    errText = "ProfileName cannot be Empty !";
                } else {
                    
                    String foldername=txtFolder.getText();
                    if(!StringHelper.isEmpty(foldername)){
                        
                        if(foldername.startsWith("/"))
                            foldername=foldername.substring(1);

                        if(!foldername.endsWith("/"))
                            foldername=foldername+ "/";
                    
                    }
                    connectionInfo.setProfileName(foldername+ txtOnlyProfileName.getText());
                }

                serverInfo.setPort(Integer.parseInt(cmbServerPort.getText()));

                if (chkUseTunnel.isSelected()) {

                    if (StringHelper.isEmpty(txtTunnelHostName.getText())) {
                        errText = "Tunnel HostName cannot be Empty !";
                    } else {
                        tunnelInfo.setHost(txtTunnelHostName.getText());
                    }

                    if (StringHelper.isEmpty(new String(txtTunnelPassword.getPassword()))) {
                        errText = "Tunnel Password cannot be Empty !";
                    } else {
                        tunnelInfo.setPassword(new String(txtTunnelPassword.getPassword()));
                    }

                    if (StringHelper.isEmpty(txtTunnelUserName.getText())) {
                        errText = "Tunnel Username cannot be Empty !";
                    } else {
                        tunnelInfo.setUser(txtTunnelUserName.getText());
                    }

                    tunnelInfo.setPort(Integer.parseInt(cmbTunnelPort.getText()));
                }

                if (!StringHelper.isEmpty(errText)) {

                    setError(errText);
                    return;
                }
                {

                    //Save the connectionInfo Object and save it in settings
                    connectionInfo.setServerInfo(serverInfo);
                    connectionInfo.setGatewayInfo(tunnelInfo);

                    doClose(RET_OK);

                }

            }
        }.start();

    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void chkUseTunnelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkUseTunnelActionPerformed
        // TODO add your handling code here:

        new Thread() {
            public void run() {
                //ServerDetails dialog = new ServerDetails("Add Server",new javax.swing.JFrame(), true);
            }
        }.start();
    }//GEN-LAST:event_chkUseTunnelActionPerformed

    private void chkUseTunnelItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkUseTunnelItemStateChanged
        // TODO add your handling code here:
        final int eventtype = evt.getStateChange();
        new Thread() {
            public void run() {

                if (eventtype == ItemEvent.SELECTED) {
                    jTabbedPane1.setEnabledAt(1, true);
                } else {
                    jTabbedPane1.setEnabledAt(1, false);
                }

            }
        }.start();

    }//GEN-LAST:event_chkUseTunnelItemStateChanged

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowOpened

    private void txtServerHostNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtServerHostNameKeyPressed
        // TODO add your handling code here:
        resetError();
    }//GEN-LAST:event_txtServerHostNameKeyPressed

    private void txtServerUserNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtServerUserNameKeyPressed
        // TODO add your handling code here:
        resetError();
    }//GEN-LAST:event_txtServerUserNameKeyPressed

    private void txtServerPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtServerPasswordKeyPressed
        // TODO add your handling code here:
        resetError();
    }//GEN-LAST:event_txtServerPasswordKeyPressed

    private void txtOnlyProfileNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOnlyProfileNameKeyPressed
        // TODO add your handling code here:
        resetError();
    }//GEN-LAST:event_txtOnlyProfileNameKeyPressed

    private void txtTunnelHostNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTunnelHostNameKeyPressed
        // TODO add your handling code here:
        resetError();
    }//GEN-LAST:event_txtTunnelHostNameKeyPressed

    private void txtTunnelUserNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTunnelUserNameKeyPressed
        // TODO add your handling code here:
        resetError();
    }//GEN-LAST:event_txtTunnelUserNameKeyPressed

    private void txtTunnelPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTunnelPasswordKeyPressed
        // TODO add your handling code here:
        resetError();
    }//GEN-LAST:event_txtTunnelPasswordKeyPressed

    private void txtServerHostNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtServerHostNameFocusLost
        // TODO add your handling code here:
        updateProfileName();

    }//GEN-LAST:event_txtServerHostNameFocusLost

    private void txtServerUserNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtServerUserNameFocusLost
        // TODO add your handling code here:
        updateProfileName();
    }//GEN-LAST:event_txtServerUserNameFocusLost

    private void txtFolderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFolderKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFolderKeyPressed

    private void doClose(int retStatus) {
        returnStatus = retStatus;

        setVisible(false);
        dispose();
    }

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
            java.util.logging.Logger.getLogger(ServerDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ServerDetails dialog = new ServerDetails("empty", null, new javax.swing.JFrame(), true);
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
    private javax.swing.JButton cancelButton;
    private javax.swing.JCheckBox chkUseTunnel;
    private javax.swing.JTextField cmbServerPort;
    private javax.swing.JTextField cmbTunnelPort;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblError;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel tabHost;
    private javax.swing.JPanel tabTunnel;
    private javax.swing.JTextField txtFolder;
    private javax.swing.JTextField txtOnlyProfileName;
    private javax.swing.JTextField txtServerHostName;
    private javax.swing.JPasswordField txtServerPassword;
    private javax.swing.JTextField txtServerUserName;
    private javax.swing.JTextField txtTunnelHostName;
    private javax.swing.JPasswordField txtTunnelPassword;
    private javax.swing.JTextField txtTunnelUserName;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
