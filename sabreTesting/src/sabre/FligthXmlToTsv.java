/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sabre;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Sheshadri
 */
public class FligthXmlToTsv {
    
public static void main(String args[]) {
		
		String outputFileName="d:/office/sabre/outputs/Worldmate.tsv";
                String InputdirectoryName = "D:\\office\\sabre\\sravn_data\\Worldmate_response-xml";
                String output = saveXmlToTsv(InputdirectoryName,outputFileName);
                System.out.println(output+" Converted XML to TSV");
		
	}

    public static String saveXmlToTsv(String inputDirectory,String outputFileName){
                System.out.println("FlightXmlParsing STARTED");
                FileReader reader = null;
                FileWriter writer =null;
            try {
                        
			
                        String worldmateHeading[] = new String[]{"File Name", "Worldmate Supplier Name","Worldmate Supplier Code","Worldmate Supplier Flight Number","Worldmate Departure  Airport Code","Worldmate Arrival Airport Code","Worldmate Departure Airport Name","Worldmate Arrival Airport Name","Worldmate DateTime of Departure(LOCAL)","Worldmate DateTime of Arrival(LOCAL)"};
                        String expressions[] = new  String[]{"//flight//provider-details//name","//flight//details//@airline-code","//flight//details//@number","//flight//departure//airport-code","//flight//arrival//airport-code","//flight//departure//name","//flight//arrival//name","//flight//departure//local-date-time","//flight//arrival//local-date-time"};
                        int count = 0;
                        String getFlightDetails = "//items//flight";
                        String tabSpace = "\t";
                        /*
                        GET FILE NAME
                        */                        
                        
                        File directory = new File(inputDirectory);
                        
                        File[] fList = directory.listFiles();
                        StringBuilder sb = new StringBuilder();
                         /*
                        CREATE HEADER
                        */
                        for (int i = 0; i < worldmateHeading.length; i++) {
                           sb.append(worldmateHeading[i]+tabSpace);
                          }
                        sb.append("\n");   
                         
                        for (File file : fList){                        
                        System.out.println(file.getName());
                        String worldmateFileName=file.getName().substring(0,file.getName().lastIndexOf('.'));
			reader=new FileReader(inputDirectory+"\\"+file.getName());
                        System.out.println(inputDirectory+"\\"+file.getName());
                        
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder =  builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(new InputSource(reader));
			XPath xPath =  XPathFactory.newInstance().newXPath();
                        
                        NodeList flightList = (NodeList) xPath.compile(getFlightDetails).evaluate(xmlDocument, XPathConstants.NODESET);
                        
                        if(null != flightList && flightList.getLength()>0){
                         sb.append(worldmateFileName+tabSpace);
                            System.out.println("Count---"+count++);
                            for(int k=0;k<expressions.length;k++){
                                NodeList airLineNameList = (NodeList) xPath.compile(expressions[k]).evaluate(xmlDocument, XPathConstants.NODESET);

                                if(null != airLineNameList && airLineNameList.getLength()>0){
                                        String responseCode = airLineNameList.item(0).getTextContent().trim();
                                        System.out.println(responseCode);
                                        sb.append(responseCode+tabSpace);
                                }else{
                                    
                                    sb.append(" "+tabSpace);
                                }
                            }
                            sb.append("\n");
                        }
                    }
                        writer = new FileWriter(outputFileName,false);
                        
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
            return "success";
    }
}
