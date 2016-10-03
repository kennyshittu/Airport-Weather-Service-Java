package com.crossover.trial.weather;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.crossover.trial.weather.daos.WeatherDao;
import com.crossover.trial.weather.domains.DataPoint;
import com.crossover.trial.weather.services.WeatherService;
import com.crossover.trial.weather.services.WeatherServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by Shittu on 02/10/2016.
 */
public class WeatherServiceImplTest {
    @Mock private WeatherDao weatherDao;

    private WeatherServiceImpl weatherService = new WeatherServiceImpl();

    @Before public void setUp() throws Exception {
        configure();
        weatherService.setWeatherDao(weatherDao);
    }

    @Test public void testGetWeather() throws Exception {
        weatherService.getWeather(anyString(), anyString());
        verify(weatherDao, times(1)).updateRequestFrequency(anyString(), anyDouble());
        verify(weatherDao, times(1)).getAirportDataIdx(anyString());
        verify(weatherDao, times(1)).getAtmosphericInformation(0);
    }

    @Test public void testGetPingData() throws Exception {
        weatherService.getPingData();
        verify(weatherDao, times(1)).getDataSize();
        verify(weatherDao, times(1)).getIataFrequency();
        verify(weatherDao, times(1)).getRadiusFrequency();
    }

    @Test public void testGetAirportData() throws Exception {
        weatherService.getPingData();
        verify(weatherDao, times(1)).getDataSize();
        verify(weatherDao, times(1)).getIataFrequency();
        verify(weatherDao, times(1)).getRadiusFrequency();
    }

    @Test public void testAddDataPoint() throws Exception {
        weatherService.addDataPoint("iataCode", "pointType", new DataPoint.Builder().build());
        verify(weatherDao, times(1)).getAirportDataIdx(anyString());
        verify(weatherDao, times(1)).getAtmosphericInformation(anyInt());
        verify(weatherDao, times(1)).updateAtmosphericInformation(any(), anyString(), any());
    }

    @Test public void testFindAirportData() throws Exception {
        weatherService.findAirportData(anyString());
        verify(weatherDao, only()).findAirportData(anyString());
        verify(weatherDao, times(1)).findAirportData(anyString());
    }

    @Test public void deleteAirport() throws Exception {
        weatherService.deleteAirport(anyString());
        verify(weatherDao, only()).deleteAirport(anyString());
        verify(weatherDao, times(1)).deleteAirport(anyString());
    }

    public ResourceConfig configure() {
        MockitoAnnotations.initMocks(this);
        return new ResourceConfig().register(WeatherServiceImpl.class)
            .register(new AbstractBinder() {
                @Override protected void configure() {
                    bind(weatherDao).to(WeatherDao.class);
                }
            });
    }
}
