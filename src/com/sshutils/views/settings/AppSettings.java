



package com.sshutils.views.settings;


import com.sshutils.beans.ConnectionInfo;
import com.sshutils.beans.Settings;
import com.sshutils.controller.SettingsController;
import com.sshutils.utils.ImportHelper;
import com.sshutils.utils.StringHelper;
import com.sshutils.utils.TreeViewHelper;
import com.sshutils.views.components.ImportFileFilter;
import com.sshutils.views.components.TreeCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

public class AppSettings extends javax.swing.JDialog implements TreeSelectionListener {

    private static final Logger log = Logger.getLogger(AppSettings.class);

    boolean flgEditEnabled = false;
    boolean flgDeleteEnabled = false;
TreeCellRenderer treeCellRenderer=null;
    private DefaultTreeModel treeModel;

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;

  
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;
  TreeViewHelper treeViewHelper =null;
    private Settings settings = null;
    private SettingsController settingsController = null;

    /**
     * Creates new form SettingsMain
     */
    public AppSettings(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

         treeViewHelper = new TreeViewHelper();

        progressbar.setVisible(false);
        URL iconURL = getClass().getResource("/com/sshutils/resources/red/16x16/app.png");
        // iconURL is null when not found
        ImageIcon icon = new ImageIcon(iconURL);
        this.setIconImage(icon.getImage());
        this.setTitle("Servers");
        settingsController = new SettingsController();
        settings = settingsController.retrieveSettingsFromFile();
        lstServerProfiles.removeAll();
        
          DefaultMutableTreeNode root = new DefaultMutableTreeNode("");

        // Create the tree model and add the root node to it
        treeModel = new DefaultTreeModel(root);
        
        lstServerProfiles.setModel(treeModel);
        treeCellRenderer=new TreeCellRenderer(settings);
        lstServerProfiles.setCellRenderer(treeCellRenderer);
        
        if (null != settings && null != settings.getConnectionInfo()) {

            for (ConnectionInfo connectionInfo : settings.getConnectionInfo()) {

              

                   treeViewHelper.addNode(treeModel,connectionInfo.getProfileName());
        
                
                  
            }
            treeModel.reload();
//
//             treeViewHelper.addNode( "Node 1/Node 2/Node 3/Node 4");
//         treeViewHelper.addNode( "Node 2/Node 3/Node 5");
//        treeViewHelper.addNode( "Node 1/Node 2/Node 3/Node 6");
//         treeViewHelper.addNode("Node 1/Node 2/Node 4/Node 5");
//         treeViewHelper.addNode( "Node 1/Node 1/Node 3/Node 5");
//         
        }


        lstServerProfiles.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        lstServerProfiles.addTreeSelectionListener(this);

        //lstServerProfiles.addTreeSelectionListener(listSelectionListener);
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

   
    
    
    public void showError(String msg, String title) {

        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);

    }

