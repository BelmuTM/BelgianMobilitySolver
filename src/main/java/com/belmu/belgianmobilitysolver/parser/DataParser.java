package com.belmu.belgianmobilitysolver.parser;

import com.belmu.belgianmobilitysolver.model.*;

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.*;

public class DataParser {

    public Map<String, Route>          routesMap    = new HashMap<>();
    public Map<String, Stop>           stopsMap     = new HashMap<>();
    public Map<String, List<StopTime>> stopTimesMap = new HashMap<>();
    public Map<String, Trip>           tripsMap     = new HashMap<>();

    public DataParser() {
        final String routesDataFile    = "/routes.csv";
        final String stopsDataFile     = "/stops.csv";
        final String stopTimesDataFile = "/stop_times.csv";
        final String tripsDataFile     = "/trips.csv";

        final String[] transportCompanies = {
                "DELIJN", "SNCB", "STIB", "TEC"
        };

        long startTimeMs = System.currentTimeMillis();
        System.out.println("Info: Parsing transport data files.");

        for (String company : transportCompanies) {
            parseData(company + routesDataFile   , Route.class   , Route   .resolver(routesMap              ));
            parseData(company + stopsDataFile    , Stop.class    , Stop    .resolver(stopsMap               ));
            parseData(company + stopTimesDataFile, StopTime.class, StopTime.resolver(stopsMap , stopTimesMap));
            parseData(company + tripsDataFile    , Trip.class    , Trip    .resolver(routesMap, tripsMap    ));
        }

        long elapsedTimeMs = System.currentTimeMillis() - startTimeMs;
        System.out.println("Info: Parsing transport data files took " + elapsedTimeMs + " ms.");
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
     * @param solver used to link the different entities together directly after parsing
     */
    @SuppressWarnings("unchecked")
    private <T> void parseData(String path, Class<T> type, ReferenceResolver<T> solver) {
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

                // Creating a new instance from the class' found constructor and adding it to the specified list
                if (constructor != null) {
                    T instance = constructor.newInstance((Object[]) nextLine);

                    // Linking the entity to its peers in the graph
                    if (solver != null) {
                        solver.resolveEntity(instance);
                    }
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
