package com.belmu.belgianmobilitysolver.pathfinder;

import java.time.LocalTime;

public record Vertex(String stopId, LocalTime time) implements Comparable<Vertex> {

    @Override
    public int compareTo(Vertex other) {
        return this.time.compareTo(other.time);
    }

}
