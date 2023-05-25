package com.sinontech.tools.common;

import java.io.StringReader;   
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;   
import javax.xml.parsers.DocumentBuilderFactory;   
import org.w3c.dom.Document;   
import org.w3c.dom.Element;   
import org.w3c.dom.Node;   
import org.w3c.dom.NodeList;   
import org.xml.sax.InputSource;  

public class ReaderXmlForSAX {
 
	public static void main(String[] args){
		String text = "<?xml version=\"1.0\" encoding=\"GBK\"?><response><result>T</result><coopOrderNo>xxx</coopOrderNo><failCode>123</failCode ><failDesc>abc</failDesc></response>";
//		 String text ="<XML><A>123</A><B>whl123</B><C>亮亮</C><D>1</D><E>1</E><F>165074</F><G>贫穷</G><H>1698.0</H><I>初级士官</I><J>湖南</J><K>常德</K><L>1</L></XML>"  ;
		 text="<?xml version=\"1.0\" encoding=\"GBK\"?><response><result>T</result><coopOrderNo>2413067552254066</coopOrderNo><failCode></failCode><failDesc></failDesc></response>";
	            
	         long begin = System.currentTimeMillis();   
	        Map map =   parse( text );   
	        System.out.println(map.toString());
	         long after = System.currentTimeMillis();   
	          System.out.println("DOM用时"+(after-begin)+"毫秒");   
	         }   
	       
	    public static Map<String,String> parse(String protocolXML) {   
	            Map<String,String> map = new HashMap<String,String>();
	        try {   
	             DocumentBuilderFactory factory = DocumentBuilderFactory   
	                     .newInstance();   
	             DocumentBuilder builder = factory.newDocumentBuilder();   
	             Document doc = builder   
	                     .parse(new InputSource(new StringReader(protocolXML.trim())));   
	  
	             Element root = doc.getDocumentElement();   
	             NodeList books = root.getChildNodes();   
	            if (books != null) {   
	                for (int i = 0; i < books.getLength(); i++) {   
	                     Node book = books.item(i);   
	                     if(null!=book.getFirstChild()){
	                     System.out.println("节点=" + book.getNodeName() + "\ttext="  
	                             + book.getFirstChild().getNodeValue());   
	                     map.put(book.getNodeName(), book.getFirstChild().getNodeValue());
	                     }
	                 }   
	             }   
	         } catch (Exception e) {   
	             e.printStackTrace();   
	         } 
	         return map;
	     }   
	}

 
