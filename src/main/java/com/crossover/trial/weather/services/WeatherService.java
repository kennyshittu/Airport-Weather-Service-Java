package com.crossover.trial.weather.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
 * Created by Shittu on 01/10/2016.
 */
@Named @RequestScoped public class WeatherService {

    private static final String RADIUS_FREQ_KEY = "radius_freq";

    private static final String IATA_FREQ_KEY = "iata_freq";

    private static final String DATASIZE_KEY = "datasize";

    private WeatherDao weatherDao;

    @Inject
    public void setWeatherDao(WeatherDao weatherDao){
        this.weatherDao = weatherDao;
    }

    public List<AtmosphericInformation> getWeather(String iata, String radiusString) {
        double radius = radiusString == null || radiusString.trim().isEmpty() ?
            0 :
            Double.valueOf(radiusString);
        weatherDao.updateRequestFrequency(iata, radius);

        List<AtmosphericInformation> retval = new ArrayList<>();
        if (radius == 0) {
            int idx = weatherDao.getAirportDataIdx(iata);
            retval.add(weatherDao.getAtmosphericInformation(idx));
        } else {
            retval = weatherDao.getAtmosphericInformationByRadius(iata, radius);
        }
        return retval;
    }

    public Map<String, Object> getPingData() {

        Map<String, Object> retval = new HashMap<>();

        retval.put(DATASIZE_KEY, weatherDao.getDataSize());
        retval.put(IATA_FREQ_KEY, weatherDao.getIataFrequency());
        retval.put(RADIUS_FREQ_KEY, weatherDao.getRadiusFrequency());

        return retval;
    }

    public Set<String> getAirportData() {
        Set<String> retval = new HashSet<>();
        for (AirportData ad : WeatherDao.airportData) {
            retval.add(ad.getIata());
        }
        return retval;
    }

    public AirportData addAirport(String iataCode, double latitude, double longitude) {
        return WeatherDao.addAirport(iataCode, latitude, longitude);
    }

    public void addDataPoint(String iataCode, String pointType, DataPoint dp)
        throws WeatherException {
//        LOGGER.info("reached here last");
        System.out.println("reached inside add data point 1");
        int airportDataIdx = weatherDao.getAirportDataIdx(iataCode);
        AtmosphericInformation ai = weatherDao.getAtmosphericInformation(airportDataIdx);
        weatherDao.updateAtmosphericInformation(ai, pointType, dp);
    }


    public AirportData findAirportData(String iataCode) {
        return weatherDao.findAirportData(iataCode);
    }

    public void deleteAirport(String iataCode) {
        weatherDao.deleteAirport(iataCode);
    }
}
