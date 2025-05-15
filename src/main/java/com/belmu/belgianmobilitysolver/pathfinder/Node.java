package com.belmu.belgianmobilitysolver.pathfinder;

import com.belmu.belgianmobilitysolver.model.Route;

public class Node {

    public long cost;

    public Node previous;

    public String stopId;
    public Route route;

    public long time;
    public long previousTime;

    public boolean isWalking;

    /**
     * Node class for the weighted graph.
     * @param cost          cost/weight of the node in the graph
     * @param previous      node preceding this one
     * @param stopId        ID of the stop representing the node on the path
     * @param route         route of the trip the stop belongs to
     * @param time          time of arrival in the node
     * @param previousTime  time of arrival before walking
     * @param isWalking     whether the last edge was walked or not
     */
    public Node(long cost, Node previous, String stopId, Route route, long time, long previousTime, boolean isWalking) {
        this.cost     = cost;
        this.previous = previous;

        this.stopId = stopId;
        this.route  = route;

        this.time         = time;
        this.previousTime = previousTime;

        this.isWalking = isWalking;
    }
}
