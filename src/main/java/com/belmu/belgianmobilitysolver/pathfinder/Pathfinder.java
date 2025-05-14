package com.belmu.belgianmobilitysolver.pathfinder;

import com.belmu.belgianmobilitysolver.MobilitySolver;
import com.belmu.belgianmobilitysolver.model.Route;
import com.belmu.belgianmobilitysolver.model.Stop;
import com.belmu.belgianmobilitysolver.model.StopTime;
import com.belmu.belgianmobilitysolver.parser.DataParser;
import com.belmu.belgianmobilitysolver.utility.TimeParser;

import java.util.*;

public class Pathfinder {

    private static final double MAX_WALK_DISTANCE_KM = 1.0;

    private static final double WALK_SPEED_KPH = 5.0; // km/h

    private final DataParser data;

    private final SpatialHashGrid hashGrid;

    public Pathfinder(DataParser dataParser) {
        this.data = dataParser;

        // Building the spatial hash grid of cells (buckets) storing stops per fixed unit of area
        hashGrid = new SpatialHashGrid();
        hashGrid.build(this.data.stops.values());
    }

    /**
     * Performs a binary search to find the departure time which is the earliest
     * possible after the current time. Assumes that the stop times list is pre-sorted
     * by the departure times.
     *
     * @param stopTimes the list of possible stop times
     * @param currentTime the current time in milliseconds
     * @return the index of the departure that is the earliest after the current time
     */
    public int findNextDepartureIndex(List<StopTime> stopTimes, long currentTime) {
        int lowIndex  = 0;
        int highIndex = stopTimes.size() - 1;

        int result = -1;
        while (lowIndex <= highIndex) {
            int  middleIndex = (lowIndex + highIndex) / 2;
            long middleTime  = stopTimes.get(middleIndex).getDepartureTime();

            if (middleTime >= currentTime) {
                result    = middleIndex;
                highIndex = middleIndex - 1;
            } else {
                lowIndex = middleIndex + 1;
            }
        }
        return result;
    }

    public Node findShortestGTFSPath(String startStopId, String endStopId, long departureTime) {

        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingLong(s -> s.time));
        queue.add(new Node(null, startStopId, null, departureTime, 0, false));

        // Keeping track of each stop's departure/arrival times to compare the current node to candidate nodes
        Map<String, Long> times = new HashMap<>();
        times.put(startStopId, departureTime);

        Node destination = null;

        // Keep iterating if there are nodes to traverse
        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // If the current stop corresponds to the specified destination, end the pathfinder
            if (current.stopId.equals(endStopId)) {
                destination = current;
                break;
            }

            // Skip this node if we already have a better arrival time
            if (current.time > times.getOrDefault(current.stopId, Long.MAX_VALUE))
                continue;

            /*
                1) Transit edges: from the current node's stop, after the current node's arrival time
             */
            List<StopTime> departures = data.stopTimesByStop.get(current.stopId);

            if (departures == null || departures.isEmpty())
                continue;

            // Finding the earliest departure after the current time
            int index = findNextDepartureIndex(departures, current.time);
            // If a next departure was found
            if (index != -1) {
                StopTime departure = departures.get(index);
                Route    route     = data.trips.get(departure.getTripId()).getRoute();

                // Fetching all the stop times on the next departure's trip
                List<StopTime> tripTimes = data.stopTimesByTrip.get(departure.getTripId());
                // Finding the next departure's index in the trip
                int position = tripTimes.indexOf(departure);

                if (position != -1) {
                    // Enqueuing all subsequent stops on the trip
                    for (int j = position + 1; j < tripTimes.size(); j++) {
                        StopTime next     = tripTimes.get(j);
                        String nextStopId = next.getStopId();

                        long arrivalTime = next.getDepartureTime();
                        long bestTime    = times.getOrDefault(nextStopId, Long.MAX_VALUE);

                        // Relaxation check (prevents enqueuing slower paths)
                        if (arrivalTime < bestTime) {
                            times.put(nextStopId, arrivalTime);
                            queue.add(new Node(
                                    current, nextStopId, route, arrivalTime, departure.getDepartureTime(), false
                            ));
                        }
                    }
                }
            }

            /*
                2) Walking edges: from the current node's stop, at a constant walking speed
                with a maximum acceptable walking distance

             */
            Stop currentStop = data.stops.get(current.stopId);

            List<Stop> nearbyStops = hashGrid.getNearbyStops(currentStop, MAX_WALK_DISTANCE_KM);

            for (Stop nearbyStop : nearbyStops) {
                double distance = DistanceFunction.computeDistance(currentStop, nearbyStop);
                // Discarding stops that are beyond the acceptable walking distance
                if (distance > MAX_WALK_DISTANCE_KM) continue;

                long walkTime    = (long) ((distance / WALK_SPEED_KPH) * 60 * 60 * 1000); // milliseconds
                long arrivalTime = current.time + walkTime;
                long bestTime    = times.getOrDefault(nearbyStop.getStopId(), Long.MAX_VALUE);

                // Relaxation check (prevents enqueuing slower paths)
                if (arrivalTime < bestTime) {
                    times.put(nearbyStop.getStopId(), arrivalTime);
                    queue.add(new Node(
                            current, nearbyStop.getStopId(), null, arrivalTime, 0, true
                    ));
                }
            }
        }

        if (destination == null) {
            System.out.println(MobilitySolver.ERROR + "No path found.");
        }

        return destination;
    }

    public void outputShortestGTFSPath(Node destination) {
        Node current = destination;

        final String separator     = "--------------------------------------------------";
        final String walkFormat    = "Walk from %s (%s) to %s (%s)";
        final String transitFormat = "Take %s %s %s from %s (%s) to %s (%s)";

        List<String> route = new ArrayList<>();

        // Backtracking down the path to output each of the trips (stop 1, departure time, stop 2, arrival time)
        while (current.previous != null) {
            Stop   departureStop = data.stops.get(current.previous.stopId);
            String departureTime = TimeParser.convertMillisecondsToFormattedTime(current.previous.time);

            String departureStopName = departureStop.getName();
            String arrivalStopName   = data.stops.get(current.stopId).getName();

            // Using different formats depending on whether the trip is a walk or a transit
            if (current.isWalking) {
                String arrivalTime = TimeParser.convertMillisecondsToFormattedTime(current.time);

                route.add(walkFormat.formatted(
                        departureStopName, departureTime, arrivalStopName, arrivalTime
                ));
            } else {
                String companyName   = current.route.getRouteId().split("-")[0];
                String transportType = current.route.getTransportType().toString();
                String shortName     = current.route.getShortName();

                String arrivalTime = TimeParser.convertMillisecondsToFormattedTime(current.previousTime);

                route.add(transitFormat.formatted(
                        companyName, transportType, shortName, departureStopName, departureTime, arrivalStopName, arrivalTime
                ));
            }
            // Moving down one node
            current = current.previous;
        }

        // Reversing the list of string outputs to start at the beginning of the path
        Collections.reverse(route);

        // Printing a line separator for readability
        System.out.println(separator);

        // Printing all outputs in order
        for (String trip : route) {
            System.out.println(trip);
        }

        System.out.println(separator);
    }
}
