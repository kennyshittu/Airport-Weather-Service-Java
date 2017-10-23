package com.crossover.trial.weather.domains;

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
    private String mIata;

    /**
     * mLatitude value in degrees
     */
    private double mLatitude;

    /**
     * mLongitude value in degrees
     */
    private double mLongitude;

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
        return other instanceof AirportData && ((AirportData) other).getIata()
            .equals(this.getIata());

    }
}
