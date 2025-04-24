package com.belmu.belgianmobilitysolver.model;

import java.time.LocalTime;

public class Departure {

    private final String tripId;

    private final LocalTime time;

    private final short sequence;

    public Departure(String tripId, LocalTime time, short sequence) {
        this.tripId   = tripId;
        this.time     = time;
        this.sequence = sequence;
    }

    public String getTripId() {
        return tripId;
    }

    public LocalTime getTime() {
        return time;
    }

    public short getSequence() {
        return sequence;
    }
}
