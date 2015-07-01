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

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 *
 * @author Sheshadri
 */
public class ComparisionTest {

    public static void main(String args[]) throws JAXBException, FileNotFoundException, IOException {

        JAXBContext jc = JAXBContext.newInstance(WorldmateParsingResult.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        WorldmateParsingResult wordmateCollection = (WorldmateParsingResult) unmarshaller.unmarshal(new File("D:\\office\\sabre\\sravn_data\\Worldmate_response-xml\\4789444.xml"));
        WorldmateParsingResult mlCollection = (WorldmateParsingResult) unmarshaller.unmarshal(new File("D:\\office\\sabre\\sravn_data\\Worldmate_response-xml\\4810057.xml"));
        ObjectMapper mapper = new ObjectMapper();
        
        //For single json data read
        //MlFlightDetails jsonCollection = mapper.readValue(new File("D:\\office\\sabre\\4810057.json"),MlFlightDetails.class);
        //for json array
        List<MlFlightDetails> mlJsonObjects = Arrays.asList(mapper.readValue(new File("D:\\office\\sabre\\4810062.json"), MlFlightDetails[].class));
        List<ItineraryItem> itenaryItemList = wordmateCollection.getItems().getFlightOrCarRentalOrHotelReservation();
        
                
        System.out.println("===========================json =======================");
       
         for (int index = 0; index < itenaryItemList.size(); index++) {
                if (itenaryItemList.get(index) instanceof Flight) {
                    Flight fl = (Flight) itenaryItemList.get(index);
                    String supplierName = fl.getBookingDetails().getName();
                    for (int index1 = 0; index1 < mlJsonObjects.size(); index1++) {
                        if(fl.getProviderDetails().getName().equals(mlJsonObjects.get(index).getSupplierName())){
                            System.out.println("Worlmate supplier name---"+fl.getBookingDetails().getName()+" ML supplier name -" + mlJsonObjects.get(index1).getSupplierName());
                            System.out.println("Worlmate supplier flightName---"+fl.getDetails().getNumber()+" ML supplier flightname -" + mlJsonObjects.get(index1).getSupplierFlightNumber());                            
                        }
                    }
                }
            }
     /*
        List<ItineraryItem> itenaryItemList = wordmateCollection.getItems().getFlightOrCarRentalOrHotelReservation();
        int index = 0;
        for (index = 0; index < itenaryItemList.size(); index++) {
            if (itenaryItemList.get(index) instanceof Flight) {
                System.out.println("===========================flight =======================");
                Flight fl = (Flight) itenaryItemList.get(index);
                System.out.println(" Flight -" + fl.getArrival().getName());
                System.out.println(" Flight Number -" + fl.getDetails().getAirlineCode());
                System.out.println(" Supplier name -" + fl.getProviderDetails().getName());
                System.out.println(" arrival Date -" + fl.getArrival().getLocalDateTime());
                System.out.println("===============================================================");
            }
            if (itenaryItemList.get(index) instanceof HotelReservation) {
                System.out.println("=======================hotel reservation ========================================");
                HotelReservation fl = (HotelReservation) itenaryItemList.get(index);
                System.out.println(" hotelName -" + fl.getHotelName());
                System.out.println(" BookiningDetails -" + fl.getPhone());                
                System.out.println("===============================================================");
            }
        }
         List<ItineraryItem> mlitenaryItemList = mlCollection.getItems().getFlightOrCarRentalOrHotelReservation();
         int index1 = 0;
         for(index1 = 0 ; index1 < mlitenaryItemList.size();index1++){
         if(mlitenaryItemList.get(index1) instanceof Flight){
         System.out.println("===========================flight2 =======================");
         Flight fl = (Flight) mlitenaryItemList.get(index1);
         System.out.println(" Flight -" + fl.getArrival().getName());
         System.out.println(" Flight Number -" + fl.getDetails().getAirlineCode());
         System.out.println("===============================================================");
         }
         if(mlitenaryItemList.get(index1) instanceof HotelReservation){
         System.out.println("=======================hotel reservation ========================================");
         HotelReservation fl = (HotelReservation) mlitenaryItemList.get(index1);
         System.out.println(" hotelName -" + fl.getHotelName());
         System.out.println(" BookiningDetails -" + fl.getPhone());
         System.out.println("===============================================================");
         }
         }
         
        */
    }

}
