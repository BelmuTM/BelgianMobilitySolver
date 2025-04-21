package com.belmu.belgianmobilitysolver.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StopTime {

    private final String tripId;

    private final LocalTime departureTime;

    private final String stopId;
    private final short stopSequence;

    private Stop stop;

    public StopTime(String tripId, String departureTime, String stopId, String stopSequence) {
        this.tripId = tripId;

        this.departureTime = parseDepartureTime(departureTime);

        this.stopId       = stopId;
        this.stopSequence = Short.parseShort(stopSequence);
    }

    public static ReferenceResolver<StopTime> resolver(Map<String, Stop> stopsMap, Map<String, List<StopTime>> stopTimesMap) {
        return stopTime -> {
            stopTime.setStop(stopsMap.get(stopTime.getStopId()));
            stopTimesMap
                    .computeIfAbsent(stopTime.getTripId(), k -> new ArrayList<>())
                    .add(stopTime);
        };
    }

    private LocalTime parseDepartureTime(String departureTime) {
        // Replacing hours with a value of 24 by 00 (midnight) to respect the ISO format (0-23)
        String hours      = departureTime.split(":")[0];
        short  hoursValue = Short.parseShort(hours);

        if (hoursValue > 23) {
            // Formatting the hours to be in the [0;23] range with one leading zero
            departureTime = departureTime.replaceFirst(hours, String.format("%02d", hoursValue % 23));
        }

        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(departureTime, timeFormatter);
    }

    public String getTripId() {
        return tripId;
    }

    public LocalTime getDepartureTime() {
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
