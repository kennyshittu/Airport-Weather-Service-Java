package com.crossover.trial.weather;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.crossover.trial.weather.controllers.WeatherCollectorEndpointImpl;
import com.crossover.trial.weather.controllers.WeatherQueryEndpointImpl;
import com.crossover.trial.weather.daos.WeatherDao;
import com.crossover.trial.weather.daos.WeatherDaoImpl;
import com.crossover.trial.weather.services.WeatherService;
import com.crossover.trial.weather.services.WeatherServiceImpl;
import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.HttpServerFilter;
import org.glassfish.grizzly.http.server.HttpServerProbe;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;


/**
 * This main method will be use by the automated functional grader. You shouldn't move this class or remove the
 * main method. You may change the implementation, but we encourage caution.
 *
 * @author code test administrator
 */
public class WeatherServer {

    private static final String BASE_URL = "http://localhost:9090/";

    public static void main(String[] args) {
        try {
            System.out.println("Starting Weather App local testing server: " + BASE_URL);

            final ResourceConfig resourceConfig = new ResourceConfig();
            resourceConfig.register(WeatherCollectorEndpointImpl.class);
            resourceConfig.register(WeatherQueryEndpointImpl.class);
            resourceConfig.register(new AbstractBinder() {
                @Override protected void configure() {
                    bind(WeatherDaoImpl.class).to(WeatherDao.class);
                    bind(WeatherServiceImpl.class).to(WeatherService.class);
                }
            });

            HttpServer server = GrizzlyHttpServerFactory
                .createHttpServer(URI.create(BASE_URL), resourceConfig, false);
            Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));

            HttpServerProbe probe = new HttpServerProbe.Adapter() {
                public void onRequestReceiveEvent(HttpServerFilter filter, Connection connection,
                    Request request) {
                    System.out.println(request.getRequestURI());
                }
            };
            server.getServerConfiguration().getMonitoringConfig().getWebServerConfig()
                .addProbes(probe);

            // the autograder waits for this output before running automated tests, please don't remove it
            server.start();
            System.out.println(format("Weather Server started.\n url=%s\n", BASE_URL));
            //load data airports.dat
            AirportLoader.main(args);

            // blocks until the process is terminated
            Thread.currentThread().join();
            server.shutdown();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(WeatherServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
