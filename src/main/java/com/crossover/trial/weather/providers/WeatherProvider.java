package com.crossover.trial.weather.providers;

import java.util.List;
import java.util.Map;

import com.crossover.trial.weather.domains.AirportData;
import com.crossover.trial.weather.domains.AtmosphericInformation;

/**
 * Created by Shittu on 03/10/2016.
 */
public interface WeatherProvider {
    /**
     * all known airports
     */
    List<AirportData> getAirportDatas();

    /**
     * atmospheric information for each airport, idx corresponds with airportData
     */
    List<AtmosphericInformation> getAtmosphericInformations();

    /**
     * Internal performance counter to better understand most requested information, this map can be improved but
     * for now provides the basis for future performance optimizations. Due to the stateless deployment architecture
     * we don't want to write this to disk, but will pull it off using a REST request and aggregate with other
     * performance metrics {link #ping()}
     */
    Map<AirportData, Integer> getRequestFrequency();

    Map<Double, Integer> getRadiusFreq();
}
