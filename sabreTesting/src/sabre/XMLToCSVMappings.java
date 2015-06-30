/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sabre;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
 
import org.w3c.dom.Document;
 
public class XMLToCSVMappings {
 private static XPathFactory xPathFactory = null;
 private static DocumentBuilderFactory domFactory = null;
 
public static void main(String args[]) {
 
domFactory = DocumentBuilderFactory.newInstance();
 domFactory.setNamespaceAware(true);
 xPathFactory = XPathFactory.newInstance();
 TransformerFactory.newInstance();
 
ReadXML();
 }
 
public static void ReadXML() {
 System.out.println("In ReadXML method");
 File xmlFile = new File(
 "D:/java_exe/sample.xml");
 try {
 InputStream fis = new FileInputStream(xmlFile);
 if (fis != null) {
 Document xmlDoc = getDocFromXMLString(fis);
 HashMap<String, String> propertiesKeypair = readPropertyFile();
 FileWriter writer = new FileWriter("d:/java_exe/SampleXMLtoCSVFile.csv");
 writer.append("Key");
 writer.append(',');
 writer.append("Value");
 writer.append('\n');
 
for (Map.Entry<String, String> entry : propertiesKeypair
 .entrySet()) {
 System.out.println("Key : " + entry.getKey()
 + "Xpath value is::"
 + getElementValue(entry.getValue(), xmlDoc));
 
writer.append(entry.getKey());
 writer.append(',');
 writer.append(getElementValue(entry.getValue(), xmlDoc));
 writer.append('\n');
 }
 writer.flush();
 writer.close();
 System.out
 .println("ResultMap Updated. CSV File is being generated...");
 }
 } catch (FileNotFoundException e) {
 e.printStackTrace();
 } catch (Exception e) {
 e.printStackTrace();
 }
 }
 
public static Document getDocFromXMLString(InputStream xml)
 throws Exception {
 DocumentBuilder builder;
 Document doc;
 try {
 builder = domFactory.newDocumentBuilder();
 doc = builder.parse(xml);
 } catch (Exception exception) {
 throw exception;
 } finally {
 }
 return doc;
 }
 
public static String getElementValue(final String xpathExpression,
 final Document doc) {
 
String textValue = null;
 try {
 XPath xpath = xPathFactory.newXPath();
 textValue = xpath.evaluate(xpathExpression, doc);
 } catch (final XPathExpressionException xpathException) {
 xpathException.printStackTrace();
 }
 
return textValue;
 }
 
public static HashMap<String, String> readPropertyFile() {
 System.out.println("In readPropertyFile method");
 Properties prop = new Properties();
 InputStream input;
 HashMap<String, String> Propvals = new HashMap<String, String>();
 try {
 
input = XMLToCSVMappings.class
 .getResourceAsStream("JustProperties.properties");
 System.out.println("before load");
 prop.load(input);
 System.out.println("Property File Loaded Succesfully");
 Set<String> propertyNames = prop.stringPropertyNames();
 for (String Property : propertyNames) {
 Propvals.put(Property, prop.getProperty(Property));
 }
 System.out.println("HashMap generated::" + Propvals);
 } catch (FileNotFoundException e) {
 e.printStackTrace();
 } catch (IOException e) {
 e.printStackTrace();
 } catch (Exception e) {
 e.printStackTrace();
 }
 return Propvals;
 }
}