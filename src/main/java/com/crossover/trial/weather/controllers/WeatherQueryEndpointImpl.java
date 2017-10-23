package com.crossover.trial.weather.controllers;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.services.WeatherService;
import com.google.gson.Gson;

/**
 * The Weather App REST endpoint allows clients to query, update and check health stats. Currently, all data is
 * held in memory. The end point deploys to a single container
 *
 * @author code test administrator
 */
@Path("/query") public final class WeatherQueryEndpointImpl implements WeatherQueryEndpoint {

    /**
     * shared GSON json to object factory
     */
    private static final Gson GSON = new Gson();

    private WeatherService mWeatherService;

    @Inject
    public void setWeatherService(final WeatherService weatherService) {
        this.mWeatherService = weatherService;
    }

    @GET
    @Path("/ping")
    @Override
    public String ping() {
        return GSON.toJson(mWeatherService.getPingData());
    }

    @GET
    @Path("/weather/{iata}/{radius}")
    @Override
    public Response weather(@PathParam("iata") String iata,
        @PathParam("radius") String radiusString) {
        return Response.status(Response.Status.OK)
            .entity(GSON.toJson(mWeatherService.getWeather(iata, radiusString))).build();
    }
}
