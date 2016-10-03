package com.crossover.trial.weather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

/**
 * A simple airport loader which reads a file from disk and sends entries to the webservice
 * <p>
 * TODO: Implement the Airport Loader
 *
 * @author code test administrator
 */
public class AirportLoader {

    public static final int NAME_INDEX = 1;
    public static final int CITY_INDEX = 2;
    public static final int COUNTRY_INDEX = 3;
    public static final int IATA_INDEX = 4;
    public static final int ICAO_INDEX = 5;
    public static final int LAT_INDEX = 6;
    public static final int LONG_INDEX = 7;
    public static final int ALT_INDEX = 8;
    public static final int TIMEZONE_INDEX = 9;
    public static final int DST_INDEX = 10;
    public static final String SEPARATOR = ",";
    private static final String BASE_URI = "http://localhost:9090";
    public static final String FILE_NAME = "airports.dat";
    /**
     * end point for read queries
     */
    private WebTarget query;

    /**
     * end point to supply updates
     */
    private WebTarget collect;

    public AirportLoader() {
        Client client = ClientBuilder.newClient();
        query = client.target(BASE_URI + "/query");
        collect = client.target(BASE_URI);
    }

    public static void main(String args[]) {
        try {
            Optional<URL> fileUrl = Optional
                .ofNullable(AirportLoader.class.getClassLoader().getResource(FILE_NAME));

            if (fileUrl.isPresent()) {
                File airportDataFile = new File(fileUrl.get().getPath());

                if (!airportDataFile.exists() || airportDataFile.length() == 0) {
                    System.err.println(airportDataFile + " is not a valid input");
                    System.exit(1);
                }

                AirportLoader al = new AirportLoader();
                al.upload(new FileInputStream(airportDataFile));
                System.exit(0);
            }

            System.err.println("airports.dat file not found in resources.");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void upload(InputStream airportDataStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(airportDataStream));
        String l;
        while ((l = reader.readLine()) != null) {
            String[] b = l.split(SEPARATOR);
            WebTarget path = collect.path(String
                .format("/collect/airport/%s/%s/%s", b[IATA_INDEX], b[LAT_INDEX], b[LONG_INDEX])
                .replace("\"", ""));
            path.request().post(Entity.entity(null, "application/json"));
        }
    }
}
