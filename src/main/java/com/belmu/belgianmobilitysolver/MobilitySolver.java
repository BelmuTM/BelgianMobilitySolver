package com.belmu.belgianmobilitysolver;

import com.belmu.belgianmobilitysolver.model.Stop;
import com.belmu.belgianmobilitysolver.parser.DataParser;
import com.belmu.belgianmobilitysolver.pathfinder.Node;
import com.belmu.belgianmobilitysolver.pathfinder.Pathfinder;
import com.belmu.belgianmobilitysolver.utility.TimeParser;

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

        if (!TimeParser.isValidTimeFormat(args[2])) {
            System.out.println(ERROR + "Please encode the departure time in the HH:mm:ss format.");
            return;
        }

        long departureTime = TimeParser.convertFormattedTimeToMilliseconds(args[2]);

        Node destination = pathfinder.findShortestGTFSPath(startStop.getStopId(), endStop.getStopId(), departureTime);

        // Discarding the output if no path was found
        if (destination != null) {
            pathfinder.outputShortestGTFSPath(destination);
        }

        long elapsedTimeMs = System.currentTimeMillis() - startTimeMs;
        System.out.println(INFO + outputString + " took " + elapsedTimeMs + " ms.");
    }
}
