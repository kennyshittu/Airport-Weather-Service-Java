package com.crossover.trial.weather.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.crossover.trial.weather.domains.AirportData;
import com.crossover.trial.weather.domains.AtmosphericInformation;
import com.crossover.trial.weather.domains.DataPoint;
import com.crossover.trial.weather.utils.DataPointType;
import com.crossover.trial.weather.utils.WeatherException;

/**
 * Created by Shittu on 02/10/2016.
 */
public interface WeatherService {

    /**
     * Retrieve the most up to date atmospheric information from the given airport and other airports in the given
     * radius.
     *
     * @param iata the three letter airport code
     * @param radiusString the radius, in km, from which to collect weather data
     *
     * @return list of {@link AtmosphericInformation} from the requested airport and
     * airports in the given radius
     */
    List<AtmosphericInformation> getWeather(String iata, String radiusString);

    /**
     * Returns information about the number of datapoints currently held in memory,
     * the frequency of requests for each IATA code and the frequency of
     * requests for each radius.
     *
     * @return Map with health information.
     */
    Map<String, Object> getPingData();

    /**
     * Return a list of known airports
     *
     * @return unique list of IATA codes
     */
    Set<String> getAirportData();

    /**
     * Add a new airport to the known airport list.
     *
     * @param iataCode the 3 letter airport code of the new airport
     * @param latitude the airport's latitude in degrees
     * @param longitude the airport's longitude in degrees
     * @return newly added airport data
     */
    AirportData addAirport(String iataCode, double latitude, double longitude);

    /**
     * Update the airports atmospheric information for a particular pointType with
     * new data point information.
     *
     * @param iataCode the 3 letter airport code
     * @param pointType the point type, {@link DataPointType} for a complete list
     * @param dp the new data point
     */
    void addDataPoint(String iataCode, String pointType, DataPoint dp)
        throws WeatherException;

    /**
     * returns airport data, including latitude and longitude for a particular airport
     *
     * @param iataCode the 3 letter airport code
     * @return {@link AirportData}
     */
    AirportData findAirportData(String iataCode);

    /**
     * Remove an airport and associated atmospheric info from the known airport list
     * and atmospheric information list
     *
     * @param iataCode the 3 letter airport code
     */
    void deleteAirport(String iataCode);
}
