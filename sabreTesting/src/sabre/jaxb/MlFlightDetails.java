/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sabre.jaxb;

/**
 *
 * @author Sunitha
 */
public class MlFlightDetails {
  
  private String supplierName;
  private String supplierAirlineCode;
  private String supplierFlightNumber;
  private String departureAirportCode;
  private String arrivalAirportCode;
  private String departureAirportName;
  private String arrivalAirportName;
  private String dateTimeofDeparture;
  private String dateTimeofArrival;
   

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierAirlineCode() {
        return supplierAirlineCode;
    }

    public void setSupplierAirlineCode(String supplierAirlineCode) {
        this.supplierAirlineCode = supplierAirlineCode;
    }

    public String getSupplierFlightNumber() {
        return supplierFlightNumber;
    }

    public void setSupplierFlightNumber(String supplierFlightNumber) {
        this.supplierFlightNumber = supplierFlightNumber;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public void setDepartureAirpoertName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public String getDateTimeofDeparture() {
        return dateTimeofDeparture;
    }

    public void setDateTimeofDeparture(String dateTimeofDeparture) {
        this.dateTimeofDeparture = dateTimeofDeparture;
    }

    public String getDateTimeofArrival() {
        return dateTimeofArrival;
    }

    public void setDateTimeofArrival(String dateTimeofArrival) {
        this.dateTimeofArrival = dateTimeofArrival;
    }
 
  
}
