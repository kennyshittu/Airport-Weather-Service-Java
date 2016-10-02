package com.crossover.trial.weather.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import com.crossover.trial.weather.domains.AirportData;
import com.crossover.trial.weather.domains.AtmosphericInformation;
import com.crossover.trial.weather.domains.DataPoint;
import com.crossover.trial.weather.utils.DataPointType;
import com.crossover.trial.weather.utils.WeatherUtils;
import com.crossover.trial.weather.utils.WeatherException;
import org.glassfish.jersey.process.internal.RequestScoped;

/**
 * Created by Shittu on 01/10/2016.
 */
@Named @RequestScoped public class WeatherDao {
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

    static {
        init();
    }

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
     * A dummy init method that loads hard coded data
     */
    public static void init() {

        airportData.clear();
        atmosphericInformation.clear();
        requestFrequency.clear();

        addAirport("BOS", 42.364347, -71.005181);
        addAirport("EWR", 40.6925, -74.168667);
        addAirport("JFK", 40.639751, -73.778925);
        addAirport("LGA", 40.777245, -73.872608);
        addAirport("MMU", 40.79935, -74.4148747);
    }

    /**
     * Records information about how often requests are made
     *
     * @param iata an iata code
     * @param radius query radius
     */
    public void updateRequestFrequency(String iata, Double radius) {
        AirportData airportData = findAirportData(iata);
        requestFrequency.put(airportData, requestFrequency.getOrDefault(airportData, 0) + 1);
        radiusFreq.put(radius, radiusFreq.getOrDefault(radius, 0));
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    public AirportData findAirportData(String iataCode) {
        return airportData.stream().filter(ap -> ap.getIata().equals(iataCode)).findFirst()
            .orElse(null);
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode as a string
     * @return airport data or null if not found
     */
    public int getAirportDataIdx(String iataCode) {
        System.out.println("get airport data idx 2");
        AirportData ad = findAirportData(iataCode);
        return airportData.indexOf(ad);
    }

    public AtmosphericInformation getAtmosphericInformation(int idx) {
        System.out.println("got inside get atmo info. 3");
        return atmosphericInformation.get(idx);
    }

    public void deleteAirport(String iataCode) {
       AirportData ad = findAirportData(iataCode);
        int idx = getAirportDataIdx(iataCode);
        airportData.remove(ad);
        atmosphericInformation.remove(idx);
    }

    public List<AtmosphericInformation> getAtmosphericInformationByRadius(String iata,
        double radius) {
        List<AtmosphericInformation> retval = new ArrayList<>();
        AirportData ad = findAirportData(iata);
        for (int i = 0; i < airportData.size(); i++) {
            if (WeatherUtils.calculateDistance(ad, airportData.get(i)) <= radius) {
                AtmosphericInformation ai = atmosphericInformation.get(i);
                if (ai.getCloudCover() != null || ai.getHumidity() != null
                    || ai.getPrecipitation() != null || ai.getPressure() != null
                    || ai.getTemperature() != null || ai.getWind() != null) {
                    retval.add(ai);
                }
            }
        }
        return retval;
    }

    public int getDataSize() {
        int datasize = 0;
        for (AtmosphericInformation ai : atmosphericInformation) {
            // we only count recent readings
            if (ai.getCloudCover() != null || ai.getHumidity() != null || ai.getPressure() != null
                || ai.getPrecipitation() != null || ai.getTemperature() != null
                || ai.getWind() != null) {
                // updated in the last day
                if (ai.getLastUpdateTime() > System.currentTimeMillis() - 86400000) {
                    datasize++;
                }
            }
        }
        return datasize;
    }

    public Map<String, Double> getIataFrequency() {
        Map<String, Double> freq = new HashMap<>();
        // fraction of queries
        for (AirportData data : airportData) {
            double frac = (double) requestFrequency.getOrDefault(data, 0) / requestFrequency.size();
            freq.put(data.getIata(), frac);
        }

        return freq;
    }

    public int[] getRadiusFrequency() {
        int m = radiusFreq.keySet().stream().max(Double::compare).orElse(1000.0).intValue() + 1;

        int[] hist = new int[m];
        for (Map.Entry<Double, Integer> e : radiusFreq.entrySet()) {
            int i = e.getKey().intValue() % 10;
            hist[i] += e.getValue();
        }
        return hist;
    }

    public void updateAtmosphericInformation(AtmosphericInformation ai, String pointType,
        DataPoint dp) throws WeatherException {
        System.out.println("reached here too , update atmo info 4");

        final DataPointType dptype = DataPointType.valueOf(pointType.toUpperCase());

        System.out.println("ai : " + ai);
        if (pointType.equalsIgnoreCase(DataPointType.WIND.name())) {
            System.out.println("reached here inside, update atmo info 5");

            if (dp.getMean() >= 0) {
                ai.setWind(dp);
                ai.setLastUpdateTime(System.currentTimeMillis());
                System.out.println("ai : 6" + ai);
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
            if (dp.getMean() >= 0 && dp.getMean() < 100) {
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
