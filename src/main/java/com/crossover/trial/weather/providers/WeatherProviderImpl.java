package com.crossover.trial.weather.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.crossover.trial.weather.domains.AirportData;
import com.crossover.trial.weather.domains.AtmosphericInformation;

/**
 * Created by Shittu on 03/10/2016.
 */
public class WeatherProviderImpl implements WeatherProvider {
    private static final WeatherProvider ourInstance = new WeatherProviderImpl();

    private final List<AirportData> mAirportDatas;

    private final List<AtmosphericInformation> mAtmosphericInformations;

    private final Map<AirportData, Integer> mRequestFrequency;

    private final Map<Double, Integer> mRadiusFreq;

    private WeatherProviderImpl() {
        mRadiusFreq = new HashMap<Double, Integer>();
        mRequestFrequency = new HashMap<AirportData, Integer>();
        mAtmosphericInformations = new LinkedList<>();
        mAirportDatas = new ArrayList<>();
    }

    public static WeatherProvider getInstance() {
        return ourInstance;
    }

    @Override
    public List<AirportData> getAirportDatas() {
        return mAirportDatas;
    }

    @Override
    public List<AtmosphericInformation> getAtmosphericInformations() {
        return mAtmosphericInformations;
    }

    @Override
    public Map<AirportData, Integer> getRequestFrequency() {
        return mRequestFrequency;
    }

    @Override
    public Map<Double, Integer> getRadiusFreq() {
        return mRadiusFreq;
    }
}
