package com.crossover.trial.weather.controllers;

import com.crossover.trial.weather.services.WeatherService;
import com.google.gson.Gson;

import javax.annotation.Resource;
import javax.inject.Inject;
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
@Resource
@Path("/collect") public class WeatherCollectorEndpointImpl implements WeatherCollectorEndpoint {
    public final static Logger LOGGER =
        Logger.getLogger(WeatherCollectorEndpointImpl.class.getName());

    /**
     * shared gson json to object factory
     */
    public final static Gson gson = new Gson();

    private WeatherService mWeatherService;

    public WeatherCollectorEndpointImpl() {}

    @Inject public void setWeatherService(final WeatherService weatherService) {
        this.mWeatherService = weatherService;
    }

    @Override public Response ping() {
        return Response.status(Response.Status.OK).entity("ready").build();
    }

    @Override public Response updateWeather(@PathParam("iata") String iataCode,
        @PathParam("pointType") String pointType, String datapointJson) {
        System.out.println("reached here collect post method");
//        try {
//            mWeatherService
//                .addDataPoint(iataCode, pointType, gson.fromJson(datapointJson, DataPoint.class));
//            System.out.println("reached here last 7");
//        } catch (WeatherException e) {
//            e.printStackTrace();
//        }
        return Response.status(Response.Status.OK).build();
    }


    @Override public Response getAirports() {
        return Response.status(Response.Status.OK).entity(mWeatherService.getAirportData())
            .build();
    }


    @Override public Response getAirport(@PathParam("iata") String iata) {
        return Response.status(Response.Status.OK).entity(mWeatherService.findAirportData(iata))
            .build();
    }


    @Override
    public Response addAirport(@PathParam("iata") String iata, @PathParam("lat") String latString,
        @PathParam("long") String longString) {
        System.out.println("kenny shittu");
        mWeatherService.addAirport(iata, Double.valueOf(latString), Double.valueOf(longString));
        return Response.status(Response.Status.OK).build();
    }


    @Override public Response deleteAirport(@PathParam("iata") String iata) {
        mWeatherService.deleteAirport(iata);
        return Response.status(Response.Status.OK).build();
    }

    @Override public Response exit() {
        System.exit(0);
        return Response.noContent().build();
    }
}
