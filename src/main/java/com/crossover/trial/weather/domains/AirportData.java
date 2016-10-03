package com.crossover.trial.weather.domains;

import com.crossover.trial.weather.utils.DstType;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public final class AirportData {

    /**
     * the three letter IATA code
     */
    String mIata;

    String icao;

    String city;

    String country;

    /**
     * mLatitude value in degrees
     */
    double mLatitude;

    /**
     * mLongitude value in degrees
     */
    double mLongitude;

    double altitude;

    double timezone;

    DstType dst;



    public AirportData() {
    }

    public String getIata() {
        return mIata;
    }

    public void setIata(String iata) {
        this.mIata = iata;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
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
