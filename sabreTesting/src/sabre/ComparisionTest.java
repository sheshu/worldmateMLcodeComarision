/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sabre;

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
        WorldmateParsingResult wordmateCollection = (WorldmateParsingResult) unmarshaller.unmarshal(new File("D:\\office\\sabre\\sravn_data\\Worldmate_response-xml\\4817086.xml"));
        String worldmateHeading[] = new String[]{"Worldmate Supplier Name", "ML Supplier Name", "Matching Status",
            "Worldmate Supplier Code", "ML Supplier Cod", "Matching Status",
            "Worldmate Supplier Flight Number", "ML Supplier Flight Number", "Matching Status",
            "Worldmate Departure  Airport Code", "ML Departure  Airport Code", "Matching Status",
            "Worldmate Arrival Airport Code", "ML Arrival Airport Code", "Matching Status",
            "Worldmate Departure Airport Name", "ML Departure Airport Name", "Matching Status",
            "Worldmate Arrival Airport Name", "ML Arrival Airport Name", "Matching Status",
            "Worldmate DateTime of Departure(LOCAL)", "ML DateTime of Departure(LOCAL)", "Matching Status",
            "Worldmate DateTime of Arrival(LOCAL)", "ML DateTime of Arrival(LOCAL)", "Matching Status", "Accuracy %", "Final Status"};

        //READ JSON FILE AND CREATE JSON PARSER
        JsonFactory mlJsonFactory = new MappingJsonFactory();
        JsonParser mlJsonPasrer = mlJsonFactory.createJsonParser(new File("d:\\office\\sabre\\4817086.json"));
        try {
            List<ItineraryItem> itenaryItemList = wordmateCollection.getItems().getFlightOrCarRentalOrHotelReservation();
            String tabSpace = "\t";
            StringBuilder worldmateStringBuilder = new StringBuilder();

            /* CREATE HEADER*/
            String outputFileName = "d:/office/sabre/outputs/Finaloutput.tsv";
            File file = new File(outputFileName);
            if (!file.exists()) {

                for (int i = 0; i < worldmateHeading.length; i++) {
                    worldmateStringBuilder.append(worldmateHeading[i] + tabSpace);
                }
                worldmateStringBuilder.append("\n");
            }
            //GET JSON CURRENT TOKEN
            JsonToken current = mlJsonPasrer.nextToken();
            FileWriter writer = null;

            Boolean flightDetailsmatch = false;
            //COMPARE WITH ML DATA WITH WORLDMATE DATA START
            while (mlJsonPasrer.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = mlJsonPasrer.getCurrentName();
                // move from field name to field value
                current = mlJsonPasrer.nextToken();
                if (fieldName.equals("itinerary")) {
                    if (current == JsonToken.START_ARRAY) {
                        // For each of the records in the array
                        for (int index = 0; index < itenaryItemList.size(); index++) {
                            if (itenaryItemList.get(index) instanceof Flight) {
                                Flight worldmateFlightItenary = (Flight) itenaryItemList.get(index);
                                while (mlJsonPasrer.nextToken() != JsonToken.END_ARRAY) {
                                    // read the record into a tree model,
                                    // this moves the parsing position to the end of it
                                    JsonNode node = mlJsonPasrer.readValueAsTree();
                                    // And now we have random access to everything in the object

                                    if (worldmateFlightItenary.getDetails().getNumber() == node.get("flight_number").asInt() && flightDetailsmatch == false) {
                                        flightDetailsmatch = true;
                                        int accurancy = 0;
                                        String matchingStatus;
                                        worldmateStringBuilder.append(worldmateFlightItenary.getProviderDetails().getName() + tabSpace);
                                        worldmateStringBuilder.append(node.get("supplier").get("name").asText() + tabSpace);
                                        matchingStatus = matchingStatus(worldmateFlightItenary.getProviderDetails().getName(), node.get("supplier").get("name").asText());
                                        worldmateStringBuilder.append(matchingStatus + tabSpace);
                                        if (matchingStatus == "Perfect Match") {
                                            accurancy++;
                                            matchingStatus = "";
                                        }

                                        worldmateStringBuilder.append(worldmateFlightItenary.getDetails().getAirlineCode() + tabSpace);
                                        worldmateStringBuilder.append(node.get("supplier").get("code").asText() + tabSpace);
                                        matchingStatus = matchingStatus(worldmateFlightItenary.getDetails().getAirlineCode(), node.get("supplier").get("code").asText());
                                        worldmateStringBuilder.append(matchingStatus + tabSpace);
                                        if (matchingStatus == "Perfect Match") {
                                            accurancy++;
                                            matchingStatus = "";
                                        }

                                        worldmateStringBuilder.append(worldmateFlightItenary.getDetails().getNumber() + tabSpace);
                                        worldmateStringBuilder.append(node.get("flight_number").asInt() + tabSpace);
                                        worldmateStringBuilder.append("Perfect Match" + tabSpace);
                                        matchingStatus = "Perfect Match";
                                        if (matchingStatus == "Perfect Match") {
                                            accurancy++;
                                            matchingStatus = "";
                                        }

                                        worldmateStringBuilder.append(worldmateFlightItenary.getDeparture().getAirportCode() + tabSpace);
                                        worldmateStringBuilder.append(node.get("origin").get("code").asText() + tabSpace);
                                        matchingStatus = matchingStatus(worldmateFlightItenary.getDetails().getAirlineCode(), node.get("supplier").get("code").asText());
                                        worldmateStringBuilder.append(matchingStatus + tabSpace);
                                        if (matchingStatus == "Perfect Match") {
                                            accurancy++;
                                            matchingStatus = "";
                                        }
                                        worldmateStringBuilder.append(worldmateFlightItenary.getArrival().getAirportCode() + tabSpace);
                                        worldmateStringBuilder.append(node.get("destination").get("code").asText() + tabSpace);
                                        matchingStatus = matchingStatus(worldmateFlightItenary.getArrival().getAirportCode(), node.get("destination").get("code").asText());
                                        worldmateStringBuilder.append(matchingStatus + tabSpace);
                                        if (matchingStatus == "Perfect Match") {
                                            accurancy++;
                                            matchingStatus = "";
                                        }
                                        worldmateStringBuilder.append(worldmateFlightItenary.getDeparture().getName() + tabSpace);
                                        worldmateStringBuilder.append(node.get("origin").get("name").asText() + tabSpace);
                                        matchingStatus = matchingStatus(worldmateFlightItenary.getDeparture().getName(), node.get("origin").get("name").asText());
                                        worldmateStringBuilder.append(matchingStatus + tabSpace);
                                        if (matchingStatus == "Perfect Match") {
                                            accurancy++;
                                            matchingStatus = "";
                                        }
                                        worldmateStringBuilder.append(worldmateFlightItenary.getArrival().getName() + tabSpace);
                                        worldmateStringBuilder.append(node.get("destination").get("name").asText() + tabSpace);
                                        matchingStatus = matchingStatus(worldmateFlightItenary.getArrival().getName(), node.get("destination").get("name").asText());
                                        worldmateStringBuilder.append(matchingStatus + tabSpace);
                                        if (matchingStatus == "Perfect Match") {
                                            accurancy++;
                                            matchingStatus = "";
                                        }
                                        worldmateStringBuilder.append(worldmateFlightItenary.getDeparture().getLocalDateTime() + tabSpace);
                                        worldmateStringBuilder.append(node.get("departure_date").asText() + tabSpace);
                                        matchingStatus = matchingStatus(worldmateFlightItenary.getDeparture().getLocalDateTime().toString().replace(".000Z", ""), node.get("departure_date").asText().replace(".000+0000", ""));
                                        worldmateStringBuilder.append(matchingStatus + tabSpace);
                                        if (matchingStatus == "Perfect Match") {
                                            accurancy++;
                                            matchingStatus = "";
                                        }
                                        worldmateStringBuilder.append(worldmateFlightItenary.getArrival().getLocalDateTime() + tabSpace);
                                        worldmateStringBuilder.append(node.get("arrival_date").asText() + tabSpace);
                                        matchingStatus = matchingStatus(worldmateFlightItenary.getArrival().getLocalDateTime().toString().replace(".000Z", ""), node.get("arrival_date").asText().replace(".000+0000", ""));
                                        worldmateStringBuilder.append(matchingStatus + tabSpace);
                                        if (matchingStatus == "Perfect Match") {
                                            accurancy++;
                                            matchingStatus = "";
                                        }
                                        int total = 9;
                                        Float finalStatus = accurencyStatus(accurancy, total);
                                        worldmateStringBuilder.append(String.format("%.0f%%", finalStatus) + tabSpace);
                                        worldmateStringBuilder.append(accurencyStatus(finalStatus));
                                        worldmateStringBuilder.append("\n");

                                        break;
                                    }
                                }
                            }
                            if (flightDetailsmatch == false) {
                                System.out.println("Flight details not match");
                            }
                            flightDetailsmatch = false;
                        }

                        writer = new FileWriter(outputFileName, true);
                        writer.write(worldmateStringBuilder.toString());
                        writer.close();
                        break;

                    } else {
                        System.out.println("Error: records should be an array: skipping.");
                        mlJsonPasrer.skipChildren();
                    }
                } else {
                    mlJsonPasrer.skipChildren();
                }

                //END
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static String matchingStatus(String wData, String mData) {
        String matchingStatus;
        if (wData.equals(mData)) {
            matchingStatus = "Perfect Match";
        } else {
            matchingStatus = "Not Match";
        }
        return matchingStatus;
    }

    public static Float accurencyStatus(int totalsuccess, int total) {

        Float x = new Float(totalsuccess);
        Float y = new Float(total);

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

}
