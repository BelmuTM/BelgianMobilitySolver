package com.belmu.belgianmobilitysolver.pathfinder;

import com.belmu.belgianmobilitysolver.model.Departure;
import com.belmu.belgianmobilitysolver.model.StopTime;
import com.belmu.belgianmobilitysolver.parser.DataParser;

import java.time.LocalTime;
import java.util.*;

public class Pathfinder {

    private final DataParser data;

    public Pathfinder(DataParser dataParser) {
        this.data = dataParser;
    }

    private Map<String, List<Departure>> gatherDeparturesByStop() {
        Map<String, List<Departure>> departuresByStop = new HashMap<>();

        for (List<StopTime> stopTimes : data.stopTimesMap.values()) {

            for (StopTime stopTime : stopTimes) {
                Departure departure = new Departure(stopTime.getTripId(), stopTime.getDepartureTime(), stopTime.getStopSequence());

                departuresByStop
                        .computeIfAbsent(stopTime.getStopId(), k -> new ArrayList<>())
                        .add(departure);
            }
        }

        for (List<Departure> list : departuresByStop.values()) {
            list.sort(Comparator.comparing(Departure::getTime));
        }

        return departuresByStop;
    }

    public List<Edge> findPath(String startStop, String endStop, LocalTime departureTime) {
        Map<String, List<Departure>> departuresByStop = gatherDeparturesByStop();


    }

}
