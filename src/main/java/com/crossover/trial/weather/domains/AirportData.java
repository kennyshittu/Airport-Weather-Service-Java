package com.crossover.trial.weather.domains;

import com.crossover.trial.weather.utils.DstType;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public class AirportData {

    /**
     * the three letter IATA code
     */
    String iata;

    String icao;

    String city;

    String country;

    /**
     * latitude value in degrees
     */
    double latitude;

    /**
     * longitude value in degrees
     */
    double longitude;

    double altitude;

    double timezone;

    DstType dst;



    public AirportData() {
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    public boolean equals(Object other) {
        if (other instanceof AirportData) {
            return ((AirportData) other).getIata().equals(this.getIata());
        }

        return false;
    }
}
