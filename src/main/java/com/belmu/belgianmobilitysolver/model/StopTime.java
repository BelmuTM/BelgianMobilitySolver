package com.belmu.belgianmobilitysolver.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StopTime {

    private final String tripId;

    private final LocalTime departureTime;

    private final String stopId;
    private final short stopSequence;

    public StopTime(String tripId, String departureTime, String stopId, String stopSequence) {
        this.tripId = tripId;

        // Replacing hours with a value of 24 by 00 (midnight) to respect the ISO format (0-23)
        String hours      = departureTime.split(":")[0];
        short  hoursValue = Short.parseShort(hours);

        if (hoursValue > 23) {
            // Formatting the hours to be in the [0;23] range with one leading zero
            departureTime = departureTime.replaceFirst(hours, String.format("%02d", hoursValue % 23));
        }

        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.departureTime = LocalTime.parse(departureTime, timeFormatter);

        this.stopId       = stopId;
        this.stopSequence = Short.parseShort(stopSequence);
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

    public short getStopSequence() {
        return stopSequence;
    }
}
