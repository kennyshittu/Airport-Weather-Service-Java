package com.crossover.trial.weather.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.crossover.trial.weather.domains.AirportData;
import com.crossover.trial.weather.domains.AtmosphericInformation;
import com.crossover.trial.weather.domains.DataPoint;
import com.crossover.trial.weather.utils.DataPointType;
import com.crossover.trial.weather.utils.WeatherUtils;
import com.crossover.trial.weather.utils.WeatherException;

/**
 * Created by Shittu on 01/10/2016.
 */
public final class WeatherDaoImpl implements WeatherDao {
    public static final int DAY_MILSEC = 86400000;
    public static final int DEFAULT_INT = 0;
    /**
     * all known airports
     */
    public static List<AirportData> airportData = new ArrayList<>();

    /**
     * atmospheric information for each airport, idx corresponds with airportData
     */
    public static List<AtmosphericInformation> atmosphericInformation = new LinkedList<>();

    /**
     * Internal performance counter to better understand most requested information, this map can be improved but
     * for now provides the basis for future performance optimizations. Due to the stateless deployment architecture
     * we don't want to write this to disk, but will pull it off using a REST request and aggregate with other
     * performance metrics {link #ping()}
     */
    public static Map<AirportData, Integer> requestFrequency = new HashMap<AirportData, Integer>();

    public static Map<Double, Integer> radiusFreq = new HashMap<Double, Integer>();

    /**
     * Add a new known airport to our list.
     *
     * @param iataCode 3 letter code
     * @param latitude in degrees
     * @param longitude in degrees
     * @return the added airport
     */
    public static AirportData addAirport(String iataCode, double latitude, double longitude) {
        AirportData ad = new AirportData();
        airportData.add(ad);

        AtmosphericInformation ai = new AtmosphericInformation();
        atmosphericInformation.add(ai);
        ad.setIata(iataCode);
        ad.setLatitude(latitude);
        ad.setLatitude(longitude);
        return ad;
    }

    /**
     * Records information about how often requests are made
     *
     * @param iata an iata code
     * @param radius query radius
     */
    @Override public void updateRequestFrequency(String iata, Double radius) {
        AirportData airportData = findAirportData(iata);
        requestFrequency.put(airportData, requestFrequency.getOrDefault(airportData, DEFAULT_INT) + 1);
        radiusFreq.put(radius, radiusFreq.getOrDefault(radius, DEFAULT_INT));
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    @Override public AirportData findAirportData(String iataCode) {
        return airportData.stream().filter(ap -> ap.getIata().equals(iataCode)).findFirst()
            .orElse(null);
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    @Override public int getAirportDataIdx(String iataCode) {
        AirportData ad = findAirportData(iataCode);
        return airportData.indexOf(ad);
    }

    /**
     * Get atmospheric information for a known airport data using given index.
     *
     * @param idx airportData index
     * @return atmospheric information
     */
    @Override public AtmosphericInformation getAtmosphericInformation(int idx) {
        return atmosphericInformation.get(idx);
    }

    /**
     * Given an iataCode delete the airport data and associated atmospheric information
     *
     * @param iataCode as a string
     */
    @Override public void deleteAirport(String iataCode) {
       AirportData ad = findAirportData(iataCode);
        int idx = getAirportDataIdx(iataCode);
        airportData.remove(ad);
        atmosphericInformation.remove(idx);
    }

    /**
     * Get atmospheric information for airport within specified radius to a known airport.
     *
     * @param iata airportData index for the known airport
     * @param radius km away from the known airport
     * @return List of atmospheric information that contains a minimum of one Data point, for all airports within radius
     */
    @Override public List<AtmosphericInformation> getAtmosphericInformationByRadius(String iata,
        double radius) {
        List<AtmosphericInformation> retval = new ArrayList<>();
        AirportData ad = findAirportData(iata);
        for (AirportData data : airportData) {
            if (!ad.equals(data) && WeatherUtils.calculateDistance(ad, data) <= radius) {
                AtmosphericInformation ai = atmosphericInformation.get(airportData.indexOf(data));
                if (ai.hasDataPoint()) {
                    retval.add(ai);
                }
            }
        }
        return retval;
    }

    @Override public int getDataSize() {
        int datasize = 0;
        for (AtmosphericInformation ai : atmosphericInformation) {
            // we only mCount recent readings
            if (ai.hasDataPoint()) {
                // updated in the mLast day
                if (ai.getLastUpdateTime() > System.currentTimeMillis() - DAY_MILSEC) {
                    datasize++;
                }
            }
        }
        return datasize;
    }

    @Override public Map<String, Double> getIataFrequency() {
        Map<String, Double> freq = new HashMap<>();
        // fraction of queries
        for (AirportData data : airportData) {
            double frac = (double) requestFrequency.getOrDefault(data, DEFAULT_INT) / requestFrequency.size();
            freq.put(data.getIata(), frac);
        }

        return freq;
    }

    @Override public int[] getRadiusFrequency() {
        int m = radiusFreq.keySet().stream().max(Double::compare).orElse(1000.0).intValue() + 1;

        int[] hist = new int[m];
        for (Map.Entry<Double, Integer> e : radiusFreq.entrySet()) {
            int i = e.getKey().intValue() % 10;
            hist[i] += e.getValue();
        }
        return hist;
    }

    @Override public void updateAtmosphericInformation(AtmosphericInformation ai, String pointType,
        DataPoint dp) throws WeatherException {

        final DataPointType dptype = DataPointType.valueOf(pointType.toUpperCase());

        if (pointType.equalsIgnoreCase(DataPointType.WIND.name())) {
            if (dp.getMean() >= DEFAULT_INT) {
                ai.setWind(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        if (pointType.equalsIgnoreCase(DataPointType.TEMPERATURE.name())) {
            if (dp.getMean() >= -50 && dp.getMean() < 100) {
                ai.setTemperature(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        if (pointType.equalsIgnoreCase(DataPointType.HUMIDTY.name())) {
            if (dp.getMean() >= DEFAULT_INT && dp.getMean() < 100) {
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
            if (dp.getMean() >= 0 && dp.getMean() < 100) {
                ai.setCloudCover(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        if (pointType.equalsIgnoreCase(DataPointType.PRECIPITATION.name())) {
            if (dp.getMean() >= 0 && dp.getMean() < 100) {
                ai.setPrecipitation(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                return;
            }
        }

        throw new IllegalStateException("couldn't update atmospheric data");
    }

}
