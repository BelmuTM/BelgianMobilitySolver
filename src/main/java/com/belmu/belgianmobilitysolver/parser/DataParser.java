package com.belmu.belgianmobilitysolver.parser;

import com.belmu.belgianmobilitysolver.model.Route;
import com.belmu.belgianmobilitysolver.model.Stop;
import com.belmu.belgianmobilitysolver.model.StopTime;
import com.belmu.belgianmobilitysolver.model.Trip;

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.*;

public class DataParser {

    Map<String, Route>          routesMap    = new HashMap<>();
    Map<String, Stop>           stopsMap     = new HashMap<>();
    Map<String, List<StopTime>> stopTimesMap = new HashMap<>();
    Map<String, Trip>           tripsMap     = new HashMap<>();

    public DataParser() {
        final String routesDataFile    = "/routes.csv";
        final String stopsDataFile     = "/stops.csv";
        final String stopTimesDataFile = "/stop_times.csv";
        final String tripsDataFile     = "/trips.csv";

        final String[] transportCompanies = {
                "DELIJN", "SNCB", "STIB", "TEC"
        };

        List<Route>    routesList    = new ArrayList<>();
        List<Stop>     stopsList     = new ArrayList<>();
        List<StopTime> stopTimesList = new ArrayList<>();
        List<Trip>     tripsList     = new ArrayList<>();

        long start = System.currentTimeMillis();

        System.out.println("Info: Parsing transport data files.");

        for (String company : transportCompanies) {
            parseData(company + routesDataFile   , Route.class   , routesList   );
            parseData(company + stopsDataFile    , Stop.class    , stopsList    );
            parseData(company + stopTimesDataFile, StopTime.class, stopTimesList);
            parseData(company + tripsDataFile    , Trip.class    , tripsList    );
        }

        for (Route route : routesList) {
            routesMap.put(route.getRouteId(), route);
        }

        for (Stop stop : stopsList) {
            stopsMap.put(stop.getStopId(), stop);
        }

        for (Trip trip : tripsList) {
            trip.setRoute(Route.getRouteFromId(routesMap, trip.getRouteId()));
            tripsMap.put(trip.getTripId(), trip);
        }

        for (StopTime stopTime : stopTimesList) {
            stopTime.setStop(Stop.getStopFromId(stopsMap, stopTime.getStopId()));

            List<StopTime> times = stopTimesMap.get(stopTime.getTripId()) == null ? new ArrayList<>() : stopTimesMap.get(stopTime.getTripId());

            times.add(stopTime);

            stopTimesMap.put(stopTime.getTripId(), times);
        }

        List<StopTime> times = stopTimesMap.get("DELIJN-0");

        for (StopTime stopTime : times) {
            System.out.println(stopTime.getTripId() + " " + stopTime.getStopId());
        }

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Info: Parsing transport data files took " + elapsed + " ms.");
    }

    /**
     * Parses the CSV data from the specified file for a specified class type.
     * <p>
     * Reads the file's headers first to determine the class's field names,
     * then reads each line to create an instance of said class after filling
     * each of the fields with its value (i.e.: Route.route_id = 123456789).
     *
     * @param path the string designating the data file's path in the resources folder
     * @param type the class type of the instance we want to create
     * @param list the list in which we store the created instance
     */
    private <T> void parseData(String path, Class<T> type, List<T> list) {
        try {
            FileReader fileReader = new FileReader(
                    Objects.requireNonNull(getClass().getClassLoader().getResource(path)).getPath()
            );

            // Reading the CSV file at the specified path
            CSVReader csvReader = new CSVReader(fileReader);

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

                if (constructor != null) {
                    T instance = constructor.newInstance((Object[]) nextLine);
                    list.add(instance);
                } else {
                    System.out.println("Error: Specified class type " + type.getName() + " does not correspond parsed data.");
                }
            }
        } catch (Exception e) {
            if (e instanceof FileNotFoundException)
                System.out.println("Error: File not found at: " + path);
            else
                System.out.println("Error: " + e.getMessage());
        }
    }
}
