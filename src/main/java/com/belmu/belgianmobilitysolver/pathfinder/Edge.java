package com.belmu.belgianmobilitysolver.pathfinder;

public class Edge {

    private final Vertex from;
    private final Vertex to;

    public Edge(Vertex from, Vertex to) {
        this.from = from;
        this.to   = to;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }
}
