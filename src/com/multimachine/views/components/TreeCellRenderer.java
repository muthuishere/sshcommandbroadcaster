/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.multimachine.views.components;

import com.multimachine.beans.ConnectionInfo;
import com.multimachine.beans.Settings;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author hutchuk
 */
public class TreeCellRenderer extends DefaultTreeCellRenderer {
    
     private Settings settings = null;
    public TreeCellRenderer( Settings settings){
    this.settings=settings;
    }
   public boolean isLeaf(String name){
   
         for (ConnectionInfo connectionInfo : settings.getConnectionInfo()) {
             if(connectionInfo.getOnlyProfileName().equals(name)){
                return true;
             }
               

            }
         return false;
   }

   
   private String getPath() {

       // if(null == lstServerProfiles.getSelectionPath() || null == lstServerProfiles.getSelectionPath().getLastPathComponent())
         //   return "";
        
        DefaultMutableTreeNode selected = null;//(DefaultMutableTreeNode) lstServerProfiles.getSelectionPath().getLastPathComponent();

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

   
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            // decide what icons you want by examining the node
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                if (node.getUserObject() instanceof String && isLeaf((String)node.getUserObject())) {
                    // your root node, since you just put a String as a user obj                    
               
                    setIcon(UIManager.getIcon("Tree.leafIcon"));
                    
                   
                    
                }else{
                  if(expanded)
                     setIcon(UIManager.getIcon("Tree.openIcon"));                    
                    else
                        setIcon(UIManager.getIcon("Tree.closedIcon"));
                }
            }

            return this;
        }

    
}
