package com.crossover.trial.weather.daos;

import java.util.List;
import java.util.Map;

import com.crossover.trial.weather.domains.AirportData;
import com.crossover.trial.weather.domains.AtmosphericInformation;
import com.crossover.trial.weather.domains.DataPoint;

/**
 * Created by Shittu on 02/10/2016.
 */
public interface WeatherDao {
    /**
     * Add a new known airport to our list.
     *
     * @param iataCode 3 letter code
     * @param latitude in degrees
     * @param longitude in degrees
     * @return the added airport
     */
    AirportData addAirport(String iataCode, double latitude, double longitude);

    /**
     * Records information about how often requests are made
     *
     * @param iata an iata code
     * @param radius query radius
     */
    void updateRequestFrequency(String iata, Double radius);

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    AirportData findAirportData(String iataCode);

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    int getAirportDataIdx(String iataCode);

    /**
     * @return all airport data
     */
    List<AirportData> getAirportData();

    /**
     * Get atmospheric information for a known airport data using given index.
     *
     * @param idx airportData index
     * @return atmospheric information
     */
    AtmosphericInformation getAtmosphericInformation(int idx);

    /**
     * Given an iataCode delete the airport data and associated atmospheric information
     *
     * @param iataCode as a string
     */
    void deleteAirport(String iataCode);

    /**
     * Get atmospheric information for airport within specified radius to a known airport.
     *
     * @param iata airportData index for the known airport
     * @param radius km away from the known airport
     * @return List of atmospheric information that contains a minimum of one Data point, for all airports within radius
     */
    List<AtmosphericInformation> getAtmosphericInformationByRadius(String iata, double radius);

    /**
     * @return total number of datapoint in all atmospheric information
     */
    long getDataSize();

    /**
     * @return iata frequency
     */
    Map<String, Double> getIataFrequency();

    /**
     * @return radius frequency
     */
    int[] getRadiusFrequency();

    /**
     * Updates atmospheric information with new data point.
     *
     * @param ai atmospheric information to be updated
     * @param pointType type of data point
     * @param dp data point
     */
    void updateAtmosphericInformation(AtmosphericInformation ai, String pointType, DataPoint dp);
}
