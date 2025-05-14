package com.belmu.belgianmobilitysolver.model;

import com.belmu.belgianmobilitysolver.utility.TimeParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StopTime {

    private final String tripId;

    private final long departureTime;

    private final String stopId;
    private final short stopSequence;

    private Stop stop;

    public StopTime(String tripId, String departureTime, String stopId, String stopSequence) {
        this.tripId = tripId;

        this.departureTime = TimeParser.convertFormattedTimeToMilliseconds(departureTime);

        this.stopId       = stopId;
        this.stopSequence = Short.parseShort(stopSequence);
    }

    public static ReferenceResolver<StopTime> resolver(Map<String, Stop> stopsMap, Map<String, List<StopTime>> stopTimesByStop, Map<String, List<StopTime>> stopTimesByTrip) {
        return stopTime -> {
            stopTime.setStop(stopsMap.get(stopTime.getStopId()));

            stopTimesByStop.computeIfAbsent(stopTime.getStopId(), k -> new ArrayList<>())
                           .add(stopTime);

            stopTimesByTrip.computeIfAbsent(stopTime.getTripId(), k -> new ArrayList<>())
                           .add(stopTime);
        };
    }

    public String getTripId() {
        return tripId;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public String getStopId() {
        return stopId;
    }

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

    public short getStopSequence() {
        return stopSequence;
    }
}
