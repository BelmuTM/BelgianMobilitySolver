package com.belmu.belgianmobilitysolver.pathfinder;

import java.time.LocalTime;
import java.util.Objects;

public class Vertex implements Comparable<Vertex> {

    private final String stopId;
    private final LocalTime time;

    public Vertex(String stopId, LocalTime time) {
        this.stopId = stopId;
        this.time   = time;
    }

    public String getStopId() {
        return stopId;
    }

    public LocalTime getTime() {
        return time;
    }

    @Override
    public int compareTo(Vertex other) {
        return this.time.compareTo(other.time);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Objects.equals(stopId, vertex.stopId) && Objects.equals(time, vertex.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stopId, time);
    }
}
