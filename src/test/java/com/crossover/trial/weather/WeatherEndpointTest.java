package com.crossover.trial.weather;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import com.crossover.trial.weather.controllers.WeatherCollectorEndpointImpl;
import com.crossover.trial.weather.controllers.WeatherQueryEndpoint;
import com.crossover.trial.weather.controllers.WeatherQueryEndpointImpl;
import com.crossover.trial.weather.services.WeatherService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WeatherEndpointTest {

    @Mock private WeatherService weatherService;

    private WeatherQueryEndpointImpl queryEndpoint = new WeatherQueryEndpointImpl();

    private WeatherCollectorEndpointImpl collectorEndpoint = new WeatherCollectorEndpointImpl();

    @Before public void setUp() throws Exception {
        configure();
        queryEndpoint.setWeatherService(weatherService);
        collectorEndpoint.setWeatherService(weatherService);
    }

    @Test public void testPing() throws Exception {
        queryEndpoint.ping();
        verify(weatherService, only()).getPingData();
    }

    @Test public void testGet() throws Exception {
        queryEndpoint.weather(anyString(), anyString());
        verify(weatherService, only()).getWeather(anyString(), anyString());
    }

    @Test public void testGetNearby() throws Exception {
        collectorEndpoint.updateWeather(anyString(), anyString(), anyString());
        verify(weatherService, only()).addDataPoint(anyString(), anyString(), any());
    }

    @Test public void testGetAirport() throws Exception {
        collectorEndpoint.getAirport(anyString());
        verify(weatherService, only()).findAirportData(anyString());

    }

    @Test public void testGetAirports() throws Exception {
        collectorEndpoint.getAirports();
        verify(weatherService, only()).getAirportData();

    }

    @Test public void testAddAirport() throws Exception {
        collectorEndpoint
            .addAirport(anyString(), String.valueOf(anyDouble()), String.valueOf(anyDouble()));
        verify(weatherService, only()).addAirport(anyString(), anyDouble(), anyDouble());
    }

    //    @Test public void deleteAirport() throws Exception {
    //        collectorEndpoint.deleteAirport(anyString());
    //        verify(weatherService, only()).getAirportData();
    //
    //    }

    public ResourceConfig configure() {
        MockitoAnnotations.initMocks(this);
        return new ResourceConfig().register(WeatherQueryEndpoint.class)
            .register(new AbstractBinder() {
                @Override protected void configure() {
                    bind(weatherService).to(WeatherService.class);
                }
            });
    }

}
