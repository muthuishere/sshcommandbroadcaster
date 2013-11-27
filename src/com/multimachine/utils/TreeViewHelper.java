/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.multimachine.utils;
import java.io.StringReader;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;


public class TreeViewHelper extends DefaultHandler {

  private Stack nodes;
  private DefaultTreeModel model = null;

    public DefaultTreeModel getModel() {
        return model;
    }
  
  public TreeViewHelper() {
        // Create the root node, I'm assuming that the delimited strings will have
        // different string value at index 0
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("");

        // Create the tree model and add the root node to it
        model = new DefaultTreeModel(root);


//        // Build the tree from the various string samples
       

       
    }
  
  
  
   /**
     * Builds a tree from a given forward slash delimited string.
     * 
     * @param model The tree model
     * @param str The string to build the tree from
     */
    public void removeNode(final String str) {
        // Fetch the root node
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        // Split the string around the delimiter
        String [] strings = str.split("/");

        // Create a node object to use for traversing down the tree as it 
        // is being created
        DefaultMutableTreeNode node = root;

        // Iterate of the string array
        for (String s: strings) {
            // Look for the index of a node at the current level that
            // has a value equal to the current string
            int index = childIndex(node, s);

            // Index less than 0, this is a new node not currently present on the tree
            if (index < 0) {
                // Add the new node
                DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(s);
                node.insert(newChild, node.getChildCount());
                node = newChild;
            }
            // Else, existing node, skip to the next string
            else {
                node = (DefaultMutableTreeNode) node.getChildAt(index);
            }
        }
    }
  
   /**
     * Builds a tree from a given forward slash delimited string.
     * 
     * @param model The tree model
     * @param str The string to build the tree from
     */
    public void addNode(final String str) {
        // Fetch the root node
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        // Split the string around the delimiter
        String [] strings = str.split("/");

        // Create a node object to use for traversing down the tree as it 
        // is being created
        DefaultMutableTreeNode node = root;

        // Iterate of the string array
        for (String s: strings) {
            // Look for the index of a node at the current level that
            // has a value equal to the current string
            int index = childIndex(node, s);

            // Index less than 0, this is a new node not currently present on the tree
            if (index < 0) {
                // Add the new node
                DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(s);
                node.insert(newChild, node.getChildCount());
                node = newChild;
            }
            // Else, existing node, skip to the next string
            else {
                node = (DefaultMutableTreeNode) node.getChildAt(index);
            }
        }
    }

    /**
     * Returns the index of a child of a given node, provided its string value.
     * 
     * @param node The node to search its children
     * @param childValue The value of the child to compare with
     * @return The index
     */
    private int childIndex(final DefaultMutableTreeNode node, final String childValue) {
        Enumeration<DefaultMutableTreeNode> children = node.children();
        DefaultMutableTreeNode child = null;
        int index = -1;

        while (children.hasMoreElements() && index < 0) {
            child = children.nextElement();

            if (child.getUserObject() != null && childValue.equals(child.getUserObject())) {
                index = node.getIndex(child);
            }
        }

        return index;
    }
    
  
  // Initialize the per-document data structures
  public void startDocument() throws SAXException {
    
    // The stack needs to be reinitialized for each document
    // because an exception might have interrupted parsing of a
    // previous document, leaving an unempty stack.
    nodes = new Stack();
    
  }
 
  // Make sure we always have the root element
  private TreeNode root;

  public TreeNode getRoot(){
  
  return root;
  }
    public static DefaultTreeModel generateTreeNode(String xml) {
            try {
      XMLReader parser = XMLReaderFactory.createXMLReader(
        "org.apache.xerces.parsers.SAXParser"
      );
      
     
      TreeViewHelper handler = new TreeViewHelper();
      parser.setContentHandler(handler);
    
      
    InputSource inputSource=  new InputSource(new StringReader(xml));
        parser.parse(inputSource);
        
            return new DefaultTreeModel(handler.getRoot());
   
    }
    catch (Exception e) {
      System.err.println(e);
    }
  return null;
  }   // end main()
    
     
    
   
 
  // Initialize the per-element data structures
  public void startElement(String namespaceURI, String localName,
   String qualifiedName, Attributes atts) {
  
    String data;
    if (namespaceURI.equals("")) data = localName;
    else {
      data = '{' + namespaceURI + "} " + qualifiedName;
    }
    MutableTreeNode node = new DefaultMutableTreeNode(data);
    try {
      MutableTreeNode parent = (MutableTreeNode) nodes.peek();
      parent.insert(node, parent.getChildCount()); 
    }
    catch (EmptyStackException e) {
      root = node; 
    }
    nodes.push(node);
   
  }
  
  public void endElement(String namespaceURI, String localName,
   String qualifiedName) {
    nodes.pop();
  }

  // Flush and commit the per-document data structures
  public void endDocument() {
    
   
    
  }
    

  public static void main(String[] args) {
      
    try {
      XMLReader parser = XMLReaderFactory.createXMLReader(
        "org.apache.xerces.parsers.SAXParser"
      );
      
      String str="<html>\n" +
"  <head>\n" +
"    <title>halo</title>\n" +
"    <style></style>\n" +
"    <body></body>\n" +
"  </head> \n" +
"  <head>\n" +
"    <title></title>\n" +
"    <style>vazhga</style>\n" +
"    <body></body>\n" +
"  </head>\n" +
"</html> ";
      ContentHandler handler = new TreeViewHelper();
      parser.setContentHandler(handler);
     // for (int i = 0; i < args.length; i++) {  
      
    InputSource inputSource=  new InputSource(new StringReader(str));
        parser.parse(inputSource);
      //}
    }
    catch (Exception e) {
      System.err.println(e);
    }
  
  }   // end main()
   
} // end TreeViewHelper