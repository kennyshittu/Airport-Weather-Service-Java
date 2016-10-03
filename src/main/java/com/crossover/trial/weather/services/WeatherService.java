package com.crossover.trial.weather.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;

import com.crossover.trial.weather.daos.WeatherDao;
import com.crossover.trial.weather.domains.AirportData;
import com.crossover.trial.weather.domains.AtmosphericInformation;
import com.crossover.trial.weather.domains.DataPoint;
import com.crossover.trial.weather.utils.WeatherException;
import org.glassfish.jersey.process.internal.RequestScoped;

/**
 * Created by Shittu on 02/10/2016.
 */
public interface WeatherService {

    List<AtmosphericInformation> getWeather(String iata, String radiusString);

    Map<String, Object> getPingData();

    Set<String> getAirportData();

    AirportData addAirport(String iataCode, double latitude, double longitude);

    void addDataPoint(String iataCode, String pointType, DataPoint dp)
            throws WeatherException;

    AirportData findAirportData(String iataCode);

    void deleteAirport(String iataCode);
}
