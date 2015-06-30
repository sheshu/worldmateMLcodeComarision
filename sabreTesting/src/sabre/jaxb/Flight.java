//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.29 at 06:41:50 PM IST 
//


package sabre.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for flight complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="flight">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.worldmate.com/schemas/worldmate-api-v1.xsd}reservation">
 *       &lt;sequence>
 *         &lt;element name="details" type="{http://www.worldmate.com/schemas/worldmate-api-v1.xsd}flight-details"/>
 *         &lt;element name="operated-by" type="{http://www.worldmate.com/schemas/worldmate-api-v1.xsd}flight-details" minOccurs="0"/>
 *         &lt;element name="departure" type="{http://www.worldmate.com/schemas/worldmate-api-v1.xsd}airport-details"/>
 *         &lt;element name="arrival" type="{http://www.worldmate.com/schemas/worldmate-api-v1.xsd}airport-details"/>
 *         &lt;element name="aircraft" type="{http://www.worldmate.com/schemas/worldmate-api-v1.xsd}coded-name" minOccurs="0"/>
 *         &lt;element name="duration" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="distance-in-miles" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="class-type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="traveler" type="{http://www.worldmate.com/schemas/worldmate-api-v1.xsd}traveler" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "flight", propOrder = {
    "details",
    "operatedBy",
    "departure",
    "arrival",
    "aircraft",
    "duration",
    "distanceInMiles",
    "classType",
    "traveler"
})
public class Flight
    extends Reservation
{

    @XmlElement(required = true)
    protected FlightDetails details;
    @XmlElement(name = "operated-by")
    protected FlightDetails operatedBy;
    @XmlElement(required = true)
    protected AirportDetails departure;
    @XmlElement(required = true)
    protected AirportDetails arrival;
    protected CodedName aircraft;
    protected Integer duration;
    @XmlElement(name = "distance-in-miles")
    protected Integer distanceInMiles;
    @XmlElement(name = "class-type")
    protected String classType;
    protected List<Traveler> traveler;

    /**
     * Gets the value of the details property.
     * 
     * @return
     *     possible object is
     *     {@link FlightDetails }
     *     
     */
    public FlightDetails getDetails() {
        return details;
    }

    /**
     * Sets the value of the details property.
     * 
     * @param value
     *     allowed object is
     *     {@link FlightDetails }
     *     
     */
    public void setDetails(FlightDetails value) {
        this.details = value;
    }

    /**
     * Gets the value of the operatedBy property.
     * 
     * @return
     *     possible object is
     *     {@link FlightDetails }
     *     
     */
    public FlightDetails getOperatedBy() {
        return operatedBy;
    }

    /**
     * Sets the value of the operatedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link FlightDetails }
     *     
     */
    public void setOperatedBy(FlightDetails value) {
        this.operatedBy = value;
    }

    /**
     * Gets the value of the departure property.
     * 
     * @return
     *     possible object is
     *     {@link AirportDetails }
     *     
     */
    public AirportDetails getDeparture() {
        return departure;
    }

    /**
     * Sets the value of the departure property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirportDetails }
     *     
     */
    public void setDeparture(AirportDetails value) {
        this.departure = value;
    }

    /**
     * Gets the value of the arrival property.
     * 
     * @return
     *     possible object is
     *     {@link AirportDetails }
     *     
     */
    public AirportDetails getArrival() {
        return arrival;
    }

    /**
     * Sets the value of the arrival property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirportDetails }
     *     
     */
    public void setArrival(AirportDetails value) {
        this.arrival = value;
    }

    /**
     * Gets the value of the aircraft property.
     * 
     * @return
     *     possible object is
     *     {@link CodedName }
     *     
     */
    public CodedName getAircraft() {
        return aircraft;
    }

    /**
     * Sets the value of the aircraft property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodedName }
     *     
     */
    public void setAircraft(CodedName value) {
        this.aircraft = value;
    }

    /**
     * Gets the value of the duration property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDuration(Integer value) {
        this.duration = value;
    }

    /**
     * Gets the value of the distanceInMiles property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDistanceInMiles() {
        return distanceInMiles;
    }

    /**
     * Sets the value of the distanceInMiles property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDistanceInMiles(Integer value) {
        this.distanceInMiles = value;
    }

    /**
     * Gets the value of the classType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassType() {
        return classType;
    }

    /**
     * Sets the value of the classType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassType(String value) {
        this.classType = value;
    }

    /**
     * Gets the value of the traveler property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the traveler property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTraveler().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Traveler }
     * 
     * 
     */
    public List<Traveler> getTraveler() {
        if (traveler == null) {
            traveler = new ArrayList<Traveler>();
        }
        return this.traveler;
    }

}
