package com.crossover.trial.weather;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.crossover.trial.weather.daos.WeatherDaoImpl;
import com.crossover.trial.weather.providers.WeatherProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Created by Shittu on 02/10/2016.
 */
public class WeatherDaoImplTest {
    private static final String TEST_STRING = "test";
    private static final double TEST_DOUBLE = 0.0;
    private final WeatherDaoImpl weatherDaoImpl = new WeatherDaoImpl();
    @Mock private WeatherProvider weatherProvider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        weatherDaoImpl.setWeatherProvider(weatherProvider);
    }

    @Test
    public void testAddAirport() {
        weatherDaoImpl.addAirport(TEST_STRING, TEST_DOUBLE, TEST_DOUBLE);
        verify(weatherProvider, times(2)).getAirportDatas();
        verify(weatherProvider, times(1)).getAtmosphericInformations();
    }

    @Test
    public void testUpdateRequestFrequency() {
        weatherDaoImpl.updateRequestFrequency(TEST_STRING, TEST_DOUBLE);
        verify(weatherProvider, times(2)).getRequestFrequency();
        verify(weatherProvider, times(2)).getRadiusFreq();
    }

    @Test
    public void testFindAirportData() {
        weatherDaoImpl.findAirportData(TEST_STRING);
        verify(weatherProvider, only()).getAirportDatas();
        verify(weatherProvider, times(1)).getAirportDatas();
    }

    @Test
    public void testGetAirportData() {
        weatherDaoImpl.getAirportData();
        verify(weatherProvider, only()).getAirportDatas();
        verify(weatherProvider, times(1)).getAirportDatas();
    }

    @Test
    public void testGetAirportDataIdx() {
        weatherDaoImpl.getAirportDataIdx(TEST_STRING);
        Mockito.verify(weatherProvider, times(2)).getAirportDatas();
    }

    @Test public void testGetAtmosphericInformation() {
        weatherDaoImpl.getAtmosphericInformation(0);
        verify(weatherProvider, only()).getAtmosphericInformations();
        verify(weatherProvider, times(1)).getAtmosphericInformations();
    }

    @Test
    public void testDeleteAirport() {
        weatherDaoImpl.deleteAirport(TEST_STRING);
        verify(weatherProvider, times(4)).getAirportDatas();
        verify(weatherProvider, times(1)).getAtmosphericInformations();
    }

    @Test
    public void testGetAtmosphericInformationByRadius() {
        weatherDaoImpl.getAtmosphericInformationByRadius(TEST_STRING, TEST_DOUBLE);
        verify(weatherProvider, times(2)).getAirportDatas();
    }

    @Test
    public void testGetDataSize() {
        weatherDaoImpl.getDataSize();
        verify(weatherProvider, only()).getAtmosphericInformations();
        verify(weatherProvider, times(1)).getAtmosphericInformations();
    }

    @Test
    public void testGetIataFrequency() {
        weatherDaoImpl.getIataFrequency();
        verify(weatherProvider, only()).getAirportDatas();
        verify(weatherProvider, times(1)).getAirportDatas();
    }

    @Test
    public void testGetRadiusFrequency() {
        weatherDaoImpl.getRadiusFrequency();
        verify(weatherProvider, times(2)).getRadiusFreq();
    }
}
