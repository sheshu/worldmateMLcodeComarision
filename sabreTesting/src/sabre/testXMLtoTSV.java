package sabre;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class testXMLtoTSV {
	public static void main(String args[]) {
		System.out.println("FlightXmlParsing STARTED");
		FileReader reader = null;
		FileWriter writer =null;
		try {
			
			String inputFileName = "d:/xml/4808610.xml";
			String outputFileName="d:/xml/output_mul.txt";
			reader=new FileReader(inputFileName);

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder =  builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(new InputSource(reader));
			XPath xPath =  XPathFactory.newInstance().newXPath();
                        String expProviderName = "//flight//provider-details//name";
			NodeList providerNameNodeList = (NodeList) xPath.compile(expProviderName).evaluate(xmlDocument, XPathConstants.NODESET);
			
			List<String> providerNameList = null;
			if(null != providerNameNodeList && providerNameNodeList.getLength()>0){
				providerNameList = new ArrayList<String>();
				for(int i=0;i<providerNameNodeList.getLength();i++){
					String responseCode = providerNameNodeList.item(i).getTextContent().trim();
					System.out.println("Number:"+responseCode);
					providerNameList.add(responseCode);
				}
			}else{
                                providerNameList.add("  ");
                        }
			String expression = "//details//@number";
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
			StringBuilder sb = new StringBuilder();
			List<String> numberList = null;
			if(null != nodeList && nodeList.getLength()>0){
				numberList = new ArrayList<String>();
				for(int i=0;i<nodeList.getLength();i++){
					String responseCode = nodeList.item(i).getTextContent().trim();
					System.out.println("Number:"+responseCode);
					numberList.add(responseCode);
				}
			}else{
                                numberList.add("  ");
                        }
			String expression1 = "//details//@airline-code";
			NodeList nodeList1= (NodeList) xPath.compile(expression1).evaluate(xmlDocument, XPathConstants.NODESET);
			List<String> airLineCodeList = null;
			if(null != nodeList1 && nodeList1.getLength()>0){
				airLineCodeList = new ArrayList<String>();
				for(int i=0;i<nodeList1.getLength();i++){
					String responseCode = nodeList1.item(i).getTextContent().trim();
					System.out.println("Airline Code:"+responseCode);
					airLineCodeList.add(responseCode);
				}
			}else{
                                airLineCodeList.add("  ");
                        }
                        String expression2 = "//flight//provider-details//name";
			NodeList nodeList2 = (NodeList) xPath.compile(expression2).evaluate(xmlDocument, XPathConstants.NODESET);
			List<String> numberList2 = null;
			if(null != nodeList2 && nodeList2.getLength()>0){
				numberList2 = new ArrayList<String>();
				for(int i=0;i<nodeList2.getLength();i++){
					String responseCode = nodeList2.item(i).getTextContent().trim();
					System.out.println("Number:"+responseCode);
					numberList2.add(responseCode);
				}
			}else{
                                numberList2.add("  ");
                        }
			String expression3 = "//flight//departure//airport-code";
			NodeList nodeList3= (NodeList) xPath.compile(expression3).evaluate(xmlDocument, XPathConstants.NODESET);
			List<String> airLineCodeList3 = null;
			if(null != nodeList3 && nodeList3.getLength()>0){
				airLineCodeList3 = new ArrayList<String>();
				for(int i=0;i<nodeList3.getLength();i++){
					String responseCode = nodeList3.item(i).getTextContent().trim();
					System.out.println("departure airport Code:"+responseCode);
					airLineCodeList3.add(responseCode);
				}
			}else{
                                airLineCodeList3.add("  ");
                        }
			if(null != numberList && null != airLineCodeList && airLineCodeList.size()>0 && numberList.size()>0 && null != numberList2 && null != airLineCodeList3 && airLineCodeList.size()>0 && numberList2.size()>0 ){
				for(int i=0;i<numberList.size();i++){
					sb.append(numberList.get(i));
					sb.append("\t");
					sb.append(airLineCodeList.get(i));
                                        sb.append("\t");
                                        sb.append(numberList2.get(i));
                                        sb.append("\t");
					sb.append(airLineCodeList3.get(i));                                        
					sb.append("\n");
				}
			}
			writer = new FileWriter(outputFileName);
			writer.write(sb.toString());
			writer.close();
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if(null != reader)
					reader.close();
				if(null != writer)
					writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		System.out.println("FlightXmlParsing FINISHED");
	}
}
