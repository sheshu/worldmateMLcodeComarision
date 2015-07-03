/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sabre;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import sabre.jaxb.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.codehaus.jackson.JsonFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 *
 * @author Sheshadri
 */
public class ComparisionTest {

    public static void main(String args[]) throws JAXBException, FileNotFoundException, IOException {
        //READ WORLDMATE XML FILE 
        JAXBContext worldmateJaxbContext = JAXBContext.newInstance(WorldmateParsingResult.class);
        Unmarshaller unmarshaller = worldmateJaxbContext.createUnmarshaller();

        String worldmateHeading[] = new String[]{"File Name", "Worldmate Supplier Name", "ML Supplier Name", "Matching Status",
            "Worldmate Supplier Code", "ML Supplier Cod", "Matching Status",
            "Worldmate Supplier Flight Number", "ML Supplier Flight Number", "Matching Status",
            "Worldmate Departure  Airport Code", "ML Departure  Airport Code", "Matching Status",
            "Worldmate Arrival Airport Code", "ML Arrival Airport Code", "Matching Status",
            "Worldmate Departure Airport Name", "ML Departure Airport Name", "Matching Status",
            "Worldmate Arrival Airport Name", "ML Arrival Airport Name", "Matching Status",
            "Worldmate DateTime of Departure(LOCAL)", "ML DateTime of Departure(LOCAL)", "Matching Status",
            "Worldmate DateTime of Arrival(LOCAL)", "ML DateTime of Arrival(LOCAL)", "Matching Status", "Accuracy %", "Final Status"};

        try {

            String tabSpace = "\t";
            StringBuilder worldmateStringBuilder = new StringBuilder();

            String outputFileName = "d:/office/sabre/sravn_data/Finaloutput.tsv";
            String inputWMDirectoryPath = "D:\\office\\sabre\\sravn_data\\Worldmate-xml";
            String inputMLDirectoryPath = "D:\\office\\sabre\\sravn_data\\Motivity\\";

            File fianlTestfile = new File(outputFileName);
            /* CREATE HEADER*/
            if (!fianlTestfile.exists()) {

                for (int i = 0; i < worldmateHeading.length; i++) {
                    worldmateStringBuilder.append(worldmateHeading[i] + tabSpace);
                }
                worldmateStringBuilder.append("\n");
            }

            //GET JSON CURRENT TOKEN
            //READ JSON FILE AND CREATE JSON PARSER
            FileWriter writer = null;
            //GET ALL FILES FORM WORLDMATE FILE DIRECORTY
            File worldmateFileDirectory = new File(inputWMDirectoryPath);
            File[] worldmateFlieList = worldmateFileDirectory.listFiles();

            boolean flightDetailsmatch = false;
            //COMPARE WITH ML DATA WITH WORLDMATE DATA START
            for (File file : worldmateFlieList) {
                System.out.println(file);
                String worldmateFileName = file.getName().substring(0, file.getName().lastIndexOf('.'));
                //System.out.println(worldmateFileName);

                JsonFactory mlJsonFactory = new MappingJsonFactory();
                File mlInputFile = new File(inputMLDirectoryPath + worldmateFileName + ".json");
                if (!mlInputFile.exists()) {

                } else {
                    JsonParser mlJsonPasrer = mlJsonFactory.createJsonParser(new File(mlInputFile.toString()));
                    JsonToken current = mlJsonPasrer.nextToken();
                    WorldmateParsingResult wordmateCollection = (WorldmateParsingResult) unmarshaller.unmarshal(new File(file.toString()));
                    List<ItineraryItem> itenaryItemList = wordmateCollection.getItems().getFlightOrCarRentalOrHotelReservation();
                    while (mlJsonPasrer.nextToken() != JsonToken.END_OBJECT) {
                        String fieldName = mlJsonPasrer.getCurrentName();
                        // move from field name to field value
                        current = mlJsonPasrer.nextToken();
                        if (fieldName.equals("itinerary")) {
                            if (current == JsonToken.START_ARRAY) {

                                while (mlJsonPasrer.nextToken() != JsonToken.END_ARRAY) {
                                    // For each of the records in the array
                                    JsonNode node = mlJsonPasrer.readValueAsTree();
                                    for (int index = 0; index < itenaryItemList.size(); index++) {
                                        if (itenaryItemList.get(index) instanceof Flight) {
                                            Flight worldmateFlightItenary = (Flight) itenaryItemList.get(index);
                                            // read the record into a tree model,
                                            // this moves the parsing position to the end of it
                                            // And now we have random access to everything in the object
                                            if (worldmateFlightItenary.getDetails().getNumber() == node.get("flight_number").asInt() && flightDetailsmatch == false) {
                                                flightDetailsmatch = true;
                                                int accurancy = 0;
                                                String matchingStatus;
                                                //1
                                                worldmateStringBuilder.append(worldmateFileName).append(tabSpace);
                                                //2
                                                try {
                                                    String supplierName = "".equals(node.get("supplier").get("name").asText()) ? "null" : node.get("supplier").get("name").asText();
                                                    String wMSupplierName = getValue(worldmateFlightItenary.getProviderDetails().getName());
                                                    String replaceSupplierAirName = replaceSupplierAirline(wMSupplierName);
                                                   
                                                    worldmateStringBuilder.append(replaceSupplierAirName).append(tabSpace).append(supplierName).append(tabSpace);
                                                    matchingStatus = matchingStatus(replaceSupplierAirName, node.get("supplier").get("name").asText());
                                                    worldmateStringBuilder.append(matchingStatus).append(tabSpace);
                                                    if ("Perfect Match".equals(matchingStatus)) {
                                                        accurancy++;
                                                        matchingStatus = "";
                                                    }
                                                } catch (NullPointerException e) {
                                                    String wMData = worldmateFlightItenary.getProviderDetails().getName() == null ? "null" : worldmateFlightItenary.getProviderDetails().getName();
                                                    worldmateStringBuilder.append(wMData).append(tabSpace).append("null").append(tabSpace).append("Not Match").append(tabSpace);
                                                }
                                                //3
                                                try {
                                                    String supplierAirlineCode = "".equals(node.get("supplier").get("code").asText()) ? "null" : node.get("supplier").get("code").asText();
                                                    String wMSupplierAirlineCode = getValue(worldmateFlightItenary.getDetails().getAirlineCode());
                                                    worldmateStringBuilder.append(wMSupplierAirlineCode).append(tabSpace).append(supplierAirlineCode).append(tabSpace);
                                                    matchingStatus = matchingStatus(wMSupplierAirlineCode, node.get("supplier").get("code").asText());
                                                    worldmateStringBuilder.append(matchingStatus).append(tabSpace);
                                                    if ("Perfect Match".equals(matchingStatus)) {
                                                        accurancy++;
                                                        matchingStatus = "";
                                                    }
                                                } catch (NullPointerException e) {
                                                    String wMData = worldmateFlightItenary.getDetails().getAirlineCode() == null ? "null" : worldmateFlightItenary.getDetails().getAirlineCode();
                                                    worldmateStringBuilder.append(wMData).append(tabSpace).append("null").append(tabSpace).append("Not Match").append(tabSpace);
                                                }
                                                //4
                                                worldmateStringBuilder.append(worldmateFlightItenary.getDetails().getNumber()).append(tabSpace).append(node.get("flight_number").asInt()).append(tabSpace).append("Perfect Match").append(tabSpace);
                                                matchingStatus = "Perfect Match";
                                                if ("Perfect Match".equals(matchingStatus)) {
                                                    accurancy++;
                                                    matchingStatus = "";
                                                }
                                                //5
                                                try {
                                                    String originCode = "".equals(node.get("origin").get("code").asText()) ? "null" : node.get("origin").get("code").asText();
                                                    String wMDepartureCode = getValue(worldmateFlightItenary.getDeparture().getAirportCode());

                                                    worldmateStringBuilder.append(wMDepartureCode).append(tabSpace).append(originCode).append(tabSpace);
                                                    matchingStatus = matchingStatus(wMDepartureCode, node.get("origin").get("code").asText());
                                                    worldmateStringBuilder.append(matchingStatus).append(tabSpace);
                                                    if ("Perfect Match".equals(matchingStatus)) {
                                                        accurancy++;
                                                        matchingStatus = "";
                                                    }
                                                } catch (NullPointerException e) {
                                                    String wMData = worldmateFlightItenary.getDeparture().getAirportCode() == null ? "null" : worldmateFlightItenary.getDeparture().getAirportCode();
                                                    worldmateStringBuilder.append(wMData).append(tabSpace).append("null").append(tabSpace).append("Not Match").append(tabSpace);
                                                }
                                                try {
                                                    String destinationCode = "".equals(node.get("destination").get("code").asText()) ? "null" : node.get("destination").get("code").asText();
                                                    String wMArrivalCode = getValue(worldmateFlightItenary.getArrival().getAirportCode());

                                                    worldmateStringBuilder.append(wMArrivalCode).append(tabSpace).append(destinationCode).append(tabSpace);
                                                    matchingStatus = matchingStatus(worldmateFlightItenary.getArrival().getAirportCode(), node.get("destination").get("code").asText());
                                                    worldmateStringBuilder.append(matchingStatus).append(tabSpace);
                                                    if ("Perfect Match".equals(matchingStatus)) {
                                                        accurancy++;
                                                        matchingStatus = "";
                                                    }
                                                } catch (NullPointerException e) {
                                                    String wMData = worldmateFlightItenary.getArrival().getAirportCode() == null ? "null" : worldmateFlightItenary.getArrival().getAirportCode();
                                                    worldmateStringBuilder.append(wMData).append(tabSpace).append("null").append(tabSpace).append("Not Match").append(tabSpace);
                                                }
                                                //6
                                                try {
                                                    String originName = "".equals(node.get("origin").get("name").asText()) ? "null" : node.get("origin").get("name").asText();
                                                    String wMDepartureName = getValue(worldmateFlightItenary.getDeparture().getName());
                                                    String replaceOriginName = replaceAirline(wMDepartureName);
                                                    worldmateStringBuilder.append(replaceOriginName).append(tabSpace).append(originName).append(tabSpace);
                                                    matchingStatus = matchingStatus(replaceOriginName, node.get("origin").get("name").asText());
                                                    worldmateStringBuilder.append(matchingStatus).append(tabSpace);
                                                    if ("Perfect Match".equals(matchingStatus)) {
                                                        accurancy++;
                                                        matchingStatus = "";
                                                    }
                                                } catch (NullPointerException e) {
                                                    String wMData = worldmateFlightItenary.getDeparture().getName() == null ? "null" : worldmateFlightItenary.getDeparture().getName();
                                                    worldmateStringBuilder.append(wMData).append(tabSpace).append("null").append(tabSpace).append("Not Match").append(tabSpace);
                                                }
                                                //7
                                                try {
                                                    String destinationName = "".equals(node.get("destination").get("name").asText()) ? "null" : node.get("destination").get("name").asText();
                                                    String wMArrivalName = getValue(worldmateFlightItenary.getArrival().getName());
                                                    String replaceArrivalName = replaceAirline(wMArrivalName);
                                                    worldmateStringBuilder.append(replaceArrivalName).append(tabSpace).append(destinationName).append(tabSpace);
                                                    matchingStatus = matchingStatus(replaceArrivalName, node.get("destination").get("name").asText());
                                                    worldmateStringBuilder.append(matchingStatus).append(tabSpace);
                                                    if ("Perfect Match".equals(matchingStatus)) {
                                                        accurancy++;
                                                        matchingStatus = "";
                                                    }
                                                } catch (NullPointerException e) {
                                                    String wMData = worldmateFlightItenary.getArrival().getName() == null ? "null" : worldmateFlightItenary.getArrival().getName();
                                                    worldmateStringBuilder.append(wMData).append(tabSpace).append("null").append(tabSpace).append("Not Match").append(tabSpace);
                                                }
                                                //8
                                                try {
                                                    String depatrureDate = "".equals(node.get("departure_date").asText()) ? "null" : node.get("departure_date").asText();
                                                    String wMDepatureDate = getValue(worldmateFlightItenary.getDeparture().getLocalDateTime().toString());

                                                    worldmateStringBuilder.append(wMDepatureDate).append(tabSpace).append(depatrureDate).append(tabSpace);
                                                    matchingStatus = matchingStatus(worldmateFlightItenary.getDeparture().getLocalDateTime().toString().replace(".000Z", ""), node.get("departure_date").asText().replace(".000+0000", ""));
                                                    worldmateStringBuilder.append(matchingStatus).append(tabSpace);
                                                    if ("Perfect Match".equals(matchingStatus)) {
                                                        accurancy++;
                                                        matchingStatus = "";
                                                    }
                                                } catch (NullPointerException e) {
                                                    String wMData = worldmateFlightItenary.getDeparture().getLocalDateTime() == null ? "null" : worldmateFlightItenary.getDeparture().getLocalDateTime().toString();
                                                    worldmateStringBuilder.append(wMData).append(tabSpace).append("null").append(tabSpace).append("Not Match").append(tabSpace);
                                                }
                                                //9
                                                try {
                                                    String arrivalDate = "".equals(node.get("arrival_date").asText()) ? "null" : "";
                                                    String wMArrivalDate = getValue(worldmateFlightItenary.getArrival().getLocalDateTime().toString());
                                                    
                                                    worldmateStringBuilder.append(wMArrivalDate).append(tabSpace).append(arrivalDate).append(tabSpace);
                                                    matchingStatus = matchingStatus(worldmateFlightItenary.getArrival().getLocalDateTime().toString().replace(".000Z", ""), node.get("arrival_date").asText().replace(".000+0000", ""));
                                                    worldmateStringBuilder.append(matchingStatus).append(tabSpace);
                                                    if ("Perfect Match".equals(matchingStatus)) {
                                                        accurancy++;
                                                        matchingStatus = "";
                                                    }
                                                } catch (NullPointerException e) {
                                                    String wMData = worldmateFlightItenary.getArrival().getLocalDateTime() == null ? "null" : worldmateFlightItenary.getArrival().getLocalDateTime().toString();
                                                    worldmateStringBuilder.append(wMData).append(tabSpace).append("null").append(tabSpace).append("Not Match").append(tabSpace);
                                                }

                                                int total = 9;
                                                Float finalStatus = accurencyStatus(accurancy, total);
                                                worldmateStringBuilder.append(String.format("%.0f%%", finalStatus)).append(tabSpace).append(accurencyStatus(finalStatus)).append("\n");

                                            } 

                                        }

                                    }
                                    flightDetailsmatch = false;
                                }

                            } else {
                                System.out.println("Error: records should be an array: skipping.");
                                mlJsonPasrer.skipChildren();
                            }
                        } else {
                            mlJsonPasrer.skipChildren();
                        }
                    }
                    //END
                }
            }
            writer = new FileWriter(outputFileName, true);
            writer.write(worldmateStringBuilder.toString());
            writer.close();
        } catch (IOException | JAXBException | NullPointerException e) {
            System.out.println(e);
        }

    }