    public void showMsg(String msg, String title) {

        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);

    }

    private String getSelectedPath() {

        if(null == lstServerProfiles.getSelectionPath() || null == lstServerProfiles.getSelectionPath().getLastPathComponent())
            return "";
        
        DefaultMutableTreeNode selected = (DefaultMutableTreeNode) lstServerProfiles.getSelectionPath().getLastPathComponent();

        String xpath = "";
        while (selected.getParent() != null) {
            int index = 1;
            String tag = selected.toString();
            DefaultMutableTreeNode selected2 = selected;
            while ((selected2 = selected2.getPreviousSibling()) != null) {
                if (tag.equals(selected2.toString())) {
                    index++;
                }
            }

            //xpath = "/" + tag + "[" + index + "]" + xpath;
            xpath = "/" + tag + xpath;

            if (selected.getParent() == null) {
                selected = null;
            } else {
                selected = (DefaultMutableTreeNode) selected.getParent();
            }
        }
        if (null != xpath && xpath.length() > 0) {
            //remove the first / , as we need to neglect
            xpath = xpath.substring(1);
        }
        return xpath;
    }

    public void valueChanged(TreeSelectionEvent e) {
//Returns the last path element of the selection.
//This method is useful only when the selection model allows a single selection.
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) lstServerProfiles.getLastSelectedPathComponent();

        
        if (node == null) //Nothing is selected.     
        {
            return;
        }

         flgEditEnabled = false;
         flgDeleteEnabled=true;
        Object nodeInfo = node.getUserObject();
        if (node.isLeaf() && node.getUserObject() instanceof String && treeCellRenderer.isLeaf((String)node.getUserObject())) {

            
            

            flgEditEnabled = true;

        } else {
            if(StringHelper.isEmpty(getSelectedPath())){
                 flgDeleteEnabled=false;
            }
            System.out.println("is folder:" + getSelectedPath());

        }

        btnEdit.setEnabled(flgEditEnabled);
        btnDelete.setEnabled(flgDeleteEnabled);
    }

    public ConnectionInfo getConnectInfoForProfile(String  profileName) {

        if (null != settings && null != settings.getConnectionInfo()) {

            for(ConnectionInfo connectionInfo: settings.getConnectionInfo()){
            
                if(connectionInfo.getProfileName().equals(profileName))
                    return connectionInfo;
            }

        }
        return null;
    }

    private boolean removeConnectionInfo(String  profileName) {

        if (null != settings && null != settings.getConnectionInfo()) {

            for(int i=0;i<settings.getConnectionInfo().size();i++){
                ConnectionInfo connectionInfo=settings.getConnectionInfo().get(i);
            if(connectionInfo.getProfileName().equals(profileName)){
                    settings.getConnectionInfo().remove(i);
                    return true;
                    }
            
            }
            

          
        }
  return false;
    }

    public boolean confirmMsg(String msg, String title) {

        int dialogresult = JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_OPTION);
        if (dialogresult == JOptionPane.YES_OPTION) {
            return true;
        }
        return false;
    }

    TreeSelectionListener listSelectionListener = new TreeSelectionListener() {
        public void valueChanged(TreeSelectionEvent listSelectionEvent) {

            boolean adjust = listSelectionEvent.isAddedPath();

            if (!adjust) {
                JList list = (JList) listSelectionEvent.getSource();

                log.info("Selected value" + list.getSelectedValue());

                boolean flgEnabled = (null == list.getSelectedValue()) ? false : true;

                btnEdit.setEnabled(flgEnabled);
                btnDelete.setEnabled(flgEnabled);
            }
        }
    };

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
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnImport = new javax.swing.JButton();
        progressbar = new javax.swing.JProgressBar();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstServerProfiles = new javax.swing.JTree();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sshutils/resources/blue/24x24/add_notes.png"))); // NOI18N
        btnAdd.setToolTipText("Add Server Profile");
        btnAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sshutils/resources/blue/24x24/edit_notes.png"))); // NOI18N
        btnEdit.setToolTipText("Edit Server Profile");
        btnEdit.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sshutils/resources/grey/24x24/edit_notes.png"))); // NOI18N
        btnEdit.setEnabled(false);
        btnEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sshutils/resources/blue/24x24/delete_notes.png"))); // NOI18N
        btnDelete.setToolTipText("Delete Server Profile");
        btnDelete.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sshutils/resources/grey/24x24/delete_notes.png"))); // NOI18N
        btnDelete.setEnabled(false);
        btnDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sshutils/resources/winscp.png"))); // NOI18N
        btnImport.setText("Import");
        btnImport.setToolTipText("Import from Winscp");
        btnImport.setVerifyInputWhenFocusTarget(false);
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(lstServerProfiles);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(progressbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnImport)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
                                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(44, 44, 44))))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(progressbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(okButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed

        settingsController.saveSettingsasXml(settings);
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:

        new Thread() {
            public void run() {
                String profileFolder="";
if(!flgEditEnabled)
    profileFolder=getSelectedPath();
                ServerDetails dialog = new ServerDetails("Add Server", profileFolder,null, new javax.swing.JFrame(), true);

                dialog.setVisible(true);

                if (dialog.getReturnStatus() == ServerDetails.RET_OK) {

                    log.info("Returned Ok" + dialog.getConnectionInfo().toString());
                    settings.getConnectionInfo().add(dialog.getConnectionInfo());
                    
                       treeViewHelper.addNode(treeModel,dialog.getConnectionInfo().getProfileName());
        
                  treeModel.reload();
                  
                  //  refreshModel();
                } else {

                    log.info("Returned Cancel");
                }

            }
        }.start();
    }//GEN-LAST:event_btnAddActionPerformed

    
    public void removeSelected(){
    
     TreePath[] paths = lstServerProfiles.getSelectionPaths();
                for (TreePath path : paths) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    if (node.getParent() != null) {
                        treeModel.removeNodeFromParent(node);
                    }
                }
                   treeModel.reload();
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        new Thread() {
            public void run() {

                ConnectionInfo connectionInfo = getConnectInfoForProfile( getSelectedPath() );

                ServerDetails dialog = new ServerDetails("Edit Server" + StringHelper.defaultString(getSelectedPath() ), connectionInfo, new javax.swing.JFrame(), true);

                dialog.setVisible(true);

                if (dialog.getReturnStatus() == ServerDetails.RET_OK) {

                    
                    log.info("Returned Ok");
                     removeSelected();
                    connectionInfo = dialog.getConnectionInfo();
                   
                      treeViewHelper.addNode(treeModel,dialog.getConnectionInfo().getProfileName());
        
                  treeModel.reload();
                   
                    //  listServers.addElement(dialog.getConnectionInfo().getProfileName());
                } else {

                    log.info("Returned Cancel");
                }

            }
        }.start();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        new Thread() {
            public void run() {

                if (confirmMsg("Do you want to remove Server profile " + getSelectedPath() + "?", "Confirm Delete")) {

                    
                    removeConnectionInfo(getSelectedPath());
                     removeSelected();
                    //listServers.remove(lstServerProfiles.getSelectedIndex());
                    //  listServers.addElement(dialog.getConnectionInfo().getProfileName());
                } else {

                    log.info("Returned Cancel");
                }

            }
        }.start();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        // TODO add your handling code here:

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new ImportFileFilter());
        if (fileChooser.showOpenDialog(AppSettings.this) == JFileChooser.APPROVE_OPTION) {
            log.info(fileChooser.getSelectedFile().getAbsolutePath());
            String filename = fileChooser.getSelectedFile().getAbsolutePath();

            ArrayList<ConnectionInfo> lsttmpConnections;
            try {
                progressbar.setVisible(true);
                progressbar.setValue(25);
                lsttmpConnections = ImportHelper.importWinscp(filename);
                progressbar.setValue(75);
                if (null == lsttmpConnections || lsttmpConnections.size() == 0) {
                    throw new Exception("No server details Identified in file");
                }
                for (ConnectionInfo connectInfo : lsttmpConnections) {

                    progressbar.setValue(progressbar.getValue() + 1);
                    settings.getConnectionInfo().add(connectInfo);
                   // listServers.addElement(connectInfo.getProfileName());
                    treeViewHelper.addNode(treeModel,connectInfo.getProfileName());
                }
            treeModel.reload();
                progressbar.setValue(100);

            } catch (Exception ex) {
                log.error(ex);
                showError("Unable to Import settings !!" + ex.getMessage(), "Error");

            } finally {
                progressbar.setVisible(false);

            }
            // load from file
        }

    }//GEN-LAST:event_btnImportActionPerformed

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
            java.util.logging.Logger.getLogger(AppSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AppSettings dialog = new AppSettings(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnImport;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTree lstServerProfiles;
    private javax.swing.JButton okButton;
    private javax.swing.JProgressBar progressbar;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
    
    
    
}
