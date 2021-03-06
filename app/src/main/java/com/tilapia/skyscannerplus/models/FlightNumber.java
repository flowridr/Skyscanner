
package com.tilapia.skyscannerplus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlightNumber {

    @SerializedName("FlightNumber")
    @Expose
    private String flightNumber;
    @SerializedName("CarrierId")
    @Expose
    private Integer carrierId;

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Integer getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Integer carrierId) {
        this.carrierId = carrierId;
    }

}
