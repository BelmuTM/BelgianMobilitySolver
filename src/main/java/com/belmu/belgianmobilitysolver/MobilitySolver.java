package com.belmu.belgianmobilitysolver;

import com.belmu.belgianmobilitysolver.cost.ExclusionCostFunction;
import com.belmu.belgianmobilitysolver.model.Stop;
import com.belmu.belgianmobilitysolver.model.TransportType;
import com.belmu.belgianmobilitysolver.parser.DataParser;
import com.belmu.belgianmobilitysolver.pathfinder.Node;
import com.belmu.belgianmobilitysolver.pathfinder.Pathfinder;
import com.belmu.belgianmobilitysolver.utility.TimeParser;

import java.util.HashSet;
import java.util.Set;

public class MobilitySolver {

    public static final String INFO  = "[INFO]: ";
    public static final String ERROR = "[ERROR]: ";

    public static void main(String[] args) {

        if (args.length < 3) {
            System.out.println(ERROR + "Please indicate a departure stop, destination stop and departure time.");
            return;
        }

        // Parsing the GTFS data on runtime
        DataParser parser     = new DataParser();
        Pathfinder pathfinder = new Pathfinder(parser);

        String outputString = "Finding shortest path from " + args[0] + " to " + args[1];

        long startTimeMs = System.currentTimeMillis();
        System.out.println(INFO + outputString + ".");

        // Fetching the start and end stops by their specified names
        Stop startStop = Stop.getStopByName(parser.stops, args[0]);
        Stop endStop   = Stop.getStopByName(parser.stops, args[1]);

        // Making sure the command arguments were correctly entered

        if (startStop == null) {
            System.out.println(ERROR + "Could not find the specified departure stop.");
            return;
        }

        if (endStop == null) {
            System.out.println(ERROR + "Could not find the specified destination stop.");
            return;
        }

        // Parsing the departure time to milliseconds
        if (!TimeParser.isValidTimeFormat(args[2])) {
            System.out.println(ERROR + "Please encode the departure time in the HH:mm:ss format.");
            return;
        }
        long departureTime = TimeParser.convertFormattedTimeToMilliseconds(args[2]);

        Set<TransportType> excludedTransportTypes = fetchExcludedTransportTypes(args);
        // Declaring the cost function used to weigh the graph's nodes
        ExclusionCostFunction costFunction = new ExclusionCostFunction(excludedTransportTypes);

        Node destination = pathfinder.findShortestGTFSPath(
                startStop.getStopId(), endStop.getStopId(), departureTime, costFunction
        );

        // Discarding the output if no path was found
        if (destination != null) {
            pathfinder.outputShortestGTFSPath(destination);
        }

        long elapsedTimeMs = System.currentTimeMillis() - startTimeMs;
        System.out.println(INFO + outputString + " took " + elapsedTimeMs + " ms.");
    }

    private static Set<TransportType> fetchExcludedTransportTypes(String[] args) {
        Set<TransportType> excludedTransportTypes = new HashSet<>();

        for (int i = 3; i < args.length; i++) {

            switch (args[i]) {
                case "-bus":
                    excludedTransportTypes.add(TransportType.BUS);
                    break;
                case "-metro":
                    excludedTransportTypes.add(TransportType.METRO);
                    break;
                case "-train":
                    excludedTransportTypes.add(TransportType.TRAIN);
                    break;
                case "-tram":
                    excludedTransportTypes.add(TransportType.TRAM);
                    break;
                case "-walk":
                    excludedTransportTypes.add(TransportType.WALK);
                    break;
                default:
                    break;
            }
        }
        return excludedTransportTypes;
    }
}
