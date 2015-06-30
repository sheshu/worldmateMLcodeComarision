package com.example.parsing.xml;

import java.io.FileReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class FlightXmlParsing {
	public static void main(String args[]) throws Exception{
		System.out.println("FlightXmlParsing STARTED");
		FileReader reader=new FileReader("d:/office/sabre/4817049.xml");

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder =  builderFactory.newDocumentBuilder();
		Document xmlDocument = builder.parse(new InputSource(reader));
		XPath xPath =  XPathFactory.newInstance().newXPath();
		String expression = "//details//@number";
		NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
		if(null != nodeList && nodeList.getLength()>0){
			String responseCode = nodeList.item(0).getTextContent().trim();
			System.out.println("Number:"+responseCode);
		}
		String expression1 = "//details//@airline-code";
		NodeList nodeList1= (NodeList) xPath.compile(expression1).evaluate(xmlDocument, XPathConstants.NODESET);
		if(null != nodeList1 && nodeList1.getLength()>0){
			String responseCode = nodeList1.item(0).getTextContent().trim();
			System.out.println("Airline Code:"+responseCode);
		}
		System.out.println("FlightXmlParsing FINISHED");
	}
}