    public static String matchingStatus(String wData, String mData) {
        String matchingStatus;

        if (wData.toLowerCase().equals(mData.toLowerCase())) {
            matchingStatus = "Perfect Match";
        } else {
            matchingStatus = "Not Match";
        }
        return matchingStatus;
    }

    public static Float accurencyStatus(int totalsuccess, int total) {

        Float x = (float) totalsuccess;
        Float y = (float) total;

        Float num = (float) (100 / y * x);
        return num;

    }

    public static String accurencyStatus(Float accurency) {

        String finalStatus = "";
        if (accurency == 0.0f) {
            finalStatus = "Not Match";
        }
        if (accurency < 100.0f) {
            finalStatus = "Partial Success";
        }
        if (accurency == 100.0f) {
            finalStatus = "Success";
        }
        return finalStatus;

    }

    public static String getValue(String inputValue) {
        return inputValue == null ? "null" : inputValue;
    }
    
    public static String replaceAirline(String wmateAirname){
        String csvFile = "D:/office/sabre/python/airport_map.csv";
        BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
        String replaceAirline = wmateAirname;
        try {
                
		br = new BufferedReader(new FileReader(csvFile));
		while ((line = br.readLine()) != null) {
		        // use comma as separator
			String[] airportNames = line.split(cvsSplitBy);
                        String data = airportNames[1];
                        if(replaceAirline.equals(data)){                            
                            replaceAirline = airportNames[2];
                            break;
                        }
 
		}
 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
        return replaceAirline;
    }
    public static String replaceSupplierAirline(String wmateSupplierAirname){
        String csvFile = "D:/office/sabre/sravn_data/supplier_map.csv";
        BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
        String replaceAirline = wmateSupplierAirname;
        try {
                
		br = new BufferedReader(new FileReader(csvFile));
		while ((line = br.readLine()) != null) {
		        // use comma as separator
			String[] supplierAirpotNames = line.split(cvsSplitBy);
                        String data = supplierAirpotNames[1];
                        if(replaceAirline.equals(data)){
                            replaceAirline = supplierAirpotNames[2];
                            break;
                        }
 
		}
 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
        return replaceAirline;
    }

}
