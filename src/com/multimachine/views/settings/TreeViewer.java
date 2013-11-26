/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.multimachine.views.settings;
import java.io.StringReader;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;


public class TreeViewer extends DefaultHandler {

  private Stack nodes;
  
  
  
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
    public static TreeNode generateTreeNode(String xml) {
            try {
      XMLReader parser = XMLReaderFactory.createXMLReader(
        "org.apache.xerces.parsers.SAXParser"
      );
      
     
      TreeViewer handler = new TreeViewer();
      parser.setContentHandler(handler);
    
      
    InputSource inputSource=  new InputSource(new StringReader(xml));
        parser.parse(inputSource);
        
            return handler.getRoot();
   
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
      ContentHandler handler = new TreeViewer();
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
   
} // end TreeViewer