package com.crossover.trial.weather;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.crossover.trial.weather.controllers.WeatherCollectorEndpointImpl;
import com.crossover.trial.weather.controllers.WeatherQueryEndpointImpl;
import com.crossover.trial.weather.services.WeatherService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WeatherEndpointTest {

    @Mock private WeatherService weatherService;

    private final WeatherQueryEndpointImpl queryEndpoint = new WeatherQueryEndpointImpl();

    private final WeatherCollectorEndpointImpl collectorEndpoint = new WeatherCollectorEndpointImpl();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        queryEndpoint.setWeatherService(weatherService);
        collectorEndpoint.setWeatherService(weatherService);
    }

    @Test
    public void testPing() {
        queryEndpoint.ping();
        verify(weatherService, only()).getPingData();
        verify(weatherService, times(1)).getPingData();
    }

    @Test
    public void testGet() {
        queryEndpoint.weather(anyString(), anyString());
        verify(weatherService, only()).getWeather(anyString(), anyString());
        verify(weatherService, times(1)).getWeather(anyString(), anyString());
    }

    @Test
    public void testGetNearby() throws Exception {
        collectorEndpoint.updateWeather(anyString(), anyString(), anyString());
        verify(weatherService, only()).addDataPoint(anyString(), anyString(), any());
        verify(weatherService, times(1)).addDataPoint(anyString(), anyString(), any());
    }

    @Test
    public void testGetAirport() {
        collectorEndpoint.getAirport(anyString());
        verify(weatherService, only()).findAirportData(anyString());
        verify(weatherService, times(1)).findAirportData(anyString());
    }

    @Test
    public void testGetAirports() {
        collectorEndpoint.getAirports();
        verify(weatherService, only()).getAirportData();
        verify(weatherService, times(1)).getAirportData();
    }

    @Test
    public void testAddAirport() {
        collectorEndpoint
            .addAirport(anyString(), String.valueOf(anyDouble()), String.valueOf(anyDouble()));
        verify(weatherService, only()).addAirport(anyString(), anyDouble(), anyDouble());
        verify(weatherService, times(1)).addAirport(anyString(), anyDouble(), anyDouble());
    }

    @Test
    public void deleteAirport() {
        collectorEndpoint.deleteAirport(anyString());
        verify(weatherService, only()).deleteAirport(anyString());
        verify(weatherService, times(1)).deleteAirport(anyString());
    }

}
