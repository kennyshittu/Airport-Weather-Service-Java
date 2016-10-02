package com.crossover.trial.weather.utils;

import com.crossover.trial.weather.domains.AirportData;

/**
 * Created by Shittu on 01/10/2016.
 */
public class WeatherUtils {
    public static final double R = 6372.8;

    /**
     * Haversine distance between two airports.
     *
     * @param ad1 airport 1
     * @param ad2 airport 2
     * @return the distance in KM
     */
    public static double calculateDistance(AirportData ad1, AirportData ad2) {
        double deltaLat = Math.toRadians(ad2.getLatitude() - ad1.getLatitude());
        double deltaLon = Math.toRadians(ad2.getLongitude() - ad1.getLongitude());
        double a = Math.pow(Math.sin(deltaLat / 2), 2) + Math.pow(Math.sin(deltaLon / 2), 2) * Math
            .cos(ad1.getLatitude()) * Math.cos(ad2.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

}
