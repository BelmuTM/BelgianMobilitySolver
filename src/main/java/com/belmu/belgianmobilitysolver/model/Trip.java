package com.belmu.belgianmobilitysolver.model;

public class Trip {

    private final String tripId;
    private final String routeId;

    private Route route;

    public Trip(String tripId, String routeId) {
        this.tripId  = tripId;
        this.routeId = routeId;
    }

    public String getTripId() {
        return tripId;
    }

    public String getRouteId() {
        return routeId;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
