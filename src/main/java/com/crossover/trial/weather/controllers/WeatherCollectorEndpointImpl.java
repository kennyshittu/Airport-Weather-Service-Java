package com.crossover.trial.weather.controllers;

import com.crossover.trial.weather.domains.DataPoint;
import com.crossover.trial.weather.services.WeatherService;
import com.crossover.trial.weather.utils.WeatherException;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport weather collection
 * sites via secure VPN.
 *
 * @author code test administrator
 */
@Path("/collect") public final class WeatherCollectorEndpointImpl implements WeatherCollectorEndpoint {
    public final static Logger LOGGER =
        Logger.getLogger(WeatherCollectorEndpointImpl.class.getName());

    /**
     * shared GSON json to object factory
     */
    public final static Gson GSON = new Gson();

    private WeatherService mWeatherService;

    public WeatherCollectorEndpointImpl() {}

    @Inject public void setWeatherService(final WeatherService weatherService) {
        this.mWeatherService = weatherService;
    }

    @GET
    @Path("/ping")
    @Override public Response ping() {
        return Response.status(Response.Status.OK).entity("ready").build();
    }

    @POST
    @Path("/weather/{iata}/{pointType}")
    @Override public Response updateWeather(@PathParam("iata") String iataCode,
        @PathParam("pointType") String pointType, String datapointJson) {
        try {
            mWeatherService
                .addDataPoint(iataCode, pointType, GSON.fromJson(datapointJson, DataPoint.class));
        } catch (WeatherException e) {
            e.printStackTrace();
        }
        return Response.status(Response.Status.OK).build();
    }


    @GET
    @Path("/airports")
    @Override public Response getAirports() {
        return Response.status(Response.Status.OK).entity(GSON.toJson(mWeatherService.getAirportData()))
            .build();
    }


    @GET
    @Path("/airport/{iata}")
    @Override public Response getAirport(@PathParam("iata") String iata) {
        return Response.status(Response.Status.OK).entity(mWeatherService.findAirportData(iata))
            .build();
    }


    @POST
    @Path("/airport/{iata}/{lat}/{long}")
    @Override
    public Response addAirport(@PathParam("iata") String iata, @PathParam("lat") String latString,
        @PathParam("long") String longString) {
        mWeatherService.addAirport(iata, Double.valueOf(latString), Double.valueOf(longString));
        return Response.status(Response.Status.OK).build();
    }


    @DELETE
    @Path("/airport/{iata}")
    @Override public Response deleteAirport(@PathParam("iata") String iata) {
        mWeatherService.deleteAirport(iata);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/exit")
    @Override public Response exit() {
        System.exit(0);
        return Response.noContent().build();
    }
}
