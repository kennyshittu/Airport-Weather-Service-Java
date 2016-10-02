package com.crossover.trial.weather;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.crossover.trial.weather.daos.WeatherDao;
import com.crossover.trial.weather.services.WeatherService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by Shittu on 02/10/2016.
 */
public class WeatherServiceTest {
    @Mock private WeatherDao weatherDao;

    private WeatherService weatherService = new WeatherService();

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

//    @Test public void testAddDataPoint() throws Exception {
//        weatherService.addDataPoint(anyString(), anyString(), any());
//        verify(weatherDao, times(1)).getAirportDataIdx(anyString());
//        verify(weatherDao, times(1)).getAtmosphericInformation(anyInt());
//        verify(weatherDao, times(1)).updateAtmosphericInformation(any(), anyString(), any());
//    }

    @Test public void testFindAirportData() throws Exception {
        weatherService.findAirportData(anyString());
        verify(weatherDao, times(1)).findAirportData(anyString());
    }

    public ResourceConfig configure() {
        MockitoAnnotations.initMocks(this);
        return new ResourceConfig().register(WeatherService.class)
            .register(new AbstractBinder() {
                @Override protected void configure() {
                    bind(weatherDao).to(WeatherDao.class);
                }
            });
    }
}
