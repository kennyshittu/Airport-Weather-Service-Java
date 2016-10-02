package com.crossover.trial.weather;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.*;

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
    public static final int LOG_INDEX = 7;
    public static final int ALT_INDEX = 8;
    public static final int TIMEZONE_INDEX = 9;
    public static final int DST_INDEX = 10;
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
        query = client.target("http://localhost:8080/query");
        collect = client.target("http://localhost:8080/collect");
    }

    public static void main(String args[]){
        try {
            File airportDataFile = new File(
                AirportLoader.class.getClassLoader().getResource("airports.dat").getPath());
            if (!airportDataFile.exists() || airportDataFile.length() == 0) {
                System.err.println(airportDataFile + " is not a valid input");
                System.exit(1);
            }
            AirportLoader al = new AirportLoader();
            al.upload(new FileInputStream(airportDataFile));
            System.exit(0);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void upload(InputStream airportDataStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(airportDataStream));
        String l = null;
        String splitBy = ",";
        while ((l = reader.readLine()) != null) {
            String[] b = l.split(splitBy);
            System.out.println(b[0]);
            System.out.println(b[NAME_INDEX]);
            System.out.println(b[CITY_INDEX]);
            System.out.println(b[COUNTRY_INDEX]);
            System.out.println(b[IATA_INDEX]);
            System.out.println(b[ICAO_INDEX]);
            System.out.println(b[LAT_INDEX]);
            System.out.println(b[LOG_INDEX]);
            System.out.println(b[ALT_INDEX]);
            System.out.println(b[TIMEZONE_INDEX]);
            System.out.println(b[DST_INDEX]);
            break;
        }
    }
}
