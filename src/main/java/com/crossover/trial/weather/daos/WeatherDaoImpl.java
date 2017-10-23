package com.crossover.trial.weather.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crossover.trial.weather.domains.AirportData;
import com.crossover.trial.weather.domains.AtmosphericInformation;
import com.crossover.trial.weather.domains.DataPoint;
import com.crossover.trial.weather.providers.WeatherProvider;
import com.crossover.trial.weather.providers.WeatherProviderImpl;
import com.crossover.trial.weather.utils.DataPointType;
import com.crossover.trial.weather.utils.WeatherUtils;

/**
 * Created by Shittu on 01/10/2016.
 */
public final class WeatherDaoImpl implements WeatherDao {
    private static final int DAY_MILSEC = 86400000;
    private static final int LOW_POINT = 0;
    private static final int HIGH_POINT = 100;

    private WeatherProvider mWeatherProvider = WeatherProviderImpl.getInstance();

    // for testing purpose
    public void setWeatherProvider(WeatherProvider weatherProvider) {
        mWeatherProvider = weatherProvider;
    }

    @Override
    public List<AirportData> getAirportData() {
        return mWeatherProvider.getAirportDatas();
    }

    /**
     * Add a new known airport to our list.
     *
     * @param iataCode 3 letter code
     * @param latitude in degrees
     * @param longitude in degrees
     * @return the added airport
     */
    @Override
    public AirportData addAirport(String iataCode, double latitude, double longitude) {
        AirportData ad = new AirportData();
        ad.setIata(iataCode);
        ad.setLatitude(latitude);
        ad.setLongitude(longitude);

        //check airport already exist
        if(findAirportData(iataCode) == null) {
            mWeatherProvider.getAirportDatas().add(ad);
            AtmosphericInformation ai = new AtmosphericInformation();
            mWeatherProvider.getAtmosphericInformations().add(ai);
        }

        return ad;
    }

    /**
     * Records information about how often requests are made
     *
     * @param iata an iata code
     * @param radius query radius
     */
    @Override
    public void updateRequestFrequency(String iata, Double radius) {
        AirportData airportData = findAirportData(iata);

        mWeatherProvider.getRequestFrequency().put(airportData,
            mWeatherProvider.getRequestFrequency().getOrDefault(airportData, LOW_POINT) + 1);

        mWeatherProvider.getRadiusFreq()
            .put(radius, mWeatherProvider.getRadiusFreq().getOrDefault(radius, LOW_POINT));
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    @Override
    public AirportData findAirportData(String iataCode) {
        return mWeatherProvider.getAirportDatas().stream()
            .filter(ap -> ap.getIata().equals(iataCode)).findFirst().orElse(null);
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    @Override
    public int getAirportDataIdx(String iataCode) {
        AirportData ad = findAirportData(iataCode);
        return mWeatherProvider.getAirportDatas().indexOf(ad);
    }

    @Override public AtmosphericInformation getAtmosphericInformation(int idx) {
        return mWeatherProvider.getAtmosphericInformations().isEmpty() ?
            null :
            mWeatherProvider.getAtmosphericInformations().get(idx);
    }

    @Override
    public void deleteAirport(String iataCode) {
        AirportData ad = findAirportData(iataCode);
        int idx = getAirportDataIdx(iataCode);
        mWeatherProvider.getAirportDatas().remove(ad);
        if(!mWeatherProvider.getAtmosphericInformations().isEmpty()) {
            mWeatherProvider.getAtmosphericInformations().remove(idx);
        }
    }

    @Override
    public List<AtmosphericInformation> getAtmosphericInformationByRadius(String iata,
        double radius) {
        List<AtmosphericInformation> retval = new ArrayList<>();
        AirportData ad = findAirportData(iata);
        mWeatherProvider.getAirportDatas().stream().forEach(data -> {
            if (!ad.equals(data) && WeatherUtils.calculateDistance(ad, data) <= radius) {
                AtmosphericInformation ai = mWeatherProvider.getAtmosphericInformations()
                    .get(mWeatherProvider.getAirportDatas().indexOf(data));
                if (ai.hasDataPoint()) {
                    retval.add(ai);
                }
            }
        });
        return retval;
    }

    @Override
    public long getDataSize() {
        return mWeatherProvider.getAtmosphericInformations().stream()
            .filter(AtmosphericInformation::hasDataPoint)
            .filter(ai -> ai.getLastUpdateTime() > System.currentTimeMillis() - DAY_MILSEC).count();
    }

    @Override
    public Map<String, Double> getIataFrequency() {
        Map<String, Double> freq = new HashMap<>();
        // fraction of queries
        for (AirportData data : mWeatherProvider.getAirportDatas()) {
            double frac =
                (double) mWeatherProvider.getRequestFrequency().getOrDefault(data, LOW_POINT)
                    / mWeatherProvider.getRequestFrequency().size();
            freq.put(data.getIata(), frac);
        }

        return freq;
    }

    @Override
    public int[] getRadiusFrequency() {
        int m =
            mWeatherProvider.getRadiusFreq().keySet().stream().max(Double::compare).orElse(1000.0)
                .intValue() + 1;

        int[] hist = new int[m];
        for (Map.Entry<Double, Integer> e : mWeatherProvider.getRadiusFreq().entrySet()) {
            int i = e.getKey().intValue() % 10;
            hist[i] += e.getValue();
        }
        return hist;
    }

    @Override
    public void updateAtmosphericInformation(AtmosphericInformation ai, String pointType,
        DataPoint dp) {

        if (pointType.equalsIgnoreCase(DataPointType.WIND.name())) {
            if (dp.getMean() >= LOW_POINT) {
                ai.setWind(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        if (pointType.equalsIgnoreCase(DataPointType.TEMPERATURE.name())) {
            if (dp.getMean() >= -50 && dp.getMean() < HIGH_POINT) {
                ai.setTemperature(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        if (pointType.equalsIgnoreCase(DataPointType.HUMIDTY.name())) {
            if (dp.getMean() >= LOW_POINT && dp.getMean() < HIGH_POINT) {
                ai.setHumidity(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        if (pointType.equalsIgnoreCase(DataPointType.PRESSURE.name())) {
            if (dp.getMean() >= 650 && dp.getMean() < 800) {
                ai.setPressure(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        if (pointType.equalsIgnoreCase(DataPointType.CLOUDCOVER.name())) {
            if (dp.getMean() >= LOW_POINT && dp.getMean() < HIGH_POINT) {
                ai.setCloudCover(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        if (pointType.equalsIgnoreCase(DataPointType.PRECIPITATION.name())) {
            if (dp.getMean() >= LOW_POINT && dp.getMean() < HIGH_POINT) {
                ai.setPrecipitation(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        throw new IllegalStateException("couldn't update atmospheric data");
    }

}
