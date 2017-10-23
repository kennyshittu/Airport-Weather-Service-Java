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
import com.crossover.trial.weather.services.WeatherServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by Shittu on 02/10/2016.
 */
public class WeatherServiceImplTest {
    private static final String TEST_STRING = "test";
    private static final DataPoint DATA_POINT = new DataPoint.Builder().build();
    @Mock private WeatherDao weatherDao;

    private final WeatherServiceImpl weatherService = new WeatherServiceImpl();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        weatherService.setWeatherDao(weatherDao);
    }

    @Test
    public void testGetWeather() {
        weatherService.getWeather(anyString(), anyString());
        verify(weatherDao, times(1)).updateRequestFrequency(anyString(), anyDouble());
        verify(weatherDao, times(1)).getAirportDataIdx(anyString());
        verify(weatherDao, times(1)).getAtmosphericInformation(0);
    }

    @Test
    public void testGetPingData() {
        weatherService.getPingData();
        verify(weatherDao, times(1)).getDataSize();
        verify(weatherDao, times(1)).getIataFrequency();
        verify(weatherDao, times(1)).getRadiusFrequency();
    }

    @Test
    public void testGetAirportData() {
        weatherService.getAirportData();
        verify(weatherDao, only()).getAirportData();
        verify(weatherDao, times(1)).getAirportData();
    }

    @Test
    public void testAddAirport() {
        weatherService.addAirport(anyString(), anyDouble(), anyDouble());
        verify(weatherDao, only()).addAirport(anyString(), anyDouble(), anyDouble());
        verify(weatherDao, times(1)).addAirport(anyString(), anyDouble(), anyDouble());
    }

    @Test
    public void testAddDataPoint() throws Exception {
        weatherService.addDataPoint(TEST_STRING, TEST_STRING, DATA_POINT);
        verify(weatherDao, times(1)).getAirportDataIdx(anyString());
        verify(weatherDao, times(1)).getAtmosphericInformation(anyInt());
        verify(weatherDao, times(1)).updateAtmosphericInformation(any(), anyString(), any());
    }

    @Test
    public void testFindAirportData() {
        weatherService.findAirportData(anyString());
        verify(weatherDao, only()).findAirportData(anyString());
        verify(weatherDao, times(1)).findAirportData(anyString());
    }

    @Test
    public void testDeleteAirport() {
        weatherService.deleteAirport(anyString());
        verify(weatherDao, only()).deleteAirport(anyString());
        verify(weatherDao, times(1)).deleteAirport(anyString());
    }
}
