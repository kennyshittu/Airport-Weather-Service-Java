package com.crossover.trial.weather.daos;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.crossover.trial.weather.domains.AirportData;
import com.crossover.trial.weather.domains.AtmosphericInformation;
import com.crossover.trial.weather.domains.DataPoint;
import com.crossover.trial.weather.utils.WeatherException;
import org.glassfish.jersey.process.internal.RequestScoped;

/**
 * Created by Shittu on 02/10/2016.
 */
public interface WeatherDao {
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

    AtmosphericInformation getAtmosphericInformation(int idx);

    void deleteAirport(String iataCode);

    List<AtmosphericInformation> getAtmosphericInformationByRadius(String iata, double radius);

    int getDataSize();

    Map<String, Double> getIataFrequency();

    int[] getRadiusFrequency();

    void updateAtmosphericInformation(AtmosphericInformation ai, String pointType, DataPoint dp) throws
        WeatherException;
}
