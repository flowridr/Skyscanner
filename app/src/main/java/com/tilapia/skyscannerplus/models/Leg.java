
package com.tilapia.skyscannerplus.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Leg {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("SegmentIds")
    @Expose
    private List<Integer> segmentIds = null;
    @SerializedName("OriginStation")
    @Expose
    private Integer originStation;
    @SerializedName("DestinationStation")
    @Expose
    private Integer destinationStation;
    @SerializedName("Departure")
    @Expose
    private String departure;
    @SerializedName("Arrival")
    @Expose
    private String arrival;
    @SerializedName("Duration")
    @Expose
    private Integer duration;
    @SerializedName("JourneyMode")
    @Expose
    private String journeyMode;
    @SerializedName("Stops")
    @Expose
    private List<Integer> stops = null;
    @SerializedName("Carriers")
    @Expose
    private List<Integer> carriers = null;
    @SerializedName("OperatingCarriers")
    @Expose
    private List<Integer> operatingCarriers = null;
    @SerializedName("Directionality")
    @Expose
    private String directionality;
    @SerializedName("FlightNumbers")
    @Expose
    private List<FlightNumber> flightNumbers = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getSegmentIds() {
        return segmentIds;
    }

    public void setSegmentIds(List<Integer> segmentIds) {
        this.segmentIds = segmentIds;
    }

    public Integer getOriginStation() {
        return originStation;
    }

    public void setOriginStation(Integer originStation) {
        this.originStation = originStation;
    }

    public Integer getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(Integer destinationStation) {
        this.destinationStation = destinationStation;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getJourneyMode() {
        return journeyMode;
    }

    public void setJourneyMode(String journeyMode) {
        this.journeyMode = journeyMode;
    }

    public List<Integer> getStops() {
        return stops;
    }

    public void setStops(List<Integer> stops) {
        this.stops = stops;
    }

    public List<Integer> getCarriers() {
        return carriers;
    }

    public void setCarriers(List<Integer> carriers) {
        this.carriers = carriers;
    }

    public List<Integer> getOperatingCarriers() {
        return operatingCarriers;
    }

    public void setOperatingCarriers(List<Integer> operatingCarriers) {
        this.operatingCarriers = operatingCarriers;
    }

    public String getDirectionality() {
        return directionality;
    }

    public void setDirectionality(String directionality) {
        this.directionality = directionality;
    }

    public List<FlightNumber> getFlightNumbers() {
        return flightNumbers;
    }

    public void setFlightNumbers(List<FlightNumber> flightNumbers) {
        this.flightNumbers = flightNumbers;
    }

}
