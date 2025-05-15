package com.belmu.belgianmobilitysolver.parser;

import com.belmu.belgianmobilitysolver.MobilitySolver;
import com.belmu.belgianmobilitysolver.model.*;

import com.opencsv.CSVReader;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;

public class DataParser {

    // <routeId, Route>
    public Map<String, Route>          routes          = new HashMap<>();
    // <stopId, Stop>
    public Map<String, Stop>           stops           = new HashMap<>();
    // <tripId, Trip>
    public Map<String, Trip>           trips           = new HashMap<>();
    // <stopId, List<StopTime>>
    public Map<String, List<StopTime>> stopTimesByStop = new HashMap<>();
    // <tripId, List<StopTime>>
    public Map<String, List<StopTime>> stopTimesByTrip = new HashMap<>();

    public DataParser() {
        final String routesDataFile    = "/routes.csv";
        final String stopsDataFile     = "/stops.csv";
        final String stopTimesDataFile = "/stop_times.csv";
        final String tripsDataFile     = "/trips.csv";

        final String[] transportCompanies = {
                "DELIJN", "SNCB", "STIB", "TEC"
        };

        // Parsing the data for all GTFS files of each transport companies and storing it in hashmaps

        long startTimeMs = System.currentTimeMillis();
        System.out.println(MobilitySolver.INFO + "Parsing GTFS transport data files.");

        for (String company : transportCompanies) {
            parseData(company + routesDataFile   , Route.class   , Route   .resolver(routes                                  ));
            parseData(company + stopsDataFile    , Stop.class    , Stop    .resolver(stops                                   ));
            parseData(company + stopTimesDataFile, StopTime.class, StopTime.resolver(stops , stopTimesByStop, stopTimesByTrip));
            parseData(company + tripsDataFile    , Trip.class    , Trip    .resolver(routes, trips                           ));
        }

        // Sorting the stop times by stop by their departure time
        for (List<StopTime> stopTimes : stopTimesByStop.values()) {
            stopTimes.sort(Comparator.comparingLong(StopTime::getDepartureTime));
        }
        // Sorting the stop times by trip by their index in the stop sequence
        for (List<StopTime> stopTimes : stopTimesByTrip.values()) {
            stopTimes.sort(Comparator.comparingInt(StopTime::getStopSequence));
        }

        long elapsedTimeMs = System.currentTimeMillis() - startTimeMs;
        System.out.println(MobilitySolver.INFO + "Parsing GTFS transport data files took " + elapsedTimeMs + " ms.");
    }

    /**
     * Parses the CSV data from the specified file for a specified class type.
     * <p>
     * Reads the file's headers first to determine the class's field names,
     * then reads each line to create an instance of said class after filling
     * each of the fields with its value (i.e.: Route.route_id = 123456789).
     *
     * @param path      string representing the data file's path in the resources folder
     * @param type      class type of the instance we want to create
     * @param solver    used to link the different entities together directly after parsing
     */
    @SuppressWarnings("unchecked")
    private <T> void parseData(String path, Class<T> type, ReferenceResolver<T> solver) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                System.out.println(MobilitySolver.ERROR + "File not found at: " + path);
                return;
            }

            // Reading the CSV file at the specified path
            CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));

            String[] headers = csvReader.readNext();
            String[] nextLine;

            while ((nextLine = csvReader.readNext()) != null) {

                // Finding the constructor that matches the CSV file's headers in length
                Constructor<T> constructor = null;
                for (Constructor<?> declaredConstructor : type.getDeclaredConstructors()) {
                    if (declaredConstructor.getParameterCount() == headers.length) {
                        constructor = (Constructor<T>) declaredConstructor;
                        break;
                    }
                }

                // Creating a new instance from the class' found constructor and adding it to the specified list
                if (constructor != null) {
                    T instance = constructor.newInstance((Object[]) nextLine);

                    // Linking the entity to its peers in the maps
                    if (solver != null) {
                        solver.resolveEntity(instance);
                    }
                } else {
                    System.out.println(MobilitySolver.ERROR + "Specified class type " + type.getName() + " does not correspond parsed data.");
                }
            }
        } catch (Exception e) {
            if (e instanceof FileNotFoundException)
                System.out.println(MobilitySolver.ERROR + "File not found at: " + path);
            else
                System.out.println(MobilitySolver.ERROR + e.getMessage());
        }
    }
}
