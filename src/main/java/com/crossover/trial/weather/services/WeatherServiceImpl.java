package com.crossover.trial.weather.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;

import com.crossover.trial.weather.daos.WeatherDao;
import com.crossover.trial.weather.domains.AirportData;
import com.crossover.trial.weather.domains.AtmosphericInformation;
import com.crossover.trial.weather.domains.DataPoint;
import com.crossover.trial.weather.utils.WeatherException;
import com.crossover.trial.weather.utils.WeatherUtils;

/**
 * Created by Shittu on 01/10/2016.
 */
public final class WeatherServiceImpl implements WeatherService {

    private static final String RADIUS_FREQ_KEY = "radius_freq";

    private static final String IATA_FREQ_KEY = "iata_freq";

    private static final String DATASIZE_KEY = "datasize";

    private WeatherDao mWeatherDao;

    @Inject
    public void setWeatherDao(WeatherDao weatherDao){
        this.mWeatherDao = weatherDao;
    }

    @Override
    public List<AtmosphericInformation> getWeather(String iata, String radiusString) {

        double radius = WeatherUtils.checkAndSetRadius(radiusString);

        mWeatherDao.updateRequestFrequency(iata, radius);

        List<AtmosphericInformation> retval = new ArrayList<>();
        int idx = mWeatherDao.getAirportDataIdx(iata);

        retval.add(mWeatherDao.getAtmosphericInformation(idx));

        if (radius != 0) {
            retval = mWeatherDao.getAtmosphericInformationByRadius(iata, radius);
        }
        return retval;
    }

    @Override
    public Map<String, Object> getPingData() {

        Map<String, Object> retval = new HashMap<>();

        retval.put(DATASIZE_KEY, mWeatherDao.getDataSize());
        retval.put(IATA_FREQ_KEY, mWeatherDao.getIataFrequency());
        retval.put(RADIUS_FREQ_KEY, mWeatherDao.getRadiusFrequency());

        return retval;
    }

    @Override
    public Set<String> getAirportData() {
        Set<String> retval = new HashSet<>();
        mWeatherDao.getAirportData().stream().forEach(airportData -> retval.add(airportData.getIata()));
        return retval;
    }

    @Override
    public AirportData addAirport(String iataCode, double latitude, double longitude) {
        return mWeatherDao.addAirport(iataCode, latitude, longitude);
    }

    @Override
    public void addDataPoint(String iataCode, String pointType, DataPoint dp)
        throws WeatherException {
        int airportDataIdx = mWeatherDao.getAirportDataIdx(iataCode);
        AtmosphericInformation ai = mWeatherDao.getAtmosphericInformation(airportDataIdx);
        mWeatherDao.updateAtmosphericInformation(ai, pointType, dp);
    }


    @Override
    public AirportData findAirportData(String iataCode) {
        return mWeatherDao.findAirportData(iataCode);
    }

    @Override
    public void deleteAirport(String iataCode) {
        mWeatherDao.deleteAirport(iataCode);
    }
}
