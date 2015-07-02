/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sabre;

import sabre.jaxb.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBException;

import org.codehaus.jackson.JsonFactory;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;

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
            String inputWMDirectoryPath = "D:\\office\\sabre\\sravn_data\\Worldmate_response-xml";
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

            Boolean flightDetailsmatch = false;
            //COMPARE WITH ML DATA WITH WORLDMATE DATA START
            for (File file : worldmateFlieList) {
                //System.out.println(file);
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
                                // For each of the records in the array
                                for (ItineraryItem itenaryItemList1 : itenaryItemList) {
                                    if (itenaryItemList1 instanceof Flight) {
                                        Flight worldmateFlightItenary = (Flight) itenaryItemList1;
                                        while (mlJsonPasrer.nextToken() != JsonToken.END_ARRAY) {
                                            // read the record into a tree model,
                                            // this moves the parsing position to the end of it
                                            JsonNode node = mlJsonPasrer.readValueAsTree();
                                            // And now we have random access to everything in the object

                                            if (worldmateFlightItenary.getDetails().getNumber() == node.get("flight_number").asInt() && flightDetailsmatch == false) {
                                                flightDetailsmatch = true;
                                                int accurancy = 0;
                                                String matchingStatus;
                                                worldmateStringBuilder.append(worldmateFileName + tabSpace);
                                                worldmateStringBuilder.append(worldmateFlightItenary.getProviderDetails().getName() + tabSpace);
                                                worldmateStringBuilder.append(node.get("supplier").get("name").asText() + tabSpace);
                                                matchingStatus = matchingStatus(worldmateFlightItenary.getProviderDetails().getName(), node.get("supplier").get("name").asText());
                                                worldmateStringBuilder.append(matchingStatus + tabSpace);
                                                if ("Perfect Match".equals(matchingStatus)) {
                                                    accurancy++;
                                                    matchingStatus = "";
                                                }

                                                worldmateStringBuilder.append(worldmateFlightItenary.getDetails().getAirlineCode() + tabSpace);
                                                worldmateStringBuilder.append(node.get("supplier").get("code").asText() + tabSpace);
                                                matchingStatus = matchingStatus(worldmateFlightItenary.getDetails().getAirlineCode(), node.get("supplier").get("code").asText());
                                                worldmateStringBuilder.append(matchingStatus).append(tabSpace);
                                                if ("Perfect Match".equals(matchingStatus)) {
                                                    accurancy++;
                                                    matchingStatus = "";
                                                }

                                                worldmateStringBuilder.append(worldmateFlightItenary.getDetails().getNumber()).append(tabSpace);
                                                worldmateStringBuilder.append(node.get("flight_number").asInt()).append(tabSpace);
                                                worldmateStringBuilder.append("Perfect Match").append(tabSpace);
                                                matchingStatus = "Perfect Match";
                                                if ("Perfect Match".equals(matchingStatus)) {
                                                    accurancy++;
                                                    matchingStatus = "";
                                                }

                                                try {
                                                    worldmateStringBuilder.append(worldmateFlightItenary.getDeparture().getAirportCode()).append(tabSpace).append(node.get("origin").get("code").asText()).append(tabSpace);
                                                    matchingStatus = matchingStatus(worldmateFlightItenary.getDeparture().getAirportCode(), node.get("origin").get("code").asText());
                                                    worldmateStringBuilder.append(matchingStatus).append(tabSpace);
                                                    if ("Perfect Match".equals(matchingStatus)) {
                                                        accurancy++;
                                                        matchingStatus = "";
                                                    }
                                                } catch (NullPointerException e) {
                                                    worldmateStringBuilder.append(worldmateFlightItenary.getDeparture().getAirportCode()).append(tabSpace).append("Not Match").append(tabSpace);
                                                }
                                                try {
                                                    worldmateStringBuilder.append(worldmateFlightItenary.getArrival().getAirportCode()).append(tabSpace).append(node.get("destination").get("code").asText()).append(tabSpace);
                                                    matchingStatus = matchingStatus(worldmateFlightItenary.getArrival().getAirportCode(), node.get("destination").get("code").asText());
                                                    worldmateStringBuilder.append(matchingStatus).append(tabSpace);
                                                    if ("Perfect Match".equals(matchingStatus)) {
                                                        accurancy++;
                                                        matchingStatus = "";
                                                    }
                                                } catch (NullPointerException e) {
                                                    worldmateStringBuilder.append(worldmateFlightItenary.getArrival().getAirportCode()).append(tabSpace).append("Not Match").append(tabSpace);
                                                }
                                                try {
                                                    worldmateStringBuilder.append(worldmateFlightItenary.getDeparture().getName()).append(tabSpace).append(node.get("origin").get("name").asText()).append(tabSpace);
                                                    matchingStatus = matchingStatus(worldmateFlightItenary.getDeparture().getName(), node.get("origin").get("name").asText());
                                                    worldmateStringBuilder.append(matchingStatus).append(tabSpace);
                                                    if ("Perfect Match".equals(matchingStatus)) {
                                                        accurancy++;
                                                        matchingStatus = "";
                                                    }
                                                } catch (NullPointerException e) {
                                                    worldmateStringBuilder.append(worldmateFlightItenary.getDeparture().getAirportCode()).append(tabSpace).append("Not Match").append(tabSpace);
                                                }
                                                worldmateStringBuilder.append(worldmateFlightItenary.getArrival().getName() + tabSpace);
                                                worldmateStringBuilder.append(node.get("destination").get("name").asText() + tabSpace);
                                                matchingStatus = matchingStatus(worldmateFlightItenary.getArrival().getName(), node.get("destination").get("name").asText());
                                                worldmateStringBuilder.append(matchingStatus).append(tabSpace);
                                                if ("Perfect Match".equals(matchingStatus)) {
                                                    accurancy++;
                                                    matchingStatus = "";
                                                }
                                                worldmateStringBuilder.append(worldmateFlightItenary.getDeparture().getLocalDateTime() + tabSpace);
                                                worldmateStringBuilder.append(node.get("departure_date").asText() + tabSpace);
                                                matchingStatus = matchingStatus(worldmateFlightItenary.getDeparture().getLocalDateTime().toString().replace(".000Z", ""), node.get("departure_date").asText().replace(".000+0000", ""));
                                                worldmateStringBuilder.append(matchingStatus).append(tabSpace);
                                                if ("Perfect Match".equals(matchingStatus)) {
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
                                        worldmateStringBuilder.append(worldmateFileName + "\n");
                                    }
                                    flightDetailsmatch = false;
                                }
                                break;

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
