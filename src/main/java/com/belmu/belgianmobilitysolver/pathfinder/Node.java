package com.belmu.belgianmobilitysolver.pathfinder;

import com.belmu.belgianmobilitysolver.model.Route;

public class Node {

    public Node previous;

    public String stopId;
    public Route route;

    public long time;
    public long previousTime;

    public boolean isWalking;

    public Node(Node previous, String stopId, Route route, long time, long previousTime, boolean isWalking) {
        this.previous = previous;

        this.stopId = stopId;
        this.route  = route;

        this.time         = time;
        this.previousTime = previousTime;

        this.isWalking = isWalking;
    }
}
